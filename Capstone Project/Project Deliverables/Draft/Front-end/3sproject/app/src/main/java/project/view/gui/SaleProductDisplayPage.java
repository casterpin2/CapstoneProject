package project.view.gui;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.stats.internal.G;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import project.firebase.Firebase;
import project.retrofit.APIService;
import project.view.R;
import project.view.adapter.ProductInStoreByUserCustomCardViewAdapter;
import project.view.adapter.SaleProductCustomCardviewAdapter;
import project.view.fragment.home.HomeFragment;
import project.view.model.Item;
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

public class SaleProductDisplayPage extends BasePage {

    private ImageView imgBarCode;
    private int storeID;
    private String storeName;
    private String phone;
    private String image_path;
    private Store store;
    private APIService apiService;
    private RecyclerView recycler_view;
    private ImageView backBtn, backdrop, imgAddCard;
    private TextView tvStoreName;
    private Spinner spinnerCategory,spinnerSort;
    private StorageReference storageReference = Firebase.getFirebase();
    private List<Product> products;
    private CoordinatorLayout main_layout;
    private SearchView searchView ;
    private List<Product> tempProductInStore;
    private ProductFilter productFilter;
    private SaleProductCustomCardviewAdapter saleProductCustomCardviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_product_display_page);
        findView();
        customView();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        apiService = APIService.retrofit.create(APIService.class);
        final Call<List<Product>> callSale = apiService.getSaleProduct();
        new SaleProductDisplayPage.SaleProductDisplayData().execute(callSale);
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

    }
    private void customView(){
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view,getBaseContext());
                return false;
            }
        });
        searchView.setQueryHint("Tìm sản phẩm giảm giá");
        imgBarCode.setVisibility(View.INVISIBLE);
        CustomInterface.setStatusBarColor(this);
    }

    public class SaleProductDisplayData extends AsyncTask<Call,List<Product>,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(List<Product>... values) {
            super.onProgressUpdate(values);
            productFilter = new ProductFilter();
            products = values[0];
            tempProductInStore = new ArrayList<>();
            if(products != null){
                productFilter.setBrandFilter(products, SaleProductDisplayPage.this, spinnerCategory);
                productFilter.setSortItem(SaleProductDisplayPage.this,spinnerSort);
                for (Product product : products){
                    tempProductInStore.add(product);
                }
            }
            saleProductCustomCardviewAdapter = new SaleProductCustomCardviewAdapter(SaleProductDisplayPage.this, tempProductInStore);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SaleProductDisplayPage.this, 2);
            recycler_view.setLayoutManager(mLayoutManager);
            recycler_view.addItemDecoration(new GridSpacingItemDecoration(2, Formater.dpToPx(2,getResources()), true));
            recycler_view.setItemAnimator(new DefaultItemAnimator());
            recycler_view.setAdapter(saleProductCustomCardviewAdapter);
            spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(adapterView.getItemAtPosition(i).toString().equals(productFilter.FROM_LOW_COST)) {
                        Collections.sort(tempProductInStore,new ProductInStoreCompareableDecrease());
                    }else if(adapterView.getItemAtPosition(i).toString().equals(productFilter.FROM_HIGH_COST)){
                        Collections.sort(tempProductInStore,new ProductInStoreCompareableIncrease());
                    }
                    saleProductCustomCardviewAdapter.notifyDataSetChanged();
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
                    }else{
                        for (Product product : products) {
                            if (adapterView.getItemAtPosition(i).toString().contains(product.getBrand_name())) {
                                tempProductInStore.add(product);
                            }
                        }
                    }
                    saleProductCustomCardviewAdapter.notifyDataSetChanged();
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
