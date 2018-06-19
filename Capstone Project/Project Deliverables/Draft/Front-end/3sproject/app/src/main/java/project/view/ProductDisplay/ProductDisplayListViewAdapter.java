package project.view.ProductDisplay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import project.view.AddProductToStore.Item;
import project.view.AddProductToStore.SearchProductPageListViewAdapter;
import project.view.R;

public class ProductDisplayListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Product> productList;
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

    public ProductDisplayListViewAdapter(Context context, int layout, List<Product> productList) {
        this.context = context;
        this.layout = layout;
        this.productList = productList;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ProductDisplayListViewAdapter.ViewHolder holder;
        if (view  == null){
            LayoutInflater li;
            li = LayoutInflater.from(getContext());
            view = li.inflate(R.layout.product_display_custom_listview, null);
            holder = new ProductDisplayListViewAdapter.ViewHolder();
            holder.productImage = (ImageView) view.findViewById(R.id.productImage);
            holder.productName = (TextView) view.findViewById(R.id.productName);
            holder.productDesc =(TextView) view.findViewById(R.id.productDesc);

            view.setTag(holder);
        }else{
            holder = (ProductDisplayListViewAdapter.ViewHolder) view.getTag();
        }
        Product product = productList.get(position);

        if(product != null) {
            holder.productName.setText(product.getProductName());
            holder.productDesc.setText(product.getProductDesc());

        }

        return view;
    }

    public Context getContext() {
        return context;
    }

    public class ViewHolder{
        ImageView productImage;
        TextView productName;
        TextView productDesc;

    }
}
