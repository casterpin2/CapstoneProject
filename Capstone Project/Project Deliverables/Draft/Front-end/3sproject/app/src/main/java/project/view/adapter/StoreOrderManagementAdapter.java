package project.view.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import project.view.model.OrderDetail;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;


import project.view.gui.OrderDetailManagement;
import project.view.R;
import project.view.util.Formater;


public class StoreOrderManagementAdapter extends ArrayAdapter<OrderDetail> {

        private int resource;
        private Context context;
        private List<OrderDetail> orderDetails ;
        private Formater formater;

        public StoreOrderManagementAdapter(@NonNull Context context, int resource, @NonNull List<OrderDetail> orderDetails) {
            super(context, resource, orderDetails);
            this.resource =  resource;
            this.context = context;
            this.orderDetails = orderDetails;
            formater = new Formater();
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_store_order_management, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tvCusName = convertView.findViewById(R.id.tvCusName);
                viewHolder.tvAddress = convertView.findViewById(R.id.tvAddress);
                viewHolder.tvOrderDate = convertView.findViewById(R.id.tvOrderDate);
                viewHolder.tvPrice = convertView.findViewById(R.id.tvPrice);
                viewHolder.btnDetail = convertView.findViewById(R.id.btnDetail);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (StoreOrderManagementAdapter.ViewHolder) convertView.getTag();
            }
            final OrderDetail order = orderDetails.get(position);

            viewHolder.tvCusName.setText(order.getName());
            viewHolder.tvAddress.setText(order.getAddress());
            viewHolder.tvOrderDate.setText(order.getOrderDate().toString());
            viewHolder.tvPrice.setText(formater.formatDoubleToMoney(order.getPrice().toString()));

            viewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent goToOrderDetail = new Intent(context, OrderDetailManagement.class);
                    goToOrderDetail.putExtra("orderID",order.getOrderCode());
                    context.startActivity(goToOrderDetail);
                }
            });
            return convertView;
        }

        public class ViewHolder {
            TextView tvCusName, tvAddress,tvOrderDate,tvPrice;
            Button btnDetail;
        }
    }
