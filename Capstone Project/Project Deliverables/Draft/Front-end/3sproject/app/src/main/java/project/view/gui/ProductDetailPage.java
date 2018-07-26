package project.view.gui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;

import project.firebase.Firebase;
import project.view.R;
import project.view.model.ProductDetail;
import project.view.model.ProductInStoreDetail;
import project.view.util.Formater;

public class ProductDetailPage extends AppCompatActivity {
    private ImageView productImage;
    private TextView salePriceText, productPriceText, promotionPercentText, productNameText, categoryNameText, brandNameText, productDescText;
    private TextView productNotInStoreName, productNotInStoreCategoryName, productNotInStoreBrandName, productNotInStoreDesc;
    private Button addToCartBtn, findStoreBtn;
    private LinearLayout isNotProductInStoreLayout, isProductInStoreLayout, productDetailLayout;
    private ProductInStoreDetail productInStore;
    private ProductDetail product;
    private Context context;

    private String productName ;
    private int storeID;
    private boolean isStoreProduct;

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


        productName = getIntent().getStringExtra("productName");
        getSupportActionBar().setTitle(productName);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storeID = getIntent().getIntExtra("storeID", -1);
        isStoreProduct = getIntent().getBooleanExtra("isStoreProduct", false);
        setLayout(isStoreProduct,isNotProductInStoreLayout,isProductInStoreLayout, productDetailLayout, findStoreBtn);
//        Glide.with(context /* context */)
//                .using(new FirebaseImageLoader())
//                .load(storageReference.child(product.getProductImage()))
//                .into(productImage);

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
            product = new ProductDetail(1,"Samsung Galaxy S9 Plus","Hello", "Điện thoại","Samsung","Đây là sản phẩm không trong cửa hàng");

            productNotInStoreName.setText(product.getProductName());
            productNotInStoreCategoryName.setText(product.getCategoryName());
            productNotInStoreBrandName.setText(product.getBrandName());
            productNotInStoreDesc.setText(product.getProductDesc());
            findStoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String productName = product.getProductName();
                    Intent toNearbyStorePage = new Intent(ProductDetailPage.this, NearbyStorePage.class);
                    toNearbyStorePage.putExtra("productName",productName);
                    if (ActivityCompat.checkSelfPermission(ProductDetailPage.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(ProductDetailPage.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ProductDetailPage.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                    } else {
                        startActivity(toNearbyStorePage);
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
}
