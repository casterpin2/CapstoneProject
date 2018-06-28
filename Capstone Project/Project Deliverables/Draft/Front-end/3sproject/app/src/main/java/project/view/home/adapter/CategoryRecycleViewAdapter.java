package project.view.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import project.view.Category.Category;
import project.view.R;
import project.view.home.model.Item;

/**
 * Created by Tung
 */

public class CategoryRecycleViewAdapter extends RecyclerView.Adapter<CategoryRecycleViewAdapter.ViewHolder> {
    private List<Category> categories;
    private Context context;
    private LayoutInflater layoutInflater;

    public CategoryRecycleViewAdapter(Context context, List<Category> datas) {
        context = context;
        categories = datas;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_recycle_view_home, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.tvCategoryName.setText(category.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCategoryName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCategoryName = (TextView) itemView.findViewById(R.id.product_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Category category = categories.get(getAdapterPosition());
                    Toast.makeText(context, category.getCategoryName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
