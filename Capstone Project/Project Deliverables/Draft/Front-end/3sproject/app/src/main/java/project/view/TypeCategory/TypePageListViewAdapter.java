package project.view.TypeCategory;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;

import java.util.List;

import project.firebase.Firebase;
import project.view.Category.Category;
import project.view.Category.CategoryCustomCardviewAdapter;
import project.view.ProductTypeDisplay.ProductTypeDisplay;
import project.view.R;

public class TypePageListViewAdapter extends RecyclerView.Adapter<TypePageListViewAdapter.MyViewHolder> {
    private Context context;
    private List<Type> typeList;


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
        Type type = typeList.get(position);
        holder.tvTypeName.setText(type.getTypeName());
        holder.numberOfRecord.setText(type.getNumberOfRecord()+"");
        holder.subCategoryImg.setImageResource(R.drawable.avatar);
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

