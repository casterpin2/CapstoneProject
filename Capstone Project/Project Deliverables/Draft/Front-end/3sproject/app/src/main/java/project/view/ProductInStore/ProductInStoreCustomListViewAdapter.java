package project.view.ProductInStore;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import project.firebase.Firebase;
import project.view.EditProductInStore.EditProductInStorePage;
import project.view.R;


public class ProductInStoreCustomListViewAdapter extends ArrayAdapter<ProductInStore> {
    private Context context;
    private List<ProductInStore> productList;
    private StorageReference storageReference = Firebase.getFirebase();
    private int storeID;

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

    public ProductInStoreCustomListViewAdapter(@NonNull Context context, int resource, @NonNull List<ProductInStore> productList) {
        super(context, resource, productList);
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ProductInStoreCustomListViewAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.product_in_store_custom_listview, parent, false);
            viewHolder = new ProductInStoreCustomListViewAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ProductInStoreCustomListViewAdapter.ViewHolder)convertView.getTag();
        }

        viewHolder.productName.setText(productList.get(position).getProductName());
        viewHolder.productPromotion.setText(productList.get(position).getPromotionPercent()+" %");
        viewHolder.productPrice.setText(convertLongToString(productList.get(position).getProductPrice())+ " đ");
        viewHolder.editBtn.setTag(productList.get(position).getProductID());
        if (viewHolder.productName.getLineCount() > 3) {
            int lineEndIndex = viewHolder.productName.getLayout().getLineEnd(2);
            String text = viewHolder.productName.getText().subSequence(0, lineEndIndex - 3) + "...";
            viewHolder.productName.setText(text);
        }

        Glide.with(context /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(productList.get(position).getProductImage()))
                .into(viewHolder.productImage);

        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.delete_alertdialog_title);
                builder.setMessage(R.string.delete_alertdialog_content);
                builder.setPositiveButton(R.string.alertdialog_acceptButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Đã xóa"+ productList.get(position).getProductName(), Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton(R.string.alertdialog_cancelButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.show();
            }
        });

        viewHolder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.edit_alertdialog_title);
                builder.setMessage(R.string.edit_alertdialog_content);
                builder.setPositiveButton(R.string.alertdialog_acceptButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent toEditProductInformationPage = new Intent(getContext(), EditProductInStorePage.class);
                        toEditProductInformationPage.putExtra("productName", productList.get(position).getProductName());
                        toEditProductInformationPage.putExtra("productID", productList.get(position).getProductID());
                        toEditProductInformationPage.putExtra("storeID", storeID);
                        toEditProductInformationPage.putExtra("categoryName", productList.get(position).getCategoryName());
                        toEditProductInformationPage.putExtra("brandName", productList.get(position).getBrandName());
                        toEditProductInformationPage.putExtra("productPrice", productList.get(position).getProductPrice());
                        toEditProductInformationPage.putExtra("promotionPercent", productList.get(position).getPromotionPercent());
                        toEditProductInformationPage.putExtra("productImageLink", productList.get(position).getProductImage());
                        context.startActivity(toEditProductInformationPage);
                    }
                });
                builder.setNegativeButton(R.string.alertdialog_cancelButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.show();



            }
        });

        return convertView;
    }



    private static class ViewHolder {
        TextView productName;
        TextView productPrice;
        TextView productPromotion;
        ImageView productImage;
        ImageView deleteBtn;
        ImageView editBtn;

        public ViewHolder(View view) {
            productImage = (ImageView) view.findViewById(R.id.productImage);
            productName = (TextView) view.findViewById(R.id.productName);
            productPrice =(TextView) view.findViewById(R.id.productPrice);
            productPromotion =(TextView) view.findViewById(R.id.productPromotion);
            deleteBtn = (ImageView) view.findViewById(R.id.deleteBtn);
            editBtn = (ImageView) view.findViewById(R.id.editBtn);

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
