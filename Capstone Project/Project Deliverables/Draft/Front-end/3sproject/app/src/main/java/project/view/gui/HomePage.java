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

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 100 && resultCode == Activity.RESULT_OK)
//            Log.e("MSG","CAC");
//        if (requestCode == 3) {
//            if (resultCode == Activity.RESULT_OK) {
//                loadingBar.setVisibility(View.VISIBLE);
//                if (!isNetworkAvailable()) {
//                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(HomePage.this, "Có lỗi xảy ra. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
//                            loadingBar.setVisibility(View.INVISIBLE);
//                        }
//                    }, 10000);
//                    return;
//                }
//
//                final String storeName = data.getStringExtra("storeName");
//                final int productId = data.getIntExtra("productId", 0);
//                final String address = data.getStringExtra("address");
//                final String deliverTime = data.getStringExtra("deliverTime");
//                final int storeId = data.getIntExtra("storeID", 0);
//                final long totalPrice = data.getLongExtra("totalPrice", 0);
//                final long price = data.getLongExtra("price", 0);
//                final String userName = data.getStringExtra("userName");
//                final String phone = data.getStringExtra("phone");
//                final double longtitude = data.getDoubleExtra("longtitude", 0.0);
//                final double latitude = data.getDoubleExtra("latitude", 0.0);
//                final int quantity = data.getIntExtra("quantity", 0);
//                final String productName = data.getStringExtra("productName");
//                final String image_path_product = data.getStringExtra("image_path_product");
//                final DatabaseReference reference = database.getReference().child("ordersStore").child(String.valueOf(storeId));
//                reference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        String key = reference.push().getKey();
//                        DatabaseReference re = reference.child(key);
//                        re.child("address").setValue(address);
//                        re.child("latitude").setValue(latitude);
//                        re.child("longtitude").setValue(longtitude);
//                        re.child("image_path").setValue("User/image/1.jpg");
//                        re.child("deliverTime").setValue(deliverTime);
//                        re.child("status").setValue("waitting");
//                        re.child("userId").setValue(0);
//                        re.child("phone").setValue(phone);
//                        re.child("userName").setValue(userName);
//                        re.child("totalPrice").setValue(totalPrice);
//                        re.child("orderDetail").child(String.valueOf(productId)).child("quantity").setValue(quantity);
//                        re.child("orderDetail").child(String.valueOf(productId)).child("unitPrice").setValue(price);
//                        re.child("orderDetail").child(String.valueOf(productId)).child("productId").setValue(productId);
//                        re.child("orderDetail").child(String.valueOf(productId)).child("productName").setValue(productName);
//                        re.child("orderDetail").child(String.valueOf(productId)).child("image_path").setValue(image_path_product);
//
//                        re = database.getReference().child("notification").child(String.valueOf(storeId));
//                        re.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                if (dataSnapshot.exists()) {
//                                    StoreNotification store = dataSnapshot.getValue(StoreNotification.class);
//                                    if (store != null && store.getHaveNotification() != null && store.getToken() != null) {
//                                        if (store.getHaveNotification().equals("false")) {
//                                            String token = store.getToken();
//                                            Notification notification = new Notification();
//                                            notification.setTo(token);
//                                            notification.setNotification(new NotificationDetail(storeName, "Bạn có đơn hàng mới"));
//                                            Call<ResultNotification> call = ApiUtils.getAPIServiceFirebaseMessage().sendNotification(notification, getResources().getString(R.string.notificationKey), "application/json");
//                                            new PushNotification(storeId).execute(call);
//                                        }
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                        loadingBar.setVisibility(View.INVISIBLE);
//                        AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
//                        builder.setTitle("Đặt hàng");
//                        builder.setMessage("Bạn đã đặt hàng thành công");
//
//                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                return;
//                            }
//                        });
//                        builder.show();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
//            if (resultCode == Activity.RESULT_CANCELED){
//                Toast.makeText(this, "Hủy đặt hàng", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
    public class PushNotification extends AsyncTask<Call,Void,ResultNotification> {
        private int storeId;

        public int getStoreId() {
            return storeId;
        }

        public void setStoreId(int storeId) {
            this.storeId = storeId;
        }

        public PushNotification(int storeId) {
            this.storeId = storeId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ResultNotification result) {
            super.onPostExecute(result);
            if (result != null) {
                //Toast.makeText(CartPage.this, result.toString(), Toast.LENGTH_SHORT).show();
                DatabaseReference databaseReference = database.getReference().child("notification").child(String.valueOf(storeId)).child("haveNotification");
                databaseReference.setValue("true");
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ResultNotification doInBackground(Call... calls) {
            try {
                Call<ResultNotification> call = calls[0];
                Response<ResultNotification> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
