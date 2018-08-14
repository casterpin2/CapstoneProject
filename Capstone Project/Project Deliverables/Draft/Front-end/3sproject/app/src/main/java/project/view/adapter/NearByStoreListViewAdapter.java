package project.view.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.List;

import project.firebase.Firebase;
import project.objects.User;
import project.view.R;
import project.view.gui.OrderPage;
import project.view.gui.ProductInStoreByUserDisplayPage;
import project.view.model.NearByStore;
import project.view.model.Product;
import project.view.model.Store;
import project.view.util.Formater;

public class NearByStoreListViewAdapter extends BaseAdapter {


    private Context context;
    private List<NearByStore> storeList;
    private int layout;
    private StorageReference storageReference = Firebase.getFirebase();
    private Product product;
    private Store store;
    private Store myStore;
    private User user;
    @Override
    public int getCount() {
        return storeList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public NearByStoreListViewAdapter(Context context, int layout, List<NearByStore> storeList,Product product) {

        this.context = context;
        this.layout = layout;
        this.storeList = storeList;
        this.product = product;
        restoringPreferences();
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        NearbyStorePageViewHolder holder;
        final NearByStore store = storeList.get(position);
            if (view == null) {
                LayoutInflater li;
                li = LayoutInflater.from(getContext());
                view = li.inflate(R.layout.nearby_store_page_custom_list_view, null);
                holder = new NearbyStorePageViewHolder();
                holder.storeName = (TextView) view.findViewById(R.id.storeName);
                holder.storeAddress = (TextView) view.findViewById(R.id.storeAddress);
                holder.distance = (TextView) view.findViewById(R.id.distance);
                holder.productPrice = (TextView) view.findViewById(R.id.productPrice);
                holder.promotionPercent = (TextView) view.findViewById(R.id.promotionPercent);
                holder.saleProduct = (TextView) view.findViewById(R.id.salePrice);
                holder.orderBtn = (Button) view.findViewById(R.id.orderBtn);

                view.setTag(holder);
            } else {
                holder = (NearbyStorePageViewHolder) view.getTag();
            }

            if (store != null) {
                holder.storeName.setText(store.getName());
                holder.storeAddress.setText(store.getAddress());
                holder.distance.setText(String.valueOf(store.getDistance()) + " km");


                if (store.getPromotion() > 0.0) {
                    holder.promotionPercent.setVisibility(View.VISIBLE);
                    holder.promotionPercent.setText(Formater.formatDoubleToInt(String.valueOf(store.getPromotion())));
                    long displayPrice = (long) store.getPrice();
                    holder.productPrice.setText(Formater.formatDoubleToMoney(String.valueOf(displayPrice)));
                    holder.productPrice.setPaintFlags(holder.productPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

                    double salePriceDouble = store.getPrice() - (store.getPrice() * store.getPromotion() / 100);
                    long salePriceLong = (long) salePriceDouble;
                    holder.saleProduct.setText(Formater.formatDoubleToMoney(String.valueOf(salePriceLong)));
                } else if (store.getPromotion() == 0.0) {
                    holder.promotionPercent.setVisibility(View.INVISIBLE);
                    holder.productPrice.setText("");
                    holder.saleProduct.setText(Formater.formatDoubleToMoney(String.valueOf(store.getPrice())));
                }
                holder.orderBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (myStore!= null ){
                            if (myStore.getId() == store.getId()) {
                                Toast.makeText(context, "Cửa hàng của bạn, không thể đặt hàng", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                        Intent intent = new Intent(getContext(), OrderPage.class);
                        intent.putExtra("isCart", false);
                        intent.putExtra("product",new Gson().toJson(product));
                        intent.putExtra("price",store.getPrice());
                        intent.putExtra("promotion",store.getPromotion());
                        intent.putExtra("storeID", store.getId());
                        intent.putExtra("storeName", store.getName());
                        intent.putExtra("phone", store.getPhone());
                        intent.putExtra("image_path", store.getImage_path());
                        ((Activity) getContext()).startActivityForResult(intent,2);
                    }
                });
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int storeID = store.getId();
                        Intent toProductInStoreByUser = new Intent(getContext(), ProductInStoreByUserDisplayPage.class);
                        toProductInStoreByUser.putExtra("storeID", storeID);
                        toProductInStoreByUser.putExtra("storeName", store.getName());
                        toProductInStoreByUser.putExtra("phone", store.getPhone());
                        toProductInStoreByUser.putExtra("image_path", store.getImage_path());
                        getContext().startActivity(toProductInStoreByUser);
                    }
                });

        }
        return view;
    }

    public Context getContext() {
        return context;
    }

    public class NearbyStorePageViewHolder {
        TextView storeName;
        TextView storeAddress;
        TextView distance;
        TextView productPrice;
        TextView promotionPercent;
        TextView saleProduct;
        Button orderBtn;

    }
    private void restoringPreferences(){
        SharedPreferences pre = context.getSharedPreferences("authentication", Context.MODE_PRIVATE);
        String userJSON = pre.getString("user", "");
        user = new Gson().fromJson(userJSON,User.class);
        String storeJSON = pre.getString("store", "");
        myStore = new Gson().fromJson(storeJSON,Store.class);
    }
}