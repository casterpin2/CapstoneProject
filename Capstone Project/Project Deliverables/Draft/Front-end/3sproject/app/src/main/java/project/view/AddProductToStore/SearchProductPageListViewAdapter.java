package project.view.AddProductToStore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import project.view.R;

public class SearchProductPageListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Item> productList;
    private int layout;

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public SearchProductPageListViewAdapter(Context context, int layout, List<Item> productList) {
        this.context = context;
        this.layout = layout;
        this.productList = productList;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        SearchProductPageListViewAdapter.ViewHolder holder;
        if (view  == null){
            LayoutInflater li;
            li = LayoutInflater.from(getContext());
            view = li.inflate(R.layout.search_product_page_custom_list_view, null);
            holder = new SearchProductPageListViewAdapter.ViewHolder();
            holder.productImage = (ImageView) view.findViewById(R.id.productImage);
            holder.productName = (TextView) view.findViewById(R.id.productName);
            holder.productBrand =(TextView) view.findViewById(R.id.productBrand);
            holder.productCategory =(TextView) view.findViewById(R.id.productCategory);

            view.setTag(holder);
        }else{
            holder = (SearchProductPageListViewAdapter.ViewHolder) view.getTag();
        }
        Item product = productList.get(position);

        if(product != null) {
            holder.productName.setText(product.getProduct_name());
            holder.productBrand.setText(product.getBrand_name());
            holder.productCategory.setText(product.getCategory_name());

        }

        return view;
    }

    public Context getContext() {
        return context;
    }

    public class ViewHolder{
        ImageView productImage;
        TextView productName;
        TextView productBrand;
        TextView productCategory;
    }
}
