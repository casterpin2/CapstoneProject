package project.view.gui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.adapter.BrandCustomCardviewAdapter;
import project.view.adapter.EndlessRecyclerViewScrollListener;
import project.view.adapter.ProductInStoreCustomListViewAdapter;
import project.view.adapter.ProductTypeCustomCardViewAdapter;
import project.view.R;
import project.view.model.Brand;
import project.view.model.Product;
import project.view.util.CustomInterface;
import project.view.util.Formater;
import project.view.util.GridSpacingItemDecoration;
import project.view.util.ProductFilter;
import retrofit2.Call;
import retrofit2.Response;

public class ProductTypeDisplayPage extends BasePage implements BrandCustomCardviewAdapter.ItemClickListener ,BrandCustomCardviewAdapter.RetryLoadMoreListener{

    public List<Product> productList;

    private RecyclerView recyclerView;
    private ProductTypeCustomCardViewAdapter adapter;
    private TextView filterLable, nullMessage;
    private Spinner spinnerCategory,spinnerBrand;
    ProductFilter productFilter;
    private  List<Product> searchedProduct;
    private SearchView searchView;
    private String typeName;
    private ProgressBar loadingBar;
    private int currentPage = 0;
    public List<Product> tempProduct;
    private boolean isLoading;
    public List<Brand> listBrands;
    private List<Product> productsLoadMore;
    private GridLayoutManager mLayoutManager;
    private boolean emulatorLoadMoreFaild = true;
    private int filterSelected;
    private EndlessRecyclerViewScrollListener listener;
    private int typeId;
    private APIService mApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_with_filter);
        findView();
        mApi = ApiUtils.getAPIService();
        isLoading = false;
        filterSelected = 0;
        loadingBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);
        productFilter = new ProductFilter();
        typeId = getIntent().getIntExtra("typeID", -1);
        typeName = getIntent().getStringExtra("typeName");
        productList = new ArrayList<>();
        tempProduct = new ArrayList<>();
        adapter = new ProductTypeCustomCardViewAdapter(this, this,this);
        mLayoutManager = new GridLayoutManager(ProductTypeDisplayPage.this, 2);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.TYPE_PROGRESS == adapter.getItemViewType(position)){
                    return 2;
                }
                return 1;
            }
        });
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, Formater.dpToPx(2,getResources()), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CustomInterface.setStatusBarColor(this);
        recyclerView.setAdapter(adapter);
        if (typeId != -1) {
            getSupportActionBar().setTitle(typeName);
            Call<List<Product>> callProduct = ApiUtils.getAPIService().getProductbyType(typeId,currentPage);
            new GetProductType(0).execute(callProduct);
            Call<List<Brand>> callBrand = ApiUtils.getAPIService().listBrandByType(typeId);
            new GetBrandType().execute(callBrand);
        } else {
            Toast.makeText(ProductTypeDisplayPage.this, "Có lỗi xảy ra !!!",Toast.LENGTH_LONG).show();
        }
    }

    private void findView(){
        recyclerView = findViewById(R.id.recycler_view);
        filterLable = findViewById(R.id.filterLabble);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerCategory.setVisibility(View.INVISIBLE);
        filterLable.setVisibility(View.VISIBLE);
        filterLable.setText("Thương hiệu");
        spinnerBrand = findViewById(R.id.spinnerSort);
        searchView = findViewById(R.id.action_search);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);
        nullMessage = findViewById(R.id.nullMessage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_search_toolbar, menu);
        MenuItem itemSearch = menu.findItem(R.id.action_search);
        searchedProduct = new ArrayList<>();
        searchView = (SearchView) itemSearch.getActionView();
        searchView.clearFocus();
        searchView.setQueryHint("Tìm trong " + typeName);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        productList
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                searchedProduct.clear();
//                if(newText.equals("") || newText == null){
//                    adapter = new ProductTypeCustomCardViewAdapter(ProductTypeDisplayPage.this,tempProduct);
//                } else {
//                    for (int i = 0; i < tempProduct.size(); i++) {
//                        if(tempProduct.get(i).getProduct_name().toLowerCase().contains(newText.toLowerCase())) {
//                            searchedProduct.add(tempProduct.get(i));
//                        }
//                    }
//                    adapter = new ProductTypeCustomCardViewAdapter(ProductTypeDisplayPage.this, searchedProduct);
//                }
//                adapter.notifyDataSetChanged();
//                recyclerView.setAdapter(adapter);
//                return true;
//            }
//        });
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
                Intent toHomPage = new Intent(ProductTypeDisplayPage.this, HomePage.class);
                startActivity(toHomPage);
                finishAffinity();
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class GetProductType extends AsyncTask<Call, Void, List<Product>> {
        private int page;

        public GetProductType(int page) {
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
                return;
            }
            loadMore(page);
            if (page == 0 && productsLoadMore != null){
                listener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
                    @Override
                    public void onLoadMore(int page) {
                        currentPage = page;
                        Call<List<Product>> call = mApi.getProductbyType(typeId,currentPage);
                        new GetProductType(page).execute(call);
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

    private class GetProductBrandType extends AsyncTask<Call, Void, List<Product>> {
        private int page;
        private int brandId;

        public GetProductBrandType(int brandId, int page) {
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
                return;
            }
            loadMore(page);
            if (page == 0 && productsLoadMore != null){
                listener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
                    @Override
                    public void onLoadMore(int page) {
                        currentPage = page;
                        Call<List<Product>> call = mApi.productWithBrandType(brandId,typeId,currentPage);
                        new GetProductBrandType(brandId,page).execute(call);
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

    private class GetBrandType extends AsyncTask<Call, Void, List<Brand>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(List<Brand> aVoid) {
            if (aVoid != null){
                listBrands = aVoid;
                spinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                            Call<List<Product>> callProduct = mApi.getProductbyType(typeId,currentPage);
                            new GetProductType(0).execute(callProduct);
                        }
                        else {
                            Call<List<Product>> call = mApi.productWithBrandType(listBrands.get(i - 1).getBrandID(), typeId, currentPage);
                            new GetProductBrandType(listBrands.get(i - 1).getBrandID(),0).execute(call);
                        }
                        filterSelected = i;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                productFilter.setBrandsFilter(listBrands,ProductTypeDisplayPage.this,spinnerBrand);

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
                if (    tempProduct != null && tempProduct.size() %2 != 0) {
                    adapter.onReachEnd();
                    return;
                }
                if(productsLoadMore != null && productsLoadMore.size() == 0){
                    adapter.onReachEnd();
                    return;
                }
                if (productsLoadMore != null){
                    adapter.add(productsLoadMore);
                }
            }
        }, 2000);
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
