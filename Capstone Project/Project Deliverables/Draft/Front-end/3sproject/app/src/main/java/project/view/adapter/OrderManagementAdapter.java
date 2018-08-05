package project.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import project.view.gui.OrderDetailManagementPage;
import project.view.gui.ProductInStoreByUserDisplayPage;
import project.view.R;
import project.view.model.OrderDetail;
import project.view.util.Formater;

public class OrderManagementAdapter extends ArrayAdapter<OrderDetail> {
    private Context context;
    private int resource;
    private List<OrderDetail> arrContact;
    private Formater formater;

    public OrderManagementAdapter(Context context, int resource, ArrayList<OrderDetail> arrContact) {
        super(context, resource, arrContact);
        this.context = context;
        this.resource = resource;
        this.arrContact = arrContact;
        formater = new Formater();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order_management, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvStoreName = (TextView) convertView.findViewById(R.id.tvStoreName);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
            viewHolder.tvOrderDate = (TextView) convertView.findViewById(R.id.tvOrderDate);
            viewHolder.imgInfo = convertView.findViewById(R.id.imgInfo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final OrderDetail order = arrContact.get(position);

        viewHolder.tvStoreName.setText(order.getStoreName());
        viewHolder.tvPrice.setText(formater.formatDoubleToMoney(String.valueOf(order.getFinalPrice()+"")));

        viewHolder.tvOrderDate.setText(order.getOrderDateTime().toString());

        viewHolder.tvStoreName.setPaintFlags(viewHolder.tvStoreName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        viewHolder.imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToOrderDetail = new Intent(context, OrderDetailManagementPage.class);
                goToOrderDetail.putExtra("orderID",order.getOrderID());
                context.startActivity(goToOrderDetail);
            }
        });
        viewHolder.tvStoreName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"Go to product in store",Toast.LENGTH_SHORT).show();
                Intent goToProductInStore = new Intent(context, ProductInStoreByUserDisplayPage.class);
                goToProductInStore.putExtra("storeID",order.getStoreID());
                context.startActivity(goToProductInStore);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        TextView tvStoreName, tvPrice,tvOrderDate;
        ImageView imgInfo;
    }
}
