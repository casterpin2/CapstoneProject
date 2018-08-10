package project.view.gui;

import android.content.Intent;
import android.support.annotation.NonNull;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import project.view.R;
import project.view.adapter.CartAdapter;
import project.view.model.Cart;
import project.view.model.CartDetail;
import project.view.util.CustomInterface;

public class CartPage extends AppCompatActivity {
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

    @Override
    protected void onResume() {
        super.onResume();
        int userId = getIntent().getIntExtra("userID",-1);
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
        myRef.removeEventListener(changeListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_page);

        getSupportActionBar().setTitle("Giỏ hàng");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CustomInterface.setStatusBarColor(CartPage.this);
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
    }

    private ValueEventListener changeListener = new ValueEventListener() {



        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

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
