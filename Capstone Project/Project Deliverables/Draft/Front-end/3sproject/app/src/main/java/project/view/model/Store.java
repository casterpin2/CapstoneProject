package project.view.model;

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
    @SerializedName("location_id")
    @Expose
    private int location_id;


    public Store() {
    }
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("longtitude")
    @Expose
    private String longtitude;
    @SerializedName("latitude")
    @Expose
    private String latitude;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
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
    @SerializedName("registerLog")
    @Expose
    private String registerLog;

    @SerializedName("user_name")
    @Expose
    private String user_name;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getRegisterLog() {
        return registerLog;
    }

    public void setRegisterLog(String registerLog) {
        this.registerLog = registerLog;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }



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
    public Store(String name,int user_id, String phone) {
        this.name = name;
        this.user_id = user_id;
        this.phone = phone;
    }

    public Store(int id,String name, String phone,String image_path) {
        this.name = name;
        this.id = id;
        this.phone = phone;
        this.image_path = image_path;
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
