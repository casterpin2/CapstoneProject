package project.view.gui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.MatrixCursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.adapter.UserSearchProductListViewCustomAdapter;
import project.view.model.Item;
import project.view.model.Product;
import project.view.R;
import project.view.util.CustomInterface;
import retrofit2.Call;
import retrofit2.Response;

public class UserSearchProductPage extends BasePage {

    private SearchView searchView;
    private ListView productListView;
    private ImageView imgBack, imgBarCode;
    private TextView noHaveProduct;
    private LinearLayout haveProduct;
    private ProgressBar loadingBar;
    private UserSearchProductListViewCustomAdapter adapter;
    RelativeLayout main_layout;
    public View footerView;
    private List<Product> productList = new ArrayList<>();
    private APIService mAPI;
    int page;
    private String query = "";
    public boolean isLoading;
    public Handler mHandle;
    private LocationManager locationManager;
    final static int REQUEST_LOCATION = 1;
    private double currentLatitude = 0.0;
    private double currentLongtitude = 0.0;
    private static final int RESULT_CODE_SCAN=220;
    private static final int REQUEST_CODE_SCAN=120;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final List<String> suggestions = new ArrayList<>();
    private final List<String> searchedList = new ArrayList<>();
    private CursorAdapter suggestionAdapter;
    DatabaseReference myRef = database.getReference();
    final DatabaseReference myRef1 = myRef.child("suggestion").child("product");
    private ValueEventListener listener;
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
        if (myRef1 != null) {
            myRef1.removeEventListener(listener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_product_page);

        CustomInterface.setStatusBarColor(this);
        findView();
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view,getBaseContext());
                return false;
            }
        });
        mAPI = ApiUtils.getAPIService();

        turnOnLocation();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setLayout(noHaveProduct, haveProduct);
        searchView.setFocusable(true);
        searchView.setFocusableInTouchMode(true);
        productListView.setVerticalScrollBarEnabled(false);
        productListView.setHorizontalScrollBarEnabled(false);
        adapter = new UserSearchProductListViewCustomAdapter(this,R.layout.user_search_product_page_custom_list_view, productList, currentLatitude, currentLongtitude);
        productListView.setAdapter(adapter);
        mHandle = new MyHandle();
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footerView = li.inflate(R.layout.footer_loading_listview_lazy_loading, null);

        productListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int count = productList.size();
                if (view.getLastVisiblePosition() == totalItemCount - 1 && count == (page * 5) && isLoading == false  && (page > 0)) {
                    isLoading = true;
                    Thread thread = new ThreadgetMoreData();
                    thread.start();
                }
            }
        });
        loadingBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);
        if(getIntent().getStringExtra("code")!=null){
            final Call<List<Product>> listBarcode = mAPI.userSearchBarcode(getIntent().getStringExtra("code"));
            new UserSearchWithBarcode().execute(listBarcode);
        }




        suggestionAdapter = new SimpleCursorAdapter(this,
                R.layout.custom_suggested_listview_nearby_store_page,
                null,
                new String[]{SearchManager.SUGGEST_COLUMN_TEXT_1},
                new int[]{android.R.id.text1},
                0);
        searchView.setSuggestionsAdapter(suggestionAdapter);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                productList.clear();
                page = 0;

                query = searchView.getQuery().toString().trim();
                setQuery(query);
                int index = productListView.getFirstVisiblePosition();
                View v = productListView.getChildAt(0);
                int top = (v == null) ? 0 : v.getTop();
                productListView.setSelectionFromTop(index, top);

                if(!query.isEmpty()) {
                    getMoreData();
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
                    Log.d("searchedList",String.valueOf(searchedList.size()));
                    if (suggestions.get(i).toLowerCase().contains(newText)) {
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

        imgBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toBarCode = new Intent(UserSearchProductPage.this, BarcodeActivity.class);
                toBarCode.putExtra("userSearchPage",1);
                startActivityForResult(toBarCode,REQUEST_CODE_SCAN);

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
                        currentLatitude = location.getLatitude();
                        currentLongtitude = location.getLongitude();
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
                            currentLatitude = location.getLatitude();
                            currentLongtitude = location.getLongitude();
                        }
                    }

            }
        }
    }

    private void findView(){

        noHaveProduct = (TextView) findViewById(R.id.noHaveProduct);
        haveProduct = (LinearLayout) findViewById(R.id.haveProduct);
        main_layout = findViewById(R.id.main_layout);
        productListView = (ListView) findViewById(R.id.productListView);
        searchView = findViewById(R.id.searchViewQuery);
        imgBack = findViewById(R.id.backBtn);
        imgBarCode = findViewById(R.id.imgBarCode);
        productListView = (ListView) findViewById(R.id.productListView);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);
    }

    public class UserSearchProductAsyncTask1 extends AsyncTask<Call, Void, List<Product>> {

        @Override
        protected void onPreExecute() {
            loadingBar.setVisibility(View.VISIBLE);
            noHaveProduct.setVisibility(View.INVISIBLE);
            super.onPreExecute();
        }

        @Override
        protected List<Product> doInBackground(Call... calls) {
            try {
                Call<List<Product>> call = calls[0];
                Response<List<Product>> re = call.execute();
//            if (re.body() != null) {
                return re.body();
//            } else {
//                return null;
//            }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Product> list) {
            super.onPostExecute(list);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    productList.add(list.get(i));
                }
                adapter.notifyDataSetChanged();
                setLayout(noHaveProduct,haveProduct);
                loadingBar.setVisibility(View.INVISIBLE);
                if (productList.isEmpty()) {
                    noHaveProduct.setVisibility(View.VISIBLE);
                    noHaveProduct.setText("Không có sản phẩm nào!");
                } else {
                    noHaveProduct.setVisibility(View.INVISIBLE);
                    noHaveProduct.setText("");
                }


            } else {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UserSearchProductPage.this, "Có lỗi xảy ra. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                        loadingBar.setVisibility(View.INVISIBLE);
                        noHaveProduct.setVisibility(View.VISIBLE);
                        noHaveProduct.setText("Có lỗi xảy ra. Vui lòng thử lại");

                    }
                },10000);
            }

        }
    }

    private void setLayout(TextView noHaveProduct, LinearLayout haveProduct) {
        if (productList.size() == 0) {
            noHaveProduct.setVisibility(View.VISIBLE);
            haveProduct.setVisibility(View.INVISIBLE);
        } else {
            haveProduct.setVisibility(View.VISIBLE);
            noHaveProduct.setVisibility(View.INVISIBLE);
        }
    }
    public class UserSearchWithBarcode extends AsyncTask<Call,List<Product>,Void>{
        @Override
        protected void onPreExecute() {
            loadingBar.setVisibility(View.VISIBLE);
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadingBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(List<Product>... values) {
            super.onProgressUpdate(values);
            productList.clear();
            productList = values[0];
            setLayout(noHaveProduct, haveProduct);
            searchView.setFocusable(true);
            searchView.setFocusableInTouchMode(true);
            productListView.setVerticalScrollBarEnabled(false);
            productListView.setHorizontalScrollBarEnabled(false);
            adapter = new UserSearchProductListViewCustomAdapter(UserSearchProductPage.this,R.layout.user_search_product_page_custom_list_view, productList, currentLatitude, currentLongtitude);
            productListView.setAdapter(adapter);

        }

        @Override
        protected Void doInBackground(Call... calls) {
            try{
                Call<List<Product>> call = calls[0];
                Response<List<Product>> response = call.execute();
                List<Product> listProduct = new ArrayList<>();
                for(int i =0 ; i< response.body().size();i++){
                    listProduct.add(response.body().get(i));
                }
                publishProgress(listProduct);
            }catch (Exception e){
                Log.e("Error",e.getMessage());
            }
            return null;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SCAN &&  resultCode == RESULT_CODE_SCAN){
            String  barcode = data.getStringExtra("code");
            final Call<List<Product>> listBarcode = mAPI.userSearchBarcode(barcode);
            new UserSearchWithBarcode().execute(listBarcode);
        }
    }

    public class MyHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0 :
                    productListView.addFooterView(footerView);
                    break;
                case 1:
                    //adapter.addListItemToAdapter((ArrayList<Item>)msg.obj);
                    productListView.removeFooterView(footerView);
                    getMoreData();
                    isLoading = false;
                    break;
                default:
                    break;
            }
        }
    }

    public void getMoreData(){
        //List<Item> addList = new ArrayList<>();

        Log.d("page",String.valueOf(page));
        callAPI(query, page);
        page ++;
        //addList = searchedProductList;

        //return addList;
    }


    public class ThreadgetMoreData extends Thread {
        @Override
        public void run() {
            mHandle.sendEmptyMessage(0);
            //List<Item> addMoreList = getMoreData();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isLoading == true){
                Message msg = mHandle.obtainMessage(1);
                mHandle.sendMessage(msg);
            }
        }
    }

    public void setQuery(String query) {
        this.query = query;
    }

    private void callAPI (String query, int page){
        if (query.isEmpty()) return;
        Call<List<Product>> call = mAPI.userSearchProduct(query,page);
        new UserSearchProductAsyncTask1().execute(call);
    }
}



