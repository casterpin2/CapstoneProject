package project.view.gui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.adapter.ProductInStoreByUserCustomListViewAdapter;
import project.view.model.ProductInStore;
import project.view.util.CustomInterface;
import project.view.util.Formater;
import project.view.util.GridSpacingItemDecoration;
import project.view.util.ProductInStoreCompareableDecrease;
import project.view.util.ProductInStoreCompareableIncrease;
import retrofit2.Call;
import retrofit2.Response;

public class ProductInStoreByUserDisplayPage extends AppCompatActivity {
    private ImageView imgBarCode;
    private int storeID;
    private String storeName;
    private APIService mAPI;
    private RecyclerView recycler_view;
    private ProductInStoreByUserCustomListViewAdapter productInStoreByUserCustomListViewAdapter;
    private ImageView backBtn;
    private TextView tvStoreName;
    private Spinner spinnerCategory,spinnerSort;
    private LinearLayout sortLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_in_store_by_user_display_page);
        findView();

        imgBarCode.setVisibility(View.INVISIBLE);
        CustomInterface.setStatusBarColor(this);
        storeName = getIntent().getStringExtra("storeName");
        tvStoreName.setText(storeName);
        storeID = getIntent().getIntExtra("storeID", -1);
        mAPI = ApiUtils.getAPIService();
        final Call<List<ProductInStore>> call = mAPI.getProductInStore(storeID);
        new ProductInStoreByUserDisplayPage.ProductInStoreList().execute(call);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addItemsOnSpinner();
        addSortItem();

    }

    public void addItemsOnSpinner() {
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

    public void addSortItem() {
        List<String> list = new ArrayList<String>();
        list.add("Sắp xếp");
        list.add("Giá từ thấp tới cao");
        list.add("Giá từ cao xuống thấp");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(dataAdapter);
    }

    private void findView(){
        imgBarCode = findViewById(R.id.imgBarCode);
        recycler_view = findViewById(R.id.recycler_view);
        backBtn = findViewById(R.id.backBtn);
        tvStoreName = findViewById(R.id.tv_store_name);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        sortLayout = findViewById(R.id.sortLayout);
        spinnerSort = findViewById(R.id.spinnerSort);
    }

    public class ProductInStoreList extends AsyncTask<Call,List<ProductInStore>,Void> {
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
            final List<ProductInStore> productInStores = values[0];
            spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(adapterView.getItemAtPosition(i).toString().equals("Giá từ thấp tới cao")) {
                        Collections.sort(productInStores,new ProductInStoreCompareableDecrease());
                    }else if(adapterView.getItemAtPosition(i).toString().equals("Giá từ cao xuống thấp")){
                        Collections.sort(productInStores,new ProductInStoreCompareableIncrease());
                    }
                    productInStoreByUserCustomListViewAdapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            productInStoreByUserCustomListViewAdapter = new ProductInStoreByUserCustomListViewAdapter(ProductInStoreByUserDisplayPage.this, productInStores);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(ProductInStoreByUserDisplayPage.this, 2);
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
