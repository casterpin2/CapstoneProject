package project.view.ProductBrandDisplay;

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

public class ProductBrandDisplayListViewAdapter extends BaseAdapter {

    private Context context;
    private List<ProductBrand> productList;
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

    public ProductBrandDisplayListViewAdapter(Context context, int layout, List<ProductBrand> productList) {
        this.context = context;
        this.layout = layout;
        this.productList = productList;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ProductBrandDisplayListViewAdapter.ViewHolder holder;
        if (view  == null){
            LayoutInflater li;
            li = LayoutInflater.from(getContext());
            view = li.inflate(R.layout.product_brand_display_custom_listview, null);
            holder = new ProductBrandDisplayListViewAdapter.ViewHolder();
            holder.productImage = (ImageView) view.findViewById(R.id.productImage);
            holder.productName = (TextView) view.findViewById(R.id.productName);
            holder.productDesc =(TextView) view.findViewById(R.id.productDesc);

            view.setTag(holder);
        }else{
            holder = (ProductBrandDisplayListViewAdapter.ViewHolder) view.getTag();
        }
        ProductBrand product = productList.get(position);

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
