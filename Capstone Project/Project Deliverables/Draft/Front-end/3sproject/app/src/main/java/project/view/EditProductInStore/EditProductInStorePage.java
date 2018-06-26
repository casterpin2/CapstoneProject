package project.view.EditProductInStore;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import project.view.ProductInStore.ProductInStore;
import project.view.R;

public class EditProductInStorePage extends AppCompatActivity {

    private TextView productName,categoryName, brandName, promotionPercentErrorMessage, productPriceErrorMessage;
    private EditText productPrice, promotionPercent;
    private ProductInStore product;
    private Button testBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product_in_store_page);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        productName = (TextView) findViewById(R.id.productName);
        categoryName = (TextView) findViewById(R.id.categoryName);
        brandName = (TextView) findViewById(R.id.brandName);
        productPrice = (EditText) findViewById(R.id.productPrice);
        promotionPercent = (EditText) findViewById(R.id.promotionPercent);
        promotionPercentErrorMessage = (TextView) findViewById(R.id.promotionPercentErrorMessage);
        productPriceErrorMessage = (TextView) findViewById(R.id.productPriceErrorMessage);
        testBtn = (Button) findViewById(R.id.testBtn);

        final String productNameValue = getIntent().getStringExtra("productName");
        final String productImageLink = getIntent().getStringExtra("productImageLink");
        final int productIDValue = getIntent().getIntExtra("productID", -1);
        int storeIDValue = getIntent().getIntExtra("storeID",-1);
        final String categoryNameValue = getIntent().getStringExtra("categoryName");
        final String brandNameValue = getIntent().getStringExtra("brandName");
        long productPriceValue = getIntent().getLongExtra("productPrice",-1);
        double promotionPercentValue = getIntent().getDoubleExtra("promotionPercent",-1.0);

        getSupportActionBar().setTitle(productNameValue);
        productName.setText(productNameValue);
        categoryName.setText(categoryNameValue);
        brandName.setText(brandNameValue);
        productPrice.setText(convertLongToString(productPriceValue));
        promotionPercent.setText(String.valueOf(promotionPercentValue));
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

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productPrice.getText().toString().length() > 0 ) {
                    productPriceErrorMessage.setText("");
                } else {
                    productPriceErrorMessage.setText("Trường này không thể để trống");
                }
                if(promotionPercent.getText().toString().length() > 0) {
                    promotionPercentErrorMessage.setText("");
                } else {
                    promotionPercentErrorMessage.setText("Trường này không thể để trống");
                }
                if(promotionPercent.getText().toString().length() > 0 && productPrice.getText().toString().length() > 0) {

                    if (Double.parseDouble(promotionPercent.getText().toString()) > 100.00 || Double.parseDouble(promotionPercent.getText().toString()) < 0.00) {
                        promotionPercentErrorMessage.setText("Chiết khấu phải ở trong khoảng từ 0% tới 100%!!");

                    } else {
                        long priceLong = Long.parseLong(productPrice.getText().toString().replaceAll(",", ""));
                        double promotionValue = Double.parseDouble(promotionPercent.getText().toString());
                        promotionPercentErrorMessage.setText("");
                        product = new ProductInStore(productIDValue, productNameValue, productImageLink, categoryNameValue, brandNameValue, priceLong, promotionValue);
                        Toast.makeText(EditProductInStorePage.this, productIDValue + " id - " + productNameValue + " name - " + productImageLink + " - " + categoryNameValue + " - " + brandNameValue + " - " + priceLong + " - " + promotionValue, Toast.LENGTH_SHORT).show();


                    }
                }
            }
        });
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
    public String convertLongToString (long needConvert) {
        String formattedString = null;
        try {
//            String originalString = s.toString();
//            Long longval;
//            if (originalString.contains(",")) {
//                originalString = originalString.replaceAll(",", "");
//            }
//            longval = Long.parseLong(originalString);

            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            formatter.applyPattern("#,###,###,###");
            formattedString = formatter.format(needConvert);

            //setting text after format to EditText

        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        return formattedString;
    }
}
