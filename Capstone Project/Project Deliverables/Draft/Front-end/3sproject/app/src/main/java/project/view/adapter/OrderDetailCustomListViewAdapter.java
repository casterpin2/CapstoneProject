package project.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import project.firebase.Firebase;
import project.view.R;
import project.view.gui.ProductDetailPage;
import project.view.model.Product;
import project.view.util.Formater;

public class OrderDetailCustomListViewAdapter extends ArrayAdapter<Product> {
    private Context context;
    private int resource;
    private List<Product> productList;
    private Formater formater;
    private StorageReference storageReference = Firebase.getFirebase();
    public OrderDetailCustomListViewAdapter(Context context, int resource, ArrayList<Product> productList) {
        super(context, resource, productList);
        this.context = context;
        this.resource = resource;
        this.productList = productList;
        formater = new Formater();
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        OrderDetailCustomListViewAdapter.OrderDetailViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_in_order_detail, parent, false);
            viewHolder = new OrderDetailCustomListViewAdapter.OrderDetailViewHolder();
            viewHolder.productNameTV = (TextView) convertView.findViewById(R.id.productNameTV);
            viewHolder.quantityTV = (TextView) convertView.findViewById(R.id.quantity);
            viewHolder.productImage = (ImageView) convertView.findViewById(R.id.productImage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OrderDetailCustomListViewAdapter.OrderDetailViewHolder) convertView.getTag();
        }
        final Product product = productList.get(position);

        viewHolder.productNameTV.setText(product.getProduct_name());
        viewHolder.quantityTV.setText(product.getQuantity()+"");
        Glide.with(context /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(product.getImage_path()))
                //.skipMemoryCache(true)
                .into(viewHolder.productImage);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailPage.class);
                intent.putExtra("productId",product.getProduct_id());
                intent.putExtra("storeID", product.getStore_id());
                intent.putExtra("isStoreProduct",true);
                intent.putExtra("isStoreSee",true);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public class OrderDetailViewHolder {
        TextView productNameTV, quantityTV;
        ImageView productImage;
    }
}
