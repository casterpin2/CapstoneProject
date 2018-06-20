package project.view.TypeCategory;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;

import java.util.List;

import project.firebase.Firebase;
import project.view.ProductTypeDisplay.ProductTypeDisplay;
import project.view.R;

public class TypePageListViewAdapter extends RecyclerView.Adapter<TypePageListViewAdapter.MyViewHolder> {
    private Context mContext;
    private List<Type> typeList;
    private StorageReference storageReference = Firebase.getFirebase();
    public Context getmContext() {
        return mContext;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView typeName, numberOfRecord;
        public ImageView typeImage;

        public MyViewHolder(View view) {
            super(view);
            typeName = (TextView) view.findViewById(R.id.typeName);
            numberOfRecord = (TextView) view.findViewById(R.id.numberOfRecord);
            typeImage = (ImageView) view.findViewById(R.id.typeImage);
        }
    }


    public TypePageListViewAdapter(Context mContext, List<Type> typeList) {
        this.mContext = mContext;
        this.typeList = typeList;
    }

    @Override
    public TypePageListViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.brand_display_page_custom_cardview, parent, false);

        return new TypePageListViewAdapter.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(final TypePageListViewAdapter.MyViewHolder holder, int position) {
        final Type type = typeList.get(position);
        holder.typeName.setText(type.getTypeName());
        holder.numberOfRecord.setText(String.valueOf(type.getNumberOfRecord())+" sản phẩm");
//        Glide.with(mContext /* context */)
//                .using(new FirebaseImageLoader())
//                .load(storageReference.child(brandList.get(position).getBrandImageLink()))
//                .into(holder.brandImage);
        holder.typeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getmContext(), ProductTypeDisplay.class);
                intent.putExtra("typeID", type.getTypeID());
                intent.putExtra("typeName", type.getTypeName());
                getmContext().startActivity(intent);
            }
        });




//         loading album cover using Glide library
//        Glide.with(mContext).load(brand.getBrandImageLink()).into(holder.brandImage);

    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }
    }

