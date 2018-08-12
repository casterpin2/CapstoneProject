package project.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.List;

import project.firebase.Firebase;
import project.objects.User;
import project.view.gui.ProductDetailPage;
import project.view.model.CartDetail;
import project.view.model.Product;
import project.view.R;
import project.view.model.Store;
import project.view.util.Formater;

public class ProductInStoreByUserCustomCardViewAdapter extends RecyclerView.Adapter<ProductInStoreByUserCustomCardViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Product> productInStores;
    private StorageReference storageReference = Firebase.getFirebase();
    private Formater formater;
    private Store store;
    private Store myStore;
    private User user;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView promotionPercent, productName, storeName, originalPrice, promotionPrice;
        public ImageView productImage;
        public LinearLayout flagSaleLayout;
        public ImageView imgAddCard;
        public MyViewHolder(View view) {
            super(view);
            promotionPercent = (TextView) view.findViewById(R.id.promotionPercent);
            productName = (TextView) view.findViewById(R.id.productName);
            storeName = (TextView) view.findViewById(R.id.storeName);
            originalPrice = (TextView) view.findViewById(R.id.originalPrice);
            promotionPrice = (TextView) view.findViewById(R.id.promotionPrice);
            productImage = (ImageView) view.findViewById(R.id.productImage);
            imgAddCard = (ImageView) view.findViewById(R.id.imgAddCard);
            flagSaleLayout = view.findViewById(R.id.flagSaleLayout);
        }
    }


    public ProductInStoreByUserCustomCardViewAdapter(Context mContext, List<Product> productInStores, Store store) {
        this.mContext = mContext;
        this.productInStores = productInStores;
        this.store = store;
        database = FirebaseDatabase.getInstance();
        restoringPreferences();
        formater = new Formater();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_display_custom_cardview, parent, false);

        return new ProductInStoreByUserCustomCardViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {
        final  Product product = productInStores.get(position);
        holder.productName.setText(product.getProduct_name());
        holder.storeName.setVisibility(View.INVISIBLE);

        if(product.getPromotion()==0.0){
            holder.flagSaleLayout.setVisibility(View.INVISIBLE);
            holder.originalPrice.setVisibility(View.GONE);
        }else {
            holder.flagSaleLayout.setVisibility(View.VISIBLE );
            holder.originalPrice.setVisibility(View.VISIBLE);
            holder.promotionPercent.setText(formater.formatDoubleToInt( String.valueOf(product.getPromotion())));
            holder.originalPrice.setText(formater.formatDoubleToMoney( String.valueOf(product.getPrice())));
            holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.promotionPrice.setText(formater.formatDoubleToMoney( String.valueOf(product.getPrice() - (product.getPrice()* product.getPromotion()/100))));

        Glide.with(mContext /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(product.getImage_path()))
                .into(holder.productImage);

        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductDetailPage.class);
                //Product pro = new Product(productInStore.getProductID(),productInStore.getProductName(),productInStore.getBrandName(),productInStore.getDescription(),productInStore.getCategoryName(),productInStore.getTypeName(),productInStore.getProductImage(),productInStore.g);
                intent.putExtra("product",new Gson().toJson(product));
                intent.putExtra("isStoreProduct",true);
                intent.putExtra("isStoreSee",false);
                intent.putExtra("storeID",getStore().getId());
                mContext.startActivity(intent);
            }
        });
        holder.imgAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Thêm sản phẩm vào giỏ hàng");
                builder.setMessage("Bạn có chắc chắn muốn thêm sản phẩm này vào cửa hàng không?");

                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addProductToCart(product);
                        return;
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return productInStores.size();
    }

    private void restoringPreferences(){
        SharedPreferences pre = mContext.getSharedPreferences("authentication", Context.MODE_PRIVATE);
        String userJSON = pre.getString("user", "");
        user = new Gson().fromJson(userJSON,User.class);
        String storeJSON = pre.getString("store", "");
        myStore = new Gson().fromJson(storeJSON,Store.class);
    }

    private void addProductToCart(final Product product){
        if (store == null){
            Toast.makeText(mContext, "Có lỗi xảy ra!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (myStore.getId() == store.getId()){
            Toast.makeText(mContext, "Cửa hàng của bạn, không thể thêm vào giỏ hàng", Toast.LENGTH_LONG).show();
            return;
        }
        myRef = database.getReference().child("cart").child(String.valueOf(user.getId())).child(String.valueOf(store.getId()));
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    myRef.child("phone").setValue(String.valueOf(store.getPhone()));
                    myRef.child("storeId").setValue(store.getId());
                    myRef.child("storeName").setValue(String.valueOf(store.getName()));
                    myRef.child("image_path").setValue(String.valueOf(store.getImage_path()));
                    CartDetail cartDetail  = new CartDetail(product.getProduct_id(),product.getProduct_name(),1,product.getPrice(),product.getImage_path());
                    myRef.child("cartDetail").child(String.valueOf(product.getProduct_id())).setValue(cartDetail);
                    Toast.makeText(mContext, "Thêm sản phẩm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                } else {
                    myRef.child("cartDetail").child(String.valueOf(product.getProduct_id())).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()){
                                CartDetail cartDetail  = new CartDetail(product.getProduct_id(),product.getProduct_name(),1,product.getPrice(),product.getImage_path());
                                myRef.child("cartDetail").child(String.valueOf(product.getProduct_id())).setValue(cartDetail);
                                Toast.makeText(mContext, "Thêm sản phẩm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                myRef.child("cartDetail").child(String.valueOf(product.getProduct_id())).child("quantity").setValue((long)dataSnapshot.child("quantity").getValue()+1);
                                Toast.makeText(mContext, "Thêm sản phẩm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(mContext, "Có lỗi xảy ra!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
