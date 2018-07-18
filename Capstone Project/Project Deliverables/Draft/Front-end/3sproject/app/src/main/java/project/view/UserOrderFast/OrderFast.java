package project.view.UserOrderFast;


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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import project.firebase.Firebase;

import project.view.NearbyStore.NearbyStorePage;
import project.view.R;





public class OrderFast extends AppCompatActivity {
    int minteger = 1;
    int max = 20000;
    private ImageView productImage;
    private TextView nameOrderFast, salePrice, promotionPercent1, saleProductPrice,sumOrder;
    private TextView etStoreName, etPhone;

    private Context context;

    private String productName ;
    private int orderID;
    private boolean isStoreProduct;

    final static int REQUEST_LOCATION = 1;

    private StorageReference storageReference = Firebase.getFirebase();

    public Context getContext() {
        return context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_fast);
        mapping();


        productName = getIntent().getStringExtra("productName");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        orderID = getIntent().getIntExtra("orderID", -1);
//       isStoreProduct = getIntent().getBooleanExtra("isStoreProduct", false);
//        setLayout(isStoreProduct,isNotProductInStoreLayout,isProductInStoreLayout, productDetailLayout, findStoreBtn);
//        Glide.with(getContext() /* context */)
//                .using(new FirebaseImageLoader())
//                .load(storageReference.child(product.getProductImage()))
//                .into(productImage);



            salePrice.setText(formatLongToMoney(getContext(),String.valueOf(salePrice.getText())));
            saleProductPrice.setPaintFlags(saleProductPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            saleProductPrice.setText(formatLongToMoney(getContext(),String.valueOf(saleProductPrice.getText())));
            promotionPercent1.setText(formatDoubleToString(getContext(), String.valueOf(promotionPercent1.getText())));
            sumOrder.setText(formatLongToMoney(getContext(),String.valueOf(sumOrder.getText())));

           //double salePriceDouble = isStoreProduct.getProductPrice() * productInStore.getPromotionPercent() / 100;
//            long salePriceLong = (long) salePriceDouble;
//            long displayPrice = productInStore.getProductPrice() - salePriceLong ;
//            salePriceText.setText(formatLongToMoney(getContext(), String.valueOf(displayPrice)));
//
//            productNameText.setText(productInStore.getProductName());
//
//            categoryNameText.setText(productInStore.getCategoryName());
//            brandNameText.setText(productInStore.getBrandName());

//            productDescText.setText(productInStore.getProductDesc());


    }
        public void mapping(){
            productImage = (ImageView) findViewById(R.id.orderFastImage);
            salePrice = (TextView) findViewById(R.id.salePrice);
            saleProductPrice = (TextView) findViewById(R.id.saleProductPrice);
            promotionPercent1 = (TextView) findViewById(R.id.promotionPercent1);
            nameOrderFast = (TextView) findViewById(R.id.nameOrderFast);
            sumOrder = (TextView) findViewById(R.id.sumOrder);
            etStoreName = (TextView) findViewById(R.id.etStoreName);
            etPhone = (TextView) findViewById(R.id.etPhone);
        }


    public void increaseInteger(View view) {
        minteger = minteger + 1;
        display(minteger);
        int pri = minteger * max;
        TextView dcm = (TextView) findViewById(R.id.sumOrder);
        dcm.setText(pri );
    }
    public void decreaseInteger(View view) {
        if(minteger > 1) {
            minteger = minteger - 1;
            display(minteger);
            int pri = minteger * max;
            TextView dcm = (TextView) findViewById(R.id.sumOrder);
            dcm.setText(pri);
        }


    }

    private void display(int number) {
        TextView displayInteger = (TextView) findViewById(
                R.id.integer_number);
        displayInteger.setText("" + number);
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
