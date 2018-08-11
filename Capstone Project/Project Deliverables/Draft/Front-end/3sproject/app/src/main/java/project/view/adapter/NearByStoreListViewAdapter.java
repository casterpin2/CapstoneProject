package project.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.List;

import project.firebase.Firebase;
import project.view.gui.MainActivity;
import project.view.R;
import project.view.gui.StoreInformationPage;
import project.view.model.NearByStore;
import project.view.util.Formater;

public class NearByStoreListViewAdapter extends BaseAdapter {


    private Context context;
    private List<NearByStore> storeList;
    private int layout;
    private StorageReference storageReference = Firebase.getFirebase();
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

    public NearByStoreListViewAdapter(Context context, int layout, List<NearByStore> storeList) {
        this.context = context;
        this.layout = layout;
        this.storeList = storeList;
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        final NearByStore store = storeList.get(position);
        if (convertView != null)
            return convertView;
        else {
            if (view == null) {
                LayoutInflater li;
                li = LayoutInflater.from(getContext());
                view = li.inflate(R.layout.nearby_store_page_custom_list_view, null);
                holder = new ViewHolder();
                holder.storeName = (TextView) view.findViewById(R.id.storeName);
                holder.storeAddress = (TextView) view.findViewById(R.id.storeAddress);
                holder.distance = (TextView) view.findViewById(R.id.distance);
                holder.productPrice = (TextView) view.findViewById(R.id.productPrice);
                holder.promotionPercent = (TextView) view.findViewById(R.id.promotionPercent);
                holder.saleProduct = (TextView) view.findViewById(R.id.salePrice);
                holder.orderBtn = (Button) view.findViewById(R.id.orderBtn);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
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
//                holder.promotionPercent.setText(formatDoubleToInt(context, String.valueOf(store.getPromotionPercent())));
                }
//                double salePriceDouble = store.getPrice() - (store.getPrice() * store.getPromotion() / 100);
//                long salePriceLong = (long) salePriceDouble;
//                holder.saleProduct.setText(Formater.formatDoubleToMoney(String.valueOf(salePriceLong)));

                holder.orderBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.putExtra("storeName", store.getName());
                        getContext().startActivity(intent);
                    }
                });
                holder.storeName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int storeID = store.getId();
                        Intent toStoreInformation = new Intent(getContext(), StoreInformationPage.class);
                        toStoreInformation.putExtra("storeID", storeID);
                        toStoreInformation.putExtra("storeName", store.getName());
                        getContext().startActivity(toStoreInformation);
                    }
                });
            }


        }
        return view;
    }

    public Context getContext() {
        return context;
    }

    public class ViewHolder{
        TextView storeName;
        TextView storeAddress;
        TextView distance;
        TextView productPrice;
        TextView promotionPercent;
        TextView saleProduct;
        Button orderBtn;

    }

}