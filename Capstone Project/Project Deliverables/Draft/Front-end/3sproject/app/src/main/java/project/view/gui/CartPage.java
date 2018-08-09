package project.view.gui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
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
    }

    private ValueEventListener changeListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            list.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot dttSnapshot2 : dataSnapshot.getChildren()) {
                    cart = dttSnapshot2.getValue(Cart.class);
                    list.add(cart);
                    for (int i = 0; i < list.size(); i++) {
                        lvPhones.expandGroup(i);
                    }
                    phoneListAdapter.notifyDataSetChanged();
                }
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
