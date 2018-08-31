package project.view.gui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import project.retrofit.ApiUtils;
import project.view.R;
import project.view.fragment.home.HomeFragment;
import project.view.fragment.home.StoreFragment;
import project.view.fragment.home.UserFragment;
import project.view.adapter.ViewPagerAdapter;
import project.view.model.Notification;
import project.view.model.NotificationDetail;
import project.view.model.ResultNotification;
import project.view.model.StoreNotification;
import retrofit2.Call;
import retrofit2.Response;


public class HomePage extends BasePage{

    BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;

    MenuItem prevMenuItem;
    private String userJSON;
    private String storeJSON;
    private ViewPagerAdapter adapter;
    private LocationManager locationManager;
    private ProgressBar loadingBar;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private boolean isOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        isOrder = getIntent().getBooleanExtra("isOrder",false);
        if (isOrder){
            //loadingBar.setVisibility(View.INVISIBLE);
            AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
            builder.setTitle("Đặt hàng");
            builder.setMessage("Bạn đã đặt hàng thành công");

            builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            builder.show();
        }
        loadingBar = findViewById(R.id.loadingBar);
        loadingBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);
        boolean isIntent = getIntent().getBooleanExtra("isLogin", false);
        if (isIntent){
            startActivity(new Intent(HomePage.this,LoginPage.class));
        }
        getAuthen();
        turnOnLocation();
        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorApplication));
        }
        //Initializing the bottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.action_store:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.action_user:
                                viewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(viewPager,userJSON,storeJSON);
    }

    private void setupViewPager(ViewPager viewPager, String userJSON, String storeJSON) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),userJSON,storeJSON);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }

    private void getAuthen(){
        restoringPreferences();
    }


    private void restoringPreferences(){
        if (userJSON == null) {
            SharedPreferences pre = getSharedPreferences("authentication", Context.MODE_PRIVATE);
            userJSON = pre.getString("user", "");
            storeJSON = pre.getString("store", "");
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Glide.get(this).clearMemory();

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
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }
}
