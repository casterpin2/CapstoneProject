package project.view.model;

import java.util.HashMap;

public class OrderUserDetail {
    private int storeId;
    private String storeName;
    private String image_path;
    private String phone;

    private HashMap<String,Product> productInOrder;

    public HashMap<String, Product> getProductInOrder() {
        return productInOrder;
    }

    public void setProductInOrder(HashMap<String, Product> productInOrder) {
        this.productInOrder = productInOrder;
    }

    public OrderUserDetail() {
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
