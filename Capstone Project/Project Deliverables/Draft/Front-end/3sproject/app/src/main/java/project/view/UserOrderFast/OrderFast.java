package project.view.UserOrderFast;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.storage.StorageReference;
import com.suke.widget.SwitchButton;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;

import project.firebase.Firebase;

import project.googleMapAPI.GoogleMapJSON;
import project.retrofit.ApiUtils;
import project.view.EditUserInformation.EditUserInformationPage;
import project.view.Login.Login;
import project.view.Login.LoginPage;
import project.view.NearbyStore.NearbyStorePage;
import project.view.R;
import project.view.RegisterStore.RegisterStorePage;
import retrofit2.Call;


public class OrderFast extends AppCompatActivity implements OnMapReadyCallback{
//    int minteger = 1;
//    int max = 20000;
    private TextView productName, productPrice, salePrice, promotionPercent, sumOrder;
    private TextView tvBuyerNameError, etPhone, handleAddressText, etBuyerName;
    private Button decreaseBtn, increaseBtn, orderBtn;
    private EditText productQuantity, orderDate, orderTime;
    private SwitchButton switch_button;
    private ImageView productImage;

    private RelativeLayout handleAddressLayout;

    private OrderDetail order;
    private ProductDetail productDetail;

    private int userID;

    //googleMap
    private final project.view.RegisterStore.Location location = new project.view.RegisterStore.Location();
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private LocationManager locationManager;
    double handleLongtitude = 0.0;
    double handleLatitude = 0.0;
    double autoLatitude = 0.0;
    double autoLongtitude = 0.0;
    private final String GOOGLE_MAP_KEY = "AIzaSyB_Wiwy7Mu3fjzCHO_7E5dn5n1n6ZaxaWs";
    final static int REQUEST_LOCATION = 1;
    private GoogleMap mMap;
    private String handleLocationPlace = "";

    //Calendar
    private int mYear, mMonth, mDay, mHour, mMinute;

    private Context context;

    private StorageReference storageReference = Firebase.getFirebase();

    public Context getContext() {
        return context;
    }

    public void setProductName(TextView productName) {
        this.productName = productName;
    }

    public void setAutoLatitude(double autoLatitude) {
        this.autoLatitude = autoLatitude;
    }

    public void setAutoLongtitude(double autoLongtitude) {
        this.autoLongtitude = autoLongtitude;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_fast);
        mapping();

        userID = LoginPage.login.getUser().getId();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Đặt hàng nhanh");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        order = new OrderDetail();
        productDetail = new ProductDetail(1,"Samsung Galaxy Note 8 64Gb màu xám","",20000,10);

        productName.setText(productDetail.getProductName());

        productPrice.setText(formatLongToMoney(getContext(),String.valueOf(productDetail.getProductPrice())));
        productPrice.setPaintFlags(productPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        promotionPercent.setText(formatDoubleToString(getContext(),String.valueOf(productDetail.getPromotionPercent())));

        salePrice.setText(formatLongToMoney(getContext(), String.valueOf(getSalesPrice(productDetail.getProductPrice(), productDetail.getPromotionPercent()))));

        sumOrder.setText(formatLongToMoney(getContext(),String.valueOf((getSalesPrice(productDetail.getProductPrice(), productDetail.getPromotionPercent()))* Integer.parseInt(productQuantity.getText().toString()))));

        productQuantity.setText(String.valueOf(1));

        if (userID == 0) {

        } else {

        }

        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseQuantity();
            }
        });

        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseQuantity();
            }
        });

        orderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(OrderFast.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                orderDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        orderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                mHour = calendar.get(Calendar.HOUR_OF_DAY);
                mMinute = calendar.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(OrderFast.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                orderTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        handleAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(OrderFast.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });

        switch_button.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(view.isChecked()) {
                    getLocation();
                    handleAddressLayout.setEnabled(false);
                    handleAddressText.setText("Vị trí hiện tại của bạn");
                } else {
                    setAutoLatitude(0.0);
                    setAutoLongtitude(0.0);
                    handleAddressText.setText(handleLocationPlace);
                    handleAddressLayout.setEnabled(true);
                    if(handleLongtitude == 0.0 && handleLatitude == 0.0) {
                        LatLng defaultLocation = new LatLng(21.028511, 105.804817);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));
                        mMap.setMinZoomPreference(10.0f);
                        mMap.setMaxZoomPreference(10.1f);
                    } else {
                        markerToMap(handleLongtitude,handleLatitude,mMap,"Vị trí đăng kí");
                    }

                }
            }

        });

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etBuyerName.getText().toString();
                int productID = getIntent().getIntExtra("productID", 0);
                int storeID = getIntent().getIntExtra("storeID", 0);
                long finalPrice = getSalesPrice(productDetail.getProductPrice(), productDetail.getPromotionPercent())* Integer.parseInt(productQuantity.getText().toString()) ;
                int quantity = Integer.parseInt(productQuantity.getText().toString());
                String phone = etPhone.getText().toString();
                String orderDateTime = orderDate.getText().toString() + " " + orderTime.getText().toString();
                double longtitude = autoLongtitude ;
                double latitude = autoLatitude;
                String address = "";
                if(switch_button.isChecked()){
                    longtitude = autoLongtitude ;
                    latitude = autoLatitude;
                    order = new OrderDetail();
                } else {
                    longtitude = 0.0 ;
                    latitude = 0.0;
                    address = handleLocationPlace;
                }

                order = new OrderDetail(userID, userName, productID, storeID, finalPrice, quantity, phone, orderDateTime, longtitude, latitude, address);
                Toast.makeText(OrderFast.this, "order: "+ order.getUserID()+"- "+ order.getUserName()+"- "+ order.getProductID()+"- "+ order.getStoreID()+"- "+ order.getFinalPrice()+"- "+ order.getProductQuantity()+"- "+ order.getPhone()+"- "+order.getOrderDateTime()+" - "+ order.getLongtitude()+"- "+ order.getLatitude()+"- "+ order.getAddress(), Toast.LENGTH_LONG).show();
            }
        });

//        Glide.with(getContext() /* context */)
//                .using(new FirebaseImageLoader())
//                .load(storageReference.child(product.getProductImage()))
//                .into(productImage);

    }



    public long getSalesPrice(long productPrice, double promotionPercent){
        double salePriceDouble = productDetail.getProductPrice() * productDetail.getPromotionPercent() / 100;
        long salePriceLong = (long) salePriceDouble;
        long displayPrice = productDetail.getProductPrice() - salePriceLong ;
        return  displayPrice;
    }

        public void mapping(){
            productImage = (ImageView) findViewById(R.id.productImage);
            productName = (TextView) findViewById(R.id.productName);
            productPrice = (TextView) findViewById(R.id.productPrice);
            salePrice = (TextView) findViewById(R.id.salePrice);
            promotionPercent = (TextView) findViewById(R.id.promotionPercent);
            sumOrder = (TextView) findViewById(R.id.sumOrder);
            etBuyerName = (TextView) findViewById(R.id.etBuyerName);
            tvBuyerNameError = (TextView) findViewById(R.id.tvBuyerNameError);
            etPhone = (TextView) findViewById(R.id.etPhone);
            handleAddressText = (TextView) findViewById(R.id.handleAddressText);
            decreaseBtn = (Button) findViewById(R.id.decreaseBtn);
            increaseBtn = (Button) findViewById(R.id.increaseBtn);
            orderBtn = (Button) findViewById(R.id.orderBtn);
            productQuantity = (EditText) findViewById(R.id.productQuantity);
            switch_button = (SwitchButton) findViewById(R.id.switch_button);
            handleAddressLayout = (RelativeLayout) findViewById(R.id.handleAddressLayout);
            orderDate = (EditText) findViewById(R.id.orderDate);
            orderTime = (EditText) findViewById(R.id.orderTime);
        }


    public void increaseQuantity() {
        int quantity = Integer.parseInt(productQuantity.getText().toString());
        quantity = quantity + 1;
        productQuantity.setText(String.valueOf(quantity));
        sumOrder.setText(formatLongToMoney(getContext(),String.valueOf((getSalesPrice(productDetail.getProductPrice(), productDetail.getPromotionPercent()))* quantity)));
    }

    public void decreaseQuantity() {
        int quantity = Integer.parseInt(productQuantity.getText().toString());
        quantity = quantity - 1;
        productQuantity.setText(String.valueOf(quantity));
        sumOrder.setText(formatLongToMoney(getContext(),String.valueOf((getSalesPrice(productDetail.getProductPrice(), productDetail.getPromotionPercent()))* quantity)));
        if (quantity == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(OrderFast.this);
            builder.setTitle("Hủy sản phẩm");
            builder.setMessage("Số lượng sản phẩm bạn chọn là 0. Bạn có muốn dừng việc đặt hàng tại đây?");

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
            decreaseBtn.setEnabled(false);
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
        price = String.format("%s %%", price);
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

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                autoLatitude = location.getLatitude();
                autoLongtitude = location.getLongitude();
                this.location.setLatitude(String.valueOf(autoLatitude));
                this.location.setLongitude(String.valueOf(autoLongtitude));
//                StringBuilder stringBuilder = new StringBuilder();
//                stringBuilder.append(autoLatitude).append(",").append(autoLongtitude);
//                mAPI = ApiUtils.getAPIServiceMap();
//                final Call<GoogleMapJSON> call = mAPI.getLocation(stringBuilder.toString(),GOOGLE_MAP_KEY);
//                new RegisterStorePage.CallMapAPI().execute(call);
                markerToMap(autoLongtitude, autoLatitude, mMap, "Ví trí của bạn");
            } else {
                Toast.makeText(this, "Chưa có vị trí định vị!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
//                cityID = data.getIntExtra("cityID", cityID);
//                townID = data.getIntExtra("townID", townID);
//                communeID = data.getIntExtra("communeID", communeID);

//                city.setText(String.valueOf(cityID));
//                town.setText(String.valueOf(townID));
//                commune.setText(String.valueOf(communeID));

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }

            if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    handleLocationPlace = place.getAddress().toString();
                    handleAddressText.setText(handleLocationPlace);
                    handleLongtitude = place.getLatLng().longitude;
                    handleLatitude = place.getLatLng().latitude;
                    this.location.setLatitude(String.valueOf(handleLatitude));
                    this.location.setLongitude(String.valueOf(handleLongtitude));
//                    StringBuilder stringBuilder = new StringBuilder();
//                    stringBuilder.append(handleLatitude).append(",").append(handleLongtitude);
//                    mAPI = ApiUtils.getAPIServiceMap();
//                    final Call<GoogleMapJSON> call = mAPI.getLocation(stringBuilder.toString(),GOOGLE_MAP_KEY);
//                    new RegisterStorePage.CallMapAPI().execute(call);
                    markerToMap(handleLongtitude, handleLatitude, mMap, "Vị trí đăng kí");
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    // TODO: Handle the error.
                    Toast.makeText(OrderFast.this, "An error occurred: " + status, Toast.LENGTH_SHORT).show();

                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
            }
        }
    }

    public void markerToMap(double longtitude, double latitude, GoogleMap mMap, String markerContent) {
        markerContent = "";
        LatLng myLocation = new LatLng(latitude, longtitude);
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(myLocation).title(markerContent));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        mMap.setMinZoomPreference(15);
        mMap.setMaxZoomPreference(25);
        mMap.getUiSettings().isMyLocationButtonEnabled();
        mMap.getUiSettings().setScrollGesturesEnabled(false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                double latitude = 21.028511;
                double longtitude = 105.804817;

//                 Add a marker in Ha Noi and move the camer
                LatLng hanoi = new LatLng(latitude, longtitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(hanoi));
                mMap.setMinZoomPreference(10.0f);

            }

        }
    }
}
