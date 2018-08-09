package project.view.gui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.firebase.Firebase;
import project.objects.User;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.model.CartDetail;
import project.view.model.NearByStore;
import project.view.model.Product;
import project.view.model.Store;
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
    private Product product;
    private String productName;
    private int storeID;
    private boolean isStoreProduct;
    private boolean isStoreSee;
    private ProgressBar loadingBar;
    private User user;
    private Store myStore;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private LocationManager locationManager;
    final static int REQUEST_LOCATION = 1;
    private StorageReference storageReference = Firebase.getFirebase();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_describe_product);
        mapping();
        product = new Gson().fromJson(getIntent().getStringExtra("product"),Product.class);
        getSupportActionBar().setTitle(productName);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        restoringPreferences();
        CustomInterface.setStatusBarColor(this);
        loadingBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);
        storeID = getIntent().getIntExtra("storeID", -1);
        isStoreProduct = getIntent().getBooleanExtra("isStoreProduct", false);
        isStoreSee = getIntent().getBooleanExtra("isStoreSee", false);
        setLayout(isStoreProduct,isNotProductInStoreLayout,isProductInStoreLayout, productDetailLayout, findStoreBtn);
        Glide.with(ProductDetailPage.this /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(product.getImage_path()))
                .skipMemoryCache(true)
                .into(productImage);

        if (isStoreProduct == true){
            if (isStoreSee){
                addToCartBtn.setVisibility(View.INVISIBLE);
                addToCartBtn.setClickable(false);
            }
            String storeJson = getIntent().getStringExtra("nearByStore");
            final Store store = new Gson().fromJson(storeJson, Store.class);
            productPriceText.setText(Formater.formatDoubleToMoney(String.valueOf(product.getPrice())));
            productPriceText.setPaintFlags(productPriceText.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

            promotionPercentText.setText(Formater.formatDoubleToInt(String.valueOf(product.getPromotion())));

            double salePriceDouble = product.getPrice() * product.getPromotion() / 100;
            long salePriceLong = (long) salePriceDouble;
            long displayPrice = product.getPrice() - salePriceLong ;
            salePriceText.setText(Formater.formatDoubleToMoney(String.valueOf(displayPrice)));

            productNameText.setText(product.getProduct_name());

            categoryNameText.setText(product.getType_name());
            brandNameText.setText(product.getBrand_name());

            productDescText.setText(product.getDescription());
            addToCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user == null){
                        Toast.makeText(ProductDetailPage.this, "Bạn chưa đăng nhập", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (myStore != null){
                        if (myStore.getId() == store.getId()){
                            Toast.makeText(ProductDetailPage.this, "Cửa hàng của bạn, không thể thêm vào giỏ hàng", Toast.LENGTH_LONG).show();
                            return;
                        }
                        myRef = database.getReference().child("cart").child(String.valueOf(user.getId())).child(String.valueOf(store.getId()));
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()){
                                    myRef.child("phone").setValue(String.valueOf(store.getPhone()));
                                    myRef.child("storeId").setValue(store.getId());
                                    myRef.child("storeName").setValue(String.valueOf(store.getName()));
                                    myRef.child("image_path").setValue(String.valueOf(store.getImage_path()));
                                    CartDetail cartDetail  = new CartDetail(product.getProduct_id(),product.getProduct_name(),1,product.getPrice(),product.getImage_path());
                                    myRef.child("cartDetail").child(String.valueOf(product.getProduct_id())).setValue(cartDetail);
                                } else {
                                    myRef.child("cartDetail").child(String.valueOf(product.getProduct_id())).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (!dataSnapshot.exists()){
                                                CartDetail cartDetail  = new CartDetail(product.getProduct_id(),product.getProduct_name(),1,product.getPrice(),product.getImage_path());
                                                myRef.child("cartDetail").child(String.valueOf(product.getProduct_id())).setValue(cartDetail);
                                            } else {
                                                Toast.makeText(ProductDetailPage.this, "Sản phẩm đã có trong cửa hàng", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            });
        }
        if(isStoreProduct == false) {
            productNotInStoreName.setText(product.getProduct_name());
            productNotInStoreCategoryName.setText(product.getType_name());
            productNotInStoreBrandName.setText(product.getBrand_name());
            productNotInStoreDesc.setText(product.getDescription());
            findStoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int productId = product.getProduct_id();
                    Intent toNearByStorePage = new Intent(ProductDetailPage.this, NearbyStorePage.class);
                    toNearByStorePage.putExtra("productId",productId);
                    startActivity(toNearByStorePage);
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
    private void restoringPreferences(){
            SharedPreferences pre = getSharedPreferences("authentication", Context.MODE_PRIVATE);
            String userJSON = pre.getString("user", "");
            user = new Gson().fromJson(userJSON,User.class);
            String storeJSON = pre.getString("store", "");
            myStore = new Gson().fromJson(storeJSON,Store.class);
    }
}
