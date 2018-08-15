package project.view.fragment.manageOrder;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import project.view.adapter.OrderManagementAdapter;
import project.view.adapter.StoreOrderManagementAdapter;
import project.view.R;
import project.view.model.Order;
import project.view.model.OrderDetail;

public class DoneOrderStore extends Fragment {
    ListView lvOrder;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private int storeId;
    private Order order;
    private List<Order> list = new ArrayList<>();
    private StoreOrderManagementAdapter adapter;
    @Override
    public void onResume() {
        super.onResume();
        if (storeId != -1) {
            myRef = database.getReference().child("ordersStore").child(String.valueOf(storeId));
            myRef.orderByChild("status").equalTo("done").addValueEventListener(changeListener);
        } else {
            Toast.makeText(getContext(), "Không có người dùng", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (storeId != -1) {
            myRef.removeEventListener(changeListener);
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_management,container,false);
        lvOrder = view.findViewById(R.id.lv_order);
        storeId = getArguments().getInt("storeId",-1);
        adapter = new StoreOrderManagementAdapter(getContext(),R.layout.item_store_order_management, list,"Done");
        lvOrder.setAdapter(adapter);
        return view;
    }
    private ValueEventListener changeListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            list.clear();
            //loadingBar.setVisibility(View.VISIBLE);
            if (dataSnapshot.exists()) {
                for (final DataSnapshot dttSnapshot2 : dataSnapshot.getChildren()) {
                    order = dttSnapshot2.getValue(Order.class);
                    if (order != null) {
                        Log.d("order", order.toString());
                        order.setOrderId(dttSnapshot2.getKey());
                        list.add(order);
                    }
                    adapter.notifyDataSetChanged();
                }
            } else {
                list.clear();
                //buyLinearLayout.setVisibility(View.INVISIBLE);
                //loadingBar.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
//                noOrder.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
