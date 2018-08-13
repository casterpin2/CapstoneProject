package project.view.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

import project.view.R;
import project.view.gui.OrderDetailPage;
import project.view.model.OrderDetail;
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
                viewHolder.tvCustomerName = convertView.findViewById(R.id.tvCustomerName);
                viewHolder.tvDeliveryAddress = convertView.findViewById(R.id.tvDeliveryAddress);
                viewHolder.tvOrderDate = convertView.findViewById(R.id.tvOrderDate);
                viewHolder.tvTotalOrder = convertView.findViewById(R.id.tvTotalOrder);
                viewHolder.btnDetail = convertView.findViewById(R.id.btnDetail);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (StoreOrderManagementAdapter.ViewHolder) convertView.getTag();
            }
            final OrderDetail order = orderDetails.get(position);

            viewHolder.tvCustomerName.setText(order.getUserName());
            viewHolder.tvDeliveryAddress.setText(order.getAddress());
            viewHolder.tvOrderDate.setText(order.getOrderDateTime().toString());
            viewHolder.tvTotalOrder.setText(formater.formatDoubleToMoney(order.getFinalPrice()+""));

            viewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent goToOrderDetail = new Intent(context, OrderDetailPage.class);
                    goToOrderDetail.putExtra("orderID",order.getOrderID());
                    context.startActivity(goToOrderDetail);
                }
            });
            return convertView;
        }

        public class ViewHolder {
            TextView tvCustomerName, tvDeliveryAddress,tvOrderDate,tvTotalOrder;
            Button btnDetail;
        }
    }
