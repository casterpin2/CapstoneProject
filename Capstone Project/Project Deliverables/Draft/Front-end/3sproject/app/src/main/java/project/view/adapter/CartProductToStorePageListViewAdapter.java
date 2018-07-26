package project.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import project.firebase.Firebase;
import project.view.R;
import project.view.model.Item;
import project.view.util.Formater;

public class CartProductToStorePageListViewAdapter extends ArrayAdapter<Item> {
    private Context context;
    private List<Item> productList;
    private StorageReference storageReference = Firebase.getFirebase();


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
        viewHolder.productPromotion.setText(String.valueOf(productList.get(position).getPromotion()));
        viewHolder.productPrice.setText(Formater.formatDoubleToMoney(String.valueOf(productList.get(position).getPrice())));

        Glide.with(context /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(productList.get(position).getImage_path()))
                .into(viewHolder.productImage);

//        viewHolder.icon.setImageResource(products.get(position).getImage());
        return convertView;
    }

    private static class ViewHolder {
        TextView productName;
        TextView productPrice;
        TextView productPromotion;
        ImageView productImage;

        public ViewHolder(View view) {
            productImage = (ImageView) view.findViewById(R.id.productImage);
            productName = (TextView) view.findViewById(R.id.productName);
            productPrice =(TextView) view.findViewById(R.id.productPrice);
            productPromotion =(TextView) view.findViewById(R.id.productPromotion);

        }
    }
}
