package project.view.gui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.firebase.Firebase;
import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.adapter.CategoryCustomCardviewAdapter;
import project.view.R;
import project.view.model.Category;
import project.view.model.Item;
import project.view.util.CustomInterface;
import project.view.util.Formater;
import project.view.util.GridSpacingItemDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryDisplayPage extends BasePage {

    private CoordinatorLayout main_layout;
    private RecyclerView recyclerView;
    private CategoryCustomCardviewAdapter adapter;
    private List<Category> categoryList;
    private APIService apiService;
    private ProgressBar loadingBar;
    private SearchView searchView;
    private ImageButton imgBack;
    private ImageView imgHome;
    private List<Category> searchedProduct = new ArrayList<>();
    private TextView nullMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_display_page);

        findView();
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view,getBaseContext());
                return false;
            }
        });
        loadingBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);

        searchView.setQueryHint("Tìm trong danh mục ...");

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
                    adapter = new CategoryCustomCardviewAdapter(CategoryDisplayPage.this, categoryList);

                } else {
                    for (int i = 0; i < categoryList.size(); i++) {
                        if(categoryList.get(i).getCategoryName().toLowerCase().contains(newText.toLowerCase())) {
                            searchedProduct.add(categoryList.get(i));
                        }
                    }
                    adapter = new CategoryCustomCardviewAdapter(CategoryDisplayPage.this, searchedProduct);
                }
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(CategoryDisplayPage.this, 2);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(adapter);
                return true;
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHomePage = new Intent(CategoryDisplayPage.this,HomePage.class);
                startActivity(toHomePage);
            }
        });
        apiService = ApiUtils.getAPIService();
        categoryList = new ArrayList<>();

        apiService = APIService.retrofit.create(APIService.class);
        final Call<List<Category>> callCategory = apiService.getCategory();
        new CategoryDisplayData().execute(callCategory);
       try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.cover));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void findView(){
        main_layout = findViewById(R.id.main_layout);
        searchView = findViewById(R.id.searchViewQuery);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        imgBack = findViewById(R.id.backBtn);
        imgHome = findViewById(R.id.imgHome);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        nullMessage = findViewById(R.id.nullMessage);
    }

    private class CategoryDisplayData extends AsyncTask<Call, List<Category>, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(!categoryList.isEmpty()){
                adapter = new CategoryCustomCardviewAdapter(CategoryDisplayPage.this, categoryList);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(CategoryDisplayPage.this, 2);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, Formater.dpToPx(10,getResources()), true));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
                loadingBar.setVisibility(View.INVISIBLE);
            } else {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            nullMessage.setText("Có lỗi xảy ra, vui lòng tải lại trang!");
                            loadingBar.setVisibility(View.INVISIBLE);
                    }
                }, 10000);
            }
        }

        @Override
        protected void onProgressUpdate(List<Category>... values) {
            super.onProgressUpdate(values);
            StorageReference storageReference = Firebase.getFirebase();
            categoryList = values[0];
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
