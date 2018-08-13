package project.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;
import java.util.List;
import project.firebase.Firebase;
import project.view.R;
import project.view.gui.NearbyStorePage;
import project.view.gui.ProductDetailPage;
import project.view.model.Product;

public class ProductTypeCustomCardViewAdapter extends RecyclerView.Adapter<ProductTypeCustomCardViewAdapter.MyViewHolder> {
    private Context context;
    private List<Product> products;
    private Product product;
    private StorageReference storageReference = Firebase.getFirebase();

    public ProductTypeCustomCardViewAdapter (@NonNull Context context, @NonNull List<Product> products){
        this.context = context;
        this.products = products;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_type_display_custom_cardview, parent, false);

        return new ProductTypeCustomCardViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        product = products.get(position);
        holder.productName.setText(product.getProduct_name());

        Glide.with(context /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(product.getImage_path()))
                .into(holder.productImage);

        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.findNearByBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toNearByStorePage = new Intent(context, NearbyStorePage.class);
                toNearByStorePage.putExtra("productId",product.getProduct_id());
                toNearByStorePage.putExtra("productName",product.getProduct_name());
                toNearByStorePage.putExtra("image_path",product.getImage_path());
                context.startActivity(toNearByStorePage);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        public ImageView productImage;
        public TextView findNearByBtn;
        public MyViewHolder(View view) {
            super(view);
            productName = (TextView) view.findViewById(R.id.productName);
            productImage = (ImageView) view.findViewById(R.id.productImage);
            findNearByBtn = view.findViewById(R.id.findNearByBtn);
        }
    }
}
