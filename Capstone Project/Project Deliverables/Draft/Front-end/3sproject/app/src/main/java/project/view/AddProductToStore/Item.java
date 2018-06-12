package project.view.AddProductToStore;

import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Simple POJO model for example
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Item {
    @SerializedName("product_id")
    @Expose
    private int product_id;
    @SerializedName("product_name")
    @Expose
    private String product_name;
    @SerializedName("brand_name")
    @Expose
    private String brand_name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("category_name")
    @Expose
    private String category_name;
    @SerializedName("type_name")
    @Expose
    private String type_name;
    @SerializedName("image_path")
    @Expose
    private String image_path;

    private View.OnClickListener requestBtnClickListener;

    public Item() {
    }

    public Item(int product_id, String product_name, String brand_name, String description, String category_name, String type_name, String image_path) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.brand_name = brand_name;
        this.description = description;
        this.category_name = category_name;
        this.type_name = type_name;
        this.image_path = image_path;
    }

    public View.OnClickListener getRequestBtnClickListener() {
        return requestBtnClickListener;
    }

    public void setRequestBtnClickListener(View.OnClickListener requestBtnClickListener) {
        this.requestBtnClickListener = requestBtnClickListener;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    /**
     * @return List of elements prepared for tests
     */
    public static ArrayList<Item> getTestingList() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item(1,"Bình sứ Lavie","Đồ uống","Pepsi.Co","ảnh mirinda","Nước lọc đóng chai","Products/Đồ uống/Nước lọc đóng chai/La Vie/Bình sứ Lavie.png"));
        items.add(new Item(1,"Lavie 350ml","Đồ uống","Pepsi.Co","ảnh mirinda","Nước lọc đóng chai","Products/Đồ uống/Nước lọc đóng chai/La Vie/Lavie 350ml.png"));
        items.add(new Item(1,"Lavie 500ml","Đồ uống","Pepsi.Co","ảnh mirinda","Nước lọc đóng chai","Products/Đồ uống/Nước lọc đóng chai/La Vie/Lavie 500ml.png"));
        items.add(new Item(1,"Lavie kids 350ml","Đồ uống","Pepsi.Co","ảnh mirinda","Nước lọc đóng chai","Products/Đồ uống/Nước lọc đóng chai/La Vie/Lavie kids 350ml.png"));
        items.add(new Item(1,"Nước khoáng Lavie bình 19L","Đồ uống","Pepsi.Co","ảnh mirinda","Nước lọc đóng chai","Products/Đồ uống/Nước lọc đóng chai/La Vie/Nước khoáng Lavie bình 19L.png"));


        return items;

    }

}
