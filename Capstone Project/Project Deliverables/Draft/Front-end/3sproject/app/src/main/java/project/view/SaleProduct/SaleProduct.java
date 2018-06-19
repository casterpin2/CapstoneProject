package project.view.SaleProduct;

/**
 * Created by Lincoln on 18/05/16.
 */
public class SaleProduct {
    private String productID;
    private String productName;
    private double productPrice;
    private double producrPromotionPercent;
    private String storeName;



    public SaleProduct() {
    }

    public SaleProduct(String productID, String productName, double productPrice, double producrPromotionPercent, String storeName) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.producrPromotionPercent = producrPromotionPercent;
        this.storeName = storeName;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public double getProducrPromotionPercent() {
        return producrPromotionPercent;
    }

    public void setProducrPromotionPercent(double producrPromotionPercent) {
        this.producrPromotionPercent = producrPromotionPercent;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}