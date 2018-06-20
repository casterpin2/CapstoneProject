package project.view.ProductBrandDisplay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductBrand implements Serializable{
    @SerializedName("product_id")
    @Expose
    private int productID;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("description")
    @Expose
    private String productDesc;
    @SerializedName("image_path")
    @Expose
    private String productImageLink;

    public ProductBrand() {}

    public ProductBrand(int productID, String productName, String productDesc, String productImageLink) {
        this.productID = productID;
        this.productName = productName;
        this.productDesc = productDesc;
        this.productImageLink = productImageLink;
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

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductImageLink() {
        return productImageLink;
    }

    public void setProductImageLink(String productImageLink) {
        this.productImageLink = productImageLink;
    }


}
