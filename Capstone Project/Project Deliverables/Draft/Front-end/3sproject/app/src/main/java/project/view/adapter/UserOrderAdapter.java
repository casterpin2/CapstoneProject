package project.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.Formatter;
import java.util.List;

import project.firebase.Firebase;
import project.view.R;
import project.view.gui.CartPage;
import project.view.gui.UserFeedbackPage;
import project.view.model.CartDetail;
import project.view.model.Order;
import project.view.model.OrderDetail;
import project.view.model.Product;
import project.view.util.Formater;

public class UserOrderAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Order> list;
    private int userId;
    private boolean isFeedback;
    private StorageReference storageReference = Firebase.getFirebase();
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
    public View getGroupView(final int groupPosition, boolean b, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.order_parent_list, null);
        }

        String storeName = ((Order) getGroup(groupPosition)).getStoreName();
        String orderDate = ((Order) getGroup(groupPosition)).getDeliverTime();
        double totalOrder = ((Order) getGroup(groupPosition)).getTotalPrice();
        final String orderId = ((Order) getGroup(groupPosition)).getOrderId();
        RelativeLayout waittingOrderLayout = convertView.findViewById(R.id.waittingOrderLayout);
        RelativeLayout processingOrderLayout = convertView.findViewById(R.id.processingOrder);
        RelativeLayout doneOrderWithoutFeedbackLayout = convertView.findViewById(R.id.doneOrderWithoutFeedback);
        RelativeLayout doneOrderWithFeedbackLayout = convertView.findViewById(R.id.doneOrderWithFeedback);

        String orderStatus = ((Order) getGroup(groupPosition)).getStatus();
        final int storeId = ((Order) getGroup(groupPosition)).getStoreId();
        if (orderStatus.equalsIgnoreCase("waitting")) {
            waittingOrderLayout.setVisibility(View.VISIBLE);
            processingOrderLayout.setVisibility(View.INVISIBLE);
            doneOrderWithoutFeedbackLayout.setVisibility(View.INVISIBLE);
            doneOrderWithFeedbackLayout.setVisibility(View.INVISIBLE);

            TextView waittingOrderStore = convertView.findViewById(R.id.waittingOrderStoreName);
            TextView waittingOrderOrderDate = convertView.findViewById(R.id.waittingOrderOrderDate);
            TextView waittingOrderTotalCast = convertView.findViewById(R.id.waittingOrderTotalCast);
            TextView cancelBtn = convertView.findViewById(R.id.cancelBtn);

            waittingOrderStore.setText(storeName);
            waittingOrderOrderDate.setText(orderDate);
            waittingOrderTotalCast.setText(Formater.formatDoubleToMoney(String.valueOf(totalOrder)));
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Hủy đơn hàng");
                    builder.setMessage("Bạn có chắc chắn muốn hủy đơn hàng này không?");

                    builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final DatabaseReference myRef = database.getReference().child("ordersUser").child(String.valueOf(userId)).child(orderId);
                            final DatabaseReference myRef1 = database.getReference().child("ordersStore").child(String.valueOf(storeId)).child(orderId);
                            myRef1.removeValue();
                            myRef.removeValue();
                            return;
                        }
                    });

                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    builder.show();
                    // hủy đơn hàng ở đây
                }
            });

        } else if (orderStatus.equalsIgnoreCase("processing")) {
            waittingOrderLayout.setVisibility(View.INVISIBLE);
            processingOrderLayout.setVisibility(View.VISIBLE);
            doneOrderWithoutFeedbackLayout.setVisibility(View.INVISIBLE);
            doneOrderWithFeedbackLayout.setVisibility(View.INVISIBLE);

            TextView processingOrderStore = convertView.findViewById(R.id.processingOrderStoreName);
            TextView processingOrderOrderDate = convertView.findViewById(R.id.processingOrderOrderDate);
            TextView processingOrderTotalCast = convertView.findViewById(R.id.processingOrderTotalCast);

            processingOrderStore.setText(storeName);
            processingOrderOrderDate.setText(orderDate);
            processingOrderTotalCast.setText(Formater.formatDoubleToMoney(String.valueOf(totalOrder)));

        } else if (orderStatus.equalsIgnoreCase("done")) {
            waittingOrderLayout.setVisibility(View.INVISIBLE);
            processingOrderLayout.setVisibility(View.INVISIBLE);
             if (((Order) getGroup(groupPosition)).getIsFeedback().equalsIgnoreCase("true")) {
                 doneOrderWithoutFeedbackLayout.setVisibility(View.INVISIBLE);
                 doneOrderWithFeedbackLayout.setVisibility(View.VISIBLE);

                 TextView doneOrderWithFeedbackStore = convertView.findViewById(R.id.doneOrderWithFeedbackStoreName);
                 TextView doneOrderWithFeedbackOrderDate = convertView.findViewById(R.id.doneOrderWithFeedbackOrderDate);
                 TextView doneOrderWithFeedbackTotalCast = convertView.findViewById(R.id.doneOrderWithFeedbackTotalCast);

                 doneOrderWithFeedbackStore.setText(storeName);
                 doneOrderWithFeedbackOrderDate.setText(orderDate);
                 doneOrderWithFeedbackTotalCast.setText(Formater.formatDoubleToMoney(String.valueOf(totalOrder)));

             } else {
                 doneOrderWithoutFeedbackLayout.setVisibility(View.VISIBLE);
                 doneOrderWithFeedbackLayout.setVisibility(View.INVISIBLE);

                 TextView doneOrderWithoutFeedbackStore = convertView.findViewById(R.id.doneOrderWithoutFeedbackStoreName);
                 TextView doneOrderWithoutFeedbackOrderDate = convertView.findViewById(R.id.doneOrderWithoutFeedbackOrderDate);
                 TextView doneOrderWithoutFeedbackTotalCast = convertView.findViewById(R.id.doneOrderWithoutFeedbackTotalCast);
                 TextView feedbackBtn = convertView.findViewById(R.id.feedbackBtn);

                 doneOrderWithoutFeedbackStore.setText(storeName);
                 doneOrderWithoutFeedbackOrderDate.setText(orderDate);
                 doneOrderWithoutFeedbackTotalCast.setText(Formater.formatDoubleToMoney(String.valueOf(totalOrder)));
                 feedbackBtn.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         Intent toUserFeedBackPage = new Intent(context, UserFeedbackPage.class);
                         toUserFeedBackPage.putExtra("orderId",orderId);
                         toUserFeedBackPage.putExtra("userId",userId);
                         toUserFeedBackPage.putExtra("storeId",((Order) getGroup(groupPosition)).getStoreId());
                         ((Activity)context).startActivityForResult(toUserFeedBackPage ,1);
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
        String productName = ((CartDetail) getChild(groupPosition, childPosition)).getProductName();
        int quantity = ((CartDetail) getChild(groupPosition, childPosition)).getQuantity();
        double price = ((CartDetail) getChild(groupPosition, childPosition)).getQuantity() * ((CartDetail) getChild(groupPosition, childPosition)).getUnitPrice();

        productNameTV.setText(productName);
        quantityTV.setText(String.valueOf(quantity));
        priceTV.setText(Formater.formatDoubleToMoney(String.valueOf(price)));
        Glide.with(context /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(((CartDetail) getChild(groupPosition, childPosition)).getImage_path()))
                //.skipMemoryCache(true)
                .into(productImage);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
