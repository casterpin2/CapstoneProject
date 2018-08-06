package project.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.List;

import project.firebase.Firebase;
import project.view.R;
import project.view.gui.ProductDetailPage;
import project.view.gui.ProductTypeDisplayPage;
import project.view.model.Type;

public class TypePageListViewAdapter extends RecyclerView.Adapter<TypePageListViewAdapter.MyViewHolder> {
    private Context context;
    private List<Type> typeList;

    private StorageReference storageReference = Firebase.getFirebase();
    public TypePageListViewAdapter(Context context, List<Type> typeList) {
        this.context = context;
        this.typeList = typeList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.type_page_custom_cardview, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Type type = typeList.get(position);
        holder.tvTypeName.setText(type.getTypeName());
        holder.numberOfRecord.setText(type.getNumberOfProduct()+" sản phẩm hiện có");
        try {
            Glide.with(context /* context */)
                    .using(new FirebaseImageLoader())
                    .load(storageReference.child(typeList.get(position).getPath()))
                    .skipMemoryCache(true)
                    .into(holder.subCategoryImg);
            holder.subCategoryImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProductTypeDisplayPage.class);
                    intent.putExtra("typeID",type.getId());
                    intent.putExtra("typeName",type.getName());
                    context.startActivity(intent);
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTypeName, numberOfRecord;
        public ImageView subCategoryImg;

        public MyViewHolder(View view) {
            super(view);
            tvTypeName = (TextView) view.findViewById(R.id.typeName);
            numberOfRecord = (TextView) view.findViewById(R.id.numberOfRecord);
            subCategoryImg = view.findViewById(R.id.typeImage);
        }
    }

}

