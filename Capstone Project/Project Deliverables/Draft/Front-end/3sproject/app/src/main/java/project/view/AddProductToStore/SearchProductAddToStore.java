package project.view.AddProductToStore;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.CameraCharacteristics;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.ramotion.circlemenu.CircleMenuView;

import java.util.ArrayList;
import java.util.List;

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.LoginPage;
import project.view.MainActivity;
import project.view.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchProductAddToStore extends AppCompatActivity {

    private MaterialSearchBar searchBar;
    private APIService mAPI;
    private Button addBtn;
    public List<Item> searchedProductList;
    // get list to display to list view;

    // get button add
    public static List<Item> addedProductList = new ArrayList<Item>();

    private ListView theListView;
    private SearchProductPageListViewAdapter adapter;
    private Dialog dialog;


//    private TextView requestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product_add_to_store);
        mAPI = ApiUtils.getAPIService();
        // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
        searchedProductList = Item.getTestingList();
        adapter = new SearchProductPageListViewAdapter(this,R.layout.search_product_page_custom_list_view,searchedProductList);
        // get our list view
        theListView = (ListView) findViewById(R.id.mainListView);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_search_product_add_to_store_page);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));

        //Toast.makeText(getApplicationContext(), addedProductList.size()+"", Toast.LENGTH_SHORT).show()


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
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {

            @Override
            public void onSearchStateChanged(boolean enabled) {
//                Toast.makeText(getApplicationContext(), "onSearchStateChanged", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onSearchConfirmed(CharSequence text) {

                Toast.makeText(getApplicationContext(), searchBar.getText().toString(), Toast.LENGTH_SHORT).show();
                if(!searchBar.getText().toString().trim().isEmpty()) {
                    mAPI.getProducts(ApiUtils.removeAccent(searchBar.getText().toString().trim())).enqueue(new Callback<List<Item>>() {
                        @Override
                        public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                            searchedProductList.clear();
                            for (int i = 0; i < response.body().size(); i++) {
                                searchedProductList.add(response.body().get(i));
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
                Intent accessToCartActivity = new Intent(SearchProductAddToStore.this, MainActivity.class);
                startActivity(accessToCartActivity);
            }
        });



        // set elements to adapter
        theListView.setAdapter(adapter);

        // set on click event listener to list view

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), searchedProductList.get(position).getProduct_name(), Toast.LENGTH_SHORT).show();
                showInfoDialog(view, searchedProductList, position);
            }
        });
    }




    //overide method of Search Bar
    @Override
    public void onBackPressed() {

    }

    //create information dialog
    public void showInfoDialog(final View view, final List<Item> productList, final int position) {

//        AlertDialog.Builder builderDialog = new AlertDialog.Builder(view.getContext());
//        builderDialog.setTitle("Thông báo");
//        builderDialog.setMessage("Tên đăng nhập hoặc mật khẩu không đúng. Vui lòng thử lại");
//        builderDialog.setView(R.layout.dialog);
//        AlertDialog alertDialog  = builderDialog.create();
//        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, 200);
//        alertDialog.show();



        dialog = new Dialog(SearchProductAddToStore.this);
        dialog.setContentView(R.layout.search_product_page_product_information_dialog_custom);
        final TextView productName_infoDialog = (TextView) dialog.findViewById(R.id.productName_infoDialog);
        final TextView productCategory_infoDialog = (TextView) dialog.findViewById(R.id.productCategory_infoDialog);
        final TextView productBrand_infoDialog = (TextView) dialog.findViewById(R.id.productBrand_infoDialog);
        final TextView productDesc_infoDialog = (TextView) dialog.findViewById(R.id.productDesc_infoDialog);
        final TextView productTypeText_infoDialog = (TextView) dialog.findViewById(R.id.productTypeText_infoDialog);
        final TextView productType_infoDialog = (TextView) dialog.findViewById(R.id.productType_infoDialog);
        final ImageView productImage = (ImageView) dialog.findViewById(R.id.productImage);
        final Button addbtn = (Button) dialog.findViewById(R.id.addBtn);

        Toast.makeText(getApplicationContext(), productList.get(position).getDescription()+"hih", Toast.LENGTH_SHORT).show();
//        productImage.setMaxHeight(productImage.getWidth());
        productName_infoDialog.setText(productList.get(position).getProduct_name());
        productCategory_infoDialog.setText(productList.get(position).getCategory_name());
        productBrand_infoDialog.setText(productList.get(position).getBrand_name());
        productDesc_infoDialog.setText(productList.get(position).getDescription());
        productTypeText_infoDialog.setText("Loại "+productList.get(position).getBrand_name().toLowerCase());
        productType_infoDialog.setText(productList.get(position).getType_name());
        productImage.setBackgroundColor(R.drawable.background);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToCartPage();
            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
//
        dialog.getWindow().setAttributes(lp);
//        dialogContent.setText("Tên đăng nhập hoặc mật khẩu không đúng. Vui lòng thử lại.");
//        dialogContent.setTextColor(Color.rgb(255,0,0));
//        okBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
        dialog.show();
    }

    public void intentToCartPage(){
        //Intent accessToCartPage = new Intent()
    }

}
