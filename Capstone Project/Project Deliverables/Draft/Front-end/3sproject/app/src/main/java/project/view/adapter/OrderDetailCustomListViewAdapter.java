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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import project.view.R;
import project.view.model.Product;
import project.view.util.Formater;

public class OrderDetailCustomListViewAdapter extends ArrayAdapter<Product> {
    private Context context;
    private int resource;
    private List<Product> productList;
    private Formater formater;

    public OrderDetailCustomListViewAdapter(Context context, int resource, ArrayList<Product> productList) {
        super(context, resource, productList);
        this.context = context;
        this.resource = resource;
        this.productList = productList;
        formater = new Formater();
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        OrderDetailCustomListViewAdapter.OrderDetailViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.product_in_order_detail, parent, false);
            viewHolder = new OrderDetailCustomListViewAdapter.OrderDetailViewHolder();
            viewHolder.productNameTV = (TextView) convertView.findViewById(R.id.productNameTV);
            viewHolder.quantityTV = (TextView) convertView.findViewById(R.id.quantityTV);
            viewHolder.productImage = (ImageView) convertView.findViewById(R.id.productImage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OrderDetailCustomListViewAdapter.OrderDetailViewHolder) convertView.getTag();
        }
        final Product product = productList.get(position);

        viewHolder.productNameTV.setText(product.getProduct_name());
        viewHolder.quantityTV.setText(String.valueOf(1));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // click vào đây ra trang show thông tin sản phẩm
                Toast.makeText(context, "Thắng - "+ position+" - nè!!!!", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    public class OrderDetailViewHolder {
        TextView productNameTV, quantityTV;
        ImageView productImage;
    }
}
