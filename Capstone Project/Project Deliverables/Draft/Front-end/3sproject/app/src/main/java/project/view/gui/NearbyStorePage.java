package project.view.gui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.MatrixCursor;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import project.retrofit.ApiUtils;
import project.view.R;
import project.view.adapter.NearByStoreListViewAdapter;
import project.view.model.Cart;
import project.view.model.CartDetail;
import project.view.model.NearByStore;
import project.view.model.Product;
import project.view.util.CustomInterface;
import retrofit2.Call;
import retrofit2.Response;

public class NearbyStorePage extends BasePage implements OnMapReadyCallback {
    private ListView storeListView;
    private NearByStoreListViewAdapter adapter;
    private List<String> nearByStore = new ArrayList<>();
    private List<NearByStore> list = new ArrayList<>();
    private LinearLayout main_layout;
    private GoogleMap mMap;
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
    //lazy loading
    public Handler mHandle;
    public View footerView;
    public boolean isLoading;
    boolean limitData = false;
    int page = 1;
    private Product p;
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        myRef1.removeEventListener(listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_store_page);
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
        productId = getIntent().getIntExtra("productId",-1);
        String image_path = getIntent().getStringExtra("image_path");
        p = new Product(productId,productName,image_path);
        turnOnLocation();


        main_layout = findViewById(R.id.main_layout);
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view,getBaseContext());
                return false;
            }
        });
        String sourceString = "Cửa hàng quanh đây có <b>" + productName + "</b> ";
        textContent.setText(Html.fromHtml(sourceString));
        adapter = new NearByStoreListViewAdapter(NearbyStorePage.this, R.layout.nearby_store_page_custom_list_view, list,p);
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
        LatLng myLocation = new LatLng(latitude, longtitude);
        googleMap.addMarker(new MarkerOptions().position(myLocation).title("Vị trí của bạn"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        if (productId != -1) {
            NearByStoreAsynTask asynTask = new NearByStoreAsynTask();
            Call<List<NearByStore>> call = ApiUtils.getAPIService().nearByStore(productId, String.valueOf(latitude), String.valueOf(longtitude));
            asynTask.execute(call);
        }
    }


    public double CalculationByDistance(LatLng point1, LatLng point2) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = point1.latitude;
        double lat2 = point2.latitude;
        double lng1 = point1.longitude;
        double lng2 = point2.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
//        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
//                + " Meter   " + meterInDec);

        return Radius * c;
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

//    public class MyHandle extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what){
//                case 0 :
//                    storeListView.addFooterView(footerView);
//                    adapter.notifyDataSetChanged();
//                    break;
//                case 1:
//                    //adapter.addListItemToAdapter((ArrayList<Item>)msg.obj);
//
//                    storeListView.removeFooterView(footerView);
//                    getMoreData();
//                    isLoading = false;
//                    adapter.notifyDataSetChanged();
//                    break;
//                default:
//                    break;
//            }
//        }
//    }

    public void getMoreData(){
        //List<Item> addList = new ArrayList<>();
//        page ++;
//        callAPI(query, page);

        //addList = searchedProductList;

        //return addList;
    }

//    public class ThreadgetMoreData extends Thread {
//        @Override
//        public void run() {
//            mHandle.sendEmptyMessage(0);
//            //List<Item> addMoreList = getMoreData();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            if (isLoading == true){
//                Message msg = mHandle.obtainMessage(1);
//                mHandle.sendMessage(msg);
//            }
//        }
//    }

    public void changeLocation (GoogleMap googleMap) {
                double storeLongtitude = 0.0;
                double storeLatitude = 0.0;
                LatLng storeLatLng;
//                LatLng myLocation = new LatLng(latitude, longtitude);
//                googleMap.addMarker(new MarkerOptions().position(myLocation).title("Vị trí của bạn"));
                googleMap.getUiSettings().setCompassEnabled(false);
                googleMap.getUiSettings().setRotateGesturesEnabled(false);
                googleMap.setMinZoomPreference(12.0f);
                googleMap.setMaxZoomPreference(22.0f);
                for (int i = 0; i < list.size(); i++) {
                    storeLongtitude = list.get(i).getLongitude();
                    storeLatitude = list.get(i).getLatitude();
                    storeLatLng = new LatLng(storeLatitude, storeLongtitude);
                    googleMap.addMarker(new MarkerOptions().position(storeLatLng).title(list.get(i).getName()).snippet(list.get(i).getAddress()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                }
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
            loadingBar.setVisibility(View.INVISIBLE);
            if (listNearByStore != null) {
                for (NearByStore near : listNearByStore) {
                    list.add(near);
                }
                adapter.notifyDataSetChanged();
                changeLocation(mMap);
            } else {
                Toast.makeText(NearbyStorePage.this,"Có lỗi xảy ra!!!",Toast.LENGTH_LONG).show();
            }
            if (list.size() == 0) {
                noHaveStore.setVisibility(View.VISIBLE);
            } else {
                noHaveStore.setVisibility(View.INVISIBLE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
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
                Toast.makeText(this, "Chưa định vị được vị trí của bạn", Toast.LENGTH_LONG).show();
            }
        }
    }
}
