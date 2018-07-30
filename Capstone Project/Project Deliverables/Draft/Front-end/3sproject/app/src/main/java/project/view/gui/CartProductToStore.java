package project.view.gui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.adapter.CartProductToStorePageListViewAdapter;
import project.view.R;
import project.view.model.Item;
import project.view.util.CustomInterface;
import retrofit2.Call;
import retrofit2.Response;

public class CartProductToStore extends AppCompatActivity {

    private ListView productListView ;
    private CartProductToStorePageListViewAdapter adapter;
    private List<Item> productList;
    private TextView nullMessage;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton delete, add;
    private APIService apiService;
    private String jsonString;
    private int storeId;
    private Gson gson;
    private ProgressBar loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_product_to_store);

        CustomInterface.setStatusBarColor(this);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.storeCartTitle));

        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);
        loadingBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);

        nullMessage = (TextView) findViewById(R.id.nullMessage);
        apiService = ApiUtils.getAPIService();
        productListView = findViewById(R.id.productListView);
        if (productList == null) {
            productList = new ArrayList<>();
        }
        productList = SearchProductAddToStore.addedProductList;
        if (productList.size() == 0) {
            productListView.setVisibility(View.INVISIBLE);
            nullMessage.setVisibility(View.VISIBLE);
            nullMessage.setText(getResources().getString(R.string.noProduct));
        }
        for(Item p : productList){
            p.setPrice(p.getPrice());
            p.setPromotion(p.getPromotion());
        }
        //Chuyển List product thành JSON
        gson = new Gson();
        jsonString = gson.toJson(productList);

        storeId = 1;
        adapter = new CartProductToStorePageListViewAdapter(CartProductToStore.this, R.layout.cart_product_to_store_page_custom_listview, productList);
        productListView.setAdapter(adapter);
        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        delete = (FloatingActionButton) findViewById(R.id.fab_delete);
        add = (FloatingActionButton) findViewById(R.id.fab_add);

        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
//                Toast.makeText(getApplicationContext(), "Đây là nút thêm sản phẩm", Toast.LENGTH_SHORT).show();

                if(productList.size() == 0) {
                    View searchPage = findViewById(R.id.search_page);
                    String message = getResources().getString(R.string.noProductToDel);
                    int duration = Snackbar.LENGTH_LONG;

                    showSnackbar(searchPage, message, duration);
                } else if (productList.size() > 0){
                    deleteAllProduct();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.deletedAllProduct), Toast.LENGTH_SHORT).show();
                }

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu third item clicked
                StringBuilder str = new StringBuilder();
                str.append(jsonString);
                //Call API
                if(productList.size()>0){
                    Call<Boolean> call = apiService.insertProduct(str,1);
                    new ProductData().execute(call);
                }else{
                    Toast.makeText(CartProductToStore.this, "Hiện tại đang không có sản phẩm nào!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void deleteProduct(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_delete);
        builder.setMessage(R.string.confirm_delete);

        final View parentView = (View) view.getParent();
        builder.setPositiveButton(R.string.btn_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final int position = productListView.getPositionForView(parentView);
                SearchProductAddToStore.searchedProductList.add(productList.get(position));

                adapter.remove(productList.get(position));
                productListView.setAdapter(adapter);
            }
        });
        builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.show();
    }
    public void deleteAllProduct() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_delete_all);
        builder.setMessage(R.string.confirm_delete_all);

        builder.setPositiveButton(R.string.btn_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (Item i : productList){
                    SearchProductAddToStore.searchedProductList.add(i);
                }
                productList.clear();
                adapter = new CartProductToStorePageListViewAdapter(CartProductToStore.this, R.layout.cart_product_to_store_page_custom_listview, productList);
                productListView.setAdapter(adapter);
            }
        });
        builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, SearchProductAddToStore.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showSnackbar(View view, String message, int duration)
    {
        // Create snackbar
        final Snackbar snackbar = Snackbar.make(view, message, duration);

        // Set an action on it, and a handler
        snackbar.setAction("Đóng", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(20);
        View sbView = snackbarView;
        sbView.setBackgroundColor(Color.BLACK);

        snackbar.show();
    }
    public class ProductData extends AsyncTask<Call,Boolean,Void> {
        @Override
        protected void onPreExecute() {
            loadingBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            loadingBar.setVisibility(View.INVISIBLE);
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Boolean... values) {
            super.onProgressUpdate(values);
            boolean checkSubmit = values[0];
            if(checkSubmit){
                Toast.makeText(CartProductToStore.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                productList.clear();
                adapter = new CartProductToStorePageListViewAdapter(CartProductToStore.this, R.layout.cart_product_to_store_page_custom_listview, productList);
                productListView.setAdapter(adapter);
            }else{
                Toast.makeText(CartProductToStore.this, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Void doInBackground(Call... calls) {
            try{
                Call<Boolean> call = calls[0];
                Response<Boolean> response = call.execute();
                boolean checkSubmit = response.isSuccessful();

                publishProgress(checkSubmit);
            }catch (Exception e){
                Log.d("ERROR",e.getMessage());
            }

            return null;
        }
    }
}
