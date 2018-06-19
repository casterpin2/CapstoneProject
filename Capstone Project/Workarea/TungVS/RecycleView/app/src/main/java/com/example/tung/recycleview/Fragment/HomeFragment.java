package com.example.tung.recycleview.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tung.recycleview.R;
import com.example.tung.recycleview.adapter.ItemsAdapter;
import com.example.tung.recycleview.model.Item;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewSale, recyclerViewCategories,recyclerViewBrands;
    private ItemsAdapter itemsAdapterSale, itemsAdapterCategories, itemsAdapterBrands;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        //on sales
        recyclerViewSale = view.findViewById(R.id.re_items);
        itemsAdapterSale = new ItemsAdapter(getContext(), new Item().setItem());
        recyclerViewSale.setAdapter(itemsAdapterSale);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSale.setLayoutManager(linearLayoutManager);



        //categorié
        recyclerViewCategories = view.findViewById(R.id.re_items1);
        itemsAdapterCategories = new ItemsAdapter(getContext(), new Item().setItem());
        recyclerViewCategories.setAdapter(itemsAdapterCategories);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategories.setLayoutManager(linearLayoutManager1);


        //brand
        recyclerViewBrands = view.findViewById(R.id.re_items2);
        itemsAdapterBrands = new ItemsAdapter(getContext(), new Item().setItem());
        recyclerViewBrands.setAdapter(itemsAdapterBrands);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewBrands.setLayoutManager(linearLayoutManager2);
        return view;
    }

}
