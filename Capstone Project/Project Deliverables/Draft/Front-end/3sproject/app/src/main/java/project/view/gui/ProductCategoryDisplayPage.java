package project.view.gui;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.adapter.EndlessRecyclerViewScrollListener;
import project.view.adapter.ProductInCategoryRecyclerViewAdapter;

import project.view.model.Product;
import project.view.util.CustomInterface;
import retrofit2.Call;
import retrofit2.Response;

public class ProductCategoryDisplayPage extends BasePage implements ProductInCategoryRecyclerViewAdapter.ItemClickListener ,ProductInCategoryRecyclerViewAdapter.RetryLoadMoreListener {
    private RecyclerView recyclerView;
    private boolean emulatorLoadMoreFaild = true;
    private APIService mAPI;
    public Handler mHandle;
    public View footerView;
    public boolean isLoading;
    boolean limitData = false;
    private ProductInCategoryRecyclerViewAdapter adapter;
    private int currentPage = 0;
    int page = 0;
    int categoryId;
    private List<Product> product;
    private List<Product> productsLoadMore;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_with_filter);
        CustomInterface.setStatusBarColor(this);
        mAPI = ApiUtils.getAPIService();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        categoryId = getIntent().getIntExtra("categoryId", -1);
        String categoryName = getIntent().getStringExtra("categoryName");
        if (categoryId != -1) {
            getSupportActionBar().setTitle(categoryName);
        } else {
            //Toast.makeText(ProductCategoryDisplayPage.this, "Có lỗi xảy ra !!!",Toast.LENGTH_LONG).show();
            return;
        }
        mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new ProductInCategoryRecyclerViewAdapter(this, this, this);
        product = new ArrayList<>();
        adapter.set(product);
        recyclerView.setAdapter(adapter);
        Call<List<Product>> call = mAPI.getProductInCategory(currentPage,categoryId);
        new GetProductCategory(0).execute(call);

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
                if (product.size() %2 != 0) {
                    adapter.onReachEnd();
                    return;
                }
                if(productsLoadMore.size() == 0){
                    adapter.onReachEnd();
                    return;
                }
                adapter.add(productsLoadMore);
            }
        }, 2000);
    }

    private void callAPI (int page, int categoryId){
        //Call API
//        if (searchedProductList == null) {
//            searchedProductList = new ArrayList<>();
//            return;
//        }
//        Call<List<Product>> call = mAPI.getProductInCategory(page,categoryId);
//        new ProductInCategoryList.execute(call);
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
            if (page != 0) {
                adapter.startLoadMore();
            }
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Product> aVoid) {
            productsLoadMore = aVoid;
            if (productsLoadMore == null){
                adapter.onReachEnd();
                Toast.makeText(ProductCategoryDisplayPage.this, "Có lỗi xảy ra !!!",Toast.LENGTH_LONG).show();
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


}
