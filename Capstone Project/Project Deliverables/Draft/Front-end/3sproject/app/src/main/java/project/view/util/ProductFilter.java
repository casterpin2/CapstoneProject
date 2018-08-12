package project.view.util;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
        for(Product product : products) {

            if (categoryFilter.containsKey(product.getCategory_name())) {
                categoryFilter.put(product.getCategory_name(), categoryFilter.get(product.getCategory_name())+1);
            }else {
                categoryFilter.put(product.getCategory_name(), 1);
            }
        }
        Set set = categoryFilter.entrySet();
        Iterator i = set.iterator();
        List<String> list = new ArrayList<String>();
        list.add(ALL_PRODUCT);
        list.add(SALE_PRODUCT);
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            list.add(me.getKey()+"");
        }
        arrayAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(arrayAdapter);
    }

    public void setSortItem(Context context,Spinner spinnerSort){
        List<String> list = new ArrayList<String>();
        list.add("Sắp xếp theo giá bán");
        list.add(FROM_LOW_COST);
        list.add(FROM_HIGH_COST);
        arrayAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, list);
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
        list.add(ALL_PRODUCT);
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            list.add(me.getKey()+"");
        }
        arrayAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brandSpinner.setAdapter(arrayAdapter);
    }
}
