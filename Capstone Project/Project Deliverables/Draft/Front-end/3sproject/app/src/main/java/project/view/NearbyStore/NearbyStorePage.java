package project.view.NearbyStore;

import android.Manifest;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.MatrixCursor;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import project.view.AddProductToStore.SearchProductAddToStore;
import project.view.MainActivity;
import project.view.R;
import project.view.UserSearchProduct.NearByStore;

public class NearbyStorePage extends AppCompatActivity implements OnMapReadyCallback {
    ListView storeListView;
    ListViewAdapter adapter;
    List<NearbyStorePage> nearbyStoreList = new ArrayList<>();
    List<NearByStore> nearByStore = new ArrayList<>();
    private GoogleMap mMap;

    private LocationManager locationManager;

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

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        String listStore = getIntent().getStringExtra("listStore");
        if (listStore != null){
            this.nearByStore = new Gson().fromJson(listStore,List.class);
            Toast.makeText(NearbyStorePage.this,"Size: "+this.nearByStore.size(),Toast.LENGTH_SHORT).show();

        }
        productName = getIntent().getStringExtra("productName");

//        nearbyStoreList.add(new NearbyStore(1, "Cửa hàng số 1 2 3 4 5 6 7 8 9 10 11 12", "Hà Nội1 2 3 4 5 6 7 8 9 10", 0.5, 12000000, 0.0, 11.323, 12.554));


        storeListView = findViewById(R.id.storeListView);
        storeListView.setVerticalScrollBarEnabled(false);
        storeListView.setHorizontalScrollBarEnabled(false);

//        adapter = new ListViewAdapter(this, R.layout.nearby_store_page_custom_list_view, nearbyStoreList);
//        storeListView.setAdapter(adapter);


//        adapter.notifyDataSetChanged();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


//        onMapReady(mMap);

        //lazy loading

        mHandle = new MyHandle();
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
//                Log.d("",String.valueOf(page));
//                Log.d("",String.valueOf(searchedProductList.size()));
//                int count = searchedProductList.size() + addedProductList.size();
                // && count == (page * 10)
                int count = nearbyStoreList.size();
                if (view.getLastVisiblePosition() == totalItemCount - 1 && count == (page * 10) && isLoading == false && (page == 1 || page == 2)) {
//                    Log.d("","Loading");
                    isLoading = true;
                    Thread thread = new ThreadgetMoreData();
                    thread.start();
                    adapter.notifyDataSetChanged();
                }
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

        changeLocation(mMap);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        // thêm search vào vào action bar
        getMenuInflater().inflate(R.menu.search_view_with_find_icon, menu);
        MenuItem itemSearch = menu.findItem(R.id.search_view);
        searchView = (SearchView) itemSearch.getActionView();
        searchView.setIconifiedByDefault(false);

        searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.LEFT));
//        searchView.setSearchableInfo(true);
        searchView.setQuery(productName, true);
        searchView.clearFocus();

        if(searchView.getQuery() != null) {
            // callAPI rồi set lại listview ở đây

            Toast.makeText(NearbyStorePage.this, "onQueryTextSubmit : "+ searchView.getQuery(), Toast.LENGTH_SHORT).show();
            Toast.makeText(NearbyStorePage.this, "Submit  : "+ productName, Toast.LENGTH_SHORT).show();
            nearbyStoreList.clear();
//            nearbyStoreList.add(new NearbyStore(1,"Cửa hàng số 1 2 3 4 5 6 7 8 9 10 11 12","Hà Nội",0.6,12000,2,11.329,12.555));
//            nearbyStoreList.add(new NearbyStore(3,"Cửa hàng số 4","Hà Nội",1.3,10000,12.2,11.326,12.557));
//            nearbyStoreList.add(new NearbyStore(4,"Cửa hàng số 6","Hà Nội",2.1,1120000,99.9,11.319,12.560));
//            nearbyStoreList.add(new NearbyStore(5,"Cửa hàng số 8","Hà Nội",3,1120000,99.9,11.319,12.560));
//            nearbyStoreList.add(new NearbyStore(6,"Cửa hàng số 10","Hà Nội",1.2,1120000,99.9,11.300,12.301));
//            nearbyStoreList.add(new NearbyStore(7,"Cửa hàng số 12","Hà Nội",1.6,1120000,99.9,11.304,12.554));
            int index = storeListView.getFirstVisiblePosition();
            View v = storeListView.getChildAt(0);
            int top = (v == null) ? 0 : v.getTop();
            storeListView.setSelectionFromTop(index, top);

            if (mMap != null) { //prevent crashing if the map doesn't exist yet (eg. on starting activity)
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
//                searchedProductList.clear();
//                page = 1;
//
//                query = searchView.getQuery().toString().trim();
//                SearchProductAddToStore.query = query;
//                int index = theListView.getFirstVisiblePosition();
//                View v = theListView.getChildAt(0);
//                int top = (v == null) ? 0 : v.getTop();
//                theListView.setSelectionFromTop(index, top);
//
//                if(!query.isEmpty()) {
//                    callAPI(query,page);
//                }

                // callAPI rồi set lại listview ở đây

                Toast.makeText(NearbyStorePage.this, "onQueryTextSubmit : "+ query, Toast.LENGTH_SHORT).show();
                Toast.makeText(NearbyStorePage.this, "Submit  : "+ productName, Toast.LENGTH_SHORT).show();
                nearbyStoreList.clear();
//                nearbyStoreList.add(new NearbyStore(2,"Cửa hàng số 1 2 3 4 5 6 7 8 9 10 11 12","Hà Nội",0.6,12000,2,21.1,21.1));
//                nearbyStoreList.add(new NearbyStore(3,"Cửa hàng số 4","Hà Nội",1.3,10000,12.2,21.11,21.12));


                int index = storeListView.getFirstVisiblePosition();
                View v = storeListView.getChildAt(0);
                int top = (v == null) ? 0 : v.getTop();
                storeListView.setSelectionFromTop(index, top);
                if (mMap != null) { //prevent crashing if the map doesn't exist yet (eg. on starting activity)
                    changeLocation(mMap);
                }
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
//                Toast.makeText(SearchProductAddToStore.this, "content : "+ newText, Toast.LENGTH_SHORT).show();
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

    public class MyHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0 :
                    storeListView.addFooterView(footerView);
                    adapter.notifyDataSetChanged();
                    break;
                case 1:
                    //adapter.addListItemToAdapter((ArrayList<Item>)msg.obj);

                    storeListView.removeFooterView(footerView);
                    getMoreData();
                    isLoading = false;
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }

    public void getMoreData(){
        //List<Item> addList = new ArrayList<>();
//        page ++;
//        callAPI(query, page);

        //addList = searchedProductList;

        //return addList;
    }

    public class ThreadgetMoreData extends Thread {
        @Override
        public void run() {
            mHandle.sendEmptyMessage(0);
            //List<Item> addMoreList = getMoreData();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isLoading == true){
                Message msg = mHandle.obtainMessage(1);
                mHandle.sendMessage(msg);
            }
        }
    }

    public void changeLocation (GoogleMap googleMap){
        googleMap.clear();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            googleMap.clear();
            if (location != null) {
                latitude = location.getLatitude();
                longtitude = location.getLongitude();
                LatLng myLocation = new LatLng(latitude, longtitude);
                for (int i = 0; i < nearbyStoreList.size(); i++) {
//                    double newLng = nearbyStoreList.get(i).getLongtitude();
//                    double newLat = nearbyStoreList.get(i).getLatitude();
//                    LatLng storeLocation = new LatLng(newLat, newLng);
//                    nearbyStoreList.get(i).setDistance(CalculationByDistance(myLocation, storeLocation));

//                    canvas1.drawBitmap(BitmapFactory.decodeResource(getResources(),
//                            R.drawable.store), 0,0, color);

//                    googleMap.addMarker(new MarkerOptions().position(storeLocation).title(nearbyStoreList.get(i).getStoreName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
////                    mMap.addMarker(new MarkerOptions().position(storeLocation).title(nearbyStoreList.get(i).getStoreName()).icon(BitmapDescriptorFactory.fromBitmap(bmp)));
//                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(storeLocation));
                    googleMap.getUiSettings().setCompassEnabled(false);
                    googleMap.getUiSettings().setRotateGesturesEnabled(false);
                }
                googleMap.addMarker(new MarkerOptions().position(myLocation).title("Vị trí của bạn"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                googleMap.setMinZoomPreference(12.0f);
                googleMap.setMaxZoomPreference(20.0f);
            }
        }
    }

}
