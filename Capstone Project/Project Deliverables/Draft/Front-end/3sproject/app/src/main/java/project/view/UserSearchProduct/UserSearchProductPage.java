package project.view.UserSearchProduct;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.MatrixCursor;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.BaseColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.Cart.Product;
import project.view.R;
import retrofit2.Call;
import retrofit2.Response;

public class UserSearchProductPage extends AppCompatActivity {

    private SearchView searchView;
    private ListView productListView;
    private UserSearchProductListViewCustomAdapter adapter;
    List<Product> productList = new ArrayList<>();
    private APIService mAPI;
    private LocationManager locationManager;
    final static int REQUEST_LOCATION = 1;
    private double currentLatitude = 0.0;
    private double currentLongtitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_product_page);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAPI = ApiUtils.getAPIService();

        turnOnLocation();
        productListView = (ListView) findViewById(R.id.productListView);

        productListView.setVerticalScrollBarEnabled(false);
        productListView.setHorizontalScrollBarEnabled(false);

        adapter = new UserSearchProductListViewCustomAdapter(this,R.layout.user_search_product_page_custom_list_view, productList, currentLatitude, currentLongtitude);

        productListView.setAdapter(adapter);
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
//        MenuItem itemSearchWithBarcode = menu.findItem(R.id.search_with_barcode);
        searchView.setIconifiedByDefault(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            searchView.setFocusedByDefault(true);
        }

        searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.LEFT));

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

//                int index = productListView.getFirstVisiblePosition();
//                View v = productListView.getChildAt(0);
//                int top = (v == null) ? 0 : v.getTop();
//                productListView.setSelectionFromTop(index, top);
                final Call<List<Product>> call =  mAPI.userSearchProduct(query);
                new UserSearchProductAsyncTask1().execute(call);
                //adapter = new UserSearchProductListViewCustomAdapter(UserSearchProductPage.this,R.layout.user_search_product_page_custom_list_view, asyncTask.getList());
                //Toast.makeText(UserSearchProductPage.this, "onQueryTextSubmit : "+  productList.size(), Toast.LENGTH_SHORT).show();

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

//        itemSearchWithBarcode.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Intent intent = new Intent(UserSearchProductPage.this, MainActivity.class);
//                startActivity(intent);
//                return false;
//            }
//        });

        return true;
    }

    private void turnOnLocation(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            googleMap.clear();
            if (location != null) {
                currentLatitude = location.getLatitude();
                currentLongtitude = location.getLongitude();
            } else {
                Toast.makeText(this, "Bạn chưa bật định vị. Chưa thể tìm cửa hàng!!!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class UserSearchProductAsyncTask1 extends AsyncTask<Call, Void, List<Product>> {

        private List<Product> list;

        public UserSearchProductAsyncTask1() {
            list = new ArrayList<>();
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
                productList.clear();
                for (int i = 0; i < list.size(); i++) {
                    productList.add(list.get(i));
                }
                adapter.notifyDataSetChanged();
        } else {Toast.makeText(UserSearchProductPage.this,"Có lỗi xảy ra!!!",Toast.LENGTH_LONG).show();}
        }

        public List<Product> getList() {
            return list;
        }
    }


}
