package project.view.adapter;

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
import project.view.gui.TypeCategoryPage;
import project.view.model.Category;
import project.view.R;

/**
 * Created by Tung
 */

public class CategoryRecycleViewAdapter extends RecyclerView.Adapter<CategoryRecycleViewAdapter.ViewHolder> {
    private List<Category> categories;
    private Context context;
    private LayoutInflater layoutInflater;
    private StorageReference storageReference = Firebase.getFirebase();


    public CategoryRecycleViewAdapter(List<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
        this.layoutInflater =  LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_recycle_view_home, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.tvCategoryName.setText(category.getCategoryName());
        Glide.with(context /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(categories.get(position).getCategoryImageLink()))
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.imgView);
//        storageReference.child(categories.get(position).getCategoryImageLink()).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                holder.imgView.setImageBitmap(bitmap);
//                // Use the bytes to display the image
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCategoryName;
        private ImageView imgView;
        public ViewHolder(View itemView) {
            super(itemView);
            tvCategoryName = (TextView) itemView.findViewById(R.id.product_name);
            imgView = (ImageView) itemView.findViewById(R.id.image_product) ;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Category category = categories.get(getAdapterPosition());
                    Intent intent = new Intent(context, TypeCategoryPage.class);
                    intent.putExtra("categoryId",category.getCategoryID());
                    intent.putExtra("categoryName",category.getCategoryName());
                    context.startActivity(intent);
                }
            });
        }
    }
}
