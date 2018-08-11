package project.view.gui;

import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.adapter.ProductTypeDisplayListViewAdapter;
import project.view.R;
import project.view.model.Product;
import retrofit2.Call;
import retrofit2.Response;

public class ProductTypeDisplayPage extends BasePage {

    public List<Product> productList;
    private ListView theListView;
    private ProductTypeDisplayListViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_type_display);
        int typeID = getIntent().getIntExtra("typeID", -1);
        String typeName = getIntent().getStringExtra("typeName");
        productList = new ArrayList<>();
        adapter = new ProductTypeDisplayListViewAdapter(this, R.layout.product_type_display_custom_cardview, productList);
        theListView = (ListView) findViewById(R.id.mainListView);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        theListView.setAdapter(adapter);
        if (typeID != -1) {
            getSupportActionBar().setTitle(typeName);
            Call<List<Product>> call = ApiUtils.getAPIService().getProductbyType(typeID);
            new GetData().execute(call);
        } else {
            Toast.makeText(ProductTypeDisplayPage.this, "Có lỗi xảy ra !!!",Toast.LENGTH_LONG).show();
        }
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

    private class GetData extends AsyncTask<Call, Void, List<Product>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(List<Product> aVoid) {
            if (aVoid != null) {
                for (Product pro : aVoid){
                    productList.add(pro);
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(ProductTypeDisplayPage.this, "Có lỗi xảy ra !!!",Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(aVoid);

        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }
        @Override
        protected List<Product> doInBackground(Call... calls) {
            try {
                Call<List<Product>> call = calls[0];
                Response<List<Product>> response = call.execute();
                return response.body();
            } catch (IOException e) {
            }
            return null;
        }
    }
}
