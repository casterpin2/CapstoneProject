package project.view.gui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.adapter.ProductInStoreCustomListViewAdapter;
import project.view.model.Product;
import project.view.util.CustomInterface;
import project.view.util.ProductFilter;
import project.view.util.ProductInStoreCompareableDecrease;
import project.view.util.ProductInStoreCompareableIncrease;
import retrofit2.Call;
import retrofit2.Response;

public class ProductInStoreDisplayPage extends BasePage {
    private ProductInStoreCustomListViewAdapter adapter;
    private ListView theListView;
    private Button addNewBtn;
    private APIService mAPI;
    private ViewGroup context;
    private int storeID;
    private TextView nullMessage;
    private SearchView searchView;
    private List<Product> tempList = new ArrayList<>();
    private RelativeLayout main_layout;
    private ProgressBar loadingBar;
    private Spinner spinnerCategory, spinnerSort;
    private List<Product> productInStores;
    private ProductFilter productFilter;

    public ProductInStoreDisplayPage() {
    }

    public ViewGroup getContext() {
        return context;
    }

    @Override
    protected void onResume() {
        super.onResume();
        tempList.clear();
        adapter.notifyDataSetChanged();
        final Call<List<Product>> call = mAPI.getProductInStore(storeID);
        new ProductInStoreList().execute(call);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_in_store_display_page);
        findView();
        productFilter = new ProductFilter();
        adapter = new ProductInStoreCustomListViewAdapter(ProductInStoreDisplayPage.this, R.layout.search_product_page_custom_list_view, tempList);
        CustomInterface.setStatusBarColor(this);
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view, getBaseContext());
                return false;
            }
        });

        loadingBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);

        getSupportActionBar().setTitle("Sản phẩm cửa hàng");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        storeID = getIntent().getIntExtra("storeID", -1);
        mAPI = ApiUtils.getAPIService();
        // addSortItem();
    }

    private void findView() {
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSort = findViewById(R.id.spinnerSort);
        main_layout = findViewById(R.id.main_layout);
        searchView = findViewById(R.id.searchViewQuery);
        nullMessage = findViewById(R.id.nullMessage);
    }

    private class ProductInStoreList extends AsyncTask<Call, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!productInStores.isEmpty()) {
                loadingBar.setVisibility(View.INVISIBLE);
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nullMessage.setText("Có lỗi xảy ra, vui lòng tải lại trang!");
                        loadingBar.setVisibility(View.INVISIBLE);
                    }
                }, 10000);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected Void doInBackground(Call... calls) {
            try {
                productInStores = new ArrayList<>();

                Call<List<Product>> call = calls[0];
                final Response<List<Product>> response = call.execute();
                for (int i = 0; i < response.body().size(); i++) {
                    productInStores.add(response.body().get(i));
                }
                if (productInStores != null) {
                    for (Product product : productInStores) {
                        tempList.add(product);
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        adapter.setStoreID(storeID);
                        theListView = (ListView) findViewById(R.id.mainListView);
                        theListView.setAdapter(adapter);

                        if (response.body().size() == 0) {
                            nullMessage.setText("Cửa hàng không có sản phẩm nào!");
                            theListView.setVisibility(View.INVISIBLE);
                        } else {
                            nullMessage.setText("");
                            theListView.setVisibility(View.VISIBLE);
                        }
                        if (productInStores != null) {
                            productFilter.setSortItem(ProductInStoreDisplayPage.this, spinnerSort);
                            productFilter.setCategoryFilter(productInStores, ProductInStoreDisplayPage.this, spinnerCategory);
                        }
                        addNewBtn = (Button) findViewById(R.id.addNewBtn);
                        addNewBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent toSearchProductToStore = new Intent(ProductInStoreDisplayPage.this, SearchProductAddToStore.class);
                                toSearchProductToStore.putExtra("storeID", storeID);
                                startActivity(toSearchProductToStore);
                            }
                        });
                        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if (adapterView.getItemAtPosition(i).toString().equals(productFilter.FROM_LOW_COST)) {
                                    Collections.sort(tempList, new ProductInStoreCompareableDecrease());
                                } else if (adapterView.getItemAtPosition(i).toString().equals(productFilter.FROM_HIGH_COST)) {
                                    Collections.sort(tempList, new ProductInStoreCompareableIncrease());
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                tempList.clear();
                                if (adapterView.getItemAtPosition(i).toString().contains(productFilter.ALL_PRODUCT)) {
                                    for (Product product : productInStores) {
                                        tempList.add(product);
                                    }
                                } else if (adapterView.getItemAtPosition(i).toString().contains(productFilter.SALE_PRODUCT)) {
                                    for (Product product : productInStores) {
                                        if (product.getPromotion() != 0) {
                                            tempList.add(product);
                                        }
                                    }

                                } else {
                                    for (Product product : productInStores) {
                                        if (adapterView.getItemAtPosition(i).toString().contains(product.getCategory_name())) {
                                            tempList.add(product);
                                        }
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }
                });
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
            case R.id.action_home: {
                Intent toHomPage = new Intent(ProductInStoreDisplayPage.this, HomePage.class);
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
                if (newText.equals("") || newText == null) {
                    adapter = new ProductInStoreCustomListViewAdapter(ProductInStoreDisplayPage.this, R.layout.search_product_page_custom_list_view, tempList);

                } else {
                    for (int i = 0; i < tempList.size(); i++) {
                        if (tempList.get(i).getProduct_name().toLowerCase().contains(newText.toLowerCase())) {
                            searchedProduct.add(tempList.get(i));
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
