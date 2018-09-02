package project.view.gui;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
    private List<Product> tempProduct;
    private List<Product> productsLoadMore;
    private List<Category> categoryList;
    private GridLayoutManager mLayoutManager;
    private Spinner spinner,spinnerSort;
    private TextView filterLabble;
    private ProductFilter productFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_with_filter);


        mAPI = ApiUtils.getAPIService();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        spinnerSort = findViewById(R.id.spinnerSort);

        spinner = findViewById(R.id.spinnerCategory);
        spinner.setVisibility(View.INVISIBLE);
        filterLabble = findViewById(R.id.filterLabble);
        filterLabble.setText("Danh Mục");
        filterLabble.setVisibility(View.VISIBLE);
        categoryId = getIntent().getIntExtra("categoryId", -1);
        String categoryName = getIntent().getStringExtra("categoryName");

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
        Call<List<Product>> call = mAPI.getProductInCategory(currentPage,categoryId);
        new GetProductCategory(0).execute(call);
        Call<List<Category>> callCategory = mAPI.getCategory();
        new ProductCategoryDisplayPage.CategoryDisplayData().execute(callCategory);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_search_toolbar, menu);
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
                recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager){
                    @Override
                    public void onLoadMore(final int page) {
                        Log.d("page" , page+"");
                        currentPage = page;
                        Call<List<Product>> call = mAPI.getProductInCategory(currentPage,categoryId);
                        new GetProductCategory(page).execute(call);
                    }
                });
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
                e.printStackTrace();
            }
            return null;
        }
    }


    private class CategoryDisplayData extends AsyncTask<Call, List<Category>, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(List<Category>... values) {
            super.onProgressUpdate(values);
            productFilter = new ProductFilter();
            categoryList = values[0];
            productFilter.setCategoryFilterListCategory(categoryList,ProductCategoryDisplayPage.this, spinnerSort);
        }

        @Override
        protected Void doInBackground(Call... calls) {

            try {
                Call<List<Category>> call = calls[0];
                Response<List<Category>> response = call.execute();
                publishProgress(response.body());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}
