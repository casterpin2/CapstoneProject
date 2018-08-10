package project.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Formatter;
import java.util.List;

import project.view.R;
import project.view.model.Order;
import project.view.model.OrderDetail;
import project.view.util.Formater;

public class UserOrderAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Order> list;
    private int userId;
    private boolean isFeedback;

    public UserOrderAdapter(Context context, List<Order> list, int userId) {
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
        return list.get(groupPosition).getOrderDetail().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Object[] orderDetails = list.get(groupPosition).getOrderDetail().values().toArray();
        return orderDetails[childPosition];
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
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.order_parent_list, null);
        }

        String storeName = ((Order) getGroup(groupPosition)).getStoreName();
        String orderDate = ((Order) getGroup(groupPosition)).getDeliverTime();
        double totalOrder = ((Order) getGroup(groupPosition)).getTotalPrice();

        RelativeLayout waittingOrderLayout = convertView.findViewById(R.id.waittingOrderLayout);
        RelativeLayout processingOrderLayout = convertView.findViewById(R.id.processingOrder);
        RelativeLayout doneOrderWithoutFeedbackLayout = convertView.findViewById(R.id.doneOrderWithoutFeedback);
        RelativeLayout doneOrderWithFeedbackLayout = convertView.findViewById(R.id.doneOrderWithFeedback);

        String orderStatus = ((Order) getGroup(groupPosition)).getStatus();

        if (orderStatus.equalsIgnoreCase("waitting")) {
            waittingOrderLayout.setVisibility(View.VISIBLE);
            processingOrderLayout.setVisibility(View.INVISIBLE);
            doneOrderWithoutFeedbackLayout.setVisibility(View.INVISIBLE);
            doneOrderWithFeedbackLayout.setVisibility(View.INVISIBLE);

            TextView waittingOrderStore = convertView.findViewById(R.id.waittingOrderStore);
            TextView waittingOrderOrderDate = convertView.findViewById(R.id.waittingOrderOrderDate);
            TextView waittingOrderTotalCast = convertView.findViewById(R.id.waittingOrderTotalCast);
            Button cancelBtn = convertView.findViewById(R.id.cancel_button);

            waittingOrderStore.setText(storeName);
            waittingOrderOrderDate.setText(orderDate);
            waittingOrderTotalCast.setText(Formater.formatDoubleToMoney(String.valueOf(totalOrder)));
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // hủy đơn hàng ở đây
                }
            });

        } else if (orderStatus.equalsIgnoreCase("processing")) {
            waittingOrderLayout.setVisibility(View.INVISIBLE);
            processingOrderLayout.setVisibility(View.VISIBLE);
            doneOrderWithoutFeedbackLayout.setVisibility(View.INVISIBLE);
            doneOrderWithFeedbackLayout.setVisibility(View.INVISIBLE);

            TextView processingOrderStore = convertView.findViewById(R.id.processingOrderStore);
            TextView processingOrderOrderDate = convertView.findViewById(R.id.processingOrderOrderDate);
            TextView processingOrderTotalCast = convertView.findViewById(R.id.processingOrderTotalCast);

            processingOrderStore.setText(storeName);
            processingOrderOrderDate.setText(orderDate);
            processingOrderTotalCast.setText(Formater.formatDoubleToMoney(String.valueOf(totalOrder)));

        } else if (orderStatus.equalsIgnoreCase("done")) {
            waittingOrderLayout.setVisibility(View.INVISIBLE);
            processingOrderLayout.setVisibility(View.INVISIBLE);
             if (isFeedback == true) {
                 doneOrderWithoutFeedbackLayout.setVisibility(View.INVISIBLE);
                 doneOrderWithFeedbackLayout.setVisibility(View.VISIBLE);

                 TextView doneOrderWithFeedbackStore = convertView.findViewById(R.id.doneOrderWithFeedbackStore);
                 TextView doneOrderWithFeedbackOrderDate = convertView.findViewById(R.id.doneOrderWithFeedbackOrderDate);
                 TextView doneOrderWithFeedbackTotalCast = convertView.findViewById(R.id.doneOrderWithFeedbackTotalCast);

                 doneOrderWithFeedbackStore.setText(storeName);
                 doneOrderWithFeedbackOrderDate.setText(orderDate);
                 doneOrderWithFeedbackTotalCast.setText(Formater.formatDoubleToMoney(String.valueOf(totalOrder)));

             } else {
                 doneOrderWithoutFeedbackLayout.setVisibility(View.VISIBLE);
                 doneOrderWithFeedbackLayout.setVisibility(View.INVISIBLE);

                 TextView doneOrderWithoutFeedbackStore = convertView.findViewById(R.id.doneOrderWithoutFeedbackStore);
                 TextView doneOrderWithoutFeedbackOrderDate = convertView.findViewById(R.id.doneOrderWithoutFeedbackOrderDate);
                 TextView doneOrderWithoutFeedbackTotalCast = convertView.findViewById(R.id.doneOrderWithoutFeedbackTotalCast);
                 Button feedbackBtn = convertView.findViewById(R.id.feedbackBtn);

                 doneOrderWithoutFeedbackStore.setText(storeName);
                 doneOrderWithoutFeedbackOrderDate.setText(orderDate);
                 doneOrderWithoutFeedbackTotalCast.setText(Formater.formatDoubleToMoney(String.valueOf(totalOrder)));
                 feedbackBtn.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         // đánh giá cửa hàng
                     }
                 });
             }
        }



        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.order_child_list, null);
        }

        ImageView productImage = convertView.findViewById(R.id.productImage);
        TextView productNameTV = convertView.findViewById(R.id.productName);
        TextView quantityTV = convertView.findViewById(R.id.quantity);
        TextView priceTV = convertView.findViewById(R.id.price);

//        String productImagePath = ((OrderDetail) getChild(groupPosition, childPosition)).get(); // ảnh cửa sản phẩm
        String productName = ((OrderDetail) getChild(groupPosition, childPosition)).getProductName();
        int quantity = ((OrderDetail) getChild(groupPosition, childPosition)).getProductQuantity();
        long price = ((OrderDetail) getChild(groupPosition, childPosition)).getFinalPrice();

        productNameTV.setText(productName);
        quantityTV.setText(String.valueOf(quantity));
        priceTV.setText(String.valueOf(price));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
