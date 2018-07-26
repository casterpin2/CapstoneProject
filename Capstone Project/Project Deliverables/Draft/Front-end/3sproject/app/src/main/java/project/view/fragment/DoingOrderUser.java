package project.view.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import project.view.adapter.OrderManagementAdapter;
import project.view.model.OrderDetail;
import project.view.R;


public class DoingOrderUser extends Fragment {


    ListView lvOrder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_management,container,false);
        lvOrder = view.findViewById(R.id.lv_order);
        OrderManagementAdapter orderManagementAdapter = new OrderManagementAdapter(getContext(),R.layout.item_order_management, OrderDetail.dataTest());
        lvOrder.setAdapter(orderManagementAdapter);
        return view;
    }

}
