package project.view.gui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
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
import project.view.model.NearByStore;
import project.view.model.Product;
import project.view.util.CustomInterface;
import project.view.util.Formater;
import retrofit2.Call;
import retrofit2.Response;

public class ProductDetailPage extends AppCompatActivity {
    private ImageView productImage;
    private TextView salePriceText, productPriceText, promotionPercentText, productNameText, categoryNameText, brandNameText, productDescText;
    private TextView productNotInStoreName, productNotInStoreCategoryName, productNotInStoreBrandName, productNotInStoreDesc, storeNameTV;
    private Button addToCartBtn, findStoreBtn, editProductInStore;
    private LinearLayout isNotProductInStoreLayout, isProductInStoreLayout, productDetailLayout, storeNameLayout;
    private Product product;
    private int storeID;
    private String storeName;
    private boolean isStoreProduct;
    private boolean isStoreSee;
    private ProgressBar loadingBar;
    private LocationManager locationManager;
    final static int REQUEST_LOCATION = 1;
    private StorageReference storageReference = Firebase.getFirebase();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_describe_product);
        mapping();
        product = new Gson().fromJson(getIntent().getStringExtra("product"),Product.class);
        getSupportActionBar().setTitle(product.getProduct_name());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                editProductInStore.setVisibility(View.VISIBLE);
                editProductInStore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent toEditProductInStorePage = new Intent(ProductDetailPage.this, EditProductInStorePage.class);
//                        toEditProductInStorePage.putExtra("productName", );
//                        toEditProductInStorePage.putExtra("productID", );
//                        toEditProductInStorePage.putExtra("storeID", storeID);
//                        toEditProductInStorePage.putExtra("categoryName", );
//                        toEditProductInStorePage.putExtra("brandName", );
//                        toEditProductInStorePage.putExtra("productPrice", );
//                        toEditProductInStorePage.putExtra("promotionPercent", );
//                        toEditProductInStorePage.putExtra("productImageLink", );
                        startActivity(toEditProductInStorePage);
                    }
                });
            } else {
                storeName = getIntent().getStringExtra("storeName");
                storeNameLayout.setVisibility(View.VISIBLE);
                storeNameTV.setText(storeName);
            }
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
                    Toast.makeText(ProductDetailPage.this,"aa",Toast.LENGTH_LONG).show();
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
        editProductInStore = (Button) findViewById(R.id.editProductInStore);
        storeNameTV = (TextView) findViewById(R.id.storeName);

        productNotInStoreCategoryName = (TextView) findViewById(R.id.productNotInStoreCategoryName);
        productNotInStoreBrandName = (TextView) findViewById(R.id.productNotInStoreBrandName);
        productNotInStoreDesc = (TextView) findViewById(R.id.productNotInStoreDesc);
        storeNameLayout = (LinearLayout) findViewById(R.id.storeNameLayout);

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

}
