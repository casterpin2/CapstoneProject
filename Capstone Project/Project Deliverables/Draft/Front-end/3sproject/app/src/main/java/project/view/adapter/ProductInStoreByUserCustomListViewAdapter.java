package project.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.List;

import project.firebase.Firebase;
import project.view.gui.ProductDetailPage;
import project.view.model.Product;
import project.view.model.ProductInStore;
import project.view.R;
import project.view.util.Formater;

public class ProductInStoreByUserCustomListViewAdapter extends RecyclerView.Adapter<ProductInStoreByUserCustomListViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<ProductInStore> productInStores;
    private StorageReference storageReference = Firebase.getFirebase();
    private Formater formater;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView promotionPercent, productName, storeName, originalPrice, promotionPrice;
        public ImageView productImage;
        public LinearLayout flagSaleLayout;

        public MyViewHolder(View view) {
            super(view);
            promotionPercent = (TextView) view.findViewById(R.id.promotionPercent);
            productName = (TextView) view.findViewById(R.id.productName);
            storeName = (TextView) view.findViewById(R.id.storeName);
            originalPrice = (TextView) view.findViewById(R.id.originalPrice);
            promotionPrice = (TextView) view.findViewById(R.id.promotionPrice);
            productImage = (ImageView) view.findViewById(R.id.productImage);
            flagSaleLayout = view.findViewById(R.id.flagSaleLayout);
        }
    }


    public ProductInStoreByUserCustomListViewAdapter(Context mContext, List<ProductInStore> productInStores) {
        this.mContext = mContext;
        this.productInStores = productInStores;
        formater = new Formater();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sale_product_display_page_custom_cardview, parent, false);

        return new ProductInStoreByUserCustomListViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {
        final  ProductInStore productInStore = productInStores.get(position);
        holder.productName.setText(productInStore.getProductName());
        holder.storeName.setVisibility(View.INVISIBLE);

        if(productInStore.getPromotionPercent()==0.0){
            holder.flagSaleLayout.setVisibility(View.INVISIBLE);
            holder.originalPrice.setVisibility(View.GONE);
        }else {
            holder.flagSaleLayout.setVisibility(View.VISIBLE );
            holder.originalPrice.setVisibility(View.VISIBLE);
            holder.promotionPercent.setText(formater.formatDoubleToInt( String.valueOf(productInStore.getPromotionPercent())));
            holder.originalPrice.setText(formater.formatDoubleToMoney( String.valueOf(productInStore.getProductPrice())));
            holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.promotionPrice.setText(formater.formatDoubleToMoney( String.valueOf(productInStore.getProductPrice() - (productInStore.getProductPrice()* productInStore.getPromotionPercent()/100))));

        Glide.with(mContext /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(productInStore.getProductImage()))
                .into(holder.productImage);

        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mContext, ProductDetailPage.class);
//                intent.putExtra("product",new Gson().toJson(saleProduct));
//                intent.putExtra("isStoreProduct",isStoreProduct);
//                intent.putExtra("isStoreSee",false);
//                intent.putExtra("storeID",saleProduct.getStore_id());
//                intent.putExtra("storeName", saleProduct.getStoreName());
//                mContext.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return productInStores.size();
    }
}
