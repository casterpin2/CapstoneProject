package project.view.gui;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.adapter.ProductBrandDisplayListViewAdapter;
import project.view.R;
import project.view.model.ProductBrand;
import project.view.util.CustomInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductBrandDisplay extends AppCompatActivity {

    private ListView theListView;
    private ProductBrandDisplayListViewAdapter adapter;
    private APIService apiService;
    private int brandID;
    private SearchView searchView;
    List<ProductBrand> list = new ArrayList<>();
    String brandName = "";
    private RelativeLayout main_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_brand_display);
        main_layout = findViewById(R.id.main_layout);
        theListView = (ListView) findViewById(R.id.mainListView);
        apiService = ApiUtils.getAPIService();
        brandID = getIntent().getIntExtra("brandID", -1);
        brandName = getIntent().getStringExtra("brandName");
        apiService = APIService.retrofit.create(APIService.class);
        final Call<List<ProductBrand>> call = apiService.getProductBrand(brandID);
        new NetworkCall().execute(call);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public List<ProductBrand> listProduct(int brandId) {
        final List<ProductBrand> productList = new ArrayList<>();
        apiService.getProductBrand(brandId).enqueue(new Callback<List<ProductBrand>>() {
            @Override
            public void onResponse(Call<List<ProductBrand>> call, Response<List<ProductBrand>> response) {
                final ProgressDialog progressDoalog;
                progressDoalog = new ProgressDialog(ProductBrandDisplay.this);
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
                    Toast.makeText(ProductBrandDisplay.this, "SomeThing Wrong", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<List<ProductBrand>> call, Throwable t) {

            }
        });
        return productList;
    }

    private class NetworkCall extends AsyncTask<Call, Void, Void> {

//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(List<ProductBrand> productBrands) {
//            super.onPostExecute(productBrands);
//        }
//
//        @Override
//        protected void onProgressUpdate(List<ProductBrand>... values) {
//            super.onProgressUpdate(values);
//
//            adapter = new ProductBrandDisplayListViewAdapter(ProductBrandDisplay.this, R.layout.product_brand_display_custom_listview, values[0]);
//            theListView.setAdapter(adapter);
//        }
//
//        @Override
//        protected List<ProductBrand> doInBackground(Call... calls) {
//            try {
//                Call<List<ProductBrand>> call = calls[0];
//                Response<List<ProductBrand>> response = call.execute();
//                List<ProductBrand> list = new ArrayList<>();
//                for (int i = 0; i < response.body().size(); i++) {
//                    list.add(response.body().get(i));
//                }
//                publishProgress(list);
//                return list;
//            } catch (IOException e) {
//            }
//            return null;
//        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected Void doInBackground(Call... calls) {
            try {
                Call<List<ProductBrand>> call = calls[0];
                Response<List<ProductBrand>> response = call.execute();

                for (int i = 0; i < response.body().size(); i++) {
                    list.add(response.body().get(i));
                }
                adapter = new ProductBrandDisplayListViewAdapter(ProductBrandDisplay.this, R.layout.product_brand_display_custom_listview, list);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        theListView.setAdapter(adapter);
                    }});
            } catch (IOException e) {
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view_with_find_icon, menu);
        MenuItem itemSearch = menu.findItem(R.id.search_view);
        final List<ProductBrand> searchedProduct = new ArrayList<>();
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
                    adapter = new ProductBrandDisplayListViewAdapter(ProductBrandDisplay.this, R.layout.product_brand_display_custom_listview, list);

                } else {
                    for (int i = 0; i < list.size(); i++) {
                        if(list.get(i).getProductName().toLowerCase().contains(newText.toLowerCase())) {
                            searchedProduct.add(list.get(i));
                        }
                    }
                    adapter = new ProductBrandDisplayListViewAdapter(ProductBrandDisplay.this, R.layout.product_brand_display_custom_listview, searchedProduct);
                }
                adapter.notifyDataSetChanged();
                theListView.setAdapter(adapter);
                return true;
            }
        });

        return true;
    }

}
