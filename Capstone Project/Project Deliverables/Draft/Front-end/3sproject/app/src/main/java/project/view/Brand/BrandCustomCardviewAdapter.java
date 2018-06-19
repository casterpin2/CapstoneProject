package project.view.Brand;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import project.view.R;

public class BrandCustomCardviewAdapter extends RecyclerView.Adapter<BrandCustomCardviewAdapter.MyViewHolder> {
    private Context mContext;
    private List<Brand> brandList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView brandName, numberOfRecord;
        public ImageView brandImage;

        public MyViewHolder(View view) {
            super(view);
            brandName = (TextView) view.findViewById(R.id.brandName);
            numberOfRecord = (TextView) view.findViewById(R.id.numberOfRecord);
            brandImage = (ImageView) view.findViewById(R.id.brandImage);
        }
    }


    public BrandCustomCardviewAdapter(Context mContext, List<Brand> brandList) {
        this.mContext = mContext;
        this.brandList = brandList;
    }

    @Override
    public BrandCustomCardviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.brand_display_page_custom_cardview, parent, false);

        return new BrandCustomCardviewAdapter.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(final BrandCustomCardviewAdapter.MyViewHolder holder, int position) {
        Brand brand = brandList.get(position);
        holder.brandName.setText(brand.getBrandName());
        holder.numberOfRecord.setText(String.valueOf(brand.getNumberOfRecord())+" sản phẩm");




//         loading album cover using Glide library
//        Glide.with(mContext).load(brand.getBrandImageLink()).into(holder.brandImage);

    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }
}
