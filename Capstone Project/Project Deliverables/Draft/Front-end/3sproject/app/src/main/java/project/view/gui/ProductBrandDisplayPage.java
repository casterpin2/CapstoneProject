package project.view.gui;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.adapter.ProductBrandDisplayListViewAdapter;
import project.view.R;
import project.view.model.Product;
import project.view.util.CustomInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductBrandDisplayPage extends BasePage {

    private ListView theListView;
    private ProductBrandDisplayListViewAdapter adapter;
    private APIService apiService;
    private int brandID;
    private SearchView searchView;
    List<Product> list = new ArrayList<>();
    String brandName = "";
    private RelativeLayout main_layout;
    private ProgressBar loadingBar;
    private LocationManager locationManager;
    final static int REQUEST_LOCATION = 1;
    private double currentLatitude = 0.0;
    private double currentLongtitude = 0.0;
    private TextView nullMessage;
    private Call<List<Product>> call;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_brand_display);
        turnOnLocation();
        main_layout = findViewById(R.id.main_layout);
        theListView = (ListView) findViewById(R.id.mainListView);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);
        nullMessage = (TextView) findViewById(R.id.nullMessage);
        apiService = ApiUtils.getAPIService();
        brandID = getIntent().getIntExtra("brandID", -1);
        brandName = getIntent().getStringExtra("brandName");

        apiService = APIService.retrofit.create(APIService.class);
        call = apiService.getProductBrand(brandID);
        new ProductBrandDisplayData().execute(call);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(brandName);
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view,getBaseContext());
                return false;
            }
        });
        CustomInterface.setStatusBarColor(this);
        loadingBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);
    }


    public List<Product> listProduct(int brandId) {
        final List<Product> productList = new ArrayList<>();
        apiService.getProductBrand(brandId).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                final ProgressDialog progressDoalog;
                progressDoalog = new ProgressDialog(ProductBrandDisplayPage.this);
                progressDoalog.setMax(100);
                progressDoalog.setMessage("Its loading....");
                progressDoalog.setTitle("ProgressDialog bar example");
                progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                // show it
                progressDoalog.show();
                for (int i = 0; i < response.body().size(); i++) {
                    productList.add(response.body().get(i));
                }
                if (productList.size() == response.body().size()) {
                    progressDoalog.dismiss();
                }
                if (productList.isEmpty()) {
                    Toast.makeText(ProductBrandDisplayPage.this, "SomeThing Wrong", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });
        return productList;
    }

    private class ProductBrandDisplayData extends AsyncTask<Call, Void, Void> {
        @Override
        protected void onPreExecute() {
            loadingBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            boolean check =false;
            if (list.isEmpty()) {
                check = false;
            } else {
                check = true;
            }
            if(check == true){
                loadingBar.setVisibility(View.INVISIBLE);
            }
            final boolean finalCheck = check;
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (finalCheck == false) {
                        nullMessage.setText("Có lỗi xảy ra, vui lòng tải lại trang!");
                        loadingBar.setVisibility(View.INVISIBLE);
                    }
                }
            },10000);
            adapter = new ProductBrandDisplayListViewAdapter(ProductBrandDisplayPage.this, R.layout.product_brand_display_custom_listview, list);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    theListView.setAdapter(adapter);
                }});
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Call... calls) {
            try {
                Call<List<Product>> call = calls[0];
                Response<List<Product>> response = call.execute();

                for (int i = 0; i < response.body().size(); i++) {
                    list.add(response.body().get(i));
                }
            } catch (IOException e) {
            }

            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.action_home:{
                Intent toHomPage = new Intent(ProductBrandDisplayPage.this, HomePage.class);
                startActivity(toHomPage);
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_search_toolbar, menu);
        MenuItem itemSearch = menu.findItem(R.id.action_search);
        final List<Product> searchedProduct = new ArrayList<>();
        searchView = (SearchView) itemSearch.getActionView();

//        searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.LEFT));
//        searchView.setSearchableInfo(true);
        searchView.clearFocus();
        searchView.setQueryHint("Tìm trong "+brandName);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        productList
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchedProduct.clear();
                if(newText.equals("") || newText == null){
                    adapter = new ProductBrandDisplayListViewAdapter(ProductBrandDisplayPage.this, R.layout.product_brand_display_custom_listview, list);

                } else {
                    for (int i = 0; i < list.size(); i++) {
                        if(list.get(i).getProduct_name().toLowerCase().contains(newText.toLowerCase())) {
                            searchedProduct.add(list.get(i));
                        }
                    }
                    adapter = new ProductBrandDisplayListViewAdapter(ProductBrandDisplayPage.this, R.layout.product_brand_display_custom_listview, searchedProduct);
                }
                adapter.notifyDataSetChanged();
                theListView.setAdapter(adapter);
                return true;
            }
        });

        return true;
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
}
