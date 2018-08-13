package project.view.gui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;

import project.view.R;
import project.view.adapter.OrderDetailCustomListViewAdapter;
import project.view.model.Product;
import project.view.util.CustomInterface;

public class OrderDetailPage extends BasePage {
    private TextView usernameTV, phoneTV, totalTV, deliveryTimeTV, deliveryAddressTV, statusTV;
    private ListView productListView;
    private TextView acceptBtn, rejectBtn, closeBtn;
    private LinearLayout waittingOrderButtonLayout, processingOrderButtonLayout;
    private RelativeLayout buttonLayout;
    private OrderDetailCustomListViewAdapter adapter;
    private ArrayList<Product> productList;
    private boolean isWaittingOrder ;
    private boolean isProcessingOrder ;
    private boolean isDoneOrder ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_page);

        getSupportActionBar().setTitle("Thông tin đơn hàng");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CustomInterface.setStatusBarColor(this);

        isWaittingOrder = getIntent().getBooleanExtra("isWaittingOrder", false);
        isProcessingOrder = getIntent().getBooleanExtra("isProcessingOrder", false);
        isDoneOrder = getIntent().getBooleanExtra("isDoneOrder", true);
        findView();
        setLayout(isWaittingOrder, isProcessingOrder, isDoneOrder,buttonLayout, waittingOrderButtonLayout, processingOrderButtonLayout);

        productListView.setHorizontalScrollBarEnabled(false);
        productListView.setVerticalScrollBarEnabled(false);

        deliveryAddressTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // cái này cho chạy qua màn chưa map fragment để chỉ đường
                Intent toMap = new Intent(OrderDetailPage.this, OrderDetailMapPage.class);
                startActivity(toMap);
            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailPage.this);
                builder.setTitle("Chấp nhận đơn hàng");
                builder.setMessage("Bạn có chắc chắn muốn nhận đơn hàng này không?");

                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // viết code nhận đơn hàng ở đấy
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.show();
            }
        });
        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailPage.this);
                builder.setTitle("Từ chối đơn hàng");
                builder.setMessage("Bạn có muốn từ chối nhận đơn hàng này không?");

                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // viết code từ chối nhận ở đấy
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.show();
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailPage.this);
                builder.setTitle("Đóng đơn hàng");
                builder.setMessage("Đơn hàng này đã xử lý thành công? Bạn có muốn đóng đơn hàng này?");

                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // viết code đóng đơn hàng ở đấy
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.show();
            }
        });

        productList= new ArrayList<>();
//        int product_id, String product_name, String brand_name, String description, String category_name, String type_name, String image_path, long price, double promotion
        productList.add(new Product(1,"Samsung Galaxy Note8", "Samsung", "Đây là Samsung","Điện thoại", "Điện thoại cao cấp", "hdhasd", 10000000,1.2));
        productList.add(new Product(1,"Samsung Galaxy Note8", "Samsung", "Đây là Samsung","Điện thoại", "Điện thoại cao cấp", "hdhasd", 10000000,1.2));
        productList.add(new Product(1,"Samsung Galaxy Note8", "Samsung", "Đây là Samsung","Điện thoại", "Điện thoại cao cấp", "hdhasd", 10000000,1.2));
        productList.add(new Product(1,"Samsung Galaxy Note8", "Samsung", "Đây là Samsung","Điện thoại", "Điện thoại cao cấp", "hdhasd", 10000000,1.2));
        productList.add(new Product(1,"Samsung Galaxy Note8", "Samsung", "Đây là Samsung","Điện thoại", "Điện thoại cao cấp", "hdhasd", 10000000,1.2));
        productList.add(new Product(1,"Samsung Galaxy Note8", "Samsung", "Đây là Samsung","Điện thoại", "Điện thoại cao cấp", "hdhasd", 10000000,1.2));
        productList.add(new Product(1,"Samsung Galaxy Note8", "Samsung", "Đây là Samsung","Điện thoại", "Điện thoại cao cấp", "hdhasd", 10000000,1.2));
        productList.add(new Product(1,"Samsung Galaxy Note8", "Samsung", "Đây là Samsung","Điện thoại", "Điện thoại cao cấp", "hdhasd", 10000000,1.2));
        productList.add(new Product(1,"Samsung Galaxy Note8", "Samsung", "Đây là Samsung","Điện thoại", "Điện thoại cao cấp", "hdhasd", 10000000,1.2));

        adapter = new OrderDetailCustomListViewAdapter(OrderDetailPage.this, R.layout.product_in_order_detail, productList);
        productListView.setAdapter(adapter);
//        adapter = new ProductBrandDisplayListViewAdapter(ProductBrandDisplayPage.this, R.layout.product_brand_display_custom_listview, list);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void findView(){
//        numberOfProductTV = (TextView) findViewById(R.id.numberOfProductTV);
        usernameTV = (TextView) findViewById(R.id.usernameTV);
        phoneTV = (TextView) findViewById(R.id.phoneTV);
        totalTV = (TextView) findViewById(R.id.totalTV);
        deliveryTimeTV = (TextView) findViewById(R.id.deliveryTimeTV);
        deliveryAddressTV = (TextView) findViewById(R.id.deliveryAddressTV);
        statusTV = (TextView) findViewById(R.id.statusTV);
        productListView = (ListView) findViewById(R.id.productListView);
        waittingOrderButtonLayout = (LinearLayout) findViewById(R.id.waittingOrderButtonLayout);
        processingOrderButtonLayout = (LinearLayout) findViewById(R.id.processingOrderButtonLayout);
        buttonLayout = (RelativeLayout) findViewById(R.id.buttonLayout);
        acceptBtn = (TextView) findViewById(R.id.acceptBtn);
        rejectBtn = (TextView) findViewById(R.id.rejectBtn);
        closeBtn = (TextView) findViewById(R.id.closeBtn);

    }

    public void setLayout(boolean isWaittingOrder, boolean isProcessingOrder, boolean isDoneOrder, RelativeLayout buttonLayout, LinearLayout waittingOrderButtonLayout, LinearLayout processingOrderButtonLayout){
        if (isWaittingOrder) {
            waittingOrderButtonLayout.setVisibility(View.VISIBLE);
            processingOrderButtonLayout.setVisibility(View.GONE);
        }
        if (isProcessingOrder) {
            waittingOrderButtonLayout.setVisibility(View.GONE);
            processingOrderButtonLayout.setVisibility(View.VISIBLE);
        }
        if (isDoneOrder) {
            buttonLayout.setVisibility(View.GONE);
            productListView.setPadding(0,0,0,0);
        }
    }

}
