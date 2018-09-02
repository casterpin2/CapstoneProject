package project.view.gui;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import project.googleMapAPI.DirectionFinder;
import project.googleMapAPI.DirectionFinderListener;
import project.googleMapAPI.Route;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.adapter.NearByStoreListViewAdapter;
import project.view.model.NearByStore;
import project.view.model.Notification;
import project.view.model.NotificationDetail;
import project.view.model.Product;
import project.view.model.ResultNotification;
import project.view.model.StoreNotification;
import project.view.util.CustomInterface;
import retrofit2.Call;
import retrofit2.Response;

public class NearbyStorePage extends BasePage implements OnMapReadyCallback, LocationListener, DirectionFinderListener {
    private FusedLocationProviderClient mFusedLocationClient;
    private ListView storeListView;
    private NearByStoreListViewAdapter adapter;
    private List<NearByStore> list = new ArrayList<>();
    private LinearLayout main_layout;
    private GoogleMap mMap;
    private Context context;
    private LocationManager locationManager;
    private int productId;
    private SearchView searchView;
    private TextView textContent, noHaveStore;
    private ProgressBar loadingBar;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    final DatabaseReference myRef1 = myRef.child("suggestion").child("product");
    private ValueEventListener listener;
    private final List<String> suggestions = new ArrayList<>();
    private final List<String> searchedList = new ArrayList<>();
    // google map
    double latitude = 0.0;
    double longtitude = 0.0;
    final static int REQUEST_LOCATION = 1;
    public View footerView;
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private Product p;
    private MapRadar mapRadar;
    private String productName;

    @Override
    protected void onResume() {
        super.onResume();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                suggestions.clear();
                for (DataSnapshot dttSnapshot2 : dataSnapshot.getChildren()) {
                    suggestions.add(dttSnapshot2.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        myRef1.addValueEventListener(listener);
        if (mapRadar != null) {
            mapRadar.startRadarAnimation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (myRef1 != null) {
            myRef1.removeEventListener(listener);
        }
        if (mapRadar != null) {
            if (mapRadar.isAnimationRunning()) {
                mapRadar.stopRadarAnimation();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_store_page);
        context = this;
        CustomInterface.setStatusBarColor(this);
        storeListView = findViewById(R.id.storeListView);
        textContent = findViewById(R.id.textContent);
        noHaveStore = findViewById(R.id.noHaveStore);
        loadingBar = findViewById(R.id.loadingBar);
        loadingBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);
        // cái này nhận tên sản phẩm từ các màn sang
        productName = getIntent().getStringExtra("productName");
        CustomInterface.setStatusBarColor(this);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        productId = getIntent().getIntExtra("productId", -1);
        String image_path = getIntent().getStringExtra("image_path");
        p = new Product(productId, productName, image_path);
        turnOnLocation();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        main_layout = findViewById(R.id.main_layout);
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view, getBaseContext());
                return false;
            }
        });
        String sourceString = "Cửa hàng quanh đây có <b>" + productName + "</b> ";
        textContent.setText(Html.fromHtml(sourceString));
        adapter = new NearByStoreListViewAdapter(NearbyStorePage.this, R.layout.nearby_store_page_custom_list_view, list, p,String.valueOf(latitude)+","+String.valueOf(longtitude));
        storeListView.setAdapter(adapter);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footerView = li.inflate(R.layout.footer_loading_listview_lazy_loading, null);

        storeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        storeListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            if (mapRadar.isAnimationRunning())
                                mapRadar.withLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                            latitude = location.getLatitude();
                            longtitude = location.getLongitude();
                        }
                    }
                });
        mMap.setMyLocationEnabled(true);
        LatLng myLocation = new LatLng(latitude, longtitude);
        //googleMap.addMarker(new MarkerOptions().position(myLocation).title("Vị trí của bạn")).showInfoWindow();
        mapRadar = new MapRadar(mMap, myLocation, context);
        mapRadar.withDistance(5000);
        mapRadar.withOuterCircleStrokeColor(0xfccd29);
        mapRadar.withRadarColors(0x00fccd29, 0xfffccd29);
        mapRadar.startRadarAnimation();
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        if (productId != -1) {
            NearByStoreAsynTask asynTask = new NearByStoreAsynTask();
            Call<List<NearByStore>> call = ApiUtils.getAPIService().nearByStore(productId, String.valueOf(latitude), String.valueOf(longtitude));
            asynTask.execute(call);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view_with_find_icon, menu);
        MenuItem itemSearch = menu.findItem(R.id.search_view);
        searchView = (SearchView) itemSearch.getActionView();
        searchView.setIconifiedByDefault(false);

        searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.LEFT));
//        searchView.setSearchableInfo(true);
        searchView.setQuery(productName, true);
        searchView.clearFocus();

        if(searchView.getQuery() != null) {

            int index = storeListView.getFirstVisiblePosition();
            View v = storeListView.getChildAt(0);
            int top = (v == null) ? 0 : v.getTop();
            storeListView.setSelectionFromTop(index, top);

            if (mMap != null) {
                changeLocation(mMap);
            }

        } else {
            Toast.makeText(NearbyStorePage.this, "Không có gì cả : "+ searchView.getQuery(), Toast.LENGTH_SHORT).show();
        }


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        final CursorAdapter suggestionAdapter = new SimpleCursorAdapter(this,
                R.layout.custom_suggested_listview_nearby_store_page,
                null,
                new String[]{SearchManager.SUGGEST_COLUMN_TEXT_1},
                new int[]{android.R.id.text1},
                0);
        searchView.setSuggestionsAdapter(suggestionAdapter);






        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                int index = storeListView.getFirstVisiblePosition();
                View v = storeListView.getChildAt(0);
                int top = (v == null) ? 0 : v.getTop();
                storeListView.setSelectionFromTop(index, top);
                String productName = "Cửa hàng quanh đây có <b>" + query + "</b> ";
                textContent.setText(Html.fromHtml(productName));
                if (mMap != null) {
                    changeLocation(mMap);
                }
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                String[] columns = { BaseColumns._ID,
                        SearchManager.SUGGEST_COLUMN_TEXT_1,
                };
                final MatrixCursor cursor = new MatrixCursor(columns);
                searchedList.removeAll(searchedList);

                for (int i = 0; i < suggestions.size(); i++) {
                    if (suggestions.get(i).toLowerCase().contains(newText.toLowerCase())) {
                        String[] tmp = {Integer.toString(i), suggestions.get(i)};
                        cursor.addRow(tmp);

                        searchedList.add(suggestions.get(i).toString());
                    }
                }
                suggestionAdapter.swapCursor(cursor);
                return true;
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                searchView.setQuery(searchedList.get(position).toString(), true);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                searchView.setQuery(searchedList.get(position).toString(), true);
                searchView.clearFocus();
                return true;
            }
        });

        return true;
    }

    public void changeLocation (GoogleMap googleMap) {
        double storeLongtitude = 0.0;
        double storeLatitude = 0.0;
        LatLng storeLatLng;
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.setMinZoomPreference(13.0f);
        googleMap.setMaxZoomPreference(22.0f);
        for (int i = 0; i < list.size(); i++) {
            final int inti = i;
            storeLongtitude = list.get(i).getLongitude();
            storeLatitude = list.get(i).getLatitude();
            storeLatLng = new LatLng(storeLatitude, storeLongtitude);
            googleMap.addMarker(new MarkerOptions().position(storeLatLng).title(list.get(i).getName()).snippet(list.get(i).getAddress()));
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    String origin = latitude+","+longtitude;
                    String destination = marker.getPosition().latitude+","+marker.getPosition().longitude;
                    try {
                        new DirectionFinder((NearbyStorePage)context, origin, destination).execute();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mapRadar != null){
            if (mapRadar.isAnimationRunning()) {
                mapRadar.withLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Xin chờ...",
                "Đang tìm đường đi", true);
        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        for (Route route : routes) {
            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    @Override
    public void onDirectionFinderFailed() {
        progressDialog.dismiss();
        Toast.makeText(this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
    }

    public class NearByStoreAsynTask extends AsyncTask<Call, Void, List<NearByStore>> {

        @Override
        protected void onPreExecute() {
            loadingBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected List<NearByStore> doInBackground(Call... calls) {
            try {
                Call<List<NearByStore>> call = calls[0];
                Response<List<NearByStore>> re = call.execute();
                return re.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<NearByStore> listNearByStore) {
            super.onPostExecute(listNearByStore);
            list.clear();

            if (listNearByStore != null) {
                for (NearByStore near : listNearByStore) {
                    list.add(near);
                }
                loadingBar.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
                changeLocation(mMap);
                if (list.size() == 0) {
                    noHaveStore.setVisibility(View.VISIBLE);
                } else {
                    noHaveStore.setVisibility(View.INVISIBLE);
                }
            } else {
//
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NearbyStorePage.this, "Có lỗi xảy ra. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
//                            nullMessage.setText("Có lỗi xảy ra, vui lòng tải lại trang!");
                        loadingBar.setVisibility(View.INVISIBLE);
                        noHaveStore.setVisibility(View.VISIBLE);
                        noHaveStore.setText("Có lỗi xảy ra, vui lòng tải lại trang!");

                    }
                },3000);
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
                showGPSDisabledAlertToUser();
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                loadingBar.setVisibility(View.VISIBLE);
                if (!isNetworkAvailable()) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NearbyStorePage.this, "Có lỗi xảy ra. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                            loadingBar.setVisibility(View.INVISIBLE);
                        }
                    },10000);
                    return;
                }

                final String address = data.getStringExtra("address");
                final String deliverTime = data.getStringExtra("deliverTime");
                final int storeId = data.getIntExtra("storeID",0);
                final int userId = data.getIntExtra("userId",0);
                final String storeName = data.getStringExtra("storeName");
                final long totalPrice = data.getLongExtra("totalPrice",0);
                final long price = data.getLongExtra("price",0);
                final String userName = data.getStringExtra("userName");
                final String storePhone = data.getStringExtra("storePhone");
                final String image_path = data.getStringExtra("image_path");
                final String userImage = data.getStringExtra("user_image");
                final String phone = data.getStringExtra("phone");
                final double longtitude = data.getDoubleExtra("longtitude",0.0);
                final double latitude = data.getDoubleExtra("latitude",0.0);
                final int quantity = data.getIntExtra("quantity",0);
                final DatabaseReference reference = database.getReference().child("ordersUser").child(String.valueOf(userId));
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String key = reference.push().getKey();
                        DatabaseReference re = reference.child(key);
                        DatabaseReference referenceStore = database.getReference().child("ordersStore").child(String.valueOf(storeId)).child(key);
                        re.child("image_path").setValue(image_path);
                        referenceStore.child("image_path").setValue(userImage);
                        re.child("address").setValue(address);
                        referenceStore.child("address").setValue(address);
                        re.child("latitude").setValue(latitude);
                        referenceStore.child("latitude").setValue(latitude);
                        re.child("longtitude").setValue(longtitude);
                        referenceStore.child("longtitude").setValue(longtitude);
                        re.child("deliverTime").setValue(deliverTime);
                        referenceStore.child("deliverTime").setValue(deliverTime);
                        re.child("storeId").setValue(storeId);
                        re.child("storeName").setValue(storeName);
                        re.child("status").setValue("waitting");
                        referenceStore.child("status").setValue("waitting");
                        re.child("phone").setValue(storePhone);
                        referenceStore.child("userId").setValue(userId);
                        referenceStore.child("phone").setValue(phone);
                        referenceStore.child("userName").setValue(userName);
                        re.child("isFeedback").setValue("false");
                        re.child("totalPrice").setValue(totalPrice);
                        referenceStore.child("totalPrice").setValue(totalPrice);
                        re.child("orderDetail").child(String.valueOf(p.getProduct_id())).child("productId").setValue(p.getProduct_id());
                        referenceStore.child("orderDetail").child(String.valueOf(p.getProduct_id())).child("productId").setValue(p.getProduct_id());
                        re.child("orderDetail").child(String.valueOf(p.getProduct_id())).child("productName").setValue(p.getProduct_name());
                        referenceStore.child("orderDetail").child(String.valueOf(p.getProduct_id())).child("productName").setValue(p.getProduct_name());
                        re.child("orderDetail").child(String.valueOf(p.getProduct_id())).child("image_path").setValue(p.getImage_path());
                        referenceStore.child("orderDetail").child(String.valueOf(p.getProduct_id())).child("image_path").setValue(p.getImage_path());
                        re.child("orderDetail").child(String.valueOf(p.getProduct_id())).child("quantity").setValue(quantity);
                        referenceStore.child("orderDetail").child(String.valueOf(p.getProduct_id())).child("quantity").setValue(quantity);
                        re.child("orderDetail").child(String.valueOf(p.getProduct_id())).child("unitPrice").setValue(price);
                        referenceStore.child("orderDetail").child(String.valueOf(p.getProduct_id())).child("unitPrice").setValue(price);
                        re = database.getReference().child("notification").child(String.valueOf(storeId));
                        re.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    StoreNotification store = dataSnapshot.getValue(StoreNotification.class);
                                    if (store != null && store.getHaveNotification() != null && store.getToken() != null){
                                        if (store.getHaveNotification().equals("false")){
                                            String token = store.getToken();
                                            Notification notification = new Notification();
                                            notification.setTo(token);
                                            notification.setNotification(new NotificationDetail(storeName,"Bạn có đơn hàng mới"));
                                            Call<ResultNotification> call = ApiUtils.getAPIServiceFirebaseMessage().sendNotification(notification,getResources().getString(R.string.notificationKey),"application/json");
                                            new PushNotification(storeId).execute(call);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        loadingBar.setVisibility(View.INVISIBLE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(NearbyStorePage.this);
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "Hủy đặt hàng", Toast.LENGTH_LONG).show();
            }
        }
    }
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
    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Bạn phải bật GPS để tìm cửa hàng")
                .setCancelable(false)
                .setPositiveButton("Đến cài đặt",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Hủy",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
