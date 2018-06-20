package project.view.Category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Category implements Serializable{
    @SerializedName("categoryId")
    @Expose
    private int categoryID;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("categoryPath")
    @Expose
    private String categoryImageLink;
    @SerializedName("count")
    @Expose
    private int numberOfRecord;

    public Category() {}

    public Category(int categoryID, String categoryName, String categoryImageLink, int numberOfRecord) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.categoryImageLink = categoryImageLink;
        this.numberOfRecord = numberOfRecord;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImageLink() {
        return categoryImageLink;
    }

    public void setCategoryImageLink(String categoryImageLink) {
        this.categoryImageLink = categoryImageLink;
    }

    public int getNumberOfRecord() {
        return numberOfRecord;
    }

    public void setNumberOfRecord(int numberOfRecord) {
        this.numberOfRecord = numberOfRecord;
    }
}
