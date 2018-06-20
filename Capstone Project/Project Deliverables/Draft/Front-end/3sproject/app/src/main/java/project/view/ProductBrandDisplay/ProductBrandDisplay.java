package project.view.ProductBrandDisplay;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductBrandDisplay extends AppCompatActivity {

    public List<ProductBrand> productList;
    private ListView theListView;
    private ProductBrandDisplayListViewAdapter adapter;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_brand_display);
        apiService = ApiUtils.getAPIService();
        int brandID = getIntent().getIntExtra("brandID", -1);
        String brandName = getIntent().getStringExtra("brandName");
        listProduct(brandID);
        adapter = new ProductBrandDisplayListViewAdapter(this, R.layout.product_brand_display_custom_listview, productList);
        // get our list view
        theListView = (ListView) findViewById(R.id.mainListView);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        theListView.setAdapter(adapter);

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
                if(productList.size() == response.body().size()){
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
}
