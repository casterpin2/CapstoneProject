package project.view.gui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.adapter.ProductInStoreByUserCustomListViewAdapter;
import project.view.model.ProductInStore;
import project.view.util.CustomInterface;
import project.view.util.Formater;
import project.view.util.GridSpacingItemDecoration;
import retrofit2.Call;
import retrofit2.Response;

public class StoreInformationPage extends AppCompatActivity {
    private ImageView imgBarCode;
    private int storeID;
    private String storeName;
    private APIService mAPI;
    private RecyclerView recycler_view;
    private ProductInStoreByUserCustomListViewAdapter productInStoreByUserCustomListViewAdapter;
    private ImageView backBtn;
    private TextView tvStoreName;
    private Spinner spinnerCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_information_page);
        findView();

        imgBarCode.setVisibility(View.INVISIBLE);
        CustomInterface.setStatusBarColor(this);
        storeName = getIntent().getStringExtra("storeName");
        tvStoreName.setText(storeName);
        storeID = getIntent().getIntExtra("storeID", -1);
        mAPI = ApiUtils.getAPIService();
        final Call<List<ProductInStore>> call = mAPI.getProductInStore(storeID);
        new StoreInformationPage.Data().execute(call);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addItemsOnSpinner2();
    }

    public void addItemsOnSpinner2() {


        List<String> list = new ArrayList<String>();
        list.add("Tất cả sản phẩm");
        list.add("Đồ uống");
        list.add("Đồ ăn");
        list.add("Đồ gia dụng");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(dataAdapter);
    }

    private void findView(){
        imgBarCode = findViewById(R.id.imgBarCode);
        recycler_view = findViewById(R.id.recycler_view);
        backBtn = findViewById(R.id.backBtn);
        tvStoreName = findViewById(R.id.tv_store_name);
        spinnerCategory = findViewById(R.id.spinnerCategory);
    }

    public class Data extends AsyncTask<Call,List<ProductInStore>,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(List<ProductInStore>... values) {
            super.onProgressUpdate(values);
            List<ProductInStore> productInStores = values[0];
            productInStoreByUserCustomListViewAdapter = new ProductInStoreByUserCustomListViewAdapter(StoreInformationPage.this, productInStores);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(StoreInformationPage.this, 2);
            recycler_view.setLayoutManager(mLayoutManager);
            recycler_view.addItemDecoration(new GridSpacingItemDecoration(2, Formater.dpToPx(2,getResources()), true));
            recycler_view.setItemAnimator(new DefaultItemAnimator());
            recycler_view.setAdapter(productInStoreByUserCustomListViewAdapter);
        }

        @Override
        protected Void doInBackground(Call... calls) {
            try {
                Call<List<ProductInStore>> call = calls[0];
                Response<List<ProductInStore>> response = call.execute();
                List<ProductInStore> list = new ArrayList<>();
                for(int i =0 ; i< response.body().size();i++){
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
