package project.view.AddProductToStore;

import android.view.View;

import java.util.ArrayList;

/**
 * Simple POJO model for example
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Item {

    private String productName;
    private String categoryName;
    private String brandName;
    private String productImageSrc;
    private String description;

    private View.OnClickListener requestBtnClickListener;

    public Item() {
    }

    public Item(String productName, String categoryName, String brandName, String productImageSrc, String description) {
        this.productName = productName;
        this.categoryName = categoryName;
        this.brandName = brandName;
        this.productImageSrc = productImageSrc;
        this.description = description;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getProductImageSrc() {
        return productImageSrc;
    }

    public void setProductImageSrc(String productImageSrc) {
        this.productImageSrc = productImageSrc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public View.OnClickListener getRequestBtnClickListener() {
        return requestBtnClickListener;
    }

    public void setRequestBtnClickListener(View.OnClickListener requestBtnClickListener) {
        this.requestBtnClickListener = requestBtnClickListener;
    }




    /**
     * @return List of elements prepared for tests
     */
    public static ArrayList<Item> getTestingList() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Nước giải khát Mirinda ","Đồ uống","Pepsi.Co","ảnh mirinda","Đạt ngu như chó"));
        items.add(new Item("Nước giải khát Mirinda 2 lít với nhiều trái cây tươi","Đồ uống","Pepsi.Co","ảnh mirinda","Đạt ngu như chóĐạt ngu như chóĐạt ngu như chóĐạt ngu như chóĐạt ngu như chóĐạt ngu như chó"));
        items.add(new Item("Nước giải khát Mirinda 3 lít với nhiều trái cây tươi","Đồ uống","Pepsi.Co","ảnh mirinda","Đạt ngu như chó"));
        items.add(new Item("Nước giải khát Mirinda 4 lít với nhiều trái cây tươi","Đồ uống","Pepsi.Co","ảnh mirinda","Đạt ngu như chó"));
        items.add(new Item("Nước giải khát Mirinda 5 lít với nhiều trái cây tươi","Đồ uống","Pepsi.Co","ảnh mirinda","Đạt ngu như chó"));
        items.add(new Item("Nước giải khát Mirinda 6 lít với nhiều trái cây tươi","Đồ uống","Pepsi.Co","ảnh mirinda","Đạt ngu như chó"));

        return items;

    }

}
