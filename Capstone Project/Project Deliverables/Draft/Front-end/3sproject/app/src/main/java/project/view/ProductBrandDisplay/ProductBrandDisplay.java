package project.view.ProductBrandDisplay;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.Category.Category;
import project.view.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductBrandDisplay extends AppCompatActivity {

    public List<ProductBrand> productList;
    private ListView theListView;
    private ProductBrandDisplayListViewAdapter adapter;
    private APIService apiService;
    private int brandID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_brand_display);
        apiService = ApiUtils.getAPIService();
        brandID = getIntent().getIntExtra("brandID", -1);
        String brandName = getIntent().getStringExtra("brandName");
//        listProduct(brandID);
//
//        // get our list view
//        theListView = (ListView) findViewById(R.id.mainListView);
        apiService = APIService.retrofit.create(APIService.class);
        final Call<List<ProductBrand>> call = apiService.getProductBrand(brandID);
        new NetworkCall().execute(call);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




//        int brandID = getIntent().getIntExtra("brandID", -1);
//        String brandName = getIntent().getStringExtra("brandName");
        getSupportActionBar().setTitle(brandName);
        Toast.makeText(this, brandID + " brand", Toast.LENGTH_SHORT).show();
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
        productList = new ArrayList<>();
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
                 List<ProductBrand> list = new ArrayList<>();
                for (int i = 0; i < response.body().size(); i++) {
                    list.add(response.body().get(i));
                }
                adapter = new ProductBrandDisplayListViewAdapter(ProductBrandDisplay.this, R.layout.product_brand_display_custom_listview, list);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        theListView = (ListView) findViewById(R.id.mainListView);
                        theListView.setAdapter(adapter);
                    }});
            } catch (IOException e) {
            }
            return null;
        }
    }
}
