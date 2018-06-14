package project.view.Cart;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

import java.util.ArrayList;
import java.util.List;

import project.view.AddProductToStore.Item;
import project.view.AddProductToStore.SearchProductAddToStore;
import project.view.R;

public class CartProductToStore extends AppCompatActivity {

    private ListView productListView ;
    private CartProductToStorePageListViewAdapter adapter;
    private List<Item> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_product_to_store);

        productListView = findViewById(R.id.productListView);
        if (productList == null) {
            productList = new ArrayList<>();
        }
        productList = Item.getTestingList();
        adapter = new CartProductToStorePageListViewAdapter(CartProductToStore.this, R.layout.cart_product_to_store_page_custom_listview, productList);
        productListView.setAdapter(adapter);

        final CircleMenu menu = findViewById(R.id.circleMenu);
        menu.setMainMenu(Color.parseColor("#CDCDCD"), R.drawable.add, R.drawable.cancel)
                .addSubMenu(Color.parseColor("#ffff4444"), R.drawable.delete)
                .addSubMenu(Color.parseColor("#ffffff"), R.drawable.back)
                .addSubMenu(Color.parseColor("#0dd9a3"), R.drawable.success)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {
                        if(index == 0) {
                            deleteAllProduct();
                            Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                        } else if (index == 1) {
                            Intent accessToSearchProductToStorePage = new Intent(CartProductToStore.this, SearchProductAddToStore.class);
                            startActivity(accessToSearchProductToStorePage);
                        } else if (index == 2) {
                            // ham add san pham
                            Toast.makeText(getApplicationContext(), "Đây là nút thêm sản phẩm", Toast.LENGTH_SHORT).show();
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

       productList.clear();
        adapter = new CartProductToStorePageListViewAdapter(CartProductToStore.this, R.layout.cart_product_to_store_page_custom_listview, productList);
        productListView.setAdapter(adapter);


    }
}
