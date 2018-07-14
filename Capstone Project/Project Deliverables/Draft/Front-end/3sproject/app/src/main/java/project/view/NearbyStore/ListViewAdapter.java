package project.view.NearbyStore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import project.firebase.Firebase;
import project.view.MainActivity;
import project.view.R;

public class ListViewAdapter extends BaseAdapter {


    private Context context;
    private List<NearbyStore> storeList;
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

    public ListViewAdapter(Context context, int layout, List<NearbyStore> storeList) {
        this.context = context;
        this.layout = layout;
        this.storeList = storeList;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        final NearbyStore store = storeList.get(position);
        if (view  == null){
            LayoutInflater li;
            li = LayoutInflater.from(getContext());
            view = li.inflate(R.layout.nearby_store_page_custom_list_view, null);
            holder = new ViewHolder();
            holder.storeName = (TextView) view.findViewById(R.id.storeName);
            holder.storeAddress = (TextView) view.findViewById(R.id.storeAddress);
            holder.distance =(TextView) view.findViewById(R.id.distance);
            holder.productPrice =(TextView) view.findViewById(R.id.productPrice);
            holder.promotionPercent = (TextView) view.findViewById(R.id.promotionPercent);
            holder.test = (LinearLayout) view.findViewById(R.id.test);
            holder.orderBtn = (Button) view.findViewById(R.id.orderBtn);



            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();

        }
        //Item product = productList.get(position);

        int width = holder.test.getWidth();


        if(store != null) {

            holder.storeName.setText(store.getStoreName());
            holder.storeAddress.setText(store.getStoreAddress());
            holder.distance.setText(String.valueOf(store.getDistance())+ " km");


            if(store.getPromotionPercent() > 0.0){
                holder.promotionPercent.setVisibility(View.VISIBLE);
                holder.promotionPercent.setText(formatDoubleToString(context, String.valueOf(store.getPromotionPercent())));
                double salePriceDouble = store.getProductPrice() * store.getPromotionPercent() / 100;
                long salePriceLong = (long) salePriceDouble;
                long displayPrice = store.getProductPrice() - salePriceLong ;
                holder.productPrice.setText(formatLongToMoney(context, String.valueOf(displayPrice)));
            } else if (store.getPromotionPercent() == 0.0){
                holder.promotionPercent.setVisibility(View.INVISIBLE);
                holder.productPrice.setText(formatLongToMoney(context, String.valueOf(store.getProductPrice())));
//                holder.promotionPercent.setText(formatDoubleToInt(context, String.valueOf(store.getPromotionPercent())));
            }

//            if (isSale == false){
//
//            } else {
//
//            }

            holder.orderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.putExtra("storeName", store.getStoreName());
                    getContext().startActivity(intent);
                }
            });
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
        LinearLayout test;
        Button orderBtn;

    }

    public static String formatLongToMoney(Context context, String price) {

        DecimalFormat formatter = new DecimalFormat("###,###,###");

        price = (!TextUtils.isEmpty(price)) ? price : "0";
        price = price.trim();
        price = formatter.format(Double.parseDouble(price));
        price = price.replaceAll("\\.", "\\,");

        price = String.format("%s đ", price);
        return price;
    }

    public static String formatDoubleToString(Context context, String price) {

        NumberFormat format =
                new DecimalFormat("#,##0.0");// #,##0.00 ¤ (¤:// Currency symbol)
        format.setCurrency(Currency.getInstance(Locale.US));//Or default locale

        price = (!TextUtils.isEmpty(price)) ? price : "0";
        price = price.trim();
        price = format.format(Double.parseDouble(price));
        price = price.replaceAll(",", "\\.");

        if (price.endsWith(".0")) {
            int centsIndex = price.lastIndexOf(".0");
            if (centsIndex != -1) {
                price = price.substring(0, centsIndex);
            }
        }
        price = String.format("%s%%", price);
        return price;
    }


}