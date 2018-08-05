package project.view.gui;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.google.gson.Gson;
import com.suke.widget.SwitchButton;

import java.util.Calendar;

import project.firebase.Firebase;

import project.objects.User;
import project.view.model.OrderDetail;
import project.view.R;
import project.view.util.Formater;


public class OrderPage extends AppCompatActivity implements OnMapReadyCallback{
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


    private int userID;

    //googleMap
    private final project.view.model.Location location = new project.view.model.Location();
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
    private User user;

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
        SharedPreferences pre = getSharedPreferences("authentication", Context.MODE_PRIVATE);
        String userJSON = pre.getString("user", "");
        Toast.makeText(this,userJSON,Toast.LENGTH_LONG);
        if (userJSON .isEmpty()){
            user = new User();
        } else {
            user = new Gson().fromJson(userJSON,User.class);
        }

        userID = user.getId();
        if (userID != 0) {
            String buyerName = user.getFirst_name() + " " + user.getLast_name();
            String buyerPhone = user.getPhone();
            etBuyerName.setText(buyerName);
            etPhone.setText(buyerPhone);
        } else {
            etBuyerName.setText("");
            etPhone.setText("");
        }


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Đặt hàng nhanh");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        order = new OrderDetail();
        productName.setText("");

        productPrice.setText(Formater.formatDoubleToMoney(String.valueOf(0.0)));
        productPrice.setPaintFlags(productPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        promotionPercent.setText(Formater.formatDoubleToInt(String.valueOf(0.0)));

        salePrice.setText(Formater.formatDoubleToMoney(String.valueOf(getSalesPrice(0, 1.0))));

        sumOrder.setText(Formater.formatDoubleToMoney(String.valueOf((getSalesPrice(0, 0.0))* Integer.parseInt(productQuantity.getText().toString()))));

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
                DatePickerDialog datePickerDialog = new DatePickerDialog(OrderPage.this,
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(OrderPage.this,
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
                                    .build(OrderPage.this);
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
                //long finalPrice = getSalesPrice(productDetail.getProductPrice(), productDetail.getPromotionPercent())* Integer.parseInt(productQuantity.getText().toString()) ;
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

                order = new OrderDetail(userID, userName, productID, storeID, 0, quantity, phone, orderDateTime, longtitude, latitude, address);
                Toast.makeText(OrderPage.this, "order: "+ order.getUserID()+"- "+ order.getUserName()+"- "+ order.getProductID()+"- "+ order.getStoreID()+"- "+ order.getFinalPrice()+"- "+ order.getProductQuantity()+"- "+ order.getPhone()+"- "+order.getOrderDateTime()+" - "+ order.getLongtitude()+"- "+ order.getLatitude()+"- "+ order.getAddress(), Toast.LENGTH_LONG).show();
            }
        });

//        Glide.with(getContext() /* context */)
//                .using(new FirebaseImageLoader())
//                .load(storageReference.child(product.getProductImage()))
//                .into(productImage);

    }



    public long getSalesPrice(long productPrice, double promotionPercent){
        double salePriceDouble = 0.0;
        long salePriceLong = (long) salePriceDouble;
        long displayPrice = 0;
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
        //sumOrder.setText(Formater.formatDoubleToMoney(String.valueOf((getSalesPrice(productDetail.getProductPrice(), productDetail.getPromotionPercent()))* quantity)));
    }

    public void decreaseQuantity() {
        int quantity = Integer.parseInt(productQuantity.getText().toString());
        quantity = quantity - 1;
        productQuantity.setText(String.valueOf(quantity));
        //sumOrder.setText(Formater.formatDoubleToMoney(String.valueOf((getSalesPrice(productDetail.getProductPrice(), productDetail.getPromotionPercent()))* quantity)));
        if (quantity == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(OrderPage.this);
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
                    Toast.makeText(OrderPage.this, "An error occurred: " + status, Toast.LENGTH_SHORT).show();

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
