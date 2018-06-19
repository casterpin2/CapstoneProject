package com.example.tung.recycleview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tung.recycleview.R;
import com.example.tung.recycleview.model.Item;

import java.util.List;

/**
 * Created by Tung
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    private List<Item> items;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public ItemsAdapter(Context context, List<Item> datas) {
        mContext = context;
        items = datas;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_product, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = items.get(position);

        holder.tvQuantity.setText(item.getCode());
        holder.tvProName.setText(item.getProductName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProName;
        private TextView tvQuantity;

        public ViewHolder(View itemView) {
            super(itemView);
            tvProName = (TextView) itemView.findViewById(R.id.product_name);
            tvQuantity = (TextView) itemView.findViewById(R.id.quantity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Item song = items.get(getAdapterPosition());
                    Toast.makeText(mContext, song.getProductName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
