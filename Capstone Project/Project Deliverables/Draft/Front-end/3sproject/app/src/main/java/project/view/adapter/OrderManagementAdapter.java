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

import project.view.gui.OrderDetailPage;
import project.view.R;
import project.view.gui.StoreInformationPage;
import project.view.model.Order;
import project.view.model.OrderDetail;
import project.view.util.Formater;

public class OrderManagementAdapter extends ArrayAdapter<Order> {
    private Context context;
    private int resource;
    private List<Order> arrContact;
    private Formater formater;

    public OrderManagementAdapter(Context context, int resource, List<Order> arrContact) {
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
        final Order order = arrContact.get(position);

        viewHolder.tvStoreName.setText(order.getStoreName());
        viewHolder.tvPrice.setText(formater.formatDoubleToMoney(String.valueOf(order.getTotalPrice()+"")));

        viewHolder.tvOrderDate.setText(order.getDeliverTime().toString());

        viewHolder.tvStoreName.setPaintFlags(viewHolder.tvStoreName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        viewHolder.imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToOrderDetail = new Intent(context, OrderDetailPage.class);
                goToOrderDetail.putExtra("orderID",order.getOrderId());
                context.startActivity(goToOrderDetail);
            }
        });
        viewHolder.tvStoreName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"Go to product in store",Toast.LENGTH_SHORT).show();
                Intent goToProductInStore = new Intent(context, StoreInformationPage.class);
                goToProductInStore.putExtra("storeID",order.getStoreId());
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
