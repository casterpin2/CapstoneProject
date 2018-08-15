package project.view.gui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import project.googleMapAPI.GoogleMapJSON;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.adapter.CartAdapter;
import project.view.model.Cart;
import project.view.model.CartDetail;
import project.view.util.CustomInterface;
import retrofit2.Call;

public class CartPage extends BasePage{
    private ExpandableListView lvPhones;
    private List<Cart> list = new ArrayList<>();
    private Cart cart;
    private CartDetail detail;
    List<CartDetail>  cartDetail;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    CartAdapter phoneListAdapter;
    private Button button;
    public static int count = 0;
    private RelativeLayout noCart;
    private TextView totalCart;
    private Button checkoutAllBtn,shoppingBtn;
    private ProgressBar loadingBar;
    private LinearLayout buyLinearLayout;
    private int userId;
    @Override
    protected void onResume() {
        super.onResume();
        if (userId != -1) {
            myRef = database.getReference().child("cart").child(String.valueOf(userId));
            phoneListAdapter = new CartAdapter(CartPage.this, list,userId);
            lvPhones.setAdapter(phoneListAdapter);
            myRef.addValueEventListener(changeListener);
        } else {
            Toast.makeText(this, "Không có người dùng", Toast.LENGTH_LONG).show();
        }
        lvPhones.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (userId != -1) {
            myRef.removeEventListener(changeListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_page);

        getSupportActionBar().setTitle("Giỏ hàng");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CustomInterface.setStatusBarColor(CartPage.this);
        userId = getIntent().getIntExtra("userID",-1);
        if ( userId == -1 ) {
            return;
        }
        lvPhones = (ExpandableListView) findViewById(R.id.phone_list);
        noCart = (RelativeLayout) findViewById(R.id.noCart);
        totalCart = (TextView) findViewById(R.id.totalCart);
        checkoutAllBtn = (Button) findViewById(R.id.checkoutAllBtn);
        buyLinearLayout = (LinearLayout) findViewById(R.id.buyLinearLayout);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);
        loadingBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);
        shoppingBtn = (Button) findViewById(R.id.shoppingBtn);
        shoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartPage.this,HomePage.class));
                finishAffinity();
            }
        });
        checkoutAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.size() == 0) return;

                Intent intent = new Intent(CartPage.this,OrderPage.class);
                intent.putExtra("isCart",true);
                intent.putExtra("priceInCart",String.valueOf(phoneListAdapter.getTotal()));
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                final String address = data.getStringExtra("address");
                final String deliverTime = data.getStringExtra("deliverTime");
                final String userImage = data.getStringExtra("user_image");
                final String userName = data.getStringExtra("userName");
                final String phone = data.getStringExtra("phone");
                final double longtitude = data.getDoubleExtra("longtitude",0.0);
                final double latitude = data.getDoubleExtra("latitude",0.0);
                final DatabaseReference reference = database.getReference().child("ordersUser").child(String.valueOf(userId));
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (Cart cart : list) {
                            String key = reference.push().getKey();
                            DatabaseReference re = reference.child(key);
                            DatabaseReference referenceStore = database.getReference().child("ordersStore").child(String.valueOf(cart.getStoreId())).child(key);
                            re.child("address").setValue(address);
                            referenceStore.child("address").setValue(address);
                            re.child("latitude").setValue(latitude);
                            referenceStore.child("latitude").setValue(latitude);
                            re.child("longtitude").setValue(longtitude);
                            referenceStore.child("longtitude").setValue(longtitude);
                            re.child("deliverTime").setValue(deliverTime);
                            referenceStore.child("deliverTime").setValue(deliverTime);
                            re.child("storeId").setValue(cart.getStoreId());
                            re.child("storeName").setValue(cart.getStoreName());
                            re.child("status").setValue("waitting");
                            referenceStore.child("status").setValue("waitting");
                            re.child("phone").setValue(cart.getPhone());
                            referenceStore.child("phone").setValue(phone);
                            re.child("isFeedback").setValue("false");
                            re.child("image_path").setValue(cart.getImage_path());
                            referenceStore.child("image_path").setValue(userImage);
                            referenceStore.child("userName").setValue(userName);
                            referenceStore.child("userId").setValue(userId);
                            Object[] cartDetails = cart.getCartDetail().values().toArray();
                            double price = 0;
                            for(int i = 0 ; i < cartDetails.length;i++) {
                                CartDetail cartDetail = (CartDetail)cartDetails[i];
                                price += cartDetail.getUnitPrice() * cartDetail.getQuantity();
                            }
                            re.child("totalPrice").setValue(price);
                            referenceStore.child("totalPrice").setValue(price);
                            re.child("orderDetail").setValue(cart.getCartDetail());
                            referenceStore.child("orderDetail").setValue(cart.getCartDetail());

                        }
                        DatabaseReference re = database.getReference().child("cart").child(String.valueOf(userId));
                        re.removeValue();
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartPage.this);
                        builder.setTitle("Đặt hàng");
                        builder.setMessage("Bạn đã đặt hàng thành công");

                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                        builder.show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }


    private ValueEventListener changeListener = new ValueEventListener() {



        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            checkoutAllBtn.setEnabled(false);
            list.clear();
            loadingBar.setVisibility(View.VISIBLE);
            if (dataSnapshot.exists()) {
                noCart.setVisibility(View.INVISIBLE);
                for (DataSnapshot dttSnapshot2 : dataSnapshot.getChildren()) {
                    cart = dttSnapshot2.getValue(Cart.class);
                    list.add(cart);
                    for (int i = 0; i < list.size(); i++) {
                        lvPhones.expandGroup(i);
                    }
                    //phoneListAdapter.setTotalPrice(0.0);
                    loadingBar.setVisibility(View.INVISIBLE);
                    phoneListAdapter.notifyDataSetChanged();

                    totalCart.setText(phoneListAdapter.getTotalPrice());
                    if (list.size() == 0) {
                        totalCart.setVisibility(View.INVISIBLE);
                    }
                }
                checkoutAllBtn.setEnabled(true);
            } else {
                list.clear();
                buyLinearLayout.setVisibility(View.INVISIBLE);
                loadingBar.setVisibility(View.INVISIBLE);
                phoneListAdapter.notifyDataSetChanged();
                totalCart.setText(phoneListAdapter.getTotalPrice());
                noCart.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public void startService(){

        String input  = "FAFSA";
        Intent intent = new Intent(this,NotificationService.class);
        intent.putExtra("inputExtra",input);
        startService(intent);
    }

    public void stopService(){
        Intent intent = new Intent(this,NotificationService.class);
        stopService(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        startService();
    }
}
