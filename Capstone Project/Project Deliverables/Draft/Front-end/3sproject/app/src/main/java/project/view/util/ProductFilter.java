package project.view.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import project.view.model.Product;

public class ProductFilter {
    private ArrayAdapter<String> arrayAdapter;
    public static final String ALL_PRODUCT = "Tất cả sản phẩm";
    public static final String SALE_PRODUCT = "Sản phẩm giảm giá";
    public static final String FROM_LOW_COST = "Giá từ thấp tới cao";
    public static final String FROM_HIGH_COST = "Giá từ cao xuống thấp";
    public ProductFilter(){
    }
    public void setCategoryFilter(List<Product> products, Context context, Spinner categorySpinner){
        HashMap<String,Integer> categoryFilter = new HashMap<>();
        int saleCount=0;
        for(Product product : products) {

            if (categoryFilter.containsKey(product.getCategory_name())) {
                categoryFilter.put(product.getCategory_name(), categoryFilter.get(product.getCategory_name())+1);
            }else {
                categoryFilter.put(product.getCategory_name(), 1);
            }

            if (product.getPromotion()!=0){
                saleCount++;
            }
        }

        Set set = categoryFilter.entrySet();
        Iterator i = set.iterator();
        List<String> list = new ArrayList<String>();
        list.add(ALL_PRODUCT+" ("+products.size()+")");
        list.add(SALE_PRODUCT+" ("+saleCount+")");
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            list.add(me.getKey()+" ("+me.getValue()+")");
        }
        arrayAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(arrayAdapter);
    }

    public void setSortItem(Context context,Spinner spinnerSort){
        final List<String> list = new ArrayList<String>();
        list.add("Sắp xếp theo giá bán");
        list.add(FROM_LOW_COST);
        list.add(FROM_HIGH_COST);
        arrayAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, list){
            @Override
            public boolean isEnabled(int position){
                if(list.get(position).equals("Sắp xếp theo giá bán"))
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View spinnerview = super.getDropDownView(position, convertView, parent);

                TextView spinnertextview = (TextView) spinnerview;

                if(list.get(position).equals("Sắp xếp theo giá bán")) {

                    //Set the disable spinner item color fade .
                    spinnertextview.setTextColor(Color.parseColor("#bcbcbb"));
                }
                else {

                    spinnertextview.setTextColor(Color.BLACK);

                }
                return spinnerview;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(arrayAdapter);
    }

    public void setBrandFilter(List<Product> products, Context context, Spinner brandSpinner){
        HashMap<String,Integer> brandFilter = new HashMap<>();
        for(Product product : products) {

            if (brandFilter.containsKey(product.getBrand_name())) {
                brandFilter.put(product.getBrand_name(), brandFilter.get(product.getBrand_name())+1);
            }else {
                brandFilter.put(product.getBrand_name(), 1);
            }
        }
        Set set = brandFilter.entrySet();
        Iterator i = set.iterator();
        List<String> list = new ArrayList<String>();
        list.add(ALL_PRODUCT+" ("+ products.size()+")");
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            list.add(me.getKey()+" ("+me.getValue()+")");
        }
        arrayAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brandSpinner.setAdapter(arrayAdapter);
    }
}
