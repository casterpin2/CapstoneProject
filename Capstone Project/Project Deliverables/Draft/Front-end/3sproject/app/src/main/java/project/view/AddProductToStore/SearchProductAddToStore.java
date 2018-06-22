package project.view.AddProductToStore;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import project.firebase.Firebase;
import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.Cart.CartProductToStore;
import project.view.MainActivity;
import project.view.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchProductAddToStore extends AppCompatActivity {

    private MaterialSearchBar searchBar;
    private APIService mAPI;
    public static List<Item> searchedProductList;
    private Context context;
    // get list to display to list view;

    // get button add
    public static List<Item> addedProductList = new ArrayList<Item>();

    private ListView theListView;
    private SearchProductPageListViewAdapter adapter;
    private Dialog informationDialog, optionDialog;
    private TextView textOne;
    private TextView nullMessage;
    private static String query = "";

//    private TextView requestBtn;


    public Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product_add_to_store);
        mAPI = ApiUtils.getAPIService();
        // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
        callAPI(query);
        adapter = new SearchProductPageListViewAdapter(this,R.layout.search_product_page_custom_list_view,searchedProductList);
        // get our list view
        theListView = (ListView) findViewById(R.id.mainListView);
        textOne = (TextView) findViewById(R.id.textOne);
        textOne.bringToFront();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_search_product_add_to_store_page);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        textOne.setText(String.valueOf(addedProductList.size()));
        informationDialog = new Dialog(SearchProductAddToStore.this);

        //Toast.makeText(getApplicationContext(), addedProductList.size()+"", Toast.LENGTH_SHORT).show()
        nullMessage = (TextView) findViewById(R.id.nullMessage);
        if(searchedProductList.size() == 0) {
            nullMessage.setVisibility(View.VISIBLE);
            theListView.setVisibility(View.INVISIBLE);
            nullMessage.setText("Không có sản phẩm nào phù hợp");
        }

        final ImageView searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setVisibility(View.VISIBLE);
                searchBtn.setVisibility(View.INVISIBLE);
                TextView pageTitle = findViewById(R.id.pageTitle);
                pageTitle.setVisibility(View.INVISIBLE);
            }
        });

        // searchBar initial value and do something
        searchBar = findViewById(R.id.searchBar);
        searchBar.setSpeechMode(false);
        searchBar.setArrowIconTint(R.color.colorBlack);
        searchBar.showSuggestionsList();
        searchBar.setRoundedSearchBarEnabled(true);
        searchBar.setText(query);
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {

            @Override
            public void onSearchStateChanged(boolean enabled) {
//                Toast.makeText(getApplicationContext(), "onSearchStateChanged", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onSearchConfirmed(CharSequence text) {
                query = searchBar.getText().toString().trim();
                //Toast.makeText(getApplicationContext(), searchBar.getText().toString(), Toast.LENGTH_SHORT).show();
                if(!query.isEmpty()) {
                    callAPI(query);
                }
            }
            @Override
            public void onButtonClicked(int buttonCode) {
                Toast.makeText(getApplicationContext(), "onButtonClicked", Toast.LENGTH_SHORT).show();

            }
        });

        //get floating action button
        FloatingActionButton addFab = findViewById(R.id.fab);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accessToCartActivity = new Intent(SearchProductAddToStore.this, CartProductToStore.class);
                startActivity(accessToCartActivity);
            }
        });

        // set elements to adapter
        theListView.setAdapter(adapter);

        // set on click event listener to list view

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //
                // Toast.makeText(getApplicationContext(), searchedProductList.get(position).getProduct_name(), Toast.LENGTH_SHORT).show();
                showInfoDialog(searchedProductList, position, view);
            }
        });
    }
    //create information dialog
    public void showInfoDialog(final List<Item> productList, final int position, View view) {


        view = (View) findViewById(R.id.search_page);
        StorageReference storageReference = Firebase.getFirebase();

        informationDialog.setContentView(R.layout.search_product_page_product_information_dialog_custom);
        final TextView productName_infoDialog = (TextView) informationDialog.findViewById(R.id.productName_infoDialog);
        final TextView productCategory_infoDialog = (TextView) informationDialog.findViewById(R.id.productCategory_infoDialog);
        final TextView productBrand_infoDialog = (TextView) informationDialog.findViewById(R.id.productBrand_infoDialog);
        final TextView productDesc_infoDialog = (TextView) informationDialog.findViewById(R.id.productDesc_infoDialog);
        final TextView productTypeText_infoDialog = (TextView) informationDialog.findViewById(R.id.productTypeText_infoDialog);
        final TextView productType_infoDialog = (TextView) informationDialog.findViewById(R.id.productType_infoDialog);
        final TextView addBtn_infoDialog = (TextView) informationDialog.findViewById(R.id.addBtn_infoDialog);
        final ImageView productImage = (ImageView) informationDialog.findViewById(R.id.productImage);

//        productImage.setMaxHeight(productImage.getWidth());
        productName_infoDialog.setText(productList.get(position).getProduct_name());
        productCategory_infoDialog.setText(productList.get(position).getCategory_name());
        productBrand_infoDialog.setText(productList.get(position).getBrand_name());
        productDesc_infoDialog.setText(productList.get(position).getDescription());
        productTypeText_infoDialog.setText("Loại "+productList.get(position).getCategory_name().toLowerCase());
        productType_infoDialog.setText(productList.get(position).getType_name());
        //productImage.setBackgroundColor(R.drawable.background);
        Glide.with(this /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(productList.get(position).getImage_path()))
                .into(productImage);
        final View optionView = view;
        addBtn_infoDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showOptionDialog(productList, position, optionView);
//                intentToCartPageFromDialog(v, position, new Item(productList.get(position).getProduct_id(),productName_infoDialog.getText().toString(),productBrand_infoDialog.getText().toString(),productDesc_infoDialog.getText().toString(),productCategory_infoDialog.getText().toString(),productType_infoDialog.getText().toString(),productList.get(position).getImage_path()));
            }
        });



        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(informationDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;


        informationDialog.getWindow().setAttributes(lp);
//        dialogContent.setText("Tên đăng nhập hoặc mật khẩu không đúng. Vui lòng thử lại.");
//        dialogContent.setTextColor(Color.rgb(255,0,0));
//        okBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
        informationDialog.show();
    }

    public void showOptionDialog(final List<Item> productList, final int position, final View view) {



        StorageReference storageReference = Firebase.getFirebase();
        optionDialog = new Dialog(SearchProductAddToStore.this);
        optionDialog.setContentView(R.layout.search_product_page_price_dialog_custom);
        final CurrencyEditText priceValue_optionDialog = (CurrencyEditText) optionDialog.findViewById(R.id.priceValue_optionDialog);
        final TextView promotionPercent_optionDialog = (TextView) optionDialog.findViewById(R.id.promotionPercentText_optionDialog);
        final TextView promotionPercentErrorMessage_optionDialog = (TextView) optionDialog.findViewById(R.id.promotionPercentErrorMessage_optionDialog);
        final Button addButton_optionDialog = (Button) optionDialog.findViewById(R.id.addBtn_optionDialog);


        addButton_optionDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchProductAddToStore.this);
                builder.setTitle(R.string.title_add_alert_dialog);
                builder.setMessage(R.string.content_add_alert_dialog);

                builder.setPositiveButton(R.string.btn_add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        long priceLong = priceValue_optionDialog.getRawValue();
                        double promotionPercent = Double.parseDouble(promotionPercent_optionDialog.getText().toString());
                        if (Double.parseDouble(promotionPercent_optionDialog.getText().toString()) > 100.00 || Double.parseDouble(promotionPercent_optionDialog.getText().toString()) < 0.00) {
                            promotionPercentErrorMessage_optionDialog.setText("Chiết khấu phải ở trong khoảng từ 0% tới 100%!!");
                        } else {
                            Toast.makeText(SearchProductAddToStore.this, priceLong+ " - price", Toast.LENGTH_SHORT).show();
                            promotionPercentErrorMessage_optionDialog.setText("");
                            productList.get(position).setPromotion(promotionPercent);
//                            productList.get(position).setPrice(priceLong);

                            String message = "Thêm sản phẩm thành công";
                            int duration = Snackbar.LENGTH_LONG;
                            showSnackbar(view, message, duration);
                            textOne.setText(String.valueOf(addedProductList.size()));

                            optionDialog.dismiss();
                            informationDialog.dismiss();

                        }

//                        final double promotionPercent = Double.parseDouble(promotionPercent_optionDialog.toString());

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
        });



        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(optionDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;


        optionDialog.getWindow().setAttributes(lp);
//        dialogContent.setText("Tên đăng nhập hoặc mật khẩu không đúng. Vui lòng thử lại.");
//        dialogContent.setTextColor(Color.rgb(255,0,0));
//        okBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
        optionDialog.show();
    }



    public void intentToCartPageFromDialog(View view, int position, Item item){

        addedProductList.add(item);

        View pageView = findViewById(R.id.search_page);
        String message = "Thêm sản phẩm thành công";
        int duration = Snackbar.LENGTH_LONG;
        showSnackbar(pageView, message, duration);
        informationDialog.dismiss();
        textOne.setText(String.valueOf(addedProductList.size()));

    }
    public void intentToCartPageFromListView(View view){
//        Toast.makeText(getApplicationContext(), "hahah", Toast.LENGTH_SHORT).show();
        //Intent accessToCartPage = new Intent()
        //Toast.makeText(getApplicationContext(), "Đã add được sản phẩm bằng listview", Toast.LENGTH_SHORT).show();
        TextView textviewValue = (TextView) view;
        int position = (Integer) textviewValue.getTag();
        for (int i = 0 ;i<searchedProductList.size();i++) {
            if (position == searchedProductList.get(i).getProduct_id()) {

                showOptionDialog(searchedProductList,i, view);

//                addedProductList.add(searchedProductList.get(i));
//                //Dat Fix
//                searchedProductList.remove(searchedProductList.get(i));
//                adapter.notifyDataSetChanged();
                break;
            }
            //
        }


    }

    public void showConfirmDialogWhenAdd(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_add_alert_dialog);
        builder.setMessage(R.string.content_add_alert_dialog);

        builder.setPositiveButton(R.string.btn_add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intentToCartPageFromListView(view);
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

        snackbar.setActionTextColor(Color.RED);

// styling for rest of text
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(20);

// styling for background of snackbar
        View sbView = snackbarView;
        sbView.setBackgroundColor(Color.BLACK);

        snackbar.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private boolean checkID(int productID){
        for (Item item:SearchProductAddToStore.addedProductList) {
            if (item.getProduct_id() == productID) return false;
        }
        return true;
    }

    private void callAPI (String query){
        //Call API
        if (SearchProductAddToStore.searchedProductList == null) {
            SearchProductAddToStore.searchedProductList = new ArrayList<>();
            return;
        }
        if (query.isEmpty()) return;
        mAPI.getProducts(query).enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                searchedProductList.clear();
                for (int i = 0; i < response.body().size(); i++) {
                    Item item = response.body().get(i);
                    if (checkID(item.getProduct_id()))
                        searchedProductList.add(item);
                }
                //searchedProductList = response.body();
                adapter.notifyDataSetChanged();
                if(searchedProductList.isEmpty()) {
                    TextView nullMessage = findViewById(R.id.nullMessage);
                    nullMessage.setText("Không có sản phẩm nào phù hợp!");
                    theListView.setVisibility(View.INVISIBLE);
                } else {
                    TextView nullMessage = findViewById(R.id.nullMessage);
                    nullMessage.setText("");
                    theListView.setVisibility(View.VISIBLE);
                }
                //Toast.makeText(SearchProductAddToStore.this,searchedProductList.get(0).getProduct_name(),Toast.LENGTH_LONG).show();
                //Toast.makeText(SearchProductAddToStore.this,searchBar.getText().toString().trim(),Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(SearchProductAddToStore.this, MainActivity.class);
//                            startActivity(intent);
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(SearchProductAddToStore.this, "Something Wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

}