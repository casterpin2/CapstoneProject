package project.view.UserOrderFast;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class OrderDetail {

    private int userID;
    private String userName;
    private int productID;
    private int storeID;
    private long finalPrice;
    private int productQuantity;
    private String phone;
    private String orderDateTime;
    private double longtitude;
    private double latitude;
    private String address;


    public OrderDetail() {
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

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
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
}


