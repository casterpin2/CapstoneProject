package project.view.OrderManagerment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import project.view.OrderManagerment.model.OrderDetail;
import project.view.R;

public class OrderManagementAdapter extends ArrayAdapter<OrderDetail> {
    private Context context;
    private int resource;
    private List<OrderDetail> arrContact;

    public OrderManagementAdapter(Context context, int resource, ArrayList<OrderDetail> arrContact) {
        super(context, resource, arrContact);
        this.context = context;
        this.resource = resource;
        this.arrContact = arrContact;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order_management, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvProductName = (TextView) convertView.findViewById(R.id.tvProductName);
            viewHolder.tvQuantity = (TextView) convertView.findViewById(R.id.tvQuantity);
            viewHolder.tvOrderDate = (TextView) convertView.findViewById(R.id.tvOrderDate);
            viewHolder.tvCusName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvNumberPhone = (TextView) convertView.findViewById(R.id.tvPhoneNumber);
            viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        OrderDetail contact = arrContact.get(position);

        viewHolder.tvProductName.setText(contact.getProductName());
        viewHolder.tvQuantity.setText(contact.getQuantity()+"");
        viewHolder.tvOrderDate.setText(contact.getOrderDate().toString());
        viewHolder.tvCusName.setText(contact.getName());
        viewHolder.tvNumberPhone.setText(contact.getPhoneNumber());
        viewHolder.tvAddress.setText(contact.getAddress());

        return convertView;
    }

    public class ViewHolder {
        TextView tvOrderCode, tvProductName,tvOrderDate, tvCusName, tvNumberPhone, tvQuantity,tvAddress;

    }
}
