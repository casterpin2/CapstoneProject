package project.view.AddProductToStore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import project.firebase.Firebase;
import project.view.R;

public class SearchProductPageListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Item> productList;
    private int layout;
    private StorageReference storageReference = Firebase.getFirebase();
    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public SearchProductPageListViewAdapter(Context context, int layout, List<Item> productList) {
        this.context = context;
        this.layout = layout;
        this.productList = productList;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        SearchProductPageListViewAdapter.ViewHolder holder;
        if (view  == null){
            LayoutInflater li;
            li = LayoutInflater.from(getContext());
            view = li.inflate(R.layout.search_product_page_custom_list_view, null);
            holder = new SearchProductPageListViewAdapter.ViewHolder();
            holder.productImage = (ImageView) view.findViewById(R.id.productImage);
            holder.productName = (TextView) view.findViewById(R.id.productName);
            holder.productBrand =(TextView) view.findViewById(R.id.productBrand);
            holder.productCategory =(TextView) view.findViewById(R.id.productCategory);
            holder.addBtn = (TextView) view.findViewById(R.id.addBtn);
            holder.addBtn.setTag(position);

            view.setTag(holder);
        }else{
            holder = (SearchProductPageListViewAdapter.ViewHolder) view.getTag();
        }
        Item product = productList.get(position);

        if(product != null) {
            holder.productName.setText(product.getProduct_name());
            holder.productBrand.setText(product.getBrand_name());
            holder.productCategory.setText(product.getCategory_name());
            Glide.with(context /* context */)
                    .using(new FirebaseImageLoader())
                    .load(storageReference.child(productList.get(position).getImage_path()))
                    .into(holder.productImage);
        }

        return view;
    }

    public Context getContext() {
        return context;
    }

    public class ViewHolder{
        ImageView productImage;
        TextView productName;
        TextView productBrand;
        TextView productCategory;
        TextView addBtn;
    }
}
