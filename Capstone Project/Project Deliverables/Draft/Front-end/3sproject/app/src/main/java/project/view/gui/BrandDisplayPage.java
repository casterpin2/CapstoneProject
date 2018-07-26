package project.view.gui;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.firebase.Firebase;
import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.adapter.BrandCustomCardviewAdapter;
import project.view.R;
import project.view.model.Brand;
import project.view.model.Item;
import project.view.util.CustomInterface;
import project.view.util.GridSpacingItemDecoration;
import retrofit2.Call;
import retrofit2.Response;

public class BrandDisplayPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BrandCustomCardviewAdapter adapter;
    private List<Brand> brandList;
    private APIService apiService;
    private ProgressBar loadingBar;
    private SearchView searchView;
    private ImageButton imgBack,imgBarCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_display_page);

        findView();
        imgBarCode.setVisibility(View.INVISIBLE);
        CustomInterface.setStatusBarColor(this);
        apiService = ApiUtils.getAPIService();
        loadingBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);
        searchView.setQueryHint("Tìm trong thương hiệu ...");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        brandList = new ArrayList<>();
        apiService = APIService.retrofit.create(APIService.class);
        final Call<List<Brand>> callBrand = apiService.getBrands();
        new BrandDisplayData().execute(callBrand);
        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findView(){
        searchView = findViewById(R.id.searchViewQuery);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        imgBack = findViewById(R.id.backBtn);
        imgBarCode = findViewById(R.id.imgBarCode);
    }
    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private boolean checkID(int productID){
        for (Item item: SearchProductAddToStore.addedProductList) {
            if (item.getProduct_id() == productID) return false;
        }
        return true;
    }
    private class BrandDisplayData extends AsyncTask<Call, List<Brand>, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(List<Brand>... values) {
            super.onProgressUpdate(values);
            StorageReference storageReference = Firebase.getFirebase();
            List<Brand> brandList = values[0];


            adapter = new BrandCustomCardviewAdapter(BrandDisplayPage.this, brandList);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(BrandDisplayPage.this, 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            loadingBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Call... calls) {

            try {
                Call<List<Brand>> call = calls[0];
                Response<List<Brand>> response = call.execute();
                List<Brand> list = new ArrayList<>();
                for(int i =0;i< response.body().size();i++){
                    list.add(response.body().get(i));
                }
                publishProgress(list);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
