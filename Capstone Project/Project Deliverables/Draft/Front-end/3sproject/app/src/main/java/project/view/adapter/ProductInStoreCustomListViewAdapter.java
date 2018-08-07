package project.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import project.firebase.Firebase;
import project.retrofit.ApiUtils;
import project.view.gui.EditProductInStorePage;
import project.view.R;
import project.view.gui.ProductDetailPage;
import project.view.model.Product;
import project.view.model.ProductInStore;
import project.view.util.Formater;
import retrofit2.Call;
import retrofit2.Response;


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
        viewHolder.productPrice.setText(Formater.formatDoubleToMoney(String.valueOf(productList.get(position).getProductPrice())));
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
                        Log.d("storeid",String.valueOf(storeID));
                        Call<Boolean> call = ApiUtils.getAPIService().deleteProductInStore(storeID,productList.get(position).getProductID());
                        new DeleteProduct(position).execute(call);
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
        final ProductInStore productInStore = productList.get(position);
        final Product p = new Product(productInStore.getProductID(),productInStore.getProductName(),productInStore.getBrandName(),productInStore.getDescription(),"",productInStore.getTypeName(),productInStore.getProductImage(),productInStore.getProductPrice(),productInStore.getPromotionPercent());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isStoreProduct = true;
                Intent toProductDetail = new Intent(getContext(), ProductDetailPage.class);
                toProductDetail.putExtra("product",new Gson().toJson(p));
                toProductDetail.putExtra("isStoreProduct",isStoreProduct);
                toProductDetail.putExtra("isStoreSee",true);
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

    public class DeleteProduct extends AsyncTask<Call, Void, Boolean> {

        private int index;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public DeleteProduct(int index) {
            this.index = index;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Call... calls) {
            try {
                Call<Boolean> call = calls[0];
                Response<Boolean> re = call.execute();
                boolean result = re.body();
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if (result == null) {
                Toast.makeText(context,"Không có mạng",Toast.LENGTH_LONG).show();
                return;
            }
            if (!result){
                Toast.makeText(context,"Có lỗi xảy ra !!!",Toast.LENGTH_LONG).show();
                return;
            } else {
                productList.remove(index);
                ProductInStoreCustomListViewAdapter.this.notifyDataSetChanged();
            }
            super.onPostExecute(result);

        }
    }
}
