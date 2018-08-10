package project.view.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Order {
    private String status;
    private double totalPrice;
    private String deliverTime;
    private String storeName;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public HashMap<String, CartDetail> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(HashMap<String, CartDetail> orderDetail) {
        this.orderDetail = orderDetail;
    }

    private HashMap<String,CartDetail> orderDetail;
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
}
