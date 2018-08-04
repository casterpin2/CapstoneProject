package project.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.firebase.Firebase;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.gui.NearbyStorePage;
import project.view.gui.ProductDetailPage;
import project.view.model.NearByStore;
import project.view.model.Product;
import retrofit2.Call;
import retrofit2.Response;

public class ProductBrandDisplayListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Product> productList;
    private int layout;
    private StorageReference storageReference = Firebase.getFirebase();
    final static int REQUEST_LOCATION = 1;
    private double currentLatitude = 0.0;
    private double currentLongtitude = 0.0;
    private LocationManager locationManager;
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

    public ProductBrandDisplayListViewAdapter(Context context, int layout, List<Product> productList, double latitude, double longtitude) {
        this.context = context;
        this.layout = layout;
        this.productList = productList;
        this.currentLatitude = latitude;
        this.currentLongtitude = longtitude;
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
            holder.findNearByBtn = (Button) view.findViewById(R.id.findNearByBtn);
            view.setTag(holder);
        }else{
            holder = (ProductBrandDisplayListViewAdapter.ViewHolder) view.getTag();
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
                    String productName = product.getProduct_name();
                    NearByStoreAsynTask1 asynTask = new NearByStoreAsynTask1();
                    Call<List<NearByStore>> call = ApiUtils.getAPIService().nearByStore(product.getProduct_id(),String.valueOf(currentLatitude),String.valueOf(currentLongtitude));
                    asynTask.execute(call);
                }
            });
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isStoreProduct = false;
                int productID = product.getProduct_id();
                String productName = product.getProduct_name();
                Intent toProductDetail = new Intent(getContext(), ProductDetailPage.class);
                toProductDetail.putExtra("product",new Gson().toJson(product));
                getContext().startActivity(toProductDetail);
            }
        });

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
    public class NearByStoreAsynTask1 extends AsyncTask<Call, Void, List<NearByStore>> {
        @Override
        protected List<NearByStore> doInBackground(Call... calls) {
            try {
                Call<List<NearByStore>> call = calls[0];
                Response<List<NearByStore>> re = call.execute();
//            if (re.body() != null) {

                return re.body();
//            } else {
//                return null;
//            }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<NearByStore> list) {
            super.onPostExecute(list);
            ArrayList<String> listStore = new ArrayList<>();
            if (list != null) {
                Intent toNearByStore = new Intent(context,NearbyStorePage.class);
                for (int i = 0 ; i< list.size();i++){
                    String storeJSON = new Gson().toJson(list.get(i),NearByStore.class);
                    listStore.add(storeJSON);
                }
                toNearByStore.putExtra("listStore",listStore);
                context.startActivity(toNearByStore);
            }

        }
    }
}
