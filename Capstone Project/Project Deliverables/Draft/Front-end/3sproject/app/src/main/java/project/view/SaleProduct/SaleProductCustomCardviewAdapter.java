package project.view.SaleProduct;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import project.firebase.Firebase;
import project.view.ProductBrandDisplay.ProductBrandDisplay;
import project.view.ProductDetail.ProductDetailPage;
import project.view.R;
import project.view.util.Formater;

public class SaleProductCustomCardviewAdapter extends RecyclerView.Adapter<SaleProductCustomCardviewAdapter.MyViewHolder>  {

    private Context mContext;
    private List<SaleProduct> saleProductList;
    private StorageReference storageReference = Firebase.getFirebase();
    private Formater formater;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView promotionPercent, productName, storeName, originalPrice, promotionPrice;
        public ImageView productImage;

        public MyViewHolder(View view) {
            super(view);
            promotionPercent = (TextView) view.findViewById(R.id.promotionPercent);
            productName = (TextView) view.findViewById(R.id.productName);
            storeName = (TextView) view.findViewById(R.id.storeName);
            originalPrice = (TextView) view.findViewById(R.id.originalPrice);
            promotionPrice = (TextView) view.findViewById(R.id.promotionPrice);
            productImage = (ImageView) view.findViewById(R.id.productImage);
        }
    }


    public SaleProductCustomCardviewAdapter(Context mContext, List<SaleProduct> saleProductList) {
        this.mContext = mContext;
        this.saleProductList = saleProductList;
        formater = new Formater();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sale_product_display_page_custom_cardview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,  int position) {
        final SaleProduct saleProduct = saleProductList.get(position);
        holder.productName.setText(saleProduct.getProductName());
        holder.storeName.setText(saleProduct.getStoreName());
        holder.originalPrice.setText(formater.formatDoubleToMoney( String.valueOf(saleProduct.getProductPrice())));
        holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.promotionPrice.setText(formater.formatDoubleToMoney( String.valueOf(saleProduct.getProductPrice() - (saleProduct.getProductPrice()* saleProduct.getProducrPromotionPercent()/100))));
        holder.promotionPercent.setText(formater.formatDoubleToInt( "-"+String.valueOf(saleProduct.getProducrPromotionPercent())));
        Glide.with(mContext /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(saleProduct.getImgProductSale()))
                .into(holder.productImage);

        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductDetailPage.class);
                intent.putExtra("productName", saleProduct.getProductName());
                intent.putExtra("storeName", saleProduct.getStoreName());
                mContext.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return saleProductList.size();
    }
}
