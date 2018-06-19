package project.view.Cart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import project.view.AddProductToStore.Item;
import project.view.AddProductToStore.Product;
import project.view.R;

public class CartProductToStorePageListViewAdapter extends ArrayAdapter<Item> {
    private Context context;
    private List<Item> productList;

    public CartProductToStorePageListViewAdapter(@NonNull Context context, int resource, @NonNull List<Item> productList) {
        super(context, resource, productList);
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.cart_product_to_store_page_custom_listview, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.productName.setText(productList.get(position).getProduct_name());
        viewHolder.productCategory.setText(productList.get(position).getCategory_name());
        viewHolder.productBrand.setText(productList.get(position).getBrand_name());


//        viewHolder.icon.setImageResource(products.get(position).getImage());
        return convertView;
    }

    private static class ViewHolder {
        TextView productName;
        TextView productBrand;
        TextView productCategory;
        ImageView productImage;

        public ViewHolder(View view) {
            productImage = (ImageView) view.findViewById(R.id.productImage);
            productName = (TextView) view.findViewById(R.id.productName);
            productBrand =(TextView) view.findViewById(R.id.productBrand);
            productCategory =(TextView) view.findViewById(R.id.productCategory);

        }
    }
}
