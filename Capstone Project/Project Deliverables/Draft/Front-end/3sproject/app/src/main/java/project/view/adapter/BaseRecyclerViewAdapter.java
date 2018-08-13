package project.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import project.view.model.Product;

public abstract class BaseRecyclerViewAdapter
    extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        protected LayoutInflater mInflater;
        protected List<Product> mDataList;
        protected ItemClickListener mItemClickListener;

    protected BaseRecyclerViewAdapter(@NonNull Context context,
                ItemClickListener itemClickListener) {
            mInflater = LayoutInflater.from(context);
            mItemClickListener = itemClickListener;
            mDataList = new ArrayList<>();
        }

        public void add(List<Product> itemList) {
            mDataList.addAll(itemList);
            notifyDataSetChanged();
        }

        public void set(List<Product> dataList) {
            List<Product> clone = new ArrayList<>(dataList);
            mDataList.clear();
            mDataList.addAll(clone);
            notifyDataSetChanged();
        }

        public void clear() {
            mDataList.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        public interface ItemClickListener {
            void onItemClick(View view, int position);
        }
}
