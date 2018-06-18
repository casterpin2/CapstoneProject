package project.view.Cart;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

import java.util.ArrayList;
import java.util.List;

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.AddProductToStore.Item;
import project.view.AddProductToStore.SearchProductAddToStore;
import project.view.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartProductToStore extends AppCompatActivity {

    private ListView productListView ;
    private CartProductToStorePageListViewAdapter adapter;
    private List<Item> productList;
    private TextView nullMessage;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton delete, back, success;
    private APIService apiService;
    private String jsonString;
    private int storeId;
    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_product_to_store);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_search_product_add_to_store_page);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            nullMessage.setText("Không có sản phẩm nào");
        }
        //Chuyển List product thành JSON
        gson = new Gson();
        jsonString = gson.toJson(productList);

        storeId = 1;
        adapter = new CartProductToStorePageListViewAdapter(CartProductToStore.this, R.layout.cart_product_to_store_page_custom_listview, productList);
        productListView.setAdapter(adapter);
        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        delete = (FloatingActionButton) findViewById(R.id.fab_delete);
        back = (FloatingActionButton) findViewById(R.id.fab_back);
        success = (FloatingActionButton) findViewById(R.id.fab_success);
        Toast.makeText(this, ""+ jsonString, Toast.LENGTH_LONG).show();
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
//                Toast.makeText(getApplicationContext(), "Đây là nút thêm sản phẩm", Toast.LENGTH_SHORT).show();

                if(productList.size() == 0) {
                    View searchPage = findViewById(R.id.search_page);
                    String message = "Không có sản phẩm để xóa";
                    int duration = Snackbar.LENGTH_LONG;

                    showSnackbar(searchPage, message, duration);
                } else if (productList.size() > 0){
                    deleteAllProduct();
                    Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked
                Intent accessToSearchProductToStorePage = new Intent(CartProductToStore.this, SearchProductAddToStore.class);
                startActivity(accessToSearchProductToStorePage);
                //Toast.makeText(getApplicationContext(), "Đây là nút quay về", Toast.LENGTH_SHORT).show();
            }
        });

        success.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu third item clicked
                StringBuilder str = new StringBuilder();
                str.append(jsonString);
                //Call API
                apiService.insertProduct(str,storeId).enqueue(new Callback<Boolean>(){

                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                        if(response.isSuccessful()){
                            Toast.makeText(CartProductToStore.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(CartProductToStore.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Log.d("MainActivity", "error loading from API");
                    }
                });

            }
        });




//        final CircleMenu menu = findViewById(R.id.circleMenu);
//        menu.setMainMenu(Color.parseColor("#CDCDCD"), R.drawable.add, R.drawable.cancel)
//                .addSubMenu(Color.parseColor("#ffff4444"), R.drawable.delete)
//                .addSubMenu(Color.parseColor("#ffffff"), R.drawable.back)
//                .addSubMenu(Color.parseColor("#0dd9a3"), R.drawable.success)
//                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
//                    @Override
//                    public void onMenuSelected(int index) {
//                        if(index == 0) {
//
//                            deleteAllProduct();
//                            Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
//                        } else if (index == 1) {
//                            Intent accessToSearchProductToStorePage = new Intent(CartProductToStore.this, SearchProductAddToStore.class);
//                            startActivity(accessToSearchProductToStorePage);
//                        } else if (index == 2) {
//                            // ham add san pham
//                            Toast.makeText(getApplicationContext(), "Đây là nút thêm sản phẩm", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });



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
    private boolean check = false;
//    public boolean addProduct(String jsonString,int storeId){
//
//        apiService.insertProduct(jsonString,storeId).enqueue(new Callback<Boolean>(){
//
//            @Override
//            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
//
//                if(response.isSuccessful()){
//                    Toast.makeText(CartProductToStore.this, "Pro Vl", Toast.LENGTH_SHORT).show();
//                    check = true;
//                }else{
//                    Toast.makeText(CartProductToStore.this, "Server Error", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<Boolean> call, Throwable t) {
//                Log.d("MainActivity", "error loading from API");
//            }
//        });
//        return check;
//    }

}
