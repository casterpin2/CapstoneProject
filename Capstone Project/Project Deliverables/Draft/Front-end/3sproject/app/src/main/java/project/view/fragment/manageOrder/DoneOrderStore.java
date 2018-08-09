package project.view.fragment.manageOrder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import project.view.adapter.StoreOrderManagementAdapter;
import project.view.R;
import project.view.model.OrderDetail;

public class DoneOrderStore extends Fragment {
    ListView lvOrder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_management,container,false);
        lvOrder = view.findViewById(R.id.lv_order);
        StoreOrderManagementAdapter storeOrderManagementAdapter = new StoreOrderManagementAdapter(getContext(),R.layout.item_store_order_management, OrderDetail.dataTest());
        lvOrder.setAdapter(storeOrderManagementAdapter);
        return view;
    }
}
