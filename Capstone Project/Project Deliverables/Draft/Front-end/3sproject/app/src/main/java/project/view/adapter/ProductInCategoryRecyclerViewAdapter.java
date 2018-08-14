package project.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import project.firebase.Firebase;
import project.view.R;
import project.view.gui.NearbyStorePage;
import project.view.model.Product;

public class ProductInCategoryRecyclerViewAdapter extends LoadMoreRecyclerViewAdapter {
    private static final int TYPE_ITEM = 1;
    private StorageReference storageReference = Firebase.getFirebase();
    public ProductInCategoryRecyclerViewAdapter(Context context, ItemClickListener itemClickListener,
                                                RetryLoadMoreListener retryLoadMoreListener) {
        super(context, itemClickListener, retryLoadMoreListener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = mInflater.inflate(R.layout.product_type_display_custom_cardview, parent, false);
            return new ItemViewHolder(view);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            final Product product = mDataList.get(position);
            ((ItemViewHolder) holder).productName.setText(product.getProduct_name());
            Glide.with(mInflater.getContext() /* context */)
                    .using(new FirebaseImageLoader())
                    .load(storageReference.child(mDataList.get(position).getImage_path()))
                    .skipMemoryCache(true)
                    .into(((ItemViewHolder) holder).productImage);
            ((ItemViewHolder) holder).findNearByBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toNearByStorePage = new Intent(mInflater.getContext(), NearbyStorePage.class);
                    toNearByStorePage.putExtra("productId",product.getProduct_id());
                    toNearByStorePage.putExtra("productName",product.getProduct_name());
                    toNearByStorePage.putExtra("image_path",product.getImage_path());
                    mInflater.getContext().startActivity(toNearByStorePage);
                }
            });
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    protected int getCustomItemViewType(int position) {
        return TYPE_ITEM;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView productName;
        public ImageView productImage;
        public TextView findNearByBtn;
        public ItemViewHolder(View view) {
            super(view);
            productName = (TextView) view.findViewById(R.id.productName);
            productImage = (ImageView) view.findViewById(R.id.productImage);
            findNearByBtn = view.findViewById(R.id.findNearByBtn);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
