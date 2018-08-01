package project.view.gui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import project.view.adapter.UserSearchProductListViewCustomAdapter;
import project.view.model.NearByStore;
import project.view.model.Product;
import project.view.model.ProductInStoreDetail;
import project.view.util.CustomInterface;
import project.view.util.Formater;
import retrofit2.Call;
import retrofit2.Response;

public class ProductDetailPage extends AppCompatActivity {
    private ImageView productImage;
    private TextView salePriceText, productPriceText, promotionPercentText, productNameText, categoryNameText, brandNameText, productDescText;
    private TextView productNotInStoreName, productNotInStoreCategoryName, productNotInStoreBrandName, productNotInStoreDesc;
    private Button addToCartBtn, findStoreBtn;
    private LinearLayout isNotProductInStoreLayout, isProductInStoreLayout, productDetailLayout;
    private ProductInStoreDetail productInStore;
    private Context context;
    private Product product;
    private String productName ;
    private int storeID;
    private boolean isStoreProduct;
    private ProgressBar loadingBar;

    final static int REQUEST_LOCATION = 1;

    private StorageReference storageReference = Firebase.getFirebase();

    public Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_describe_product);

        mapping();

        product = new Gson().fromJson(getIntent().getStringExtra("product"),Product.class);

        getSupportActionBar().setTitle(productName);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CustomInterface.setStatusBarColor(this);
        loadingBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);

        storeID = getIntent().getIntExtra("storeID", -1);
        isStoreProduct = getIntent().getBooleanExtra("isStoreProduct", false);
        setLayout(isStoreProduct,isNotProductInStoreLayout,isProductInStoreLayout, productDetailLayout, findStoreBtn);
        Glide.with(ProductDetailPage.this /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(product.getImage_path()))
                .skipMemoryCache(true)
                .into(productImage);

        if (isStoreProduct == true){
            productInStore = new ProductInStoreDetail(1,"Samsung Galaxy Note 8","123","Điện thoại", "Samsung",20000000,1,"Dây là mô tả");

            productPriceText.setText(Formater.formatDoubleToMoney(String.valueOf(productInStore.getProductPrice())));
            productPriceText.setPaintFlags(productPriceText.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

            promotionPercentText.setText(Formater.formatDoubleToInt(String.valueOf(productInStore.getPromotionPercent())));

            double salePriceDouble = productInStore.getProductPrice() * productInStore.getPromotionPercent() / 100;
            long salePriceLong = (long) salePriceDouble;
            long displayPrice = productInStore.getProductPrice() - salePriceLong ;
            salePriceText.setText(Formater.formatDoubleToMoney(String.valueOf(displayPrice)));

            productNameText.setText(productInStore.getProductName());

            categoryNameText.setText(productInStore.getCategoryName());
            brandNameText.setText(productInStore.getBrandName());

            productDescText.setText(productInStore.getProductDesc());
        } else {
            productNotInStoreName.setText(product.getProduct_name());
            productNotInStoreCategoryName.setText(product.getType_name());
            productNotInStoreBrandName.setText(product.getBrand_name());
            productNotInStoreDesc.setText(product.getDescription());
            findStoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    String productName = product.getProduct_name();
                    Intent toNearbyStorePage = new Intent(ProductDetailPage.this, NearbyStorePage.class);
                    toNearbyStorePage.putExtra("productName",productName);
                    if (ActivityCompat.checkSelfPermission(ProductDetailPage.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(ProductDetailPage.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ProductDetailPage.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                    } else {
                        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            googleMap.clear();
                        if (location != null) {
                           double latitude = location.getLatitude();
                           double longtitude = location.getLongitude();
                            NearByStoreAsynTask1 synTask = new NearByStoreAsynTask1();
                            Call<List<NearByStore>> call = ApiUtils.getAPIService().nearByStore(product.getProduct_id(),String.valueOf(latitude),String.valueOf(longtitude));
                            synTask.execute(call);
                        }else {
                            Toast.makeText(ProductDetailPage.this, "Bạn chưa bật định vị. Chưa thể tìm cửa hàng!!!!!", Toast.LENGTH_SHORT).show();
                        }

                    }


                }
            });
        }

    }

    public void mapping(){
        productImage = (ImageView) findViewById(R.id.productImage);
        salePriceText = (TextView) findViewById(R.id.salePrice);
        productPriceText = (TextView) findViewById(R.id.productPrice);
        promotionPercentText = (TextView) findViewById(R.id.promotionPercent);
        productNameText = (TextView) findViewById(R.id.productName);
        productNotInStoreName = (TextView) findViewById(R.id.productNotInStoreName);
        categoryNameText = (TextView) findViewById(R.id.categoryName);
        brandNameText = (TextView) findViewById(R.id.brandName);
        productDescText = (TextView) findViewById(R.id.productDesc);
        addToCartBtn = (Button) findViewById(R.id.addToCartBtn);
        isNotProductInStoreLayout = (LinearLayout) findViewById(R.id.isNotProductInStoreLayout);
        isProductInStoreLayout = (LinearLayout) findViewById(R.id.isProductInStoreLayout);
        productDetailLayout = (LinearLayout) findViewById(R.id.productDetailLayout);
        findStoreBtn = (Button) findViewById(R.id.findStoreBtn);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);

        productNotInStoreCategoryName = (TextView) findViewById(R.id.productNotInStoreCategoryName);
        productNotInStoreBrandName = (TextView) findViewById(R.id.productNotInStoreBrandName);
        productNotInStoreDesc = (TextView) findViewById(R.id.productNotInStoreDesc);

    }

    public void setLayout(boolean isStoreProduct, LinearLayout isNotProductInStoreLayout, LinearLayout isProductInStoreLayout, LinearLayout productDetailLayout, Button findStoreBtn){
        if(isStoreProduct == false) {
            isNotProductInStoreLayout.setVisibility(View.VISIBLE);
            isProductInStoreLayout.setVisibility(View.INVISIBLE);
            productDetailLayout.setVisibility(View.INVISIBLE);
            findStoreBtn.setVisibility(View.VISIBLE);
        } else {
            isNotProductInStoreLayout.setVisibility(View.INVISIBLE);
            isProductInStoreLayout.setVisibility(View.VISIBLE);
            productDetailLayout.setVisibility(View.VISIBLE);
            findStoreBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class NearByStoreAsynTask1 extends AsyncTask<Call, Void, List<NearByStore>> {

        @Override
        protected void onPreExecute() {
            loadingBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

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
                Intent toNearByStore = new Intent(ProductDetailPage.this,NearbyStorePage.class);
                for (int i = 0 ; i< list.size();i++){
                    String storeJSON = new Gson().toJson(list.get(i),NearByStore.class);
                    listStore.add(storeJSON);
                }
                toNearByStore.putExtra("listStore",listStore);
                loadingBar.setVisibility(View.VISIBLE);
                startActivity(toNearByStore);
            }

        }
    }
}
