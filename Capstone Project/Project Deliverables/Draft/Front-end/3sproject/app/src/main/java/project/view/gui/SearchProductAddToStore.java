package project.view.gui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import project.firebase.Firebase;
import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.adapter.SearchProductPageListViewAdapter;
import project.view.model.Item;
import project.view.model.Product;
import project.view.util.CustomInterface;
import retrofit2.Call;
import retrofit2.Response;

public class SearchProductAddToStore extends AppCompatActivity {


    private static final int RESULT_CODE_SCAN=220;
    private static final int REQUEST_CODE_SCAN=120;
    private APIService mAPI;
    public List<Item> searchedProductList;
    private Context context;
    private ListView theListView;
    private SearchProductPageListViewAdapter adapter;
    private Dialog informationDialog, optionDialog;
    private TextView nullMessage;
    private String query = "";
    private SearchView searchView;
    private ProgressBar loadingBar;
    private int storeID;
    //lazy loading
    public Handler mHandle;
    public View footerView;
    public boolean isLoading;
    boolean limitData = false;
    int page;
    private  String barcode;
    private RelativeLayout main_layout;
    public Context getContext() {
        return context;
    }
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final List<String> suggestions = new ArrayList<>();
    private final List<String> searchedList = new ArrayList<>();
    private CursorAdapter suggestionAdapter;
    DatabaseReference myRef = database.getReference();
    final DatabaseReference myRef1 = myRef.child("suggestion").child("product");
    private ValueEventListener listener;
    @Override
    protected void onResume() {
        super.onResume();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                suggestions.clear();
                for (DataSnapshot dttSnapshot2 : dataSnapshot.getChildren()) {
                    suggestions.add(dttSnapshot2.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        myRef1.addValueEventListener(listener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        myRef1.removeEventListener(listener);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product_add_to_store);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.searhProductAddToStorePageTitle);
        CustomInterface.setStatusBarColor(this);
        storeID = getIntent().getIntExtra("storeID", -1);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);
        main_layout = findViewById(R.id.main_layout);
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view,getBaseContext());
                return false;
            }
        });
        loadingBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);

        mAPI = ApiUtils.getAPIService();
        // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
//callAPI(query,page);
        if (searchedProductList == null) {
            searchedProductList = new ArrayList<>();
        }
        adapter = new SearchProductPageListViewAdapter(this, R.layout.search_product_page_custom_list_view, searchedProductList);
        // get our list view
        theListView = (ListView) findViewById(R.id.mainListView);


        informationDialog = new Dialog(SearchProductAddToStore.this);

        nullMessage = (TextView) findViewById(R.id.nullMessage);
        if (searchedProductList.size() == 0) {
            nullMessage.setVisibility(View.VISIBLE);
            theListView.setVisibility(View.INVISIBLE);
            nullMessage.setText("Không có sản phẩm nào phù hợp");
        }

        // set elements to adapter
        theListView.setAdapter(adapter);

        // set on click event listener to list view
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showInfoDialog(searchedProductList, position, view);
            }
        });

        //lazy loading

        mHandle = new MyHandle();
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footerView = li.inflate(R.layout.footer_loading_listview_lazy_loading, null);

        theListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int count = searchedProductList.size();
                if (view.getLastVisiblePosition() == totalItemCount - 1 && count == (page * 10) && isLoading == false  && (page > 0)) {
                    isLoading = true;
                    Thread thread = new ThreadgetMoreData();
                    thread.start();
                }
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
                .skipMemoryCache(true)
                .into(productImage);
        final View optionView = view;
        addBtn_infoDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        showOptionDialog(productList, position, optionView);
            }
        });



        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(informationDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;


        informationDialog.getWindow().setAttributes(lp);

        informationDialog.show();
    }

    public void showOptionDialog(final List<Item> productList, final int position, View view) {
        optionDialog = new Dialog(SearchProductAddToStore.this);
        optionDialog.setContentView(R.layout.search_product_page_price_dialog_custom);
        final EditText priceValue_optionDialog = (EditText) optionDialog.findViewById(R.id.priceValue_optionDialog);
        final TextView promotionPercent_optionDialog = (TextView) optionDialog.findViewById(R.id.promotionPercentText_optionDialog);
        final TextView promotionPercentErrorMessage_optionDialog = (TextView) optionDialog.findViewById(R.id.promotionPercentErrorMessage_optionDialog);
        final TextView priceErrorMessage_optionDialog = (TextView) optionDialog.findViewById(R.id.priceErrorMessage_optionDialog);
        final Button addButton_optionDialog = (Button) optionDialog.findViewById(R.id.addBtn_optionDialog);

        priceValue_optionDialog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                priceValue_optionDialog.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();
                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    priceValue_optionDialog.setText(formattedString);
                    priceValue_optionDialog.setSelection(priceValue_optionDialog.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                priceValue_optionDialog.addTextChangedListener(this);
            }
        });


        final View finalView = view;
        addButton_optionDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchProductAddToStore.this);
                builder.setTitle(R.string.title_add_alert_dialog);
                builder.setMessage(R.string.content_add_alert_dialog);

                builder.setPositiveButton(R.string.btn_add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (priceValue_optionDialog.getText().toString().length() > 0 && promotionPercent_optionDialog.getText().toString().length() > 0) {
                            if (Double.parseDouble(promotionPercent_optionDialog.getText().toString()) > 100.00 || Double.parseDouble(promotionPercent_optionDialog.getText().toString()) < 0.00) {
                                promotionPercentErrorMessage_optionDialog.setText(R.string.promotionOption);
                            } else {
                                long priceLong = Long.parseLong(priceValue_optionDialog.getText().toString().replaceAll(",", ""));
                                double promotionPercent = Double.parseDouble(promotionPercent_optionDialog.getText().toString());
                                promotionPercentErrorMessage_optionDialog.setText("");
                                Item p = productList.get(position);
                                p.setPromotion(promotionPercent);
                                p.setPrice(priceLong);
                                //counter_fab.setCount(addedProductList.size());

                                Call<Boolean> call = mAPI.insertProduct(p,storeID);
                                new AddProduct(p.getProduct_id(),finalView).execute(call);
                               // Log.d("Test add product", new Gson().toJson(p));
//                                searchedProductList.remove(searchedProductList.get(position));
//                                adapter.notifyDataSetChanged();
                                optionDialog.dismiss();
                                informationDialog.dismiss();

                            }
                        } else {
                            priceErrorMessage_optionDialog.setText(R.string.error_empty);
                            promotionPercentErrorMessage_optionDialog.setText(R.string.error_empty);
                        }


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

        optionDialog.show();
    }



    public void intentToCartPageFromDialog(View view, int position, Item item){

        //addedProductList.add(item);

        View pageView = findViewById(R.id.search_page);
        String message = getResources().getString(R.string.addSuccessful);
        int duration = Snackbar.LENGTH_LONG;
        showSnackbar(pageView, message, duration);
        informationDialog.dismiss();
        //counter_fab.setCount(addedProductList.size());

    }
    public void intentToCartPageFromListView(View view){
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
        snackbar.setAction(R.string.close, new View.OnClickListener() {
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
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void callAPI (String query, int page){
        //Call API
        if (searchedProductList == null) {
            searchedProductList = new ArrayList<>();
            return;
        }
        if (query.isEmpty()) return;
//        mAPI.getProducts(query,page).enqueue(new Callback<List<Item>>() {
//            @Override
//            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
//                //searchedProductList.clear();
//                if (response != null && response.body().size() != 0 ) {
//                    //theListView.removeFooterView(footerView);
//                    for (int i = 0; i < response.body().size(); i++) {
//                        Item item = response.body().get(i);
//                        if (checkID(item.getProduct_id()))
//                            searchedProductList.add(item);
//                    }
//                } else {
//                    //theListView.removeFooterView(footerView);
//                    limitData = true;
//                }
//                //searchedProductList = response.body();
//                adapter.notifyDataSetChanged();
//                if(searchedProductList.isEmpty()) {
//                    TextView nullMessage = findViewById(R.id.nullMessage);
//                    nullMessage.setText("Không có sản phẩm nào phù hợp!");
//                    theListView.setVisibility(View.INVISIBLE);
//                } else {
//                    TextView nullMessage = findViewById(R.id.nullMessage);
//                    nullMessage.setText("");
//                    theListView.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Item>> call, Throwable t) {
//                Toast.makeText(SearchProductAddToStore.this, "Something Wrong", Toast.LENGTH_LONG).show();
//            }
//        });
        Call<List<Item>> call = mAPI.getProducts(query,page,storeID);
        new ProductListWithName().execute(call);
    }

    //lazy loading

    public class MyHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0 :
                    theListView.addFooterView(footerView);
                    break;
                case 1:
                    //adapter.addListItemToAdapter((ArrayList<Item>)msg.obj);
                    theListView.removeFooterView(footerView);
                    getMoreData();
                    isLoading = false;
                    break;
                default:
                    break;
            }
        }
    }

    public void getMoreData(){
        //List<Item> addList = new ArrayList<>();

        Log.d("page",String.valueOf(page));
        callAPI(query, page);
        page ++;
        //addList = searchedProductList;

        //return addList;
    }


    public class ThreadgetMoreData extends Thread {
        @Override
        public void run() {
            mHandle.sendEmptyMessage(0);
            //List<Item> addMoreList = getMoreData();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isLoading == true){
                Message msg = mHandle.obtainMessage(1);
                mHandle.sendMessage(msg);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // thêm search vào vào action bar
        getMenuInflater().inflate(R.menu.search_view_with_barcode, menu);
        MenuItem itemSearch = menu.findItem(R.id.search_view);
        MenuItem itemSearchWithBarcode = menu.findItem(R.id.search_with_barcode);
        searchView = (SearchView) itemSearch.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchedProductList.clear();
                page = 0;

                query = searchView.getQuery().toString().trim();
                setQuery(query);
                int index = theListView.getFirstVisiblePosition();
                View v = theListView.getChildAt(0);
                int top = (v == null) ? 0 : v.getTop();
                theListView.setSelectionFromTop(index, top);

                if(!query.isEmpty()) {
                    getMoreData();
                }
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });



        itemSearchWithBarcode.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent toBarCodeScanPage = new Intent(SearchProductAddToStore.this, BarcodeActivity.class);
                toBarCodeScanPage.putExtra("searchProductAdd",2);
                startActivityForResult(toBarCodeScanPage,REQUEST_CODE_SCAN);
                return false;
            }
        });

        return true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class ProductListWithName extends AsyncTask<Call,List<Item>,Void> {
        @Override
        protected void onPreExecute() {
            loadingBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(List<Item>... values) {
            super.onProgressUpdate(values);
            List<Item> listProduct = values[0];
            if (listProduct != null && listProduct.size() != 0 ) {
                //theListView.removeFooterView(footerView);
            } else {
                //theListView.removeFooterView(footerView);
                limitData = true;
            }
            for (Item item : listProduct) {
                searchedProductList.add(item);
            }
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
            loadingBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Call... calls) {
            try {
                Call<List<Item>> call = calls[0];
                Response<List<Item>> response = call.execute();
                List<Item> list = new ArrayList<>();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SCAN &&  resultCode == RESULT_CODE_SCAN){
             barcode = data.getStringExtra("code");
            Call<List<Item>> call = mAPI.getProductWithBarcode(barcode,storeID);
            new ProductListWithBarcode().execute(call);
        }
    }
    public class ProductListWithBarcode extends AsyncTask<Call,List<Item>,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        @Override
        protected void onProgressUpdate(List<Item>... values) {
            super.onProgressUpdate(values);
            List<Item> listProduct = values[0];
            if (listProduct != null && listProduct.size() != 0 ) {
                //theListView.removeFooterView(footerView);
                searchedProductList.clear();
            } else {
                //theListView.removeFooterView(footerView);
                limitData = true;
            }
            //searchedProductList = response.body();
            if(searchedProductList.isEmpty() && listProduct.isEmpty()) {
                TextView nullMessage = findViewById(R.id.nullMessage);
                nullMessage.setText("Không có sản phẩm nào phù hợp!");
                theListView.setVisibility(View.INVISIBLE);
            } else {
                TextView nullMessage = findViewById(R.id.nullMessage);
                nullMessage.setText("");
                adapter = new SearchProductPageListViewAdapter(SearchProductAddToStore.this, R.layout.search_product_page_custom_list_view, listProduct);
                theListView.setAdapter(adapter);
                theListView.setVisibility(View.VISIBLE);
            }
            loadingBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Call... calls) {
            try {
                Call<List<Item>> call = calls[0];
                Response<List<Item>> response = call.execute();
                List<Item> list = new ArrayList<>();
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

    public class AddProduct extends AsyncTask<Call,Void,Boolean>{
        private int productId;
        private View view;
        public AddProduct(int productId,View view) {
            this.productId = productId;
            this.view = view;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //loadingBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result == null) {
                Toast.makeText(SearchProductAddToStore.this,"Có lỗi xảy ra !!!!",Toast.LENGTH_LONG).show();
                return;
            }
            else if (result) {
                String message = getResources().getString(R.string.addSuccessful);
                int duration = Snackbar.LENGTH_LONG;
                showSnackbar(view, message, duration);
                for (Item item : searchedProductList){
                    if (item.getProduct_id() == productId) {
                        searchedProductList.remove(item);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();

            } else {
                Toast.makeText(SearchProductAddToStore.this,"Thêm sản phẩm không thành công do có lỗi xảy ra !!!!",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected Boolean doInBackground(Call... calls) {
            try {
                Call<Boolean> call = calls[0];
                Response<Boolean> response = call.execute();
                boolean result = response.body();
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Toast.makeText(SearchProductAddToStore.this,"Có lỗi xảy ra !!!!",Toast.LENGTH_LONG).show();
            }
            return null;
        }
    }

    public void setQuery(String query) {
        this.query = query;
    }
}