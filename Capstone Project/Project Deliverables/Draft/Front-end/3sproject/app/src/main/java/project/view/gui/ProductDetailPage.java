package project.view.gui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.IOException;

import project.firebase.Firebase;
import project.objects.User;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.model.CartDetail;
import project.view.model.Product;
import project.view.model.Store;
import project.view.util.CustomInterface;
import project.view.util.Formater;
import project.view.util.TweakUI;
import retrofit2.Call;
import retrofit2.Response;

public class ProductDetailPage extends BasePage {
    private ImageView productImage;
    private TextView salePriceText, productPriceText, promotionPercentText, productNameText, categoryNameText, brandNameText, productDescText;
    private TextView productNotInStoreName, productNotInStoreCategoryName, productNotInStoreBrandName, productNotInStoreDesc, storeNameTV;
    private Button addToCartBtn, findStoreBtn, editProductInStore;
    private LinearLayout isNotProductInStoreLayout, isProductInStoreLayout, productDetailLayout, storeNameLayout, isProductSaleLayout;

    private Product product; //intent

    private int storeID; //intent

    private String storeName; //intent nếu là isStoreProduct == true và isStoreSee == false

    private boolean isStoreProduct; //intent

    private boolean isStoreSee; //intent

    private int productId; //intent
    private boolean isStoreBefore; // intent true nếu đã có màn ProductInStoreByUser trước
    private ProgressBar loadingBar;
    private User user;
    private Store myStore;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private Store store;
    private LocationManager locationManager;
    final static int REQUEST_LOCATION = 1;


    private StorageReference storageReference = Firebase.getFirebase();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_describe_product);
        mapping();
        storeID = getIntent().getIntExtra("storeID", -1);
        isStoreProduct = getIntent().getBooleanExtra("isStoreProduct", false);
        isStoreSee = getIntent().getBooleanExtra("isStoreSee", false);
        isStoreBefore = getIntent().getBooleanExtra("isStoreBefore", false);
        product = new Gson().fromJson(getIntent().getStringExtra("product"),Product.class);
        if (product != null){
            getView();
        }
        else {
            productId = getIntent().getIntExtra("productId", -1);
            Call<Product> call = ApiUtils.getAPIService().getProductById(productId,storeID);
            new GetProductById().execute(call);
        }


    }

    public void getView(){
        getSupportActionBar().setTitle(product.getProduct_name());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        restoringPreferences();
        CustomInterface.setStatusBarColor(ProductDetailPage.this);
        loadingBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);
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
                        toEditProductInStorePage.putExtra("productName", product.getProduct_name());
                        toEditProductInStorePage.putExtra("productID", product.getProduct_id());
                        toEditProductInStorePage.putExtra("storeID", storeID);
                        toEditProductInStorePage.putExtra("categoryName", product.getCategory_name());
                        toEditProductInStorePage.putExtra("brandName", product.getBrand_name());
                        toEditProductInStorePage.putExtra("productPrice", product.getPrice());
                        toEditProductInStorePage.putExtra("promotionPercent", product.getPromotion());
                        toEditProductInStorePage.putExtra("productImageLink", product.getImage_path());
                        startActivity(toEditProductInStorePage);
                    }
                });
            } else {
                if (product.getPromotion() == 0.0){
                    isProductSaleLayout.setVisibility(View.VISIBLE);
                }
                storeName = getIntent().getStringExtra("storeName");
                storeNameLayout.setVisibility(View.VISIBLE);
                storeNameTV.setText(storeName);
                storeNameTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isStoreBefore) {
                            finish();
                            return;
                        }
                        Intent toProductInStoreByUser = new Intent(ProductDetailPage.this,ProductInStoreByUserDisplayPage.class);
                        toProductInStoreByUser.putExtra("storeID",storeID);
                        startActivity(toProductInStoreByUser);
                    }
                });
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
                    if (user == null){
                        Toast.makeText(ProductDetailPage.this, "Bạn chưa đăng nhập", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (myStore != null && storeID != -1){
                        if (myStore.getId() == storeID) {
                            Toast.makeText(ProductDetailPage.this, "Cửa hàng của bạn, không thể đặt hàng", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            Call<Store> call = ApiUtils.getAPIService().getStoreById(storeID);
                            new GetStoreById().execute(call);
                        }
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
                    toNearByStorePage.putExtra("productName",product.getProduct_name());
                    toNearByStorePage.putExtra("image_path",product.getImage_path());
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
        isProductSaleLayout = (LinearLayout) findViewById(R.id.isProductSaleLayout);

        productNotInStoreCategoryName = (TextView) findViewById(R.id.productNotInStoreCategoryName);
        productNotInStoreBrandName = (TextView) findViewById(R.id.productNotInStoreBrandName);
        productNotInStoreDesc = (TextView) findViewById(R.id.productNotInStoreDesc);
        storeNameLayout = (LinearLayout) findViewById(R.id.storeNameLayout);

    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailPage.this);
        builder.setTitle("Thêm sản phẩm vào giỏ hàng");
        builder.setMessage("Sản phẩm đã được thêm vào giỏ hàng");

        builder.setPositiveButton("Đến giỏ hàng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent toCartPage = new Intent(getBaseContext(), CartPage.class);
                toCartPage.putExtra("userID",user.getId());
                getBaseContext().startActivity(toCartPage);
                return;
            }
        });

        builder.setNegativeButton("Tiếp tục mua", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.show();
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

    private class GetStoreById extends AsyncTask<Call, Void, Store> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final Store store) {
            super.onPostExecute(store);
            setStore(store);
            addProductToCart();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected Store doInBackground(Call... calls) {
            try {
                Call<Store> call = calls[0];
                Response<Store> response = call.execute();
                return response.body();
            } catch (IOException e) {
            }
            return null;
        }
    }

    private class GetProductById extends AsyncTask<Call, Void, Product> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final Product product1) {
            if (product1 == null) {
                Toast.makeText(ProductDetailPage.this, "Có lỗi xảy ra!!!", Toast.LENGTH_SHORT).show();
                return;
            }
            product = product1;
            getView();
            super.onPostExecute(product1);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected Product doInBackground(Call... calls) {
            try {
                Call<Product> call = calls[0];
                Response<Product> response = call.execute();
                return response.body();
            } catch (IOException e) {
            }
            return null;
        }
    }

    private void setStore(Store store) {
        this.store = store;
    }

    private void addProductToCart(){
        if (store == null){
            Toast.makeText(ProductDetailPage.this, "Có lỗi xảy ra!!!", Toast.LENGTH_SHORT).show();
            return;
        }
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
                    double price =  product.getPrice();
                    if(product.getPromotion()!=0){
                        price = product.getPrice()*product.getPromotion();
                    }
                    CartDetail cartDetail  = new CartDetail(product.getProduct_id(),product.getProduct_name(),1,price,product.getImage_path());
                    myRef.child("cartDetail").child(String.valueOf(product.getProduct_id())).setValue(cartDetail);
                    showDialog();
                } else {
                    myRef.child("cartDetail").child(String.valueOf(product.getProduct_id())).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()){
                                double price =  product.getPrice();
                                if(product.getPromotion()!=0){
                                    price = product.getPrice()-(product.getPrice()*product.getPromotion()/100);
                                }
                                CartDetail cartDetail  = new CartDetail(product.getProduct_id(),product.getProduct_name(),1,price,product.getImage_path());
                                myRef.child("cartDetail").child(String.valueOf(product.getProduct_id())).setValue(cartDetail);
                                showDialog();
                            } else {
                                double price =  product.getPrice();
                                if(product.getPromotion()!=0){
                                    price = product.getPrice()-(product.getPrice()*product.getPromotion()/100);
                                }
                                if (price != dataSnapshot.child("unitPrice").getValue(Double.class)){
                                    myRef.child("cartDetail").child(String.valueOf(product.getProduct_id())).child("unitPrice").setValue(price);
                                }
                                myRef.child("cartDetail").child(String.valueOf(product.getProduct_id())).child("quantity").setValue((long)dataSnapshot.child("quantity").getValue()+1);
                                showDialog();
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
