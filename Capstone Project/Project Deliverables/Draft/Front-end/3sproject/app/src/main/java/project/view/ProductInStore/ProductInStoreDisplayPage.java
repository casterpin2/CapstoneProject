package project.view.ProductInStore;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import project.view.AddProductToStore.SearchProductAddToStore;
import project.view.R;

public class ProductInStoreDisplayPage extends AppCompatActivity {

    private List<ProductInStore> productList;
    private ProductInStoreCustomListViewAdapter adapter;
    private ListView theListView;
    private Button addNewBtn;
    private ViewGroup context;

    public ProductInStoreDisplayPage() {
    }

    public ViewGroup getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_in_store_display_page);

//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.actionbar_search_product_add_to_store_page);
        getSupportActionBar().setTitle("Sản phẩm cửa hàng");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        final int storeID = getIntent().getIntExtra("storeID", -1);

//        adapter.setStoreID(storeID);

        productList = ProductInStore.getExample();

        adapter = new ProductInStoreCustomListViewAdapter(this,R.layout.search_product_page_custom_list_view,productList);
        adapter.setStoreID(1);
        // get our list view
        theListView = (ListView) findViewById(R.id.mainListView);
        theListView.setAdapter(adapter);


        addNewBtn = (Button) findViewById(R.id.addNewBtn);
        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSearchProductToStore = new Intent(ProductInStoreDisplayPage.this, SearchProductAddToStore.class);
//                toSearchProductToStore.putExtra("storeID",storeID);
                startActivity(toSearchProductToStore);
            }
        });
    }
}
