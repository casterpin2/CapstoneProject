package project.view.OrderManagerment.model;

import java.util.ArrayList;
import java.util.Date;

public class OrderDetail {

    private String orderCode;
    private String productName;
    private int quantity;
    private Date orderDate;
    private String name;
    private String phoneNumber;
    private String address;
    private Double price;
    private String storeName;
    private int storeId;

    public OrderDetail(String orderCode, String productName, int quantity, Date orderDate, String name, String phoneNumber, String address, Double price, String storeName, int storeId) {
        this.orderCode = orderCode;
        this.productName = productName;
        this.quantity = quantity;
        this.orderDate = orderDate;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.price = price;
        this.storeName = storeName;
        this.storeId = storeId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public static ArrayList<OrderDetail> dataTest(){
        ArrayList<OrderDetail> arrContact = new ArrayList<>();
        OrderDetail orderDetail = new OrderDetail("1111111111","Nước khoáng lavi 500ml",3,new Date(),"Vũ Sỹ Tùng","0988 933 xxx", "Hà Nội, Việt Nam",100000.0,"Cửa hàng của thằng nào đó",1);

        for (int i = 0; i<20;i++){
            arrContact.add(orderDetail);
        }

        return  arrContact;
    }
}