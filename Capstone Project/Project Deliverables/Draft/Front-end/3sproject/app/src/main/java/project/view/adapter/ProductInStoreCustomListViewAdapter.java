package project.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import project.firebase.Firebase;
import project.retrofit.ApiUtils;
import project.view.gui.EditProductInStorePage;
import project.view.R;
import project.view.gui.ProductDetailPage;
import project.view.gui.SaleProductDisplayPage;
import project.view.model.Cart;
import project.view.model.CartDetail;
import project.view.model.Product;
import project.view.util.Formater;
import retrofit2.Call;
import retrofit2.Response;


public class ProductInStoreCustomListViewAdapter extends ArrayAdapter<Product> {
    private Context context;
    private List<Product> productList;
    private StorageReference storageReference = Firebase.getFirebase();
    private int storeID;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;

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

    public ProductInStoreCustomListViewAdapter(@NonNull Context context, int resource, @NonNull List<Product> productList) {
        super(context, resource, productList);
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ProductInStoreViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.product_in_store_custom_listview, parent, false);
            viewHolder = new ProductInStoreViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ProductInStoreViewHolder)convertView.getTag();
        }
        viewHolder.loadingBar.getIndeterminateDrawable().setColorFilter(getContext().getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);
        viewHolder.productName.setText(productList.get(position).getProduct_name());
        viewHolder.productPromotion.setText(productList.get(position).getPromotion()+" %");
        viewHolder.productPrice.setText(Formater.formatDoubleToMoney(String.valueOf(productList.get(position).getPrice())));
        viewHolder.editBtn.setTag(productList.get(position).getProduct_id());
        if (viewHolder.productName.getLineCount() > 3) {
            int lineEndIndex = viewHolder.productName.getLayout().getLineEnd(2);
            String text = viewHolder.productName.getText().subSequence(0, lineEndIndex - 3) + "...";
            viewHolder.productName.setText(text);
        }

        Glide.with(context /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(productList.get(position).getImage_path()))
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
                        viewHolder.loadingBar.setVisibility(View.VISIBLE);
                        viewHolder.deleteBtn.setVisibility(View.INVISIBLE);
                        if (!isNetworkAvailable()) {
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "Có lỗi xảy ra. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                                    viewHolder.loadingBar.setVisibility(View.INVISIBLE);
                                    viewHolder.deleteBtn.setVisibility(View.VISIBLE);
                                }
                            },5000);
                            return;
                        }
                        viewHolder.loadingBar.setVisibility(View.INVISIBLE);
                        viewHolder.deleteBtn.setVisibility(View.VISIBLE);
                        myRef = database.getReference().child("cart");
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                    String userId = dataSnapshot1.getKey();
                                    for (DataSnapshot dataSnapshot2 :dataSnapshot1.getChildren()){
                                        long storeCount = dataSnapshot2.getChildrenCount();
                                        Cart cart = dataSnapshot2.getValue(Cart.class);
                                        Object[] cartDetails = cart.getCartDetail().values().toArray();
                                        for(int i = 0 ; i < cartDetails.length;i++) {
                                            String productId = String.valueOf(((CartDetail)cartDetails[i]).getProductId());
                                            if (((CartDetail)cartDetails[i]).getProductId() == productList.get(position).getProduct_id()){
                                                if (cartDetails.length == 1) {
                                                    myRef.child(userId).child(String.valueOf(cart.getStoreId())).removeValue();
                                                }
                                                if (cartDetails.length == 1 && storeCount == 1) {
                                                    myRef.child(userId).removeValue();
                                                } else {
                                                    myRef.child(userId).child(String.valueOf(cart.getStoreId())).child("cartDetail").child(productId).removeValue();
                                                }
                                            }
                                        }
                                    }
                                }
                                Call<Boolean> call = ApiUtils.getAPIService().deleteProductInStore(storeID,productList.get(position).getProduct_id());
                                new DeleteProduct(position,viewHolder).execute(call);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(context,"Có lỗi xảy ra !!!",Toast.LENGTH_LONG).show();
                            }
                        });
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
                toEditProductInformationPage.putExtra("productName", productList.get(position).getProduct_name());
                toEditProductInformationPage.putExtra("productID", productList.get(position).getProduct_id());
                toEditProductInformationPage.putExtra("storeID", storeID);
                toEditProductInformationPage.putExtra("categoryName", productList.get(position).getCategory_name());
                toEditProductInformationPage.putExtra("brandName", productList.get(position).getBrand_name());
                toEditProductInformationPage.putExtra("productPrice", productList.get(position).getPrice());
                toEditProductInformationPage.putExtra("promotionPercent", productList.get(position).getPromotion());
                toEditProductInformationPage.putExtra("productImageLink", productList.get(position).getImage_path());
                context.startActivity(toEditProductInformationPage);

            }
        });
        final Product product = productList.get(position);
        viewHolder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isStoreProduct = true;
                Intent toProductDetail = new Intent(getContext(), ProductDetailPage.class);
                toProductDetail.putExtra("product",new Gson().toJson(product));
                toProductDetail.putExtra("isStoreProduct",isStoreProduct);
                toProductDetail.putExtra("isStoreSee",true);
                getContext().startActivity(toProductDetail);
            }
        });
        return convertView;
    }

    private static class ProductInStoreViewHolder {
        TextView productName;
        TextView productPrice;
        TextView productPromotion;
        ImageView productImage;
        ImageView deleteBtn;
        ImageView editBtn;
        ProgressBar loadingBar;
        public ProductInStoreViewHolder(View view) {
            productImage = (ImageView) view.findViewById(R.id.productImage);
            productName = (TextView) view.findViewById(R.id.productName);
            productPrice =(TextView) view.findViewById(R.id.productPrice);
            productPromotion =(TextView) view.findViewById(R.id.productPromotion);
            deleteBtn = (ImageView) view.findViewById(R.id.deleteBtn);
            editBtn = (ImageView) view.findViewById(R.id.editBtn);
            loadingBar = (ProgressBar) view.findViewById(R.id.loadingBar);
        }
    }

    public class DeleteProduct extends AsyncTask<Call, Void, Boolean> {
        private ProductInStoreViewHolder viewHolder ;

        public ProductInStoreViewHolder getViewHolder() {
            return viewHolder;
        }

        private int index;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public DeleteProduct(int index, ProductInStoreViewHolder viewHolder) {
            this.index = index;
            this.viewHolder = viewHolder;
        }

        @Override
        protected void onPreExecute() {
            getViewHolder().loadingBar.setVisibility(View.VISIBLE);
            getViewHolder().deleteBtn.setVisibility(View.INVISIBLE);
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
                viewHolder.loadingBar.setVisibility(View.INVISIBLE);
                viewHolder.deleteBtn.setVisibility(View.VISIBLE);
                Toast.makeText(context,"Xóa sản phẩm thành công",Toast.LENGTH_LONG).show();
                productList.remove(index);
                ProductInStoreCustomListViewAdapter.this.notifyDataSetChanged();
            }
            super.onPostExecute(result);

        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
