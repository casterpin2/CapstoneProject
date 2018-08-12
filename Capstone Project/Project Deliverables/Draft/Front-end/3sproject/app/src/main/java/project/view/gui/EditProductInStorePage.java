package project.view.gui;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import project.firebase.Firebase;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.model.Cart;
import project.view.model.CartDetail;
import project.view.util.CustomInterface;
import project.view.util.Formater;
import retrofit2.Call;
import retrofit2.Response;

public class EditProductInStorePage extends BasePage {

    private TextView productName,categoryName, brandName, promotionPercentErrorMessage, productPriceErrorMessage;
    private EditText productPrice, promotionPercent;
    private StorageReference storageReference = Firebase.getFirebase();
    private Button saveBtn;
    private ImageView imageView;
    private ScrollView scroll;
    private RelativeLayout main_layout;
    private String productNameValue;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product_in_store_page);
        findView();
        CustomInterface.setStatusBarColor(this);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view,getBaseContext());
                return false;
            }
        });

        productNameValue = getIntent().getStringExtra("productName");
        final String productImageLink = getIntent().getStringExtra("productImageLink");
        final int productIDValue = getIntent().getIntExtra("productID", -1);
        final int storeIDValue = getIntent().getIntExtra("storeID",-1);
        final String categoryNameValue = getIntent().getStringExtra("categoryName");
        final String brandNameValue = getIntent().getStringExtra("brandName");
        final long productPriceValue = getIntent().getLongExtra("productPrice",-1);
        final double promotionPercentValue = getIntent().getDoubleExtra("promotionPercent",-1.0);

        getSupportActionBar().setTitle(productNameValue);

        scroll.setVerticalScrollBarEnabled(false);
        scroll.setHorizontalScrollBarEnabled(false);

        Glide.with(this /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(productImageLink))
                .into(imageView);
        productName.setText(productNameValue);
        categoryName.setText(categoryNameValue);
        brandName.setText(brandNameValue);
        productPrice.setText(Formater.formatDoubleToMoney(String.valueOf(productPriceValue)));
        productPrice.setSelection(productPrice.getText().toString().length());
        promotionPercent.setText(String.valueOf(promotionPercentValue));
        promotionPercent.setSelection(promotionPercent.getText().toString().length());
        productPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                productPrice.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();
                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    productPrice.setText(formattedString);
                    productPrice.setSelection(productPrice.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                productPrice.addTextChangedListener(this);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productPrice.getText().toString().length() > 0 ) {
                    productPriceErrorMessage.setText("");
                } else {
                    productPriceErrorMessage.setText(getResources().getString(R.string.error_empty));
                }
                if(promotionPercent.getText().toString().length() > 0) {
                    promotionPercentErrorMessage.setText("");
                } else {
                    promotionPercentErrorMessage.setText(getResources().getString(R.string.promotionOption));
                }
                if(promotionPercent.getText().toString().length() > 0 && productPrice.getText().toString().length() > 0) {

                    if (Double.parseDouble(promotionPercent.getText().toString()) > 100.00 || Double.parseDouble(promotionPercent.getText().toString()) < 0.00) {
                        promotionPercentErrorMessage.setText(getResources().getString(R.string.promotionOption));

                    } else {
                        final long priceLong = Long.parseLong(productPrice.getText().toString().replaceAll(",+", "").replaceAll("\\.+", "").replaceAll("đ","").trim());
                        final double promotionValue = Double.parseDouble(promotionPercent.getText().toString());
                        promotionPercentErrorMessage.setText("");
                        if (productPriceValue == priceLong && promotionPercentValue == promotionValue ) {
                            Toast.makeText(EditProductInStorePage.this, getResources().getString(R.string.unchange), Toast.LENGTH_SHORT).show();

                        } else {
                            myRef = database.getReference().child("cart");
                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                        String userId = dataSnapshot1.getKey();
                                        for (DataSnapshot dataSnapshot2 :dataSnapshot1.getChildren()){
                                            Cart cart = dataSnapshot2.getValue(Cart.class);
                                            Object[] cartDetails = cart.getCartDetail().values().toArray();
                                            for(int i = 0 ; i < cartDetails.length;i++) {
                                                String productId = String.valueOf(((CartDetail)cartDetails[i]).getProductId());
                                                if (((CartDetail)cartDetails[i]).getProductId() == productIDValue){
                                                    myRef.child(userId).child(String.valueOf(cart.getStoreId())).child("cartDetail").child(productId).child("unitPrice").setValue(priceLong-(priceLong*promotionValue/100));
                                                }
                                            }
                                        }
                                    }
                                    Call<Boolean> call = ApiUtils.getAPIService().editProductInStore(storeIDValue,productIDValue,priceLong,promotionValue);
                                    new EditProduct().execute(call);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(EditProductInStorePage.this,"Có lỗi xảy ra !!!",Toast.LENGTH_LONG).show();
                                }
                            });

                        }

                    }
                }
            }
        });
    }

    private void findView(){
        main_layout = findViewById(R.id.main_layout);
        productName = (TextView) findViewById(R.id.productName);
        categoryName = (TextView) findViewById(R.id.categoryName);
        brandName = (TextView) findViewById(R.id.brandName);
        productPrice = (EditText) findViewById(R.id.productPrice);
        promotionPercent = (EditText) findViewById(R.id.promotionPercent);
        promotionPercentErrorMessage = (TextView) findViewById(R.id.promotionPercentErrorMessage);
        productPriceErrorMessage = (TextView) findViewById(R.id.productPriceErrorMessage);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        imageView = (ImageView) findViewById(R.id.productImage);
        scroll = (ScrollView) findViewById(R.id.scroll);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                productPrice = (EditText) findViewById(R.id.productPrice);
                promotionPercent = (EditText) findViewById(R.id.promotionPercent);
                final long productPriceValue = getIntent().getLongExtra("productPrice",-1);
                final double promotionPercentValue = getIntent().getDoubleExtra("promotionPercent",-1.0);
                long priceLong = 0;
                if (productPrice.getText().toString().length() != 0){
                    priceLong= Long.parseLong(productPrice.getText().toString().replaceAll("\\.+", "").replaceAll("đ","").trim().replaceAll(",+",""));
                }
                double promotionValue = 0.0;
                if(promotionPercent.getText().toString().length() != 0){
                    promotionValue = Double.parseDouble(promotionPercent.getText().toString());
                }

                if (productPriceValue == priceLong && promotionPercentValue == promotionValue ) {
                    finish();
                } else if (productPriceValue != priceLong || promotionPercentValue != promotionValue || (productPrice.getText().toString().length() != 0 && promotionPercent.getText().toString().length() != 0 )) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditProductInStorePage.this);
                    builder.setTitle(R.string.back_alertdialog_title);
                    builder.setMessage(R.string.back_alertdialog_content);

                    builder.setPositiveButton(R.string.alertdialog_acceptButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

                    builder.setNegativeButton(R.string.alertdialog_cancelButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    builder.show();
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class EditProduct extends AsyncTask<Call, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Call... calls) {
            try {
                Call<Boolean> call = calls[0];
                Response<Boolean> re = call.execute();
                boolean result = re.body();
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if (result == null) {
                Toast.makeText(EditProductInStorePage.this,"Không có kết nối mạng!",Toast.LENGTH_LONG).show();
                return;
            }
            if (!result){
                Toast.makeText(EditProductInStorePage.this,"Có lỗi xảy ra !!!",Toast.LENGTH_LONG).show();
                return;
            } else {
                Toast.makeText(EditProductInStorePage.this,"Sửa thông tin " + productNameValue + " thành công",Toast.LENGTH_LONG).show();
                finish();
            }
            super.onPostExecute(result);

        }
    }
}
