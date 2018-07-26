package project.view.gui;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.adapter.ProductTypeDisplayListViewAdapter;
import project.view.R;
import project.view.model.ProductType;

public class ProductTypeDisplay extends AppCompatActivity {

    public List<ProductType> productList;
    private ListView theListView;
    private ProductTypeDisplayListViewAdapter adapter;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_type_display);

        apiService = ApiUtils.getAPIService();
        int typeID = getIntent().getIntExtra("typeID", -1);
        String typeName = getIntent().getStringExtra("typeName");
//        listProduct(typeID);
        adapter = new ProductTypeDisplayListViewAdapter(this, R.layout.product_type_display_custom_cardview, productList);
        // get our list view
        theListView = (ListView) findViewById(R.id.mainListView);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        theListView.setAdapter(adapter);

        getSupportActionBar().setTitle(typeName);
        Toast.makeText(this, typeID + " type", Toast.LENGTH_SHORT).show();
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

//    public List<ProductType> listProduct(int typeId) {
//        productList = new ArrayList<>();
//        apiService.getProductBrand(typeId).enqueue(new Callback<List<ProductBrand>>() {
//            @Override
//            public void onResponse(Call<List<ProductBrand>> call, Response<List<ProductBrand>> response) {
//                final ProgressDialog progressDoalog;
//                progressDoalog = new ProgressDialog(ProductTypeDisplay.this);
//                progressDoalog.setMax(100);
//                progressDoalog.setMessage("Its loading....");
//                progressDoalog.setTitle("ProgressDialog bar example");
//                progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                // show it
//                progressDoalog.show();
//                for (int i = 0; i < response.body().size(); i++) {
//                    productList.add(response.body().get(i));
//                }
//                if(productList.size() == response.body().size()){
//                    progressDoalog.dismiss();
//                }
//                if (productList.isEmpty()) {
//                    Toast.makeText(ProductTypeDisplay.this, "SomeThing Wrong", Toast.LENGTH_LONG).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<List<ProductBrand>> call, Throwable t) {
//
//            }
//        });
//        return productList;
//    }
}
