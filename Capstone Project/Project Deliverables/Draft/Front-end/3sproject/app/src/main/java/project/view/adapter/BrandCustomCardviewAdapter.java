package project.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import project.firebase.Firebase;
import project.view.R;
import project.view.gui.ProductBrandDisplayPage;
import project.view.model.Brand;

public class BrandCustomCardviewAdapter extends LoadMoreRecyclerViewAdapter<Brand> {
    private static final int TYPE_ITEM = 1;
    private StorageReference storageReference = Firebase.getFirebase();

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView brandName, numberOfRecord;
        public ImageView brandImage;

        public ItemViewHolder(View view) {
            super(view);
            brandName = (TextView) view.findViewById(R.id.brandName);
            numberOfRecord = (TextView) view.findViewById(R.id.numberOfRecord);
            brandImage = (ImageView) view.findViewById(R.id.brandImage);
        }
        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }


    public BrandCustomCardviewAdapter(Context context, ItemClickListener itemClickListener,
                                                RetryLoadMoreListener retryLoadMoreListener) {
        super(context, itemClickListener, retryLoadMoreListener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = mInflater.inflate(R.layout.brand_display_page_custom_cardview, parent, false);
            return new ItemViewHolder(view);
        }
        return super.onCreateViewHolder(parent, viewType);
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            final Brand brand = mDataList.get(position);
            ((ItemViewHolder) holder).brandName.setText(brand.getBrandName());
            ((ItemViewHolder) holder).numberOfRecord.setText(String.valueOf(brand.getNumberOfRecord()) + " sản phẩm");
            Glide.with(mInflater.getContext() /* context */)
                    .using(new FirebaseImageLoader())
                    .load(storageReference.child(brand.getBrandImageLink()))
                    .into(((ItemViewHolder) holder).brandImage);
            ((ItemViewHolder) holder).brandImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mInflater.getContext(), ProductBrandDisplayPage.class);
                    intent.putExtra("brandID", brand.getBrandID());
                    intent.putExtra("brandName", brand.getBrandName());
                    mInflater.getContext().startActivity(intent);
                }
            });
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    protected int getCustomItemViewType(int position) {
        return TYPE_ITEM;
    }
}
