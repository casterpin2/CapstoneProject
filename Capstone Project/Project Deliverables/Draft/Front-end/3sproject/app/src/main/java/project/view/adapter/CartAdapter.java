package project.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import project.firebase.Firebase;
import project.view.R;
import project.view.gui.StoreInformationPage;
import project.view.model.Cart;
import project.view.model.CartDetail;
import project.view.model.StoreInformation;

public class CartAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Cart> list;
    private double totalPrice = 0;
    private StorageReference storageReference = Firebase.getFirebase();
    private int userId;

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Context getContext() {
        return context;
    }

    public String getTotalPrice() {
        totalPrice = 0;
        for (Cart cart : list){
            Object[] cartDetails = cart.getCartDetail().values().toArray();
            double count = 0;
            for(int i = 0 ; i < cartDetails.length;i++) {
                CartDetail cartDetail = (CartDetail)cartDetails[i];
                count += cartDetail.getUnitPrice() * cartDetail.getQuantity();
            }
            totalPrice += count;
        }
        return formatDoubleToMoney(String.valueOf(totalPrice));
    }

    public CartAdapter(Context context, List<Cart> list, int userId) {
        this.context = context;
        this.list = list;
        this.userId = userId;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).getCartDetail().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Object[] cartDetails = list.get(groupPosition).getCartDetail().values().toArray();
        return cartDetails[childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View convertView, ViewGroup viewGroup) {
        String storeName = ((Cart) getGroup(groupPosition)).getStoreName();
        final int storeId = ((Cart) getGroup(groupPosition)).getStoreId();
//        total += totalProduct;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.parent_list, null);
        }
        final ImageView storeImage = (ImageView) convertView.findViewById(R.id.storeAvatar);
        Glide.with(context /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(((Cart) getGroup(groupPosition)).getImage_path()))
                //.skipMemoryCache(true)
                .into(storeImage);
        TextView storeNameTV = (TextView) convertView.findViewById(R.id.storeName);
        storeNameTV.setText(storeName);
        storeNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(getContext(), StoreInformationPage.class).putExtra("storeID",storeId));
            }
        });
        TextView totalTV = (TextView) convertView.findViewById(R.id.totalStoreOrder);
        double count = 0;
        Object[] cartDetails = list.get(groupPosition).getCartDetail().values().toArray();
        for(int i = 0 ; i < cartDetails.length;i++) {
            CartDetail cartDetail = (CartDetail)cartDetails[i];
            count += cartDetail.getUnitPrice() * cartDetail.getQuantity();
        }
        //totalPrice = 0;
        totalPrice += count;
        totalTV.setText(formatDoubleToMoney(String.valueOf(count)));
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        final int storeId = ((Cart) getGroup(groupPosition)).getStoreId();
        final int productId = ((CartDetail) getChild(groupPosition, childPosition)).getProductId();
        final String productName = ((CartDetail) getChild(groupPosition, childPosition)).getProductName();
        final int quantity = ((CartDetail) getChild(groupPosition, childPosition)).getQuantity();
        final String productImagePath = ((CartDetail) getChild(groupPosition, childPosition)).getImage_path();
        double price = ((CartDetail) getChild(groupPosition, childPosition)).getUnitPrice();


        //totalProduct = price * quantityLong;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_list, null);
        }

        TextView productNameTV = (TextView) convertView.findViewById(R.id.productName);
        productNameTV.setText(productName);
        final TextView quantityTv = (TextView) convertView.findViewById(R.id.quantity);
        quantityTv.setText(String.valueOf(quantity));
        TextView priceTv = (TextView) convertView.findViewById(R.id.price);
        priceTv.setText(formatDoubleToMoney(String.valueOf(price)));
        final Button decreaseBtn = (Button) convertView.findViewById(R.id.decreaseBtn);
        final ImageView productImage = (ImageView) convertView.findViewById(R.id.productImage);
        Glide.with(context /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(productImagePath))
                //.skipMemoryCache(true)
                .into(productImage);
        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseQuantity(quantityTv, decreaseBtn);
                if (quantity == 1) return;
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("cart").child(String.valueOf(userId));
                ((CartDetail) getChild(groupPosition, childPosition)).setQuantity(quantity-1);
                myRef.child(String.valueOf(storeId)).child("cartDetail").child(String.valueOf(productId)).child("quantity").setValue(quantity-1);
            }
        });

        Button increaseBtn = (Button) convertView.findViewById(R.id.increaseBtn);
        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseQuantity(quantityTv);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("cart").child(String.valueOf(userId));
                ((CartDetail) getChild(groupPosition, childPosition)).setQuantity(quantity+1);
                myRef.child(String.valueOf(storeId)).child("cartDetail").child(String.valueOf(productId)).child("quantity").setValue(quantity+1);
            }
        });

        ImageButton deleteBtn = (ImageButton) convertView.findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // xóa sản phẩm khỏi cart
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xóa sản phẩm");
                builder.setMessage("Bạn có muốn xóa sản phẩm này khỏi cửa hàng?");

                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference myRef = database.getReference().child("cart").child(String.valueOf(userId));
                        myRef.child(String.valueOf(storeId)).child("cartDetail").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount() == 1) {
                                    myRef.child(String.valueOf(storeId)).removeValue();
                                } else {
                                    myRef.child(String.valueOf(storeId)).child("cartDetail").child(String.valueOf(productId)).removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });
                builder.show();
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public static String formatDoubleToMoney(String price) {

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

    public void increaseQuantity(TextView quantityTV) {
        int quantity = Integer.parseInt(quantityTV.getText().toString());
        quantity = quantity + 1;
        quantityTV.setText(String.valueOf(quantity));
//        sumOrder.setText(Formater.formatDoubleToMoney(String.valueOf((getSalesPrice(productDetail.getProductPrice(), productDetail.getPromotionPercent()))* quantity)));
    }

    public void decreaseQuantity(TextView quantityTV, Button decreaseBtn) {
        int quantity = Integer.parseInt(quantityTV.getText().toString());

        if (quantity <= 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Hủy sản phẩm");
            builder.setMessage("Số lượng sản phẩm bạn chọn là 1. Không thể giảm?");

            builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   return;
                }
            });
            builder.show();
            return;
            //decreaseBtn.setEnabled(false);
        }
            quantity = quantity - 1;
            quantityTV.setText(String.valueOf(quantity));
//        sumOrder.setText(Formater.formatDoubleToMoney(String.valueOf((getSalesPrice(productDetail.getProductPrice(), productDetail.getPromotionPercent()))* quantity)));

    }

}
