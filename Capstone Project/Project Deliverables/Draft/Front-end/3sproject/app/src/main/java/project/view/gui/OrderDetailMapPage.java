package project.view.gui;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import project.view.R;
import project.view.util.TweakUI;

public class OrderDetailMapPage extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LocationManager locationManager;
    double latitude = 0.0;
    double longtitude = 0.0;
    final static int REQUEST_LOCATION = 1;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_map_page);
        TweakUI.makeTransparent(OrderDetailMapPage.this);
        turnOnLocation();
        backBtn = (Button) findViewById(R.id.backBtn);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            LatLng myLocation = new LatLng(latitude, longtitude);
            googleMap.addMarker(new MarkerOptions().position(myLocation).title("Vị trí của bạn"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
            if (location != null) {
                latitude = getIntent().getDoubleExtra("latitude",0.0);
                longtitude = getIntent().getDoubleExtra("longtitude",0.0);
                if (latitude != 0.0 && longtitude != 0.0){
                    changeLocation(mMap);
                }
            }

        }
    }

    public void changeLocation (GoogleMap googleMap) {
        double storeLongtitude = longtitude;
        double storeLatitude = latitude;
        LatLng orderAddress = new LatLng(storeLatitude, storeLongtitude);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.setMinZoomPreference(12.0f);
        googleMap.setMaxZoomPreference(22.0f);
        googleMap.addMarker(new MarkerOptions().position(orderAddress).title("Địa chỉ giao hàng").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(orderAddress));
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
                Toast.makeText(this, "Bạn chưa bật định vị. Chưa thể tìm cửa hàng!!!!!", Toast.LENGTH_SHORT).show();
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
                        latitude = location.getLatitude();
                        longtitude = location.getLongitude();
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
                        latitude = location.getLatitude();
                        longtitude = location.getLongitude();
                    }
                }

            }
        }
    }
}
