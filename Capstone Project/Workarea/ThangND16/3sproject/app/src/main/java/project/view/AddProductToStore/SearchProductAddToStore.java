package project.view.AddProductToStore;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.List;

import project.view.MainActivity;
import project.view.R;

public class SearchProductAddToStore extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MaterialSearchBar.OnSearchActionListener {

    private MaterialSearchBar searchBar;
    private Button addBtn;
    public List<Item> searchedProductList;
    public static List<Item> addedProductList = new ArrayList<Item>();
//    private TextView requestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product_add_to_store);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_search_product_add_to_store_page);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0,187,0)));

        Toast.makeText(getApplicationContext(), addedProductList.size()+"", Toast.LENGTH_SHORT).show();

        // searchBar initial value and do something
        searchBar = findViewById(R.id.searchBar);
        searchBar.setSpeechMode(true);
        searchBar.setArrowIconTint(R.color.colorBlack);
//        searchBar.showSuggestionsList();
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
//                Toast.makeText(getApplicationContext(), "onSearchStateChanged", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
//                Toast.makeText(getApplicationContext(), searchBar.getText().toString(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SearchProductAddToStore.this, MainActivity.class);
                startActivity(intent);

            }

            @Override
            public void onButtonClicked(int buttonCode) {
                Toast.makeText(getApplicationContext(), "onButtonClicked", Toast.LENGTH_SHORT).show();

            }
        });



        // get our list view
        ListView theListView = findViewById(R.id.mainListView);
        // get our to_cart_btn
//        requestBtn = findViewById(R.id.content_request_btn);

        // get list to display to list view
        searchedProductList = Item.getTestingList();

        // get button add





        // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
        final FoldingCellListAdapter adapter = new FoldingCellListAdapter(this, searchedProductList);



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
