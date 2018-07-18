package project.view.UserSearchProduct;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import project.firebase.Firebase;
import project.view.MainActivity;
import project.view.NearbyStore.NearbyStorePage;
import project.view.ProductDetail.ProductDetailPage;
import project.view.R;

public class UserSearchProductListViewCustomAdapter extends BaseAdapter {
    private Context context;
    private List<ProductInfor> productList;
    private int layout;
    private StorageReference storageReference = Firebase.getFirebase();
    final static int REQUEST_LOCATION = 1;
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

    public UserSearchProductListViewCustomAdapter(Context context, int layout, List<ProductInfor> productList) {
        this.context = context;
        this.layout = layout;
        this.productList = productList;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        final ProductInfor product = productList.get(position);
        if (view  == null){
            LayoutInflater li;
            li = LayoutInflater.from(getContext());
            view = li.inflate(R.layout.user_search_product_page_custom_list_view, null);
            holder = new ViewHolder();
            holder.productName = (TextView) view.findViewById(R.id.productName);
            holder.brandName = (TextView) view.findViewById(R.id.brandName);
            holder.productImage =(ImageView) view.findViewById(R.id.productImage);
            holder.findNearByBtn = (Button) view.findViewById(R.id.findNearByBtn);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        if(product != null) {
            holder.productName.setText(product.getProductName());
            holder.brandName.setText(product.getBrandName());
            Glide.with(getContext() /* context */)
                    .using(new FirebaseImageLoader())
                    .load(storageReference.child(productList.get(position).getProductImagePath()))
                    .into(holder.productImage);


            holder.findNearByBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Clicked!!!!", Toast.LENGTH_SHORT).show();
                    String productName = product.getProductName();
                    Intent toNearbyStorePage = new Intent(getContext(), NearbyStorePage.class);
                    toNearbyStorePage.putExtra("productName",productName);
                    getContext().startActivity(toNearbyStorePage);
                }
            });

        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isStoreProduct = false;
                int productID = product.getProductID();
                Intent toProductDetail = new Intent(getContext(), ProductDetailPage.class);
                toProductDetail.putExtra("isStoreProduct",isStoreProduct);
                toProductDetail.putExtra("productID",productID);
                toProductDetail.putExtra("productName",product.getProductName());
                getContext().startActivity(toProductDetail);

            }
        });

        return view;
    }

    public Context getContext() {
        return context;
    }

    public class ViewHolder{
        TextView productName;
        TextView brandName;
        ImageView productImage;
        Button findNearByBtn;

    }

}
