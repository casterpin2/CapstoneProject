package project.view.Cart;

import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Simple POJO model for example
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Product implements Serializable{
    @SerializedName("product_id")
    @Expose
    private int product_id;
    @SerializedName("product_name")
    @Expose
    private String product_name;
    @SerializedName("brand_name")
    @Expose
    private String brand_name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("category_name")
    @Expose
    private String category_name;
    @SerializedName("type_name")
    @Expose
    private String type_name;
    @SerializedName("image_path")
    @Expose
    private String image_path;
    @SerializedName("price")
    @Expose
    private Long price;
    @SerializedName("promotion")
    @Expose
    private Double promotion;
    @SerializedName("storeName")
    @Expose
    private String storeName;

    public Product() {
    }

    public Product(int product_id, String product_name, String brand_name, String description, String category_name, String type_name, String image_path, long price, double promotion) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.brand_name = brand_name;
        this.description = description;
        this.category_name = category_name;
        this.type_name = type_name;
        this.image_path = image_path;
        this.price = price;
        this.promotion = promotion;
    }


    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public Long getPrice() {return price;}

    public void setPrice(Long price) {this.price = price;}

    public Double getPromotion() {return promotion;}

    public void setPromotion(Double promotion) {this.promotion = promotion;}

    @Override
    public String toString() {
        return "Product{" +
                "product_id=" + product_id +
                ", product_name='" + product_name + '\'' +
                ", brand_name='" + brand_name + '\'' +
                ", description='" + description + '\'' +
                ", category_name='" + category_name + '\'' +
                ", type_name='" + type_name + '\'' +
                ", image_path='" + image_path + '\'' +
                ", price=" + price +
                ", promotion=" + promotion +
                ", storeName='" + storeName + '\'' +
                '}';
    }
}
