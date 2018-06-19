package project.view.ProductDisplay;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import project.view.AddProductToStore.Item;
import project.view.AddProductToStore.SearchProductPageListViewAdapter;
import project.view.R;

public class ProductDisplay extends AppCompatActivity {

    public List<Product> productList;
    private ListView theListView;
    private ProductDisplayListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);

        productList = Product.getTestingList();
        adapter = new ProductDisplayListViewAdapter(this, R.layout.product_display_custom_listview, productList);
        // get our list view
        theListView = (ListView) findViewById(R.id.mainListView);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_search_product_add_to_store_page);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        theListView.setAdapter(adapter);
    }
}
