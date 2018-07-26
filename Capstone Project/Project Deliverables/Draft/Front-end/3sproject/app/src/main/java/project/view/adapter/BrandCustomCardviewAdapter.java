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
import project.view.gui.ProductBrandDisplay;
import project.view.R;
import project.view.model.Brand;

public class BrandCustomCardviewAdapter extends RecyclerView.Adapter<BrandCustomCardviewAdapter.MyViewHolder> {
    private Context mContext;
    private List<Brand> brandList;
    private StorageReference storageReference = Firebase.getFirebase();
    public Context getmContext() {
        return mContext;
    }

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
        final Brand brand = brandList.get(position);
        holder.brandName.setText(brand.getBrandName());
        holder.numberOfRecord.setText(String.valueOf(brand.getNumberOfRecord())+" sản phẩm");
        Glide.with(mContext /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(brandList.get(position).getBrandImageLink()))
                .into(holder.brandImage);
        holder.brandImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getmContext(), ProductBrandDisplay.class);
                intent.putExtra("brandID", brand.getBrandID());
                intent.putExtra("brandName", brand.getBrandName());
                getmContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }
}
