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

    public OrderDetail(String orderCode, String productName, int quantity, Date orderDate, String name, String phoneNumber, String address) {
        this.orderCode = orderCode;
        this.productName = productName;
        this.quantity = quantity;
        this.orderDate = orderDate;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
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

    public static ArrayList<OrderDetail> getListOrder(){
        ArrayList<OrderDetail> arrContact = new ArrayList<>();
        OrderDetail contact1 = new OrderDetail("1111111111","Nước Lavi...",3,new Date(),"Vũ Sỹ Tùng","0988 933 xxx", "Địa chỉ ... ");
        OrderDetail contact2 = new OrderDetail("2222222222","Bim Bim O...",2,new Date(),"Vũ Sỹ Tùng","0988 933 xxx", "Địa chỉ ... ");
        OrderDetail contact3 = new OrderDetail("3333333333","Coca cola...",6,new Date(),"Vũ Sỹ Tùng","0988 933 xxx", "Địa chỉ ... ");
        OrderDetail contact4 = new OrderDetail("3333333554","Nước Lavi...",1,new Date(),"Vũ Sỹ Tùng","0988 933 xxx", "Địa chỉ ... ");
        OrderDetail contact5 = new OrderDetail("4444444422","Fanta",3,new Date(),"Vũ Sỹ Tùng","0988 933 xxx", "Địa chỉ ... ");
        OrderDetail contact6 = new OrderDetail("","Pepsi",3,new Date(),"Vũ Sỹ Tùng","0988 933 xxx", "Địa chỉ ... ");
        OrderDetail contact7 = new OrderDetail("","Nước Lavi...",3,new Date(),"Vũ Sỹ Tùng","0988 933 xxx", "Địa chỉ ... ");
        OrderDetail contact8 = new OrderDetail("","Nước Lavi...",3,new Date(),"Vũ Sỹ Tùng","0988 933 xxx", "Địa chỉ ... ");
        OrderDetail contact9 = new OrderDetail("","Nước Lavi...",3,new Date(),"Vũ Sỹ Tùng","0988 933 xxx", "Địa chỉ ... ");
        OrderDetail contact10 = new OrderDetail("","Nước Lavi...",3,new Date(),"Vũ Sỹ Tùng","0988 933 xxx", "Địa chỉ ... ");

        arrContact.add(contact1);
        arrContact.add(contact2);
        arrContact.add(contact3);
        arrContact.add(contact4);
        arrContact.add(contact5);
        arrContact.add(contact6);
        arrContact.add(contact7);
        arrContact.add(contact8);
        arrContact.add(contact9);
        arrContact.add(contact10);
        return  arrContact;
    }
}