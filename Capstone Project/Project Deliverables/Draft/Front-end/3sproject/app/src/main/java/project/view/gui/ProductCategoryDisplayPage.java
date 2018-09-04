package project.view.gui;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.adapter.EndlessRecyclerViewScrollListener;
import project.view.adapter.ProductInCategoryRecyclerViewAdapter;

import project.view.model.Brand;
import project.view.model.Category;
import project.view.model.Product;
import project.view.util.CustomInterface;
import project.view.util.ProductFilter;
import retrofit2.Call;
import retrofit2.Response;

public class ProductCategoryDisplayPage extends BasePage implements ProductInCategoryRecyclerViewAdapter.ItemClickListener ,ProductInCategoryRecyclerViewAdapter.RetryLoadMoreListener {
    private RecyclerView recyclerView;
    private boolean emulatorLoadMoreFaild = true;
    private APIService mAPI;
    private ProductInCategoryRecyclerViewAdapter adapter;
    private int currentPage = 0;
    int categoryId;
    private SearchView searchView;
    private List<Product> tempProduct;
    private List<Product> productsLoadMore;
    private List<Category> categoryList;
    private GridLayoutManager mLayoutManager;
    private Spinner spinner,spinnerSort;
    private TextView filterLabble;
    public List<Brand> listBrands;
    private int filterSelected;
    private boolean isLoading = false;
    private ProductFilter productFilter;
    String categoryName;
    private EndlessRecyclerViewScrollListener listener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_with_filter);
        productFilter = new ProductFilter();

        mAPI = ApiUtils.getAPIService();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        filterSelected = 0;
        spinnerSort = findViewById(R.id.spinnerSort);
        searchView = findViewById(R.id.action_search);
        spinner = findViewById(R.id.spinnerCategory);
        spinner.setVisibility(View.INVISIBLE);
        filterLabble = findViewById(R.id.filterLabble);
        filterLabble.setText("Danh Mục");
        filterLabble.setVisibility(View.VISIBLE);
        categoryId = getIntent().getIntExtra("categoryId", -1);
        categoryName = getIntent().getStringExtra("categoryName");

        getSupportActionBar().setTitle(categoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CustomInterface.setStatusBarColor(this);

        mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.TYPE_PROGRESS == adapter.getItemViewType(position)){
                    return 2;
                }
                return 1;
            }
        });
        adapter = new ProductInCategoryRecyclerViewAdapter(this, this, this);
        tempProduct = new ArrayList<>();
        adapter.set(tempProduct);
        recyclerView.setAdapter(adapter);
        if (categoryId != -1) {
            Call<List<Product>> call = mAPI.getProductInCategory(currentPage, categoryId);
            new GetProductCategory(0).execute(call);
            Call<List<Brand>> callBrand = mAPI.listBrandByCategory(categoryId);
            new GetBrandCategory().execute(callBrand);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_search_toolbar, menu);
        MenuItem itemSearch = menu.findItem(R.id.action_search);
        searchView = (SearchView) itemSearch.getActionView();
        searchView.clearFocus();
        searchView.setQueryHint("Tìm trong " + categoryName);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        productList
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                while (isLoading == true){

                }
                adapter.onReachStart();
                recyclerView.removeOnScrollListener(listener);
                currentPage = 0;
                adapter.clear();
                Call<List<Product>> callProduct = ApiUtils.getAPIService().getProductInCategoryByName(currentPage,categoryId,query);
                new GetProductCategoryByName(0,query).execute(callProduct);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.action_home:{
                Intent toHomPage = new Intent(ProductCategoryDisplayPage.this, HomePage.class);
                startActivity(toHomPage);
                finishAffinity();
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRetryLoadMore() {
        loadMore(currentPage);
    }

    private void loadMore(final int page){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(productsLoadMore == null && emulatorLoadMoreFaild && page !=0){
                    adapter.onLoadMoreFailed();
                    emulatorLoadMoreFaild = false;
                    return;
                }
                if (tempProduct != null && tempProduct.size() %2 != 0) {
                    adapter.onReachEnd();
                    return;
                }
                if(productsLoadMore != null && productsLoadMore.size() == 0){
                    adapter.onReachEnd();
                    return;
                }
                if(productsLoadMore != null) {
                    adapter.add(productsLoadMore);
                }
            }
        }, 2000);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    private class GetProductCategory extends AsyncTask<Call,Void,List<Product>> {
        private int page;

        public GetProductCategory(int page) {
            this.page = page;
        }

        @Override
        protected void onPreExecute() {
            isLoading = true;
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Product> aVoid) {
            productsLoadMore = aVoid;

            if (productsLoadMore == null){
                adapter.onLoadMoreFailed();
                emulatorLoadMoreFaild = false;
                isLoading = false;
                return;
            }
            loadMore(page);
            if (page == 0 && productsLoadMore != null){
                listener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
                    @Override
                    public void onLoadMore(int page) {
                        currentPage = page;
                        Call<List<Product>> call = mAPI.getProductInCategory(currentPage,categoryId);
                        new GetProductCategory(page).execute(call);
                    }
                };
                recyclerView.addOnScrollListener(listener);
            }
            isLoading = false;
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
                e.printStackTrace();
            }
            return null;
        }
    }

    private class GetProductCategoryByName extends AsyncTask<Call,Void,List<Product>> {
        private int page;
        private String query;
        public GetProductCategoryByName(int page, String query) {
            this.page = page;
            this.query = query;
        }

        @Override
        protected void onPreExecute() {
            isLoading = true;
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Product> aVoid) {
            productsLoadMore = aVoid;

            if (productsLoadMore == null){
                adapter.onLoadMoreFailed();
                emulatorLoadMoreFaild = false;
                isLoading = false;
                return;
            }
            loadMore(page);
            if (page == 0 && productsLoadMore != null){
                listener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
                    @Override
                    public void onLoadMore(int page) {
                        currentPage = page;
                        Call<List<Product>> call = mAPI.getProductInCategoryByName(currentPage,categoryId,query);
                        new GetProductCategory(page).execute(call);
                    }
                };
                recyclerView.addOnScrollListener(listener);
            }
            isLoading = false;
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
                e.printStackTrace();
            }
            return null;
        }
    }

    private class GetBrandCategory extends AsyncTask<Call, Void, List<Brand>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(List<Brand> aVoid) {
            if (aVoid != null){
                listBrands = aVoid;
                spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (filterSelected == i){
                            return;
                        }
                        while (isLoading == true){

                        }
                        adapter.onReachStart();
                        recyclerView.removeOnScrollListener(listener);
                        currentPage = 0;
                        adapter.clear();
                        if (i == 0){
                            Call<List<Product>> call = mAPI.getProductInCategory(currentPage, categoryId);
                            new GetProductCategory(0).execute(call);
                        }
                        else {
                            Call<List<Product>> call = mAPI.productWithBrandCategory(listBrands.get(i - 1).getBrandID(), categoryId, currentPage);
                            new GetProductBrandCategory(listBrands.get(i - 1).getBrandID(),0).execute(call);
                        }
                        filterSelected = i;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                productFilter.setBrandsFilter(listBrands,ProductCategoryDisplayPage.this,spinnerSort);

            }
            super.onPostExecute(aVoid);

        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected List<Brand> doInBackground(Call... calls) {
            try {
                Call<List<Brand>> call = calls[0];
                Response<List<Brand>> response = call.execute();
                return response.body();
            } catch (IOException e) {
            }
            return null;
        }
    }

    private class GetProductBrandCategory extends AsyncTask<Call, Void, List<Product>> {
        private int page;
        private int brandId;

        public GetProductBrandCategory(int brandId, int page) {
            this.page = page;
            this.brandId = brandId;
        }
        @Override
        protected void onPreExecute() {
            isLoading = true;
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(List<Product> aVoid) {
            productsLoadMore = aVoid;
            if (productsLoadMore == null){
                adapter.onLoadMoreFailed();
                emulatorLoadMoreFaild = false;
                isLoading = false;
                return;
            }
            loadMore(page);
            if (page == 0 && productsLoadMore != null){
                listener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
                    @Override
                    public void onLoadMore(int page) {
                        currentPage = page;
                        Call<List<Product>> call = mAPI.productWithBrandCategory(brandId,categoryId,currentPage);
                        new GetProductBrandCategory(brandId,page).execute(call);
                    }
                };
                recyclerView.addOnScrollListener(listener);
            }
            isLoading = false;
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
