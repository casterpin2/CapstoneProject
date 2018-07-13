
package project.view.home.adapter;

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
        import project.view.Brand.Brand;
        import project.view.ProductBrandDisplay.ProductBrandDisplay;
        import project.view.R;

/**
 * Created by Tung
 */

public class BrandRecycleViewAdapter extends RecyclerView.Adapter<BrandRecycleViewAdapter.ViewHolder> {
    private List<Brand> brands;
    private Context context;
    private LayoutInflater layoutInflater;
    private StorageReference storageReference = Firebase.getFirebase();
//    public BrandRecycleViewAdapter(Context context, List<Brand> datas) {
//        context = context;
//        brands = datas;
//        layoutInflater = LayoutInflater.from(context);
//    }

    public BrandRecycleViewAdapter(List<Brand> brands, Context context) {
        this.brands = brands;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_recycle_view_home, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Brand brand = brands.get(position);
        holder.tvBrandName.setText(brand.getBrandName());
        Glide.with(context /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(brand.getBrandImageLink()))
                .into(holder.imgView);
    }

    @Override
    public int getItemCount() {
        return brands.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBrandName;
        private ImageView imgView;
        public ViewHolder(View itemView) {
            super(itemView);
            tvBrandName = (TextView) itemView.findViewById(R.id.product_name);
            imgView = (ImageView) itemView.findViewById(R.id.image_product);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Brand brand = brands.get(getAdapterPosition());
                    Intent intent = new Intent(context, ProductBrandDisplay.class);
                    intent.putExtra("brandID",brand.getBrandID());
                    intent.putExtra("brandName",brand.getBrandName());
                    context.startActivity(intent);
                }
            });
        }
    }
}
