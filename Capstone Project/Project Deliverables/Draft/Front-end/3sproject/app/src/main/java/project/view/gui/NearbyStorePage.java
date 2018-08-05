package project.view.gui;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.MatrixCursor;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import project.googleMapAPI.GoogleMapJSON;
import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.adapter.UserSearchProductListViewCustomAdapter;
import project.view.model.NearByStore;
import project.view.adapter.ListViewAdapter;
import project.view.util.CustomInterface;
import retrofit2.Call;
import retrofit2.Response;

public class NearbyStorePage extends AppCompatActivity implements OnMapReadyCallback {
    private ListView storeListView;
    private ListViewAdapter adapter;
    private List<String> nearByStore = new ArrayList<>();
    private List<NearByStore> list = new ArrayList<>();
    private RelativeLayout main_layout;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private int productId;
    private SearchView searchView;

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

    private String productName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_store_page);
        storeListView = findViewById(R.id.storeListView);
        CustomInterface.setStatusBarColor(this);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        int productId = getIntent().getIntExtra("productId",-1);
        turnOnLocation();
        if (productId != -1) {
            NearByStoreAsynTask asynTask = new NearByStoreAsynTask();
            Call<List<NearByStore>> call = ApiUtils.getAPIService().nearByStore(productId, String.valueOf(latitude), String.valueOf(longtitude));
            asynTask.execute(call);
        }
        main_layout = findViewById(R.id.main_layout);
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view,getBaseContext());
                return false;
            }
        });
//        nearByStore = getIntent().getStringArrayListExtra("listStore");
//
//        if (nearByStore != null){
//            for (int i = 0 ; i < nearByStore.size() ; i++){
//                NearByStore store = new Gson().fromJson(nearByStore.get(i),NearByStore.class);
//                list.add(store);
//            }
//        }

        adapter = new ListViewAdapter(NearbyStorePage.this, R.layout.nearby_store_page_custom_list_view, list);
        storeListView.setAdapter(adapter);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


//        onMapReady(mMap);

        //lazy loading

        //mHandle = new MyHandle();
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footerView = li.inflate(R.layout.footer_loading_listview_lazy_loading, null);

        storeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                double storeLat = nearbyStoreList.get(position).getLatitude();
//                double storeLng = nearbyStoreList.get(position).getLongtitude();
//                Toast.makeText(NearbyStorePage.this, "Lat: " + storeLat + "----- Lng: " + storeLng + "----- id: " + id, Toast.LENGTH_SHORT).show();
//                LatLng chosenStore = new LatLng(storeLat, storeLng);
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(chosenStore));
//                Toast.makeText(NearbyStorePage.this, "Done!", Toast.LENGTH_SHORT).show();
            }
        });

        storeListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                int count = list.size();
//                if (view.getLastVisiblePosition() == totalItemCount - 1 && count == (page * 10) && isLoading == false && (page == 1 || page == 2)) {
////                    Log.d("","Loading");
//                    isLoading = true;
//                    Thread thread = new ThreadgetMoreData();
//                    thread.start();
//                    adapter.notifyDataSetChanged();
//                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


//        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
//        Bitmap bmp = Bitmap.createBitmap(100, 100, conf);
//        Canvas canvas1 = new Canvas(bmp);
//
//// paint defines the text color, stroke width and size
//        Paint color = new Paint();
//        color.setTextSize(35);
//        color.setColor(getResources().getColor(R.color.colorApplication));


// modify canvas

//        view = (FrameLayout)findViewById(R.id.storeMarker);
//        view.setDrawingCacheEnabled(true);
//        view.buildDrawingCache();
//        Bitmap storeBitmap = view.getDrawingCache();
//        storeName = findViewById(R.id.storeName);
//        storeImage = findViewById(R.id.storeImage);
//        storeImage.setBackgroundResource(R.drawable.add);


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

        final List<String> suggestions = new ArrayList<>();
        suggestions.add("Sản phẩm kia là abc xyz");
        suggestions.add("DM");
        suggestions.add("Cool");
        suggestions.add("Sản phẩm này là Samsung Galaxy Note 8 128Gb");

        final List<String> searchedList = new ArrayList<>();


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
                LatLng myLocation = new LatLng(latitude, longtitude);
                googleMap.addMarker(new MarkerOptions().position(myLocation).title("Vị trí của bạn"));
                googleMap.getUiSettings().setCompassEnabled(false);
                googleMap.getUiSettings().setRotateGesturesEnabled(false);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                googleMap.setMinZoomPreference(12.0f);
                googleMap.setMaxZoomPreference(20.0f);
                for (int i = 0; i < list.size(); i++) {
                    storeLongtitude = list.get(i).getLongitude();
                    storeLatitude = list.get(i).getLatitude();
                    storeLatLng = new LatLng(storeLatitude, storeLongtitude);
                    googleMap.addMarker(new MarkerOptions().position(storeLatLng).title(list.get(i).getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                }
    }
    public class NearByStoreAsynTask extends AsyncTask<Call, Void, List<NearByStore>> {
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
            if (listNearByStore != null) {
                for (NearByStore near : listNearByStore) {
                    list.add(near);
                }
                adapter = new ListViewAdapter(NearbyStorePage.this, R.layout.nearby_store_page_custom_list_view, listNearByStore);
                storeListView.setAdapter(adapter);
                changeLocation(mMap);
            } else {
                Toast.makeText(NearbyStorePage.this,"Có lỗi xảy ra!!!",Toast.LENGTH_LONG).show();
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
}
