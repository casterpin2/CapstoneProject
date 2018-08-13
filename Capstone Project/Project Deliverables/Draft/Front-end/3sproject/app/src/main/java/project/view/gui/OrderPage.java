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
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import com.google.gson.Gson;
import com.suke.widget.SwitchButton;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import project.firebase.Firebase;

import project.googleMapAPI.Address;
import project.googleMapAPI.Address_Component;
import project.googleMapAPI.GoogleMapJSON;
import project.objects.User;
import project.retrofit.ApiUtils;
import project.view.model.OrderDetail;
import project.view.R;
import project.view.model.Product;
import project.view.util.Formater;
import retrofit2.Call;
import retrofit2.Response;


public class OrderPage extends BasePage implements OnMapReadyCallback{
    //    int minteger = 1;
//    int max = 20000;
    private TextView productName, productPrice, salePrice, promotionPercent, sumOrder;
    private TextView tvBuyerNameError, etPhone, handleAddressText, etBuyerName;
    private Button decreaseBtn, increaseBtn, orderBtn;
    private EditText productQuantity, orderDate, orderTime;
    private SwitchButton switch_button;
    private ImageView productImage;
    private RelativeLayout productDetailLayout;

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
    private boolean isCart;
    private long price;
    private long salesPrice;
    private double promotion;
    private Product product;
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

        isCart = getIntent().getBooleanExtra("isCart", true);
        price = (long)getIntent().getDoubleExtra("price",0);
        promotion = getIntent().getDoubleExtra("promotion",0.0);
        Toast.makeText(OrderPage.this, price + " " + promotion, Toast.LENGTH_LONG).show();
        mapping();
        if (isCart) {
            String priceInCart = getIntent().getStringExtra("priceInCart");
            productDetailLayout.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Đặt hàng");
            sumOrder.setText(Formater.formatDoubleToMoney(String.valueOf(priceInCart)));
        } else {
            product = new Gson().fromJson(getIntent().getStringExtra("product"),Product.class);
            productDetailLayout.setVisibility(View.VISIBLE);
            getSupportActionBar().setTitle("Đặt hàng nhanh");
        }
        SharedPreferences pre = getSharedPreferences("authentication", Context.MODE_PRIVATE);
        String userJSON = pre.getString("user", "");
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
            etBuyerName.setEnabled(false);
            etPhone.setEnabled(false);
        } else {
            etBuyerName.setText("");
            etPhone.setText("");
        }


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        order = new OrderDetail();
        if (product != null && isCart == false) {

            productName.setText(product.getProduct_name());

            productPrice.setText(Formater.formatDoubleToMoney(String.valueOf(price)));

            productPrice.setPaintFlags(productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            promotionPercent.setText(Formater.formatDoubleToInt(String.valueOf(promotion)));

            salesPrice = getSalesPrice(price, promotion);

            salePrice.setText(Formater.formatDoubleToMoney(String.valueOf(salesPrice)));

            sumOrder.setText(Formater.formatDoubleToMoney(String.valueOf(salesPrice)));

            productQuantity.setText(String.valueOf(1));

            Glide.with(OrderPage.this /* context */)
                    .using(new FirebaseImageLoader())
                    .load(storageReference.child(product.getImage_path()))
                    .skipMemoryCache(true)
                    .into(productImage);

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
        } else if (product == null && isCart == false){
            defaultView();
            Toast.makeText(OrderPage.this, "Có lỗi xảy ra !!!", Toast.LENGTH_SHORT).show();
        }
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

                                orderDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
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
                    turnOnLocation();
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
            Intent intent = new Intent();
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
                if (orderDate.getText().toString().isEmpty()||orderTime.getText().toString().isEmpty()){
                    Toast.makeText(OrderPage.this, "Chọn ngày giờ", Toast.LENGTH_SHORT).show();
                    return;}
                if(switch_button.isChecked()){
                    longtitude = autoLongtitude ;
                    latitude = autoLatitude;
                    order = new OrderDetail();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(location.getApartment_number()+" ");
                    stringBuilder.append(location.getStreet()+" ");
                    stringBuilder.append(location.getDistrict()+" ");
                    stringBuilder.append(location.getCounty()+" ");
                    stringBuilder.append(location.getCity()+" ");
                    intent.putExtra("longtitude",autoLongtitude);
                    intent.putExtra("latitude",autoLatitude);
                    intent.putExtra("address",stringBuilder.toString().replaceAll("null","").replaceAll("0", "").replaceAll("Unnamed Road", "").replaceAll("\\s+", " ").trim());
                } else {
                    longtitude = 0.0;
                    latitude = 0.0;
                    if (handleAddressText.getText().toString().isEmpty()) {
                        Toast.makeText(OrderPage.this, "Chọn vị trí", Toast.LENGTH_SHORT).show();
                        return;}
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(location.getApartment_number()+" ");
                    stringBuilder.append(location.getStreet()+" ");
                    stringBuilder.append(location.getDistrict()+" ");
                    stringBuilder.append(location.getCounty()+" ");
                    stringBuilder.append(location.getCity()+" ");
                    intent.putExtra("longtitude",handleLatitude);
                    intent.putExtra("latitude",handleLongtitude);
                    intent.putExtra("address",stringBuilder.toString().replaceAll("null","").replaceAll("0", "").replaceAll("Unnamed Road", "").replaceAll("\\s+", " ").trim());
                }
                if ( isCart == false ){
                    int storeId = getIntent().getIntExtra("storeID",0);
                    String storeName = getIntent().getStringExtra("storeName");
                    String storePhone = getIntent().getStringExtra("phone");
                    String image_path = getIntent().getStringExtra("image_path");
                    intent.putExtra("userId",userID);
                    intent.putExtra("price",salesPrice);
                    intent.putExtra("totalPrice",salesPrice * quantity);
                    intent.putExtra("storeName", storeName);
                    intent.putExtra("storePhone", storePhone);
                    intent.putExtra("image_path", image_path);
                    intent.putExtra("storeID", storeId);
                    intent.putExtra("quantity",Integer.parseInt(productQuantity.getText().toString()));
                }
                intent.putExtra("deliverTime",orderDateTime);
                intent.putExtra("phone",phone);
                intent.putExtra("userName",userName);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

//        Glide.with(getContext() /* context */)
//                .using(new FirebaseImageLoader())
//                .load(storageReference.child(product.getProductImage()))
//                .into(productImage);

    }



    public long getSalesPrice(long productPrice, double promotionPercent){

        return  (long)(productPrice - (productPrice * promotionPercent /100));
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
        productDetailLayout = (RelativeLayout) findViewById(R.id.productDetailOrderPageLayout);
    }


    public void increaseQuantity() {
        decreaseBtn.setEnabled(true);
        int quantity = Integer.parseInt(productQuantity.getText().toString());
        quantity = quantity + 1;
        productQuantity.setText(String.valueOf(quantity));
        sumOrder.setText(Formater.formatDoubleToMoney(String.valueOf((salesPrice * quantity))));
    }

    public void decreaseQuantity() {
        int quantity = Integer.parseInt(productQuantity.getText().toString());
        quantity = quantity - 1;
        productQuantity.setText(String.valueOf(quantity));
        sumOrder.setText(Formater.formatDoubleToMoney(String.valueOf((salesPrice * quantity))));
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
                    increaseQuantity();
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
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(handleLatitude).append(",").append(handleLongtitude);
                    final Call<GoogleMapJSON> call = ApiUtils.getAPIServiceMap().getLocation(stringBuilder.toString(),GOOGLE_MAP_KEY);
                    new CallMapAPI().execute(call);
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
                mMap.setMinZoomPreference(12.0f);
                mMap.setMaxZoomPreference(22.0f);

            }

        }
    }

    private class CallMapAPI extends AsyncTask<Call, Void, project.view.model.Location> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(project.view.model.Location location1) {

            location.setStreet(location1.getStreet());
            location.setCity(location1.getCity());
            location.setDistrict(location1.getDistrict());
            location.setCounty(location1.getCounty());
            location.setApartment_number(location1.getApartment_number());
            super.onPostExecute(location1);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected project.view.model.Location doInBackground(Call... calls) {
            project.view.model.Location location = new project.view.model.Location();
            try {
                Call<GoogleMapJSON> call = calls[0];
                Response<GoogleMapJSON> response = call.execute();
                List<Address_Component> result = response.body().getResult();
                Address_Component component = result.get(0);
                final List<Address> address = component.getAddressList();
                for (int i = 0; i < address.size(); i++) {
                    Address a = address.get(i);
                    for (int j = 0; j < a.getTypes().length; j++) {
                        if (a.getTypes()[j].equalsIgnoreCase("street_number")) {
                            location.setApartment_number(a.getLong_name());
                        }
                        if (a.getTypes()[j].equalsIgnoreCase("route")) {
                            location.setStreet(a.getLong_name());
                        }
                        if (a.getTypes()[j].equalsIgnoreCase("administrative_area_level_2")) {
                            location.setCounty(a.getLong_name());
                        }
                        if (a.getTypes()[j].equalsIgnoreCase("administrative_area_level_3")) {
                            location.setDistrict(a.getLong_name());
                        }
                        if (a.getTypes()[j].equalsIgnoreCase("administrative_area_level_1")) {
                            location.setCity(a.getLong_name());
                        }
                    }
                }
            } catch (IOException e) {
            }
            return location;
        }
    }
    private void defaultView(){

        productName.setText("");

        productPrice.setText(Formater.formatDoubleToMoney(String.valueOf(0.0)));

        productPrice.setPaintFlags(productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        promotionPercent.setText(Formater.formatDoubleToInt(String.valueOf(0.0)));

        salePrice.setText(Formater.formatDoubleToMoney(String.valueOf(getSalesPrice(0, 1.0))));

        //sumOrder.setText(price);

        productQuantity.setText(String.valueOf(1));

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
    }
    private void turnOnLocation(){
        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
            }

            public void onProviderDisabled(String provider){
            }

            public void onProviderEnabled(String provider){ }
            public void onStatusChanged(String provider, int status,
                                        Bundle extras){ }
        };
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // getting GPS status
        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1000,locationListener);
//            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
////            googleMap.clear();
//            if (location != null) {
//                currentLatitude = location.getLatitude();
//                currentLongtitude = location.getLongitude();
//            } else {
//                Toast.makeText(this, "Bạn chưa bật định vị. Chưa thể tìm cửa hàng!!!!!", Toast.LENGTH_SHORT).show();
//            }
            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(this, "Bạn chưa bật định vị. Chưa thể xác định vị trí!", Toast.LENGTH_SHORT).show();
            }
            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        1000,
                        1000, locationListener);
                if (locationManager != null) {
                    Location location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        autoLatitude = location.getLatitude();
                        autoLongtitude = location.getLongitude();
                        this.location.setLatitude(String.valueOf(autoLatitude));
                        this.location.setLongitude(String.valueOf(autoLongtitude));
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(autoLatitude).append(",").append(autoLongtitude);
                        final Call<GoogleMapJSON> call = ApiUtils.getAPIServiceMap().getLocation(stringBuilder.toString(),GOOGLE_MAP_KEY);
                        new CallMapAPI().execute(call);
                        //Toast.makeText(RegisterStorePage.this, this.location.toString(), Toast.LENGTH_LONG).show();
                        //Toast.makeText(this, location1.toString(), Toast.LENGTH_SHORT).show();
                        markerToMap(autoLongtitude, autoLatitude, mMap, "Ví trí của bạn");
                    }
                }
            }
            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        1000,
                        1000, locationListener);
                if (locationManager != null) {
                    Location location = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        autoLatitude = location.getLatitude();
                        autoLongtitude = location.getLongitude();
                        this.location.setLatitude(String.valueOf(autoLatitude));
                        this.location.setLongitude(String.valueOf(autoLongtitude));
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(autoLatitude).append(",").append(autoLongtitude);
                        final Call<GoogleMapJSON> call = ApiUtils.getAPIServiceMap().getLocation(stringBuilder.toString(),GOOGLE_MAP_KEY);
                        new CallMapAPI().execute(call);
                        //Toast.makeText(RegisterStorePage.this, this.location.toString(), Toast.LENGTH_LONG).show();
                        //Toast.makeText(this, location1.toString(), Toast.LENGTH_SHORT).show();
                        markerToMap(autoLongtitude, autoLatitude, mMap, "Ví trí của bạn");
                    }
                }

            }
        }
    }
}
