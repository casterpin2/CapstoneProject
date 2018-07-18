package project.view.UserOrderFast;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class OrderDetail {

    private int orderID;
    private String productName;
    private String productImage;
    private String salePrice;
    private String SalePriceInstore;
    private String totalOrder;
    private long productPrice;
    private double promotionPercent;
    private String productDesc;
    private String nameStore;
    private String phone;

    public OrderDetail() {
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getSalePriceInstore() {
        return SalePriceInstore;
    }

    public void setSalePriceInstore(String salePriceInstore) {
        SalePriceInstore = salePriceInstore;
    }

    public String getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(String totalOrder) {
        this.totalOrder = totalOrder;
    }

    public long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(long productPrice) {
        this.productPrice = productPrice;
    }

    public double getPromotionPercent() {
        return promotionPercent;
    }

    public void setPromotionPercent(double promotionPercent) {
        this.promotionPercent = promotionPercent;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getNameStore() {
        return nameStore;
    }

    public void setNameStore(String nameStore) {
        this.nameStore = nameStore;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}


