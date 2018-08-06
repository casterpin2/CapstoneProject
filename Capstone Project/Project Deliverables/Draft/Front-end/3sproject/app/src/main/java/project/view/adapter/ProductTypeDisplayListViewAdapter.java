package project.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import project.view.R;
import project.view.model.Product;

public class ProductTypeDisplayListViewAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Product> productList;

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

    public ProductTypeDisplayListViewAdapter(Context context, int layout, List<Product> productList) {
        this.context = context;
        this.layout = layout;
        this.productList = productList;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ProductTypeDisplayListViewAdapter.ViewHolder holder;
        if (view  == null){
            LayoutInflater li;
            li = LayoutInflater.from(getContext());
            view = li.inflate(R.layout.product_type_display_custom_cardview, null);
            holder = new ProductTypeDisplayListViewAdapter.ViewHolder();
            holder.productImage = (ImageView) view.findViewById(R.id.productImage);
            holder.productName = (TextView) view.findViewById(R.id.productName);
            holder.productDesc =(TextView) view.findViewById(R.id.productDesc);

            view.setTag(holder);
        }else{
            holder = (ProductTypeDisplayListViewAdapter.ViewHolder) view.getTag();
        }
        Product product = productList.get(position);

        if(product != null) {
            holder.productName.setText(product.getProduct_name());
            holder.productDesc.setText(product.getDescription());

//            productImage
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
