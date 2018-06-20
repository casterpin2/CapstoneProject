package project.view.SaleProduct;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import project.view.R;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class SaleProductCustomCardviewAdapter extends RecyclerView.Adapter<SaleProductCustomCardviewAdapter.MyViewHolder>  {

    private Context mContext;
    private List<SaleProduct> saleProductList;


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
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sale_product_display_page_custom_cardview, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder,  int position) {
        SaleProduct saleProduct = saleProductList.get(position);
        holder.productName.setText(saleProduct.getProductName());
        holder.storeName.setText(saleProduct.getStoreName());
        holder.originalPrice.setText(formatDoubleToMoney(mContext, String.valueOf(saleProduct.getProductPrice())));
        holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.promotionPrice.setText(formatDoubleToMoney(mContext, String.valueOf(saleProduct.getProductPrice() - (saleProduct.getProductPrice()* saleProduct.getProducrPromotionPercent()/100))));
        holder.promotionPercent.setText(formatDoubleToInt(mContext, "-"+String.valueOf(saleProduct.getProducrPromotionPercent())));



        // loading album cover using Glide library
//        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);
//
//        holder.overflow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//             //   listener.onAlbumsSelected(albumList.get(position));
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return saleProductList.size();
    }

    public static String formatDoubleToMoney(Context context, String price) {

        NumberFormat format =
                new DecimalFormat("#,##0.00");// #,##0.00 ¤ (¤:// Currency symbol)
        format.setCurrency(Currency.getInstance(Locale.US));//Or default locale

        price = (!TextUtils.isEmpty(price)) ? price : "0";
        price = price.trim();
        price = format.format(Double.parseDouble(price));
        price = price.replaceAll(",", "\\.");

        if (price.endsWith(".00")) {
            int centsIndex = price.lastIndexOf(".00");
            if (centsIndex != -1) {
                price = price.substring(0, centsIndex);
            }
        }
        price = String.format("%s đ", price);
        return price;
    }

    public static String formatDoubleToInt(Context context, String price) {

        NumberFormat format =
                new DecimalFormat("#,##0.00");// #,##0.00 ¤ (¤:// Currency symbol)
        format.setCurrency(Currency.getInstance(Locale.US));//Or default locale

        price = (!TextUtils.isEmpty(price)) ? price : "0";
        price = price.trim();
        price = format.format(Double.parseDouble(price));
        price = price.replaceAll(",", "\\.");

        if (price.endsWith(".00")) {
            int centsIndex = price.lastIndexOf(".00");
            if (centsIndex != -1) {
                price = price.substring(0, centsIndex);
            }
        }
        price = String.format("%s%%", price);
        return price;
    }
}
