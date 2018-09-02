package project.view.gui;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.firebase.Firebase;
import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.adapter.BrandCustomCardviewAdapter;
import project.view.R;
import project.view.adapter.EndlessRecyclerViewScrollListener;
import project.view.adapter.ProductInCategoryRecyclerViewAdapter;
import project.view.model.Brand;
import project.view.model.Product;
import project.view.util.CustomInterface;
import project.view.util.Formater;
import project.view.util.GridSpacingItemDecoration;
import retrofit2.Call;
import retrofit2.Response;

public class BrandDisplayPage extends BasePage implements BrandCustomCardviewAdapter.ItemClickListener ,BrandCustomCardviewAdapter.RetryLoadMoreListener{

    private CoordinatorLayout main_layout;
    private RecyclerView recyclerView;
    private BrandCustomCardviewAdapter adapter;
    private boolean emulatorLoadMoreFaild = true;
    private List<Brand> brandList;
    private APIService apiService;
    private ProgressBar loadingBar;
    private SearchView searchView;
    private ImageButton imgBack;
    private ImageView imgHome;
    private List<Brand> searchedProduct = new ArrayList<>();
    private TextView nullMessage;
    private int currentPage = 0;
    private List<Brand> tempBrand;
    private final List<String> suggestions = new ArrayList<>();
    private List<Brand> brandtsLoadMore;
    private GridLayoutManager mLayoutManager;
    private EndlessRecyclerViewScrollListener listener;
    private boolean isLoading;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    final DatabaseReference myRef1 = myRef.child("suggestion").child("brands");
    private ValueEventListener listener1;
    private CursorAdapter suggestionAdapter;
    private final List<String> searchedList = new ArrayList<>();
    @Override
    protected void onResume() {
        super.onResume();
        listener1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                suggestions.clear();
                for (DataSnapshot dttSnapshot2 : dataSnapshot.getChildren()) {
                    suggestions.add(dttSnapshot2.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        myRef1.addValueEventListener(listener1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (myRef1 != null) {
            myRef1.removeEventListener(listener1);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_display_page);
        findView();
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view, getBaseContext());
                return false;
            }
        });
        isLoading = false;
        apiService = ApiUtils.getAPIService();
        loadingBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);
        searchView.setQueryHint("Tìm trong thương hiệu ...");
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
        adapter = new BrandCustomCardviewAdapter(this, this, this);
        tempBrand = new ArrayList<>();
        adapter.set(tempBrand);
        recyclerView.setAdapter(adapter);
        suggestionAdapter = new SimpleCursorAdapter(this,
                R.layout.custom_suggested_listview_nearby_store_page,
                null,
                new String[]{SearchManager.SUGGEST_COLUMN_TEXT_1},
                new int[]{android.R.id.text1},
                0);
        searchView.setSuggestionsAdapter(suggestionAdapter);
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
                if (query.isEmpty()){
                    final Call<List<Brand>> callBrand = apiService.getBrands(currentPage);
                    new BrandDisplayData(0).execute(callBrand);
                } else {

                    final Call<List<Brand>> callBrand = apiService.getBrandsByName(query,currentPage);
                    new BrandDisplayDataByName(0,query).execute(callBrand);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String[] columns = { BaseColumns._ID,
                        SearchManager.SUGGEST_COLUMN_TEXT_1,
                };
                final MatrixCursor cursor = new MatrixCursor(columns);
                searchedList.removeAll(searchedList);
                for (int i = 0; i < suggestions.size(); i++) {
                    Log.d("searchedList",String.valueOf(searchedList.size()));
                    if (suggestions.get(i).toLowerCase().contains(newText)) {
                        String[] tmp = {Integer.toString(i), suggestions.get(i)};
                        cursor.addRow(tmp);

                        searchedList.add(suggestions.get(i).toString());

                    }
                }
                suggestionAdapter.swapCursor(cursor);
                return true;
            }
        });
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                searchView.setQuery(searchedList.get(position).toString(), true);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                searchView.setQuery(searchedList.get(position).toString(), true);
                searchView.clearFocus();
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
                Intent toHomePage = new Intent(BrandDisplayPage.this, HomePage.class);
                startActivity(toHomePage);
                finishAffinity();
            }
        });
        brandList = new ArrayList<>();
        apiService = APIService.retrofit.create(APIService.class);
        final Call<List<Brand>> callBrand = apiService.getBrands(currentPage);
        new BrandDisplayData(0).execute(callBrand);
        try {
            Glide.with(this).load(R.drawable.brand_cover).into((ImageView) findViewById(R.id.cover));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void findView() {
        searchView = findViewById(R.id.searchViewQuery);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        imgBack = findViewById(R.id.backBtn);
        imgHome = findViewById(R.id.imgHome);
        main_layout = findViewById(R.id.main_layout);
        nullMessage = findViewById(R.id.nullMessage);
    }

    private class BrandDisplayData extends AsyncTask<Call, Void, List<Brand>> {
        private int page;

        public BrandDisplayData(int page) {
            this.page = page;
        }
        @Override
        protected void onPreExecute() {
            isLoading = true;
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Brand> aVoid) {
            brandtsLoadMore = aVoid;

            if (brandtsLoadMore == null){
                adapter.onLoadMoreFailed();
                emulatorLoadMoreFaild = false;
                return;
            }
            loadMore(page);
            if (page == 0 && brandtsLoadMore != null){
                listener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
                    @Override
                    public void onLoadMore(int page) {
                        currentPage = page;
                        apiService = APIService.retrofit.create(APIService.class);
                        final Call<List<Brand>> callBrand = apiService.getBrands(currentPage);
                        new BrandDisplayData(page).execute(callBrand);
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
        protected List<Brand> doInBackground(Call... calls) {
            try {
                Call<List<Brand>> call = calls[0];
                Response<List<Brand>> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    private class BrandDisplayDataByName extends AsyncTask<Call, Void, List<Brand>> {
        private int page;
        private String query;
        public BrandDisplayDataByName(int page,String query) {
            this.query = query;
            this.page = page;
        }
        @Override
        protected void onPreExecute() {
            isLoading = true;
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Brand> aVoid) {
            brandtsLoadMore = aVoid;

            if (brandtsLoadMore == null){
                adapter.onLoadMoreFailed();
                emulatorLoadMoreFaild = false;
                return;
            }
            loadMore(page);
            if (page == 0 && brandtsLoadMore != null){
                listener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
                    @Override
                    public void onLoadMore(int page) {
                        currentPage = page;
                        apiService = APIService.retrofit.create(APIService.class);
                        final Call<List<Brand>> callBrand = apiService.getBrandsByName(query,currentPage);
                        new BrandDisplayDataByName(page,query).execute(callBrand);
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
        protected List<Brand> doInBackground(Call... calls) {
            try {
                Call<List<Brand>> call = calls[0];
                Response<List<Brand>> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
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
                if(brandtsLoadMore == null && emulatorLoadMoreFaild && page !=0){
                    adapter.onLoadMoreFailed();
                    emulatorLoadMoreFaild = false;
                    return;
                }
                if (tempBrand != null && tempBrand.size() %2 != 0) {
                    adapter.onReachEnd();
                    return;
                }
                if(brandtsLoadMore != null && brandtsLoadMore.size() == 0){
                    adapter.onReachEnd();
                    return;
                }
                if (brandtsLoadMore != null){
                    adapter.add(brandtsLoadMore);
                }
            }
        }, 2000);
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
