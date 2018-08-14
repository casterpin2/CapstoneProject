package project.view.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Order {
    private String status;
    private double totalPrice;
    private String deliverTime;
    private String address;
    private int storeId;
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsFeedback() {
        return isFeedback;
    }

    public void setIsFeedback(String isFeedback) {
        this.isFeedback = isFeedback;
    }

    private String isFeedback;
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    private String orderId;

    private HashMap<String,CartDetail> orderDetail;

    public HashMap<String, CartDetail> getOrderDetail() {
        return orderDetail;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    private String storeName;

    public void setOrderDetail(HashMap<String, CartDetail> orderDetail) {
        this.orderDetail = orderDetail;
    }
    private double longtitude;

    private double latitude;

    private String userName;

    private int userId;

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Order() {
    }

    public Order(String status, double totalPrice, String deliverTime) {
        this.status = status;
        this.totalPrice = totalPrice;
        this.deliverTime = deliverTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(String deliverTime) {
        this.deliverTime = deliverTime;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("status", status);
        result.put("totalPrice", totalPrice);
        result.put("deliverTime", deliverTime);
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "status='" + status + '\'' +
                ", totalPrice=" + totalPrice +
                ", deliverTime='" + deliverTime + '\'' +
                ", isFeedback='" + isFeedback + '\'' +
                ", orderId='" + orderId + '\'' +
                ", orderDetail=" + orderDetail +
                ", storeName='" + storeName + '\'' +
                '}';
    }
}
