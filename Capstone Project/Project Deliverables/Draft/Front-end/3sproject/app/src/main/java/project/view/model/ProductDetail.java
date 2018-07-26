package project.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetail {
    @SerializedName("product_id")
    @Expose
    private int productID;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("productImage")
    @Expose
    private String productImage;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("brandName")
    @Expose
    private String brandName;
    @SerializedName("productDesc")
    @Expose
    private String productDesc;
    private long productPrice;
    private double promotionPercent;

    public ProductDetail(int productID, String productName, String productImage, long productPrice, double promotionPercent) {
        this.productID = productID;
        this.productName = productName;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.promotionPercent = promotionPercent;
    }

    public ProductDetail(int productID, String productName, String productImage, String categoryName, String brandName, String productDesc) {
        this.productID = productID;
        this.productName = productName;
        this.productImage = productImage;
        this.categoryName = categoryName;
        this.brandName = brandName;
        this.productDesc = productDesc;
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

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }


    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }
}
