package project.view.gui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
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
import project.view.adapter.ProductInStoreCustomListViewAdapter;
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
    private  List<Product> searchedProduct;
    private SearchView searchView;
    private String typeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_with_filter);
        findView();


        int typeID = getIntent().getIntExtra("typeID", -1);
        typeName = getIntent().getStringExtra("typeName");
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
            new GetProductType().execute(call);
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
        searchView = findViewById(R.id.action_search);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_search_toolbar, menu);
        MenuItem itemSearch = menu.findItem(R.id.action_search);
        searchedProduct = new ArrayList<>();
        searchView = (SearchView) itemSearch.getActionView();
        searchView.clearFocus();
        searchView.setQueryHint("Tìm trong " + typeName);
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
                if(newText.equals("") || newText == null){
                    adapter = new ProductTypeCustomCardViewAdapter(ProductTypeDisplayPage.this,tempProduct);
                } else {
                    for (int i = 0; i < tempProduct.size(); i++) {
                        if(tempProduct.get(i).getProduct_name().toLowerCase().contains(newText.toLowerCase())) {
                            searchedProduct.add(tempProduct.get(i));
                        }
                    }
                    adapter = new ProductTypeCustomCardViewAdapter(ProductTypeDisplayPage.this, searchedProduct);
                }
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.action_home:{
                Intent toHomPage = new Intent(ProductTypeDisplayPage.this, HomePage.class);
                startActivity(toHomPage);
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class GetProductType extends AsyncTask<Call, Void, List<Product>> {
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
                        if(adapterView.getItemAtPosition(i).toString().contains(productFilter.ALL_PRODUCT)){
                            for (Product product : productList){
                                tempProduct.add(product);
                            }
                        }else{
                            for (Product product : productList) {
                                if (adapterView.getItemAtPosition(i).toString().contains(product.getBrand_name())) {
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
