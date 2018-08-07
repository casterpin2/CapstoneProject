package project.view.gui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.adapter.ProductInStoreCustomListViewAdapter;
import project.view.model.ProductInStore;
import project.view.util.CustomInterface;
import retrofit2.Call;
import retrofit2.Response;

public class ProductInStoreDisplayPage extends AppCompatActivity {
    private ProductInStoreCustomListViewAdapter adapter;
    private ListView theListView;
    private Button addNewBtn;
    private APIService mAPI;
    private ViewGroup context;
    private int storeID;
    private TextView nullMessage;
    private SearchView searchView;
    private List<ProductInStore> list = new ArrayList<>();
    private RelativeLayout main_layout;

    public ProductInStoreDisplayPage() {
    }

    public ViewGroup getContext() {
        return context;
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        final Call<List<ProductInStore>> call = mAPI.getProductInStore(storeID);
        new NetworkCall().execute(call);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_in_store_display_page);
        CustomInterface.setStatusBarColor(this);
        main_layout = findViewById(R.id.main_layout);
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view,getBaseContext());
                return false;
            }
        });
        getSupportActionBar().setTitle("Sản phẩm cửa hàng");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        storeID = getIntent().getIntExtra("storeID", -1);
        mAPI = ApiUtils.getAPIService();
        final Call<List<ProductInStore>> call = mAPI.getProductInStore(storeID);
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
                list.clear();
                Call<List<ProductInStore>> call = calls[0];
                final Response<List<ProductInStore>> response = call.execute();
                for (int i = 0; i < response.body().size(); i++) {
                    list.add(response.body().get(i));
                }
                adapter = new ProductInStoreCustomListViewAdapter(ProductInStoreDisplayPage.this,R.layout.search_product_page_custom_list_view,list);
                adapter.setStoreID(1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        nullMessage = findViewById(R.id.nullMessage);
                        theListView = (ListView) findViewById(R.id.mainListView);
                        theListView.setAdapter(adapter);

                        if(response.body().size() == 0) {
                            nullMessage.setText("Cửa hàng không có sản phẩm nào!");
                            theListView.setVisibility(View.INVISIBLE);
                        } else {
                            nullMessage.setText("");
                            theListView.setVisibility(View.VISIBLE);
                        }

                        theListView. setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                int productID = list.get(position).getProductID();
                                int isStoreProduct = 0;
                                Intent toProductDetailPage = new Intent(ProductInStoreDisplayPage.this, MainActivity.class);
                                toProductDetailPage.putExtra("productID",productID);
                                toProductDetailPage.putExtra("storeID",storeID);
                                toProductDetailPage.putExtra("isStoreProduct",isStoreProduct);
                            }
                        });

                        addNewBtn = (Button) findViewById(R.id.addNewBtn);
                        addNewBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent toSearchProductToStore = new Intent(ProductInStoreDisplayPage.this, SearchProductAddToStore.class);
                                toSearchProductToStore.putExtra("storeID",storeID);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view_with_find_icon, menu);
        MenuItem itemSearch = menu.findItem(R.id.search_view);
        final List<ProductInStore> searchedProduct = new ArrayList<>();
        searchView = (SearchView) itemSearch.getActionView();

//        searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.LEFT));
//        searchView.setSearchableInfo(true);
        searchView.clearFocus();
        searchView.setQueryHint("Tìm trong cửa hàng");

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
                    adapter = new ProductInStoreCustomListViewAdapter(ProductInStoreDisplayPage.this, R.layout.search_product_page_custom_list_view, list);

                } else {
                    for (int i = 0; i < list.size(); i++) {
                        if(list.get(i).getProductName().toLowerCase().contains(newText.toLowerCase())) {
                            searchedProduct.add(list.get(i));
                        }
                    }
                    adapter = new ProductInStoreCustomListViewAdapter(ProductInStoreDisplayPage.this, R.layout.search_product_page_custom_list_view, searchedProduct);
                }
                adapter.notifyDataSetChanged();
                theListView.setAdapter(adapter);
                return true;
            }
        });

        return true;
    }

}
