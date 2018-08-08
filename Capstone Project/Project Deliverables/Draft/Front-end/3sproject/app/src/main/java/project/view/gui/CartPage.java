package project.view.gui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ExpandableListView;

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

public class CartPage extends AppCompatActivity {
    private ExpandableListView lvPhones;
    private List<Cart> list = new ArrayList<>();
    private Cart cart;
    private CartDetail detail;
    List<CartDetail>  cartDetail;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference myRef = database.getReference().child("cart").child("1");
    CartAdapter phoneListAdapter;
    private Button button;
    public static int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_page);
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
