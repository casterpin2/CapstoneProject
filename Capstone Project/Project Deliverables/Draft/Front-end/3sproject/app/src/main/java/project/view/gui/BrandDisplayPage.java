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
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_display_page);
        CustomInterface.setStatusBarColor(this);
        apiService = ApiUtils.getAPIService();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);
        loadingBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);

        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        brandList = new ArrayList<>();
   //     prepareAlbums();
        apiService = APIService.retrofit.create(APIService.class);
        final Call<List<Brand>> callBrand = apiService.getBrands();
        new BrandDisplayData().execute(callBrand);

        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.brand_display_title));
                    isShow = true;
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                }
            }
        });
    }

//    private void prepareAlbums() {
//        //Call API
//       apiService.getBrands().enqueue(new Callback<List<Brand>>() {
//           @Override
//           public void onResponse(Call<List<Brand>> call, Response<List<Brand>> response) {
//               for(int i =0;i<response.body().size();i++){
//
//                       brandList.add(response.body().get(i));
//
//               }
//               if(brandList.isEmpty()){
//                   Toast.makeText(BrandDisplayPage.this, R.string.somethingWrong, Toast.LENGTH_LONG).show();
//               }
//           }
//
//           @Override
//           public void onFailure(Call<List<Brand>> call, Throwable t) {
//
//           }
//       });
//
//
//    }

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
