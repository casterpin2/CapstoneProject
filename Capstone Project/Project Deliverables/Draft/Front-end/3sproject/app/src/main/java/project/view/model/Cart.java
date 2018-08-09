package project.view.model;

import java.util.HashMap;
import java.util.List;

public class Cart {
    private int storeId;
    private String phone;
    private String storeName;
    private String image_path;
    private HashMap<String,CartDetail> cartDetail;

    public Cart() {
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public Cart(int storeId, String phone, String storeName, HashMap<String,CartDetail> cartDetail) {
        this.storeId = storeId;
        this.phone = phone;
        this.storeName = storeName;
        this.cartDetail = cartDetail;
    }

    public Cart(int storeId, String phone, String storeName) {
        this.storeId = storeId;
        this.phone = phone;
        this.storeName = storeName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public HashMap<String, CartDetail> getCartDetail() {
        return cartDetail;
    }

    public void setCartDetail(HashMap<String, CartDetail> cartDetail) {
        this.cartDetail = cartDetail;
    }

    @Override
    public String toString() {
        return "Order{" +
                "storeId=" + storeId +
                ", phone='" + phone + '\'' +
                ", storeName='" + storeName + '\'' +
                ", orderDetailList=" + cartDetail +
                '}';
    }
}
