package project.view.UserOrderFast;

public class ProductDetail {
    private int productID;
    private String productName;
    private String productImagePath;
    private long productPrice;
    private double promotionPercent;

    public ProductDetail(){}

    public ProductDetail(int productID, String productName, String productImagePath, long productPrice, double promotionPercent) {
        this.productID = productID;
        this.productName = productName;
        this.productImagePath = productImagePath;
        this.productPrice = productPrice;
        this.promotionPercent = promotionPercent;
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

    public String getProductImagePath() {
        return productImagePath;
    }

    public void setProductImagePath(String productImagePath) {
        this.productImagePath = productImagePath;
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
}
