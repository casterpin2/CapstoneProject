package project.view.gui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.google.gson.Gson;
import com.suke.widget.SwitchButton;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import project.googleMapAPI.Address;
import project.googleMapAPI.Address_Component;
import project.googleMapAPI.GoogleMapJSON;
import project.objects.User;
import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.fragment.home.HomeFragment;
import project.view.model.Store;
import project.view.util.CustomInterface;
import project.view.util.Regex;
import retrofit2.Call;
import retrofit2.Response;

public class RegisterStorePage extends AppCompatActivity implements OnMapReadyCallback {
    private final String GOOGLE_MAP_KEY = "AIzaSyB_Wiwy7Mu3fjzCHO_7E5dn5n1n6ZaxaWs";
    final static int REQUEST_LOCATION = 1;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private APIService mAPI;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private RelativeLayout handleAddressLayout;
    private Button registerBtn;
    private TextView handleAddressText, tvStoreNameError,tvPhoneError,tvLocationError;
    private EditText etStoreName, etPhone;
    private String handleLocationPlace = "";
    private SwitchButton iconSwitch;
    public project.view.model.Location location = new project.view.model.Location();
    double handleLongtitude = 0.0;
    double handleLatitude = 0.0;
    double autoLatitude = 0.0;
    double autoLongtitude = 0.0;
    private String result = "";
    private RelativeLayout main_layout;
    private ProgressBar loadingBar;
    private boolean isPhone = false,isStoreName = false,isLoction=false;

    public void setAutoLatitude(double autoLatitude) {
        this.autoLatitude = autoLatitude;
    }

    public void setAutoLongtitude(double autoLongtitude) {
        this.autoLongtitude = autoLongtitude;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_store_page);
        findView();
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view,getBaseContext());
                return false;
            }
        });
        CustomInterface.setStatusBarColor(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSupportActionBar().setTitle(R.string.registerStorePageTitle);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);



        etStoreName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    Regex regex = new Regex();
                    String input = etStoreName.getText().toString();
                    if(!regex.isName(input)){
                        tvStoreNameError.setText(R.string.errorStoreName);
                        isStoreName = false;
                    }
                    else
                        tvStoreNameError.setText("");
                        isStoreName = true;
                }
            }
        });

        etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                isPhone = true;
                if(!hasFocus){
                   if(etPhone.getText().length()<10||etPhone.getText().length()>11){
                       tvPhoneError.setText(R.string.error_validate_phone);
                       isPhone= false;
                   }
                   else {
                       tvPhoneError.setText("");
                       isPhone = true;
                   }
                }
            }
        });
        iconSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                tvLocationError.setText("");
                if(view.isChecked()) {
                    turnOnLocation();
                    handleAddressLayout.setEnabled(false);
                    handleAddressText.setText(R.string.yourGPS);
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

        handleAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(RegisterStorePage.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }

        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoction = true;

                if((handleLongtitude==0&&handleLatitude==0)&&(autoLatitude==0||autoLongtitude==0)){
                    tvLocationError.setText("Chưa xác định được vị trí của bạn.");
                    isLoction = false;
                }
                else {
                    tvLocationError.setText("");
                }
                if (!isStoreName){
                    tvStoreNameError.setText(R.string.errorStoreName);
                }
                if (!isPhone){
                    tvPhoneError.setText(R.string.error_validate_phone);
                }

                if(isPhone&&isStoreName&&isLoction) {
                    String storeName = etStoreName.getText().toString().trim();
                    String phone = etPhone.getText().toString().trim();
                    int user_id = getIntent().getIntExtra("user_id", 0);
                    if (user_id != 0) {
                        callAPIRegisterStore(new Store(storeName, user_id, phone), location);
                    }
                }
            }
        });

    }

    private void findView(){

        handleAddressLayout = (RelativeLayout) findViewById(R.id.handleAddressLayout);
        main_layout = findViewById(R.id.main_layout);
        handleAddressText = (TextView) findViewById(R.id.handleAddressText);
        tvStoreNameError = (TextView) findViewById(R.id.tvStoreNameError);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etStoreName = (EditText) findViewById(R.id.etStoreName);
        main_layout = findViewById(R.id.main_layout);
        iconSwitch = findViewById(R.id.switch_button);
        loadingBar = findViewById(R.id.loadingBar);
        tvPhoneError = findViewById(R.id.tvPhoneError);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        tvLocationError = findViewById(R.id.tvLocationError);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }

            if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    tvLocationError.setText("");
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    handleLocationPlace = place.getAddress().toString();
                    handleAddressText.setText(handleLocationPlace);
                    handleLongtitude = place.getLatLng().longitude;
                    handleLatitude = place.getLatLng().latitude;
                    this.location.setLatitude(String.valueOf(handleLatitude));
                    this.location.setLongitude(String.valueOf(handleLongtitude));
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(handleLatitude).append(",").append(handleLongtitude);
                    mAPI = ApiUtils.getAPIServiceMap();
                    final Call<GoogleMapJSON> call = mAPI.getLocation(stringBuilder.toString(),GOOGLE_MAP_KEY);
                    new CallMapAPI().execute(call);
                    markerToMap(handleLongtitude, handleLatitude, mMap, "Vị trí đăng kí");
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    // TODO: Handle the error.

                } else if (resultCode == RESULT_CANCELED) {
                }
            }
        }
    }


    //Đây là hàm do Đạt sửa bá đạo
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
            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(this, getResources().getString(R.string.noGPSYet), Toast.LENGTH_SHORT).show();
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
                        mAPI = ApiUtils.getAPIServiceMap();
                        final Call<GoogleMapJSON> call = mAPI.getLocation(stringBuilder.toString(),GOOGLE_MAP_KEY);
                        new CallMapAPI().execute(call);
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
                        mAPI = ApiUtils.getAPIServiceMap();
                        final Call<GoogleMapJSON> call = mAPI.getLocation(stringBuilder.toString(),GOOGLE_MAP_KEY);
                        new CallMapAPI().execute(call);
                        markerToMap(autoLongtitude, autoLatitude, mMap, getResources().getString(R.string.yourGPS));
                    }
                }

            }
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                final int storeNameLength = etStoreName.getText().toString().length();
                final int phoneLength = etPhone.getText().toString().length();

                if (storeNameLength == 0 && phoneLength == 0
                        && handleLatitude == 0.0 && handleLongtitude == 0.0
                        && autoLatitude == 0.0 && autoLongtitude == 0.0) {

                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterStorePage.this);
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
    private class CallAPI extends AsyncTask<Call, Void, Store> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Store result) {
            super.onPostExecute(result);
            if (result != null) {
                loadingBar.setVisibility(View.INVISIBLE);
                Toast.makeText(RegisterStorePage.this, "Đăng kí cửa hàng thành công", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterStorePage.this, HomePage.class);
                SharedPreferences preferences = getSharedPreferences("authentication", Context.MODE_PRIVATE);
                User user = new Gson().fromJson(preferences.getString("user",""),User.class);
                user.setHasStore(1);
                Log.d("",user.toString());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("store", new Gson().toJson(result));
                editor.putString("user", new Gson().toJson(user));
                //chấp nhận lưu xuống file
                editor.commit();
                finishAffinity();
                finish();
                startActivity(intent);
            } else {
                Toast.makeText(RegisterStorePage.this, "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected Store doInBackground(Call... calls) {
            Store result = null;
            try {
                Call<Store> call = calls[0];
                Response<Store> response = call.execute();
                if (response != null) {
                    result = response.body();
                }
            } catch (IOException e) {
            }
            return result;
        }
    }
    private void callAPIRegisterStore (Store store, project.view.model.Location location) {
        String jSon1 = new Gson().toJson(store);
        String jSon2 = new Gson().toJson(location);
        HashMap<String, String> map = new HashMap<>();
        map.put("store",jSon1);
        map.put("location",jSon2);
        Log.d("store",jSon1);
        Log.d("location",jSon2);
        Log.d("location",map.toString());
        mAPI = ApiUtils.getAPIService();
        final Call<Store> call = mAPI.registerStore(map);
        new CallAPI().execute(call);
    }
}
