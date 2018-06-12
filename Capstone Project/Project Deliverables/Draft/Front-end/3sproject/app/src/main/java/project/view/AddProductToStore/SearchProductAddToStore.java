package project.view.AddProductToStore;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.List;

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.MainActivity;
import project.view.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchProductAddToStore extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MaterialSearchBar.OnSearchActionListener {

    private MaterialSearchBar searchBar;
    private APIService mAPI;
    private Button addBtn;
    public List<Item> searchedProductList;
    // get list to display to list view;

    // get button add
    public static List<Item> addedProductList = new ArrayList<Item>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product_add_to_store);
        mAPI = ApiUtils.getAPIService();
        // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
        searchedProductList = Item.getTestingList();
        final FoldingCellListAdapter adapter = new FoldingCellListAdapter(this, searchedProductList);
        // get our list view
        final ListView theListView = findViewById(R.id.mainListView);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_search_product_add_to_store_page);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));

        //Toast.makeText(getApplicationContext(), addedProductList.size()+"", Toast.LENGTH_SHORT).show();

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
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {

            @Override
            public void onSearchStateChanged(boolean enabled) {
//                Toast.makeText(getApplicationContext(), "onSearchStateChanged", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

//                Toast.makeText(getApplicationContext(), searchBar.getText().toString(), Toast.LENGTH_SHORT).show();
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


        // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
        //final FoldingCellListAdapter adapter = new FoldingCellListAdapter(this, searchedProductList);



        // set elements to adapter
        theListView.setAdapter(adapter);

        // set on click event listener to list view
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // toggle clicked cell state
                ((FoldingCell) view).toggle(false);
                // register in adapter that state for selected cell is toggled
                adapter.registerToggle(pos);
            }
        });
    }

    //overide method of Search Bar
    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }

}
