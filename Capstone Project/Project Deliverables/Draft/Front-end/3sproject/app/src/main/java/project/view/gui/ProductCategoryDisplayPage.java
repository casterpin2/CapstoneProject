package project.view.gui;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.adapter.EndlessRecyclerViewScrollListener;
import project.view.adapter.ProductInCategoryRecyclerViewAdapter;

import project.view.adapter.ProductInStoreByUserCustomCardViewAdapter;
import project.view.model.Product;
import project.view.util.CustomInterface;
import retrofit2.Call;
import retrofit2.Response;

public class ProductCategoryDisplayPage extends BasePage implements ProductInCategoryRecyclerViewAdapter.ItemClickListener ,ProductInCategoryRecyclerViewAdapter.RetryLoadMoreListener {
    private RecyclerView recyclerView;
    private boolean emulatorLoadMoreFaild = true;
    private APIService mAPI;
    private ProductInCategoryRecyclerViewAdapter adapter;
    private int currentPage = 0;
    int categoryId;
    private List<Product> product;
    private List<Product> productsLoadMore;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView tv_type_title;
    private ImageButton backBtn;
    private ImageView imgHome;
    private SearchView searchView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_product_layout);
        CustomInterface.setStatusBarColor(this);
        mAPI = ApiUtils.getAPIService();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tv_type_title = findViewById(R.id.tv_type_title);
        backBtn = findViewById(R.id.backBtn);
        imgHome = findViewById(R.id.imgHome);
        searchView = findViewById(R.id.searchViewQuery);
        categoryId = getIntent().getIntExtra("categoryId", -1);
        String categoryName = getIntent().getStringExtra("categoryName");
        if (categoryId != -1) {
            tv_type_title.setText(categoryName);
        } else {
            //Toast.makeText(ProductCategoryDisplayPage.this, "Có lỗi xảy ra !!!",Toast.LENGTH_LONG).show();
            return;
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHomPage = new Intent(ProductCategoryDisplayPage.this, HomePage.class);
                startActivity(toHomPage);
                finishAffinity();
            }
        });

        mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new ProductInCategoryRecyclerViewAdapter(this, this, this);
        product = new ArrayList<>();
        adapter.set(product);
        recyclerView.setAdapter(adapter);
        Call<List<Product>> call = mAPI.getProductInCategory(currentPage,categoryId);
        new GetProductCategory(0).execute(call);

//        final List<Product> searchedProduct = new ArrayList<>();
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
////        productList
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                searchedProduct.clear();
//                if (newText.equals("") || newText == null) {
//                    adapter = new ProductInCategoryRecyclerViewAdapter(ProductCategoryDisplayPage.this, ProductCategoryDisplayPage.this, ProductCategoryDisplayPage.this);
//                    adapter.set(product);
//                    recyclerView.setAdapter(adapter);
//
//                } else {
//                    for (int i = 0; i < product.size(); i++) {
//                        if (product.get(i).getProduct_name().toLowerCase().contains(newText.toLowerCase())) {
//                            searchedProduct.add(product.get(i));
//                        }
//                    }
//                    adapter = new ProductInCategoryRecyclerViewAdapter(ProductCategoryDisplayPage.this, ProductCategoryDisplayPage.this, ProductCategoryDisplayPage.this);
//                    adapter.set(searchedProduct);
//                    recyclerView.setAdapter(adapter);
//                }
//                mLayoutManager = new GridLayoutManager(ProductCategoryDisplayPage.this, 2);
//                recyclerView.setLayoutManager(mLayoutManager);
//                recyclerView.setAdapter(adapter);
//                return true;
//            }
//        });
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
