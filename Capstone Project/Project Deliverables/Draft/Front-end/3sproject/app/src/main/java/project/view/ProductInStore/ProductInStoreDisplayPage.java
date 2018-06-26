package project.view.ProductInStore;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.AddProductToStore.Item;
import project.view.AddProductToStore.SearchProductAddToStore;
import project.view.ProductBrandDisplay.ProductBrand;
import project.view.ProductBrandDisplay.ProductBrandDisplay;
import project.view.ProductBrandDisplay.ProductBrandDisplayListViewAdapter;
import project.view.R;
import retrofit2.Call;
import retrofit2.Response;

public class ProductInStoreDisplayPage extends AppCompatActivity {

    private List<ProductInStore> productList;
    private ProductInStoreCustomListViewAdapter adapter;
    private ListView theListView;
    private Button addNewBtn;
    private APIService mAPI;
    private ViewGroup context;

    public ProductInStoreDisplayPage() {
    }

    public ViewGroup getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_in_store_display_page);
        mAPI = ApiUtils.getAPIService();
        final Call<List<ProductInStore>> call = mAPI.getProductInStore(1);
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.actionbar_search_product_add_to_store_page);

        new NetworkCall().execute(call);
    }
    private class NetworkCall extends AsyncTask<Call, Void, Void> {


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
                Call<List<ProductInStore>> call = calls[0];
                Response<List<ProductInStore>> response = call.execute();
                List<ProductInStore> list = new ArrayList<>();
                for (int i = 0; i < response.body().size(); i++) {
                    list.add(response.body().get(i));
                }
                adapter = new ProductInStoreCustomListViewAdapter(ProductInStoreDisplayPage.this,R.layout.search_product_page_custom_list_view,list);
                adapter.setStoreID(1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getSupportActionBar().setTitle("Sản phẩm cửa hàng");
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        theListView = (ListView) findViewById(R.id.mainListView);
                        theListView.setAdapter(adapter);
                        addNewBtn = (Button) findViewById(R.id.addNewBtn);
                        addNewBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent toSearchProductToStore = new Intent(ProductInStoreDisplayPage.this, SearchProductAddToStore.class);
//                toSearchProductToStore.putExtra("storeID",storeID);
                                startActivity(toSearchProductToStore);
                            }
                        });
                    }});
            } catch (IOException e) {
            }
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
