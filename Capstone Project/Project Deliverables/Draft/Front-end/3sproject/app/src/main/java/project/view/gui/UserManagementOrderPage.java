package project.view.gui;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import project.view.adapter.UserOrderAdapter;
import project.view.R;
import project.view.model.Order;
import project.view.util.CustomInterface;

public class UserManagementOrderPage extends BasePage {

    private ExpandableListView orderListView;
    private List<Order> list = new ArrayList<>();
    private Order order;
    private RelativeLayout noOrder;
    private UserOrderAdapter adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private int userId;
    private Button shoppingBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_management);
        CustomInterface.setStatusBarColor(this);

        shoppingBtn = (Button) findViewById(R.id.shoppingBtn);
        shoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserManagementOrderPage.this,HomePage.class));
                finishAffinity();
            }
        });

        userId = getIntent().getIntExtra("userID",-1);
        getSupportActionBar().setTitle("Quản lý đơn hàng");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        orderListView = (ExpandableListView) findViewById(R.id.orderList);
        noOrder = (RelativeLayout) findViewById(R.id.noOrder);
        orderListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
            });
        }

    @Override
    protected void onResume() {
        super.onResume();

        if (userId != -1) {
            myRef = database.getReference().child("ordersUser").child(String.valueOf(userId));
            adapter = new UserOrderAdapter(UserManagementOrderPage.this, list,userId);
            orderListView.setAdapter(adapter);
            myRef.addValueEventListener(changeListener);
        } else {
            Toast.makeText(this, "Không có người dùng", Toast.LENGTH_LONG).show();
        }
        orderListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (userId != -1) {
            myRef.removeEventListener(changeListener);
        }
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

    private ValueEventListener changeListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            list.clear();
            //loadingBar.setVisibility(View.VISIBLE);
            if (dataSnapshot.exists()) {
                noOrder.setVisibility(View.INVISIBLE);
                for (DataSnapshot dttSnapshot2 : dataSnapshot.getChildren()) {
                    order = dttSnapshot2.getValue(Order.class);
                    if (order.getStatus().equalsIgnoreCase("cancel"));
                    {
                        Log.d("order", order.toString());
                        order.setOrderId(dttSnapshot2.getKey());
                        list.add(order);
                        for (int i = 0; i < list.size(); i++) {
                            orderListView.expandGroup(i);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            } else {
                list.clear();
                //buyLinearLayout.setVisibility(View.INVISIBLE);
                //loadingBar.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
                noOrder.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
