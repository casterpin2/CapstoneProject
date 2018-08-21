package project.view.gui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
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
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.SearchView;

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.adapter.CategoryCustomCardviewAdapter;
import project.view.adapter.TypePageListViewAdapter;
import project.view.model.ResultRegister;
import project.view.model.Type;
import project.view.util.CustomInterface;
import project.view.util.Formater;
import retrofit2.Call;

import android.widget.ImageButton;
import android.widget.Toast;

import retrofit2.Response;
import project.view.util.GridSpacingItemDecoration;

public class TypeCategoryPage extends BasePage {

    private RecyclerView recyclerView;
    private TypePageListViewAdapter adapter;
    private List<Type> typeList = new ArrayList<>();
    ;
    private int categoryId;
    private APIService mAPI;
    private TextView tv_type_title;
    private android.widget.ProgressBar loadingBar;
    private SearchView searchView;
    private ImageButton imgBack;
    private List<Type> searchedProduct = new ArrayList<>();
    private CoordinatorLayout main_layout;
    private Button btnViewAll;
    private TextView nullMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_category_page);
        findView();
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view, getBaseContext());
                return false;
            }
        });
        String categoryName = getIntent().getStringExtra("categoryName");
        tv_type_title.setText(categoryName);
        searchView.setQueryHint("Tìm trong " + categoryName);
        setCoverImg();
        mAPI = ApiUtils.getAPIService();

        categoryId = getIntent().getIntExtra("categoryId", 0);

        if (categoryId != 0) {
            Call<List<Type>> call = mAPI.getType(categoryId);
            new GetType().execute(call);
        }
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setCoverImg() {
        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findView() {
        main_layout = findViewById(R.id.main_layout);
        searchView = findViewById(R.id.searchViewQuery);
        loadingBar = (android.widget.ProgressBar) findViewById(R.id.loadingBar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        imgBack = findViewById(R.id.backBtn);
        tv_type_title = (TextView) findViewById(R.id.tv_type_title);
        nullMessage = findViewById(R.id.nullMessage);
        btnViewAll = (Button) findViewById(R.id.btnAll);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TypeCategoryPage.this, ProductCategoryDisplayPage.class);
                intent.putExtra("categoryId", categoryId);
                intent.putExtra("categoryName", getIntent().getStringExtra("categoryName"));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class GetType extends AsyncTask<Call, Void, List<Type>> {

        @Override
        protected void onPreExecute() {
            loadingBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected List<Type> doInBackground(Call... calls) {
            try {
                Call<List<Type>> call = calls[0];
                Response<List<Type>> re = call.execute();
//            if (re.body() != null) {
                return re.body();
//            } else {
//                return null;
//            }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<Type> aVoid) {
            super.onPostExecute(aVoid);
            if (aVoid == null) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TypeCategoryPage.this, "Có lỗi khi định vị vị trí của bạn", Toast.LENGTH_SHORT).show();
                        nullMessage.setText("Có lỗi xảy ra, vui lòng tải lại trang!");
                        loadingBar.setVisibility(View.INVISIBLE);

                    }
                }, 10000);
                return;
            }
            adapter = new TypePageListViewAdapter(TypeCategoryPage.this, aVoid);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(TypeCategoryPage.this, 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, Formater.dpToPx(10, getResources()), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);

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
                        adapter = new TypePageListViewAdapter(TypeCategoryPage.this, aVoid);

                    } else {
                        for (int i = 0; i < aVoid.size(); i++) {
                            if (aVoid.get(i).getTypeName().toLowerCase().contains(newText.toLowerCase())) {
                                searchedProduct.add(aVoid.get(i));
                            }
                        }
                        adapter = new TypePageListViewAdapter(TypeCategoryPage.this, searchedProduct);
                    }
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(TypeCategoryPage.this, 2);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setAdapter(adapter);
                    return true;
                }
            });
        }
    }

}
