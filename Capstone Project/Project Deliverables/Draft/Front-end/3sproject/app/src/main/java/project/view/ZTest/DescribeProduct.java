package project.view.ZTest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import project.firebase.Firebase;
import project.view.NearbyStore.NearbyStore;
import project.view.NearbyStore.NearbyStorePage;
import project.view.R;

public class DescribeProduct extends AppCompatActivity {
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

            productPriceText.setText(formatLongToMoney(getContext(),String.valueOf(productInStore.getProductPrice())));
            productPriceText.setPaintFlags(productPriceText.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

            promotionPercentText.setText(formatDoubleToString(getContext(), String.valueOf(productInStore.getPromotionPercent())));

            double salePriceDouble = productInStore.getProductPrice() * productInStore.getPromotionPercent() / 100;
            long salePriceLong = (long) salePriceDouble;
            long displayPrice = productInStore.getProductPrice() - salePriceLong ;
            salePriceText.setText(formatLongToMoney(getContext(), String.valueOf(displayPrice)));

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
                    Intent toNearbyStorePage = new Intent(DescribeProduct.this, NearbyStorePage.class);
                    toNearbyStorePage.putExtra("productName",productName);
                    if (ActivityCompat.checkSelfPermission(DescribeProduct.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(DescribeProduct.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(DescribeProduct.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
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

    public static String formatLongToMoney(Context context, String price) {

        DecimalFormat formatter = new DecimalFormat("###,###,###");

        price = (!TextUtils.isEmpty(price)) ? price : "0";
        price = price.trim();
        price = formatter.format(Double.parseDouble(price));
        price = price.replaceAll("\\.", "\\,");

        price = String.format("%s đ", price);
        return price;
    }

    public static String formatDoubleToString(Context context, String price) {

        NumberFormat format =
                new DecimalFormat("#,##0.0");// #,##0.00 ¤ (¤:// Currency symbol)
        format.setCurrency(Currency.getInstance(Locale.US));//Or default locale

        price = (!TextUtils.isEmpty(price)) ? price : "0";
        price = price.trim();
        price = format.format(Double.parseDouble(price));
        price = price.replaceAll(",", "\\.");

        if (price.endsWith(".0")) {
            int centsIndex = price.lastIndexOf(".0");
            if (centsIndex != -1) {
                price = price.substring(0, centsIndex);
            }
        }
        price = String.format("%s%%", price);
        return price;
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
