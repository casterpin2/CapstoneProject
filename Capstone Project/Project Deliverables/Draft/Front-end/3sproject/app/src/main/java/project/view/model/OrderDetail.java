package project.view.model;

import java.util.ArrayList;
import java.util.Date;

public class OrderDetail {

    private String orderID;
    private int userID;
    private String userName;
    private int productID;
    private String productName;
    private int storeID;
    private String storeName;
    private long finalPrice;
    private int productQuantity;
    private String phone;
    private String orderDateTime;
    private double longtitude;
    private double latitude;
    private String address;

    public OrderDetail() {
    }


    public OrderDetail(String orderID, String userName, String productName, int storeID, String storeName, long finalPrice, int productQuantity, String phone, String orderDateTime, String address) {
        this.orderID = orderID;
        this.userName = userName;
        this.productName = productName;
        this.storeID = storeID;
        this.storeName = storeName;
        this.finalPrice = finalPrice;
        this.productQuantity = productQuantity;
        this.phone = phone;
        this.orderDateTime = orderDateTime;
        this.address = address;
    }

    public OrderDetail(int userID, String userName, int productID, int storeID, long finalPrice, int productQuantity, String phone, String orderDateTime, double longtitude, double latitude, String address) {
        this.userID = userID;
        this.userName = userName;
        this.productID = productID;
        this.storeID = storeID;
        this.finalPrice = finalPrice;
        this.productQuantity = productQuantity;
        this.phone = phone;
        this.orderDateTime = orderDateTime;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.address = address;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public long getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(long finalPrice) {
        this.finalPrice = finalPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public static ArrayList<OrderDetail> dataTest(){
        ArrayList<OrderDetail> arrContact = new ArrayList<>();
        OrderDetail orderDetail = new OrderDetail("1111111111","Nước khoáng lavi 500ml","Nước lavi 500ml",1,"Cửa hàng của tôi",22222, 2,"0916606094",new Date().toString(),"Thạch thất, Hà nội");

        for (int i = 0; i<20;i++){
            arrContact.add(orderDetail);
        }

        return  arrContact;
    }
}



