package project.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import project.firebase.Firebase;
import project.view.gui.ProductDetailPage;
import project.view.model.Product;
import project.view.model.ProductInStore;
import project.view.R;

public class ProductInStoreByUserCustomListViewAdapter extends ArrayAdapter<ProductInStore> {
    private Context context;
    private List<ProductInStore> productList;
    private StorageReference storageReference = Firebase.getFirebase();
    private int storeID;
    private String storeJson;

    public String getStoreJson() {
        return storeJson;
    }


    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    @NonNull
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ProductInStoreByUserCustomListViewAdapter(@NonNull Context context, int resource, @NonNull List<ProductInStore> productList,String storeJson) {
        super(context, resource, productList);
        this.context = context;
        this.productList = productList;
        this.storeJson = storeJson;
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ProductInStoreByUserCustomListViewAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.product_in_store_by_user_custom_listview, parent, false);
            viewHolder = new ProductInStoreByUserCustomListViewAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ProductInStoreByUserCustomListViewAdapter.ViewHolder)convertView.getTag();
        }

        viewHolder.productName.setText(productList.get(position).getProductName());
        viewHolder.productPromotion.setText(productList.get(position).getPromotionPercent()+" %");
        viewHolder.productPrice.setText(convertLongToString(productList.get(position).getProductPrice())+ " đ");
        if (viewHolder.productName.getLineCount() > 3) {
            int lineEndIndex = viewHolder.productName.getLayout().getLineEnd(2);
            String text = viewHolder.productName.getText().subSequence(0, lineEndIndex - 3) + "...";
            viewHolder.productName.setText(text);
        }

        Glide.with(context /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(productList.get(position).getProductImage()))
                .into(viewHolder.productImage);

        final ProductInStore productInStore = productList.get(position);
        final Product p = new Product(productInStore.getProductID(),productInStore.getProductName(),productInStore.getBrandName(),productInStore.getDescription(),"",productInStore.getTypeName(),productInStore.getProductImage(),productInStore.getProductPrice(),productInStore.getPromotionPercent());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isStoreProduct = true;
                Intent toProductDetail = new Intent(getContext(), ProductDetailPage.class);
                toProductDetail.putExtra("product",new Gson().toJson(p));
                toProductDetail.putExtra("isStoreProduct",isStoreProduct);
                toProductDetail.putExtra("nearByStore",getStoreJson());
                getContext().startActivity(toProductDetail);
            }
        });
        return convertView;
    }



    private static class ViewHolder {
        TextView productName;
        TextView productPrice;
        TextView productPromotion;
        ImageView productImage;

        public ViewHolder(View view) {
            productImage = (ImageView) view.findViewById(R.id.productImage);
            productName = (TextView) view.findViewById(R.id.productName);
            productPrice =(TextView) view.findViewById(R.id.productPrice);
            productPromotion =(TextView) view.findViewById(R.id.productPromotion);

        }
    }

    public String convertLongToString (long needConvert) {
        String formattedString = null;
        try {
//            String originalString = s.toString();
//            Long longval;
//            if (originalString.contains(",")) {
//                originalString = originalString.replaceAll(",", "");
//            }
//            longval = Long.parseLong(originalString);

            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            formatter.applyPattern("#,###,###,###");
            formattedString = formatter.format(needConvert);

            //setting text after format to EditText

        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        return formattedString;
    }
}
