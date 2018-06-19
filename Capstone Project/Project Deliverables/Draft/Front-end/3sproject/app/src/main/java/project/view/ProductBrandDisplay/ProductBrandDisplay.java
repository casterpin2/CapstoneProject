package project.view.ProductBrandDisplay;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import project.view.R;

public class ProductBrandDisplay extends AppCompatActivity {

    public List<ProductBrand> productList;
    private ListView theListView;
    private ProductBrandDisplayListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_brand_display);

        productList = ProductBrand.getTestingList();
        adapter = new ProductBrandDisplayListViewAdapter(this, R.layout.product_brand_display_custom_listview, productList);
        // get our list view
        theListView = (ListView) findViewById(R.id.mainListView);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        theListView.setAdapter(adapter);

        int brandID = getIntent().getIntExtra("brandID", -1);
        String brandName = getIntent().getStringExtra("brandName");
        getSupportActionBar().setTitle(brandName);
        Toast.makeText(this, brandID+" brand", Toast.LENGTH_SHORT).show();
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
}
