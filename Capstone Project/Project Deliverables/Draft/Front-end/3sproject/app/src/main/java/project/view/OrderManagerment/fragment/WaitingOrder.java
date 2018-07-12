package project.view.OrderManagerment.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import project.view.OrderManagerment.OrderDetailManagement;
import project.view.OrderManagerment.adapter.OrderManagementAdapter;
import project.view.OrderManagerment.model.OrderDetail;
import project.view.R;


public class WaitingOrder extends Fragment {

    private ListView lvOrder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_waiting_order,container,false);
        lvOrder = view.findViewById(R.id.lv_waiting_order);
        OrderManagementAdapter orderManagementAdapter = new OrderManagementAdapter(getContext(),R.layout.item_order_management, OrderDetail.getListOrder());
        lvOrder.setAdapter(orderManagementAdapter);
        lvOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),position+"", Toast.LENGTH_SHORT).show();
                goToOrderDetail();
            }
        });
        return view;
    }
    public void goToOrderDetail(){
        Intent intent = new Intent(getActivity(), OrderDetailManagement.class);
        startActivity(intent);
    }
}
