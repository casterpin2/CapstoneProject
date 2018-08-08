package project.view.model;

import java.util.List;

public class Cart {
    private int storeId;
    private String phone;
    private String storeName;
    private List<CartDetail> cartDetail;

    public Cart() {
    }

    public Cart(int storeId, String phone, String storeName, List<CartDetail> cartDetail) {
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

    public List<CartDetail> getCartDetail() {
        return cartDetail;
    }

    public void setCartDetail(List<CartDetail> cartDetail) {
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
