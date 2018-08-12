package project.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.firebase.Firebase;
import project.retrofit.ApiUtils;
import project.view.model.NearByStore;
import project.view.model.Product;
import project.view.gui.NearbyStorePage;
import project.view.gui.ProductDetailPage;
import project.view.R;
import retrofit2.Call;
import retrofit2.Response;

public class UserSearchProductListViewCustomAdapter extends BaseAdapter {
    private Context context;
    private List<Product> productList;
    private int layout;
    private StorageReference storageReference = Firebase.getFirebase();
    final static int REQUEST_LOCATION = 1;
    private double longtitude = 0.0;
    private double latitude = 0.0;
    private List<NearByStore> list;
    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

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

    public UserSearchProductListViewCustomAdapter(Context context, int layout, List<Product> productList, double latitude, double longtitude) {
        this.context = context;
        this.layout = layout;
        this.productList = productList;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        final Product product = productList.get(position);
        if (view  == null){
            LayoutInflater li;
            li = LayoutInflater.from(getContext());
            view = li.inflate(R.layout.user_search_product_page_custom_list_view, null);
            holder = new ViewHolder();
            holder.productName = (TextView) view.findViewById(R.id.productName);
            holder.brandName = (TextView) view.findViewById(R.id.productBrand);
            holder.productImage =(ImageView) view.findViewById(R.id.productImage);
            holder.findNearByBtn = (Button) view.findViewById(R.id.findNearByBtn);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        if(product != null) {
            holder.productName.setText(product.getProduct_name());
            holder.brandName.setText(product.getBrand_name());
            Glide.with(getContext() /* context */)
                    .using(new FirebaseImageLoader())
                    .load(storageReference.child(productList.get(position).getImage_path()))
                    .into(holder.productImage);


            holder.findNearByBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int productId = product.getProduct_id();
                    Intent toNearByStorePage = new Intent(getContext(), NearbyStorePage.class);
                    toNearByStorePage.putExtra("productId",productId);
                    toNearByStorePage.putExtra("productName",product.getProduct_name());
                    toNearByStorePage.putExtra("image_path",product.getImage_path());
                    getContext().startActivity(toNearByStorePage);
                }
            });

        } 

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        TextView productName;
        TextView brandName;
        ImageView productImage;
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
