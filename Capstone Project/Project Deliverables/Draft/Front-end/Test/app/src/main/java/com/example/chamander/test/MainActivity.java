package com.example.chamander.test;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ExpandableListView lvPhones;
    private List<Cart> list = new ArrayList<>();
    private Cart cart;
    private CartDetail detail;
    List<CartDetail>  cartDetail;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference myRef = database.getReference().child("cart").child("1");
    ChildAdapter phoneListAdapter;
    private Button button;
    public static int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stopService();
        button = (Button) findViewById(R.id.registerBtn);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                addData1();
            }
        });
        lvPhones = (ExpandableListView) findViewById(R.id.phone_list);
        phoneListAdapter = new ChildAdapter(MainActivity.this,list);
        lvPhones.setAdapter(phoneListAdapter);
        myRef.addValueEventListener(changeListener);
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    class LoadContentAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }

        @Override
        protected void onPostExecute(Void mainContentModel) {
            super.onPostExecute(mainContentModel);

        }
    }

    public void getList() {

    }

    ValueEventListener changeListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            list.clear();
           for (DataSnapshot dttSnapshot2 : dataSnapshot.getChildren()) {
               cart = dttSnapshot2.getValue(Cart.class);
               list.add(cart);
               phoneListAdapter.notifyDataSetChanged();
           }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public void addData(){
//        String userId = "1";
//        String key = mDatabase.child("ordersUser").child(userId).push().getKey();
//        Order order = new Order("1",20000,"12h");
//        Map<String, Object> orderValues = order.toMap();
//        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put("/ordersUser/" + userId + key, orderValues);
//        mDatabase.updateChildren(childUpdates);
        count ++;
        CartDetail detail = new CartDetail(count,"DD",3,20000,"Products/Đồ uống/Nước lọc đóng chai/La Vie/Bình sứ Lavie.png");
        String storeId = "1";
        myRef.child(storeId).child("cartDetail").child(String.valueOf(count)).setValue(detail);

    }

    public void addData1(){
        count++;
        CartDetail detail = new CartDetail(1,"DD",3,20000,"Products/Đồ uống/Nước lọc đóng chai/La Vie/Bình sứ Lavie.png");
        List<CartDetail> list = new ArrayList<>();
        list.add(detail);
        Cart cart = new Cart(count,"0942281296","Đây là cửa hàng",list);
        String storeId = String.valueOf(5);
        myRef.child(String.valueOf(count)).setValue(cart);
    }

    public void addProductExist(String storeId, final CartDetail detail){
        final DatabaseReference reference = myRef.child("5");
//        ValueEventListener eventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(!dataSnapshot.exists()) {
//                    //create new user
//                } else {
//                    //CartDetail detail = new CartDetail(1,"DD",3,20000,"Products/Đồ uống/Nước lọc đóng chai/La Vie/Bình sứ Lavie.png");
//                    String productId = String.valueOf(detail.getProductId());
//                    final DatabaseReference reference1 = reference.child("cartDetail").child(productId);
//                    ValueEventListener eventListener1 = new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if(!dataSnapshot.exists()) {
//
//                            }else{
//                                int quantity = Integer.parseInt(dataSnapshot.child("quantity").getValue().toString()) + 1;
//                                reference1.child("quantity").setValue(quantity);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    };
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        };
        myRef.child("5").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //username exist
                    //myRef.child("5").child("cartDetail").setValue(new CartDetail(1,"DD",3,20000,"Products/Đồ uống/Nước lọc đóng chai/La Vie/Bình sứ Lavie.png"));
                    
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void startService(){

        String input  = "FAFSA";
        Intent intent = new Intent(this,MyService.class);
        intent.putExtra("inputExtra",input);
        startService(intent);
    }

    public void stopService(){
        Intent intent = new Intent(this,MyService.class);
        stopService(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        startService();
    }
}
