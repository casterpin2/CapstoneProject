package project.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import project.view.model.Product;

public class ProductTypeDisplayListViewAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Product> productList;
    private StorageReference storageReference = Firebase.getFirebase();
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
            holder.findNearByBtn = (Button) view.findViewById(R.id.findNearByBtn);
            view.setTag(holder);
        }else{
            holder = (ProductTypeDisplayListViewAdapter.ViewHolder) view.getTag();
        }
        final Product product = productList.get(position);

        if(product != null) {
            holder.productName.setText(product.getProduct_name());
            holder.productDesc.setText(product.getDescription());
            Glide.with(context /* context */)
                    .using(new FirebaseImageLoader())
                    .load(storageReference.child(product.getImage_path()))
                    .into(holder.productImage);
            holder.findNearByBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int productId = product.getProduct_id();
                    Intent toNearByStorePage = new Intent(getContext(), NearbyStorePage.class);
                    toNearByStorePage.putExtra("productId",productId);
                    getContext().startActivity(toNearByStorePage);
                }
            });
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
        Button findNearByBtn;
    }
}
