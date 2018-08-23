package project.view.gui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;

import project.objects.User;
import project.view.R;
import project.view.adapter.OrderDetailCustomListViewAdapter;
import project.view.adapter.UserOrderAdapter;
import project.view.model.Cart;
import project.view.model.CartDetail;
import project.view.model.Order;
import project.view.model.OrderDetail;
import project.view.model.Product;
import project.view.model.Store;
import project.view.util.CustomInterface;
import project.view.util.Formater;

public class OrderDetailPage extends BasePage {
    private TextView usernameTV, phoneTV, totalTV, deliveryTimeTV, deliveryAddressTV, statusTV;
    private ListView productListView;
    private TextView acceptBtn, rejectBtn, closeBtn, tvCall;
    private LinearLayout waittingOrderButtonLayout, processingOrderButtonLayout;
    private RelativeLayout buttonLayout;
    private OrderDetailCustomListViewAdapter adapter;
    private ArrayList<Product> productList;
    private boolean isWaittingOrder ;
    private boolean isProcessingOrder ;
    private boolean isDoneOrder ;
    private String orderId;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef, ref;
    private Order order;
    private Store store;
    private static final int REQUEST_CALL = 1;
    private String phoneNumber;
    @Override
    protected void onResume() {
        super.onResume();
        if (!orderId.isEmpty() && store != null) {
            myRef = database.getReference().child("ordersStore").child(String.valueOf(store.getId())).child(orderId);
            myRef.addValueEventListener(changeListener);
        } else {
            Toast.makeText(this, "Không có người dùng", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!orderId.isEmpty() && store != null) {
            myRef.removeEventListener(changeListener);
            ref.removeEventListener(changeListener1);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_page);

        getSupportActionBar().setTitle("Thông tin đơn hàng");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CustomInterface.setStatusBarColor(this);
        orderId = getIntent().getStringExtra("orderId");
        if (orderId.isEmpty()){
            Toast.makeText(this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences pre = getSharedPreferences("authentication", Context.MODE_PRIVATE);
        String storeJSON = pre.getString("store", "");
        if (storeJSON .isEmpty()){
            Toast.makeText(this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            return;
        } else {
            store = new Gson().fromJson(storeJSON,Store.class);
        }
        isWaittingOrder = getIntent().getBooleanExtra("isWaittingOrder", false);
        isProcessingOrder = getIntent().getBooleanExtra("isProcessingOrder", false);
        isDoneOrder = getIntent().getBooleanExtra("isDoneOrder", false);
        findView();
        setLayout(isWaittingOrder, isProcessingOrder, isDoneOrder,buttonLayout, waittingOrderButtonLayout, processingOrderButtonLayout);

        productListView.setHorizontalScrollBarEnabled(false);
        productListView.setVerticalScrollBarEnabled(false);

        deliveryAddressTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // cái này cho chạy qua màn chưa map fragment để chỉ đường
                Intent toMap = new Intent(OrderDetailPage.this, OrderDetailMapPage.class);
                toMap.putExtra("latitude",order.getLatitude());
                toMap.putExtra("longtitude",order.getLongtitude());
                startActivity(toMap);
            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()){
                    Toast.makeText(OrderDetailPage.this, "Có lỗi xảy ra với mạng", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailPage.this);
                builder.setTitle("Chấp nhận đơn hàng");
                builder.setMessage("Bạn có chắc chắn muốn nhận đơn hàng này không?");

                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (order == null ){
                            Toast.makeText(OrderDetailPage.this, "Người dùng đã hủy đơn hàng này", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            myRef.child("status").setValue("doing");
                            DatabaseReference myRef1 = database.getReference().child("ordersUser").child(String.valueOf(order.getUserId())).child(orderId);
                            myRef1.child("status").setValue("processing");
                            finish();
                            // viết code nhận đơn hàng ở đấy
                        }
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
                if (!isNetworkAvailable()){
                    Toast.makeText(OrderDetailPage.this, "Có lỗi xảy ra với mạng", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
                builder.setTitle("Từ chối đơn hàng");
                builder.setMessage("Bạn có muốn từ chối nhận đơn hàng này không?");

                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (order == null ){
                            Toast.makeText(OrderDetailPage.this, "Người dùng đã hủy đơn hàng này", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            myRef.removeValue();
                            DatabaseReference myRef1 = database.getReference().child("ordersUser").child(String.valueOf(order.getUserId())).child(orderId);
                            myRef1.child("status").setValue("cancel");
                            myRef.removeEventListener(changeListener);
                            ref.removeEventListener(changeListener1);
                            finish();
                        }
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
                if (!isNetworkAvailable()){
                    Toast.makeText(OrderDetailPage.this, "Có lỗi xảy ra với mạng", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailPage.this);
                builder.setTitle("Đóng đơn hàng");
                builder.setMessage("Đơn hàng này đã xử lý thành công? Bạn có muốn đóng đơn hàng này?");

                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (order == null || String.valueOf(order.getUserId()) == null){
                            Toast.makeText(OrderDetailPage.this, "Người dùng đã hủy đơn hàng này", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            myRef.child("status").setValue("done");
                            DatabaseReference myRef1 = database.getReference().child("ordersUser").child(String.valueOf(order.getUserId())).child(orderId);
                            myRef1.child("status").setValue("done");
                            finish();
                        }
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
        tvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = phoneTV.getText().toString();
                makePhoneCall(phoneNumber);
            }
        });
        productList= new ArrayList<>();
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
        tvCall = findViewById(R.id.tvCall);

    }
    private void makePhoneCall(String number) {

        if (number.trim().length() > 0) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(OrderDetailPage.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(phoneNumber);
            } else {
                Toast.makeText(OrderDetailPage.this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setLayout(boolean isWaittingOrder, boolean isProcessingOrder, boolean isDoneOrder, RelativeLayout buttonLayout, LinearLayout waittingOrderButtonLayout, LinearLayout processingOrderButtonLayout){
        if (isWaittingOrder) {
            waittingOrderButtonLayout.setVisibility(View.VISIBLE);
            processingOrderButtonLayout.setVisibility(View.GONE);
            statusTV.setText("Đợi xử lý");
        }
        if (isProcessingOrder) {
            waittingOrderButtonLayout.setVisibility(View.GONE);
            processingOrderButtonLayout.setVisibility(View.VISIBLE);
            statusTV.setText("Đang xử lý");
        }
        if (isDoneOrder) {
            buttonLayout.setVisibility(View.GONE);
            statusTV.setText("Đã xử lý");
            productListView.setPadding(0,0,0,0);
        }
    }
    private ValueEventListener changeListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists())
                productList.clear();
                order = dataSnapshot.getValue(Order.class);
                if (order != null) {
                    ref = database.getReference().child("ordersUser").child(String.valueOf(order.getUserId())).child(orderId);
                    ref.addValueEventListener(changeListener1);

                    usernameTV.setText(order.getUserName());
                    phoneTV.setText(order.getPhone());
                    totalTV.setText(Formater.formatDoubleToMoney(String.valueOf(order.getTotalPrice())));
                    deliveryTimeTV.setText(order.getDeliverTime());
                    deliveryAddressTV.setText(order.getAddress());
                    Object[] orderDetails = order.getOrderDetail().values().toArray();
                    for (int i = 0; i < orderDetails.length; i++) {
                        CartDetail cartDetail = (CartDetail) orderDetails[i];
                        Product p = new Product();
                        p.setImage_path(cartDetail.getImage_path());
                        p.setProduct_name(cartDetail.getProductName());
                        p.setQuantity(cartDetail.getQuantity());
                        p.setPrice((long) cartDetail.getUnitPrice());
                        p.setQuantity(cartDetail.getQuantity());
                        p.setProduct_id(cartDetail.getProductId());
                        p.setStore_id(store.getId());
                        productList.add(p);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private ValueEventListener changeListener1 = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (!dataSnapshot.exists()){
                try {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailPage.this);
                    builder.setTitle("Đóng đơn hàng");
                    builder.setMessage("Người dùng vừa hủy đơn hàng này");

                    builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(OrderDetailPage.this, StoreManagementOrderPage.class);
                            intent.putExtra("storeId", store.getId());
                            myRef.removeEventListener(changeListener);
                            ref.removeEventListener(changeListener1);
                            startActivity(intent);
                            finish();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                } catch (WindowManager.BadTokenException e){
                    finish();
                }
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
