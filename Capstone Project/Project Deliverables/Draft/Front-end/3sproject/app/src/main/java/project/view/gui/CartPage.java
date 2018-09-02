package project.view.gui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.googleMapAPI.GoogleMapJSON;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.adapter.CartAdapter;
import project.view.model.Cart;
import project.view.model.CartDetail;
import project.view.model.Notification;
import project.view.model.NotificationDetail;
import project.view.model.ResultNotification;
import project.view.model.Store;
import project.view.model.StoreNotification;
import project.view.util.CustomInterface;
import retrofit2.Call;
import retrofit2.Response;

public class CartPage extends BasePage{
    private ExpandableListView lvPhones;
    private List<Cart> list = new ArrayList<>();
    private Cart cart;
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
//        loadingBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                loadingBar.setVisibility(View.VISIBLE);
                checkoutAllBtn.setEnabled(false);
                checkoutAllBtn.setText("");
                if (!isNetworkAvailable()) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CartPage.this, "Có lỗi xảy ra. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                            loadingBar.setVisibility(View.INVISIBLE);
                            checkoutAllBtn.setEnabled(true);
                            checkoutAllBtn.setText("Mua hàng");
                        }
                    },3000);
                    return;
                }
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
                        for (final Cart cart : list) {
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
                            re = database.getReference().child("notification").child(String.valueOf(cart.getStoreId()));
                            re.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        StoreNotification store = dataSnapshot.getValue(StoreNotification.class);
                                        if (store != null && store.getHaveNotification() != null && store.getToken() != null){
                                            if (store.getHaveNotification().equals("false")){
                                                String token = store.getToken();
                                                Notification notification = new Notification();
                                                notification.setTo(token);
                                                notification.setNotification(new NotificationDetail(cart.getStoreName(),"Bạn có đơn hàng mới"));
                                                Call<ResultNotification> call = ApiUtils.getAPIServiceFirebaseMessage().sendNotification(notification,getResources().getString(R.string.notificationKey),"application/json");
                                                new PushNotification(cart.getStoreId()).execute(call);
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        loadingBar.setVisibility(View.INVISIBLE);
                        checkoutAllBtn.setEnabled(true);
                        checkoutAllBtn.setText("Mua hàng");
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
                        Toast.makeText(CartPage.this, "Có lỗi xảy ra!!!", Toast.LENGTH_LONG).show();
                    }
                });
            } if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "Hủy đặt hàng", Toast.LENGTH_LONG).show();
            }
        }
    }


    private ValueEventListener changeListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            checkoutAllBtn.setEnabled(false);
            list.clear();
            if (dataSnapshot.exists()) {
                noCart.setVisibility(View.INVISIBLE);
                for (DataSnapshot dttSnapshot2 : dataSnapshot.getChildren()) {
                    cart = dttSnapshot2.getValue(Cart.class);
                    list.add(cart);
                    for (int i = 0; i < list.size(); i++) {
                        lvPhones.expandGroup(i);
                    }
                    //phoneListAdapter.setTotalPrice(0.0);
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
                phoneListAdapter.notifyDataSetChanged();
                totalCart.setText(phoneListAdapter.getTotalPrice());
                noCart.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public class PushNotification extends AsyncTask<Call,Void,ResultNotification> {
        private int storeId;

        public int getStoreId() {
            return storeId;
        }

        public void setStoreId(int storeId) {
            this.storeId = storeId;
        }

        public PushNotification(int storeId) {
            this.storeId = storeId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ResultNotification result) {
            super.onPostExecute(result);
            if (result != null) {
                //Toast.makeText(CartPage.this, result.toString(), Toast.LENGTH_SHORT).show();
                DatabaseReference databaseReference = database.getReference().child("notification").child(String.valueOf(storeId)).child("haveNotification");
                databaseReference.setValue("true");
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ResultNotification doInBackground(Call... calls) {
            try {
                Call<ResultNotification> call = calls[0];
                Response<ResultNotification> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
