package project.view.AddProductToStore;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;
import com.ramotion.foldingcell.FoldingCell;

import project.firebase.Firebase;
import project.view.R;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class FoldingCellListAdapter extends ArrayAdapter<Item> {

    public List<Item> puttedProductList = SearchProductAddToStore.addedProductList;

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    private StorageReference gsReference = Firebase.getFirebase();
    public FoldingCellListAdapter(Context context, List<Item> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        Item item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder
//            viewHolder.price = cell.findViewById(R.id.title_price);
//            viewHolder.time = cell.findViewById(R.id.title_time_label);
//            viewHolder.date = cell.findViewById(R.id.title_date_label);
            viewHolder.productName_content = cell.findViewById(R.id.productName_content);
            viewHolder.productBrand_content = cell.findViewById(R.id.productBrand_content);
            viewHolder.productCategory_content = cell.findViewById(R.id.productCategory_content);
            viewHolder.productDesc = cell.findViewById(R.id.productDesc);
            viewHolder.productName_title = cell.findViewById(R.id.productName_title);
            viewHolder.productBrand = cell.findViewById(R.id.productBrand);
            viewHolder.requestBtn = cell.findViewById(R.id.content_request_btn);
            viewHolder.productImage_content = cell.findViewById(R.id.productImage_content);

            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (null == item)
            return cell;

         //bind data from selected element to view through view holder
        viewHolder.productName_content.setText(item.getProduct_name());
        viewHolder.productBrand_content.setText(item.getBrand_name());
        viewHolder.productCategory_content.setText(item.getCategory_name());
        viewHolder.productDesc.setText(item.getDescription());
        viewHolder.productName_title.setText(item.getProduct_name());
        viewHolder.productBrand.setText(item.getBrand_name());
        Glide.with(this.getContext())
                .using(new FirebaseImageLoader())
                .load(gsReference.child(item.getImage_path()))
                .into(viewHolder.productImage_content);
        viewHolder.requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                puttedProductList.add(new Item(1,"Nước giải khát Mirinda ","Đồ uống","Pepsi.Co","ảnh mirinda","Đạt ngu như chó",""));
                puttedProductList.add(new Item(1,"Nước giải khát Fanta ","Đồ uống","Pepsi.Co","ảnh mirinda","Đạt ngu như chó",""));
            }
        });



        // set custom btn handler for list item from that item
//        if (item.getRequestBtnClickListener() != null) {
//            viewHolder.contentRequestBtn.setOnClickListener(item.getRequestBtnClickListener());
//        } else {
//            // (optionally) add "default" handler if no handler found in item
//            viewHolder.contentRequestBtn.setOnClickListener(defaultRequestBtnClickListener);
//        }

        return cell;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView productName_content;
        TextView productBrand_content;
        TextView productCategory_content;
        TextView productDesc;
        TextView productName_title;
        TextView productBrand;
        TextView requestBtn;
        ImageView productImage_content;
    }
}
