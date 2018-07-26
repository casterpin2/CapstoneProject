package project.view.gui;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.support.v7.widget.SearchView;
import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.adapter.TypePageListViewAdapter;
import project.view.model.ResultRegister;
import project.view.model.Type;
import retrofit2.Call;
import android.widget.ImageButton;
import retrofit2.Response;
import project.view.util.GridSpacingItemDecoration;

public class TypeCategoryPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TypePageListViewAdapter adapter;
    private List<Type> typeList;
    private int categoryId;
    private APIService mAPI;
    private TextView love_music;
    private android.widget.ProgressBar loadingBar;
    private SearchView searchView;
    private ImageButton imgBack,imgBarCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_category_page);
        findView();
        imgBarCode.setVisibility(View.INVISIBLE);
        project.view.util.CustomInterface.setStatusBarColor(this);
        String categoryName = getIntent().getStringExtra("categoryName");
        searchView.setQueryHint("Tìm trong chủng loại ...");

        setCoverImg();
        mAPI = ApiUtils.getAPIService();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        categoryId = getIntent().getIntExtra("categoryId" , 0);



        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        if (categoryId != 0){
            Call<List<Type>> call = mAPI.getType(categoryId);
            new GetType().execute(call);
        } else {
            typeList = new ArrayList<>();

            Type type = new Type(1,"Đây là Type",4,"");
            for(int i = 0 ;i < 10 ; i++){
                typeList.add(type);
            }


            adapter = new TypePageListViewAdapter(this, typeList);

        }
          imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setCoverImg(){
        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

      private void findView(){
        searchView = findViewById(R.id.searchViewQuery);
        loadingBar = (android.widget.ProgressBar) findViewById(R.id.loadingBar);
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
               onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public class GetType extends AsyncTask<Call, Void, List<Type>> {

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
        protected void onPostExecute(List<Type> aVoid) {
            super.onPostExecute(aVoid);
            adapter = new TypePageListViewAdapter(TypeCategoryPage.this, aVoid);
            recyclerView.setAdapter(adapter);
        }
    }

}
