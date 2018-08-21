package project.view.gui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import project.firebase.Firebase;
import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.adapter.ProductInStoreByUserCustomCardViewAdapter;
import project.view.model.Product;
import project.view.model.Store;
import project.view.util.CustomInterface;
import project.view.util.Formater;
import project.view.util.GridSpacingItemDecoration;
import project.view.util.ProductFilter;
import project.view.util.ProductInStoreCompareableDecrease;
import project.view.util.ProductInStoreCompareableIncrease;
import retrofit2.Call;
import retrofit2.Response;

public class ProductInStoreByUserDisplayPage extends BasePage {
    private ImageView imgBarCode;
    private int storeID;
    private String storeName;
    private String phone;
    private String image_path;
    private Store store;
    private APIService mAPI;
    private RecyclerView recycler_view;
    private ProductInStoreByUserCustomCardViewAdapter productInStoreByUserCustomListViewAdapter;
    private ImageView backBtn,backdrop;
    private TextView tvStoreName;
    private Spinner spinnerCategory,spinnerSort;
    private StorageReference storageReference = Firebase.getFirebase();
    private List<Product> products;
    private CoordinatorLayout main_layout;
    private SearchView searchView ;
    private List<Product> tempProductInStore;
    private ProductFilter productFilter;
    private LinearLayout storeNameLayout;
    private ProgressBar loadingBar;
    private TextView nullMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_in_store_by_user_display_page);
        findView();
        customView();
        loadingBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);
        storeName = getIntent().getStringExtra("storeName");
        phone = getIntent().getStringExtra("phone");
        image_path = getIntent().getStringExtra("image_path");
        tvStoreName.setText(storeName);
        storeID = getIntent().getIntExtra("storeID", -1);
        store = new Store(storeID,storeName,phone,image_path);
        searchView.setQueryHint("Tìm trong "+storeName);
        if (!image_path.isEmpty()){
            Glide.with(this /* context */)
                    .using(new FirebaseImageLoader())
                    .load(storageReference.child(image_path))
                    .into(backdrop);
        }
        mAPI = ApiUtils.getAPIService();
        final Call<List<Product>> call = mAPI.getProductInStore(storeID);
        new ProductInStoreByUserDisplayPage.ProductInStoreList().execute(call);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        storeNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toStoreInfo = new Intent(getBaseContext(), StoreInformationPage.class);
                toStoreInfo.putExtra("storeID",storeID);
                startActivity(toStoreInfo);
            }
        });
    }

    private void customView(){
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view,getBaseContext());
                return false;
            }
        });
        imgBarCode.setVisibility(View.INVISIBLE);
        CustomInterface.setStatusBarColor(this);

    }
    private void findView(){
        imgBarCode = findViewById(R.id.imgBarCode);
        recycler_view = findViewById(R.id.recycler_view);
        backBtn = findViewById(R.id.backBtn);
        tvStoreName = findViewById(R.id.tv_store_name);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSort = findViewById(R.id.spinnerSort);
        backdrop = findViewById(R.id.backdrop);
        main_layout = findViewById(R.id.main_layout);
        searchView = findViewById(R.id.searchViewQuery);
        storeNameLayout = findViewById(R.id.storeNameLayout);
        loadingBar = findViewById(R.id.loadingBar);
        nullMessage = findViewById(R.id.nullMessage);
    }

    public class ProductInStoreList extends AsyncTask<Call,List<Product>,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            boolean check = false;
            if (products != null) {
                check = true;
            } else {
                check = false;
            }
            if(check == true){
                loadingBar.setVisibility(View.INVISIBLE);
            }
            final boolean finalCheck = check;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (finalCheck == false) {
                        Toast.makeText(ProductInStoreByUserDisplayPage.this, "Có lỗi xảy ra. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                        nullMessage.setText("Có lỗi xảy ra, vui lòng tải lại trang!");
                        loadingBar.setVisibility(View.INVISIBLE);
                    }
                }
            },10000);
        }

        @Override
        protected void onProgressUpdate(List<Product>... values) {
            super.onProgressUpdate(values);
            productFilter = new ProductFilter();
            products = values[0];

            tempProductInStore = new ArrayList<>();

            if(products != null){
                productFilter.setCategoryFilter(products, ProductInStoreByUserDisplayPage.this, spinnerCategory);
                productFilter.setSortItem(ProductInStoreByUserDisplayPage.this,spinnerSort);
                for (Product product : products){
                    tempProductInStore.add(product);
                }
            }
            productInStoreByUserCustomListViewAdapter = new ProductInStoreByUserCustomCardViewAdapter(ProductInStoreByUserDisplayPage.this, tempProductInStore,store);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(ProductInStoreByUserDisplayPage.this, 2);
            recycler_view.setLayoutManager(mLayoutManager);
            recycler_view.addItemDecoration(new GridSpacingItemDecoration(2, Formater.dpToPx(2,getResources()), true));
            recycler_view.setItemAnimator(new DefaultItemAnimator());
            recycler_view.setAdapter(productInStoreByUserCustomListViewAdapter);
            spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(adapterView.getItemAtPosition(i).toString().equals(productFilter.FROM_LOW_COST)) {
                        Collections.sort(tempProductInStore,new ProductInStoreCompareableDecrease());
                    }else if(adapterView.getItemAtPosition(i).toString().equals(productFilter.FROM_HIGH_COST)){
                        Collections.sort(tempProductInStore,new ProductInStoreCompareableIncrease());
                    }
                    productInStoreByUserCustomListViewAdapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    tempProductInStore.clear();
                    if(adapterView.getItemAtPosition(i).toString().contains(productFilter.ALL_PRODUCT)){
                        for (Product product : products){
                            tempProductInStore.add(product);
                        }
                    }else if(adapterView.getItemAtPosition(i).toString().contains(productFilter.SALE_PRODUCT)){
                        for (Product product : products) {
                            if (product.getPromotion()!=0) {
                                tempProductInStore.add(product);
                            }
                        }

                    }else{
                        for (Product product : products) {
                            if (adapterView.getItemAtPosition(i).toString().contains(product.getCategory_name())) {
                                tempProductInStore.add(product);
                            }
                        }
                    }
                    productInStoreByUserCustomListViewAdapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        @Override
        protected Void doInBackground(Call... calls) {
            try {
                Call<List<Product>> call = calls[0];
                Response<List<Product>> response = call.execute();
                List<Product> list = new ArrayList<>();
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
