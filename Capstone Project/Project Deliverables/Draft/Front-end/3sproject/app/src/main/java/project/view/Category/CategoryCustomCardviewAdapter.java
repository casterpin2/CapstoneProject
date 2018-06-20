package project.view.Category;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import project.firebase.Firebase;
import project.view.R;

public class CategoryCustomCardviewAdapter extends RecyclerView.Adapter<CategoryCustomCardviewAdapter.MyViewHolder> {
    private Context mContext;
    private List<Category> categoryList;
    private StorageReference storageReference = Firebase.getFirebase();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryName, numberOfRecord, categoryID;
        public ImageView categoryImage;

        public MyViewHolder(View view) {
            super(view);
            categoryName = (TextView) view.findViewById(R.id.categoryName);
            numberOfRecord = (TextView) view.findViewById(R.id.numberOfRecord);
            categoryImage = (ImageView) view.findViewById(R.id.categoryImage);
        }
    }


    public CategoryCustomCardviewAdapter(Context mContext, List<Category> categoryList) {
        this.mContext = mContext;
        this.categoryList = categoryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_display_page_custom_cardview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.categoryName.setText(category.getCategoryName());
        holder.numberOfRecord.setText(String.valueOf(category.getNumberOfRecord())+" sản phẩm");
        Glide.with(mContext /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(categoryList.get(position).getCategoryImageLink()))
                .into(holder.categoryImage);



        // loading album cover using Glide library
//        Glide.with(mContext).load(category.getCategoryImageLink()).into(holder.categoryImage);

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
