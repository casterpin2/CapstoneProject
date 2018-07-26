package project.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lincoln on 18/05/16.
 */
public class SaleProduct implements Serializable{
    @SerializedName("product_id")
    @Expose
    private String productID;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("price")
    @Expose
    private double productPrice;
    @SerializedName("promotion")
    @Expose
    private double productPromotionPercent;
    @SerializedName("storeName")
    @Expose
    private String storeName;
    @SerializedName("image_path")
    @Expose
    private String imgProductSale;


    public SaleProduct() {
    }

    public SaleProduct(String productID, String productName, double productPrice, double producrPromotionPercent, String storeName) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productPromotionPercent = producrPromotionPercent;
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
        return productPromotionPercent;
    }

    public void setProducrPromotionPercent(double producrPromotionPercent) {
        this.productPromotionPercent = producrPromotionPercent;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public double getProductPromotionPercent() {
        return productPromotionPercent;
    }

    public void setProductPromotionPercent(double productPromotionPercent) {
        this.productPromotionPercent = productPromotionPercent;
    }

    public String getImgProductSale() {
        return imgProductSale;
    }

    public void setImgProductSale(String imgProductSale) {
        this.imgProductSale = imgProductSale;
    }
}