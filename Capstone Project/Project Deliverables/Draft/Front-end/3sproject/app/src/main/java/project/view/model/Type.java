package project.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Type {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("product_count")
    @Expose
    private int numberOfProduct;
    @SerializedName("image_path")
    @Expose
    private String path;

    public Type() {}

    public int getId() {
        return id;
    }

    public Type(int id, String name, int numberOfProduct, String path) {
        this.id = id;
        this.name = name;
        this.numberOfProduct = numberOfProduct;
        this.path = path;
    }

    public void setId(int id) {
        this.id = id;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfProduct() {
        return numberOfProduct;
    }

    public void setNumberOfProduct(int numberOfProduct) {
        this.numberOfProduct = numberOfProduct;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTypeName() {
        return name;
    }
}
