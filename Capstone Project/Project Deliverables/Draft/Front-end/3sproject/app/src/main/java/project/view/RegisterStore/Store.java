package project.view.RegisterStore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Store implements Serializable{
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("user_id")
    @Expose
    private int user_id;



    public Store() {
    }

    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("image_path")
    @Expose
    private String image_path;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("status")
    @Expose
    private int status;

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Store(String name, int user_id, String phone) {
        this.name = name;
        this.user_id = user_id;
        this.phone = phone;
    }

    public Store(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
