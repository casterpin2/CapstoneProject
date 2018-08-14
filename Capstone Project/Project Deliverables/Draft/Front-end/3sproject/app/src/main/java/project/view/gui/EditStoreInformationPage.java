package project.view.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import project.googleMapAPI.Address;
import project.googleMapAPI.Address_Component;
import project.googleMapAPI.GoogleMapJSON;
import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.fragment.home.StoreFragment;
import project.view.model.Store;
import project.view.model.StoreInformation;
import project.view.util.CustomInterface;
import retrofit2.Call;
import retrofit2.Response;

public class EditStoreInformationPage extends BasePage implements OnMapReadyCallback {
    private EditText etStoreName, etPhone;
    private TextView handleAddressText;
    private RelativeLayout handleAddressLayout,main_layout;
    private SwitchButton switch_button;

    //getIntent data
    private int storeID = 0;
    private String storeName = "";
    private String ownerName = "";
    private String address = "";
    private String phone = "";
    private String registedDate = "";
    private StoreInformation storeInformation;
    private double storeLongtitude = 0.0;
    private double storeLatitude = 0.0;
    private Button updateInformationStore;
    public project.view.model.Location location = new project.view.model.Location();
    //googleMap
    private double handleLongtitude = 0.0;
    private double handleLatitude = 0.0;
    private double autoLatitude = 0.0;
    private double autoLongtitude = 0.0;
    private GoogleMap mMap;
    private String handleLocationPlace = "";
    private LocationManager locationManager;
    private final String GOOGLE_MAP_KEY = "AIzaSyB_Wiwy7Mu3fjzCHO_7E5dn5n1n6ZaxaWs";
    final static int REQUEST_LOCATION = 1;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private APIService mAPI;
    private APIService apiService;
    private Store dataStore;
    SharedPreferences pre;
    String storeJson;
    String userJson;
    public void setAutoLatitude(double autoLatitude) {
        this.autoLatitude = autoLatitude;
    }

    public void setAutoLongtitude(double autoLongtitude) {
        this.autoLongtitude = autoLongtitude;
    }
    private final int RESULT_CODE = 2222;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_store_information_page);
        CustomInterface.setSoftInputMode(this);
        CustomInterface.setStatusBarColor(this);
        findView();
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view,getBaseContext());
                return false;
            }
        });
        getSupportActionBar().setTitle("Thay đổi thông tin cửa hàng");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getIntentFromStoreInformationPage();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        etStoreName.setText(storeName);
        handleAddressText.setText(address);
        etPhone.setText(phone);

        switch_button.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(view.isChecked()) {
                    turnOnLocation();
                    handleAddressText.setText("Vị trí hiện tại");

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
                        markerToMap(handleLongtitude,handleLatitude,mMap,"Vị trí đăng kí",handleLocationPlace);
                    }
                }
                if (switch_button.isChecked() == false && handleAddressText.getText().toString().length() == 0) {
                    markerToMap(storeLongtitude,storeLatitude,mMap,"Vị trí hiện tại của cửa hàng",address);
                    handleAddressText.setText(address);
                }
            }

        });

        handleAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(EditStoreInformationPage.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }

        });

        if (switch_button.isChecked() == false && handleAddressText.getText().toString().length() == 0) {
            markerToMap(storeLongtitude,storeLatitude,mMap,"Vị trí hiện tại của cửa hàng", address);
        }

        updateInformationStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checkChange = false;

                pre = getSharedPreferences("authentication", Context.MODE_PRIVATE);
                storeJson = pre.getString("store","");
                Store storePre = new Gson().fromJson(storeJson,Store.class);
                Log.e("MSG",storeJson+" "+location.getApartment_number()+" "+location.getDistrict()+" "+ location.getCity()+" "+location.getCounty());
                if(!storePre.getName().equals(etStoreName.getText().toString())){
                    storePre.setName(etStoreName.getText().toString());
                    checkChange = true;
                }
                if(!storePre.getPhone().equals(etPhone.getText().toString())){
                    storePre.setPhone(etPhone.getText().toString());
                    checkChange = true;
                }
                if(!checkChange && location.toString().equals("CANCEL")) {
                    Intent backToStoreFragment = new Intent(EditStoreInformationPage.this,StoreFragment.class);
                    backToStoreFragment.putExtra("userData",userJson);
                    backToStoreFragment.putExtra("storeData",storeJson);
                    setResult(RESULT_CODE,backToStoreFragment);
                    finish();
                    Toast.makeText(EditStoreInformationPage.this, "Thông tin không thay đổi", Toast.LENGTH_SHORT).show();
                }
                if(location.toString().equals("OK")){
                    String locationJson = new Gson().toJson(location);
                    String storePreJson = new Gson().toJson(storePre);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("store",storePreJson);
                    map.put("location",locationJson);
                    apiService = ApiUtils.getAPIService();
                    final Call<Store> callBrand = apiService.updateStore(map);
                    new StoreData().execute(callBrand);

                }else if( checkChange && location.toString().equals("CANCEL")){
                    String locationJson = new Gson().toJson(location);
                    String storePreJson = new Gson().toJson(storePre);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("store",storePreJson);
                    map.put("location",locationJson);
                    apiService = ApiUtils.getAPIService();
                    final Call<Store> callBrand = apiService.updateStore(map);
                    new StoreData().execute(callBrand);
                }
            }
        });
    }

    public void findView(){
        main_layout = findViewById(R.id.main_layout);
        etStoreName = (EditText) findViewById(R.id.etStoreName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        handleAddressText = (TextView) findViewById(R.id.handleAddressText);
        handleAddressLayout = (RelativeLayout) findViewById(R.id.handleAddressLayout);
        switch_button = (SwitchButton) findViewById(R.id.switch_button);
        updateInformationStore = findViewById(R.id.updateBtn);
    }

    public void getIntentFromStoreInformationPage() {
        storeName = getIntent().getStringExtra("storeName");
        ownerName = getIntent().getStringExtra("ownerName");
        address = getIntent().getStringExtra("address");
        phone = getIntent().getStringExtra("phone");
        registedDate = getIntent().getStringExtra("registedDate");
        storeLongtitude = getIntent().getDoubleExtra("longtitude", 0.0);
        storeLatitude = getIntent().getDoubleExtra("latitude", 0.0);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
        //Write your code here
        Intent backToUserFragment = new Intent(EditStoreInformationPage.this, StoreFragment.class);

        setResult(RESULT_CODE,backToUserFragment);
        finish();
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            android.location.Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                autoLatitude = location.getLatitude();
                autoLongtitude = location.getLongitude();
//                StringBuilder stringBuilder = new StringBuilder();
//                stringBuilder.append(autoLatitude).append(",").append(autoLongtitude);
//                mAPI = ApiUtils.getAPIServiceMap();
//                final Call<GoogleMapJSON> call = mAPI.getLocation(stringBuilder.toString(),GOOGLE_MAP_KEY);
//                new RegisterStorePage.CallMapAPI().execute(call);
                markerToMap(autoLongtitude, autoLatitude, mMap, getResources().getString(R.string.yourGPS),"");
            } else {
                Toast.makeText(this, "Chưa có vị trí định vị!!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getIntentFromStoreInformationPage();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {
            Location locationLibary = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (locationLibary != null) {
                double latitude = storeLatitude;
                double longtitude = storeLongtitude;

//                 Add a marker in Ha Noi and move the camera
                LatLng storeAddress = new LatLng(latitude, longtitude);
                mMap.addMarker(new MarkerOptions().position(storeAddress).title("Vị trí hiện tại của cửa hàng").snippet(address)).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(storeAddress));
                mMap.setMinZoomPreference(12.0f);
                mMap.setMaxZoomPreference(22.0f);
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
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(handleLatitude).append(",").append(handleLongtitude);
                    mAPI = ApiUtils.getAPIServiceMap();
                    final Call<GoogleMapJSON> call = mAPI.getLocation(stringBuilder.toString(),GOOGLE_MAP_KEY);
                    new CallMapAPI().execute(call);
                    markerToMap(handleLongtitude, handleLatitude, mMap, "Vị trí đăng kí",handleLocationPlace);
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    // TODO: Handle the error.
                    Toast.makeText(EditStoreInformationPage.this, "Có lỗi xảy ra:  " + status, Toast.LENGTH_SHORT).show();

                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
            }
        }
    }
    public void markerToMap(double longtitude, double latitude, GoogleMap mMap, String markerContent, String addressString) {
        LatLng myLocation = new LatLng(latitude, longtitude);
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(myLocation).title(markerContent).snippet(addressString)).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        mMap.setMinZoomPreference(12);
        mMap.setMaxZoomPreference(22);
    }
    private void turnOnLocation(){
        final android.location.LocationListener locationListener = new android.location.LocationListener() {
            public void onLocationChanged(android.location.Location location) {
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
                    android.location.Location location = locationManager
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
                        markerToMap(autoLongtitude, autoLatitude, mMap, getResources().getString(R.string.yourGPS),"");
                    }
                }
            }
            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        1000,
                        1000, locationListener);
                if (locationManager != null) {
                    android.location.Location location = locationManager
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
                        markerToMap(autoLongtitude, autoLatitude, mMap, getResources().getString(R.string.yourGPS),"");
                    }
                }

            }
        }
    }
    public class CallMapAPI extends AsyncTask<Call, Void, project.view.model.Location> {


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
    public class StoreData extends AsyncTask<Call,Void,Store>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Store store) {
            super.onPostExecute(store);
             storeJson = new Gson().toJson(store);
             userJson = pre.getString("user","");
            SharedPreferences.Editor editor = pre.edit();
            editor.putString("user", userJson);
            editor.putString("store", storeJson);
            editor.commit();
            Toast.makeText(EditStoreInformationPage.this, "Thay đổi thông tin thành công", Toast.LENGTH_SHORT).show();
            Intent backToStoreFragment = new Intent(EditStoreInformationPage.this,StoreFragment.class);
            backToStoreFragment.putExtra("userData",userJson);
            backToStoreFragment.putExtra("storeData",storeJson);
            setResult(RESULT_CODE,backToStoreFragment);
            finish();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Store doInBackground(Call... calls) {
            try{
                Call<Store> call = calls[0];
                Response<Store> response = call.execute();
                if(response.body()!=null){
                    dataStore=  response.body();
                }
                return dataStore;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

}
