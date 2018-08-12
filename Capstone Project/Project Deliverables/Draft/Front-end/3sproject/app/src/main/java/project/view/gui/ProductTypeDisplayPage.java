package project.view.gui;

import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.retrofit.ApiUtils;
import project.view.adapter.ProductTypeCustomCardViewAdapter;
import project.view.R;
import project.view.model.Product;
import project.view.util.CustomInterface;
import project.view.util.Formater;
import project.view.util.GridSpacingItemDecoration;
import project.view.util.ProductFilter;
import retrofit2.Call;
import retrofit2.Response;

public class ProductTypeDisplayPage extends BasePage {

    public List<Product> productList;
    public List<Product> tempProduct;
    private RecyclerView recyclerView;
    private ProductTypeCustomCardViewAdapter adapter;
    private TextView filterLable;
    private Spinner spinnerCategory,spinnerBrand;
    ProductFilter productFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_with_filter);
        findView();


        int typeID = getIntent().getIntExtra("typeID", -1);
        String typeName = getIntent().getStringExtra("typeName");
        productList = new ArrayList<>();
        tempProduct = new ArrayList<>();
        adapter = new ProductTypeCustomCardViewAdapter(this, tempProduct);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(ProductTypeDisplayPage.this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, Formater.dpToPx(2,getResources()), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CustomInterface.setStatusBarColor(this);
        recyclerView.setAdapter(adapter);
        if (typeID != -1) {
            getSupportActionBar().setTitle(typeName);
            Call<List<Product>> call = ApiUtils.getAPIService().getProductbyType(typeID);
            new GetData().execute(call);
        } else {
            Toast.makeText(ProductTypeDisplayPage.this, "Có lỗi xảy ra !!!",Toast.LENGTH_LONG).show();
        }
    }

    private void findView(){
        recyclerView = findViewById(R.id.recycler_view);
        filterLable = findViewById(R.id.filterLabble);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerCategory.setVisibility(View.INVISIBLE);
        filterLable.setVisibility(View.VISIBLE);
        filterLable.setText("Thương hiệu");
        spinnerBrand = findViewById(R.id.spinnerSort);
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

    private class GetData extends AsyncTask<Call, Void, List<Product>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(List<Product> aVoid) {

            productFilter = new ProductFilter();
            if (aVoid != null) {
                for (Product pro : aVoid){
                    productList.add(pro);
                }
                for (Product product : productList){
                    tempProduct.add(product);
                }
                productFilter.setBrandFilter(productList,ProductTypeDisplayPage.this,spinnerBrand);
                spinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        tempProduct.clear();
                        if(productFilter.ALL_PRODUCT.equals(adapterView.getItemAtPosition(i).toString())){
                            for (Product product : productList){
                                tempProduct.add(product);
                            }
                        }else{
                            for (Product product : productList) {
                                if (product.getBrand_name().equals(adapterView.getItemAtPosition(i).toString())) {
                                    tempProduct.add(product);
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(ProductTypeDisplayPage.this, "Có lỗi xảy ra !!!",Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(aVoid);

        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected List<Product> doInBackground(Call... calls) {
            try {
                Call<List<Product>> call = calls[0];
                Response<List<Product>> response = call.execute();
                return response.body();
            } catch (IOException e) {
            }
            return null;
        }
    }
}
