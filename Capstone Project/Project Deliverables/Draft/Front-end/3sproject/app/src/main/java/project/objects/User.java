package project.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class User {
    @SerializedName("userID")
    @Expose
    private int id;
    @SerializedName("firstName")
    @Expose
    private String first_name;
    @SerializedName("lastName")
    @Expose
    private String last_name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("deviceId")
    @Expose
    private String deviceId;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("image_path")
    @Expose
    private String image_path;
    @SerializedName("dateOfBirth")
    @Expose
    private String dateOfBirth;

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("userName")
    @Expose
    private String username;
    @SerializedName("roleId")
    @Expose
    private String roleId;
    @SerializedName("hasStore")
    @Expose
    private int hasStore;

    public User() {
        this.id = 0;
        this.first_name = "";
        this.last_name = "";
        this.phone = "";
        this.deviceId = "";
        this.gender = "";
        this.image_path = "";
        this.dateOfBirth = "";
        this.email = "";
        this.username = "";
        this.roleId = "";
        this.hasStore = 0;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public int getHasStore() {
        return hasStore;
    }

    public void setHasStore(int hasStore) {
        this.hasStore = hasStore;
    }

    public User(int id, String name, String img) {
        this.id = id;
        this.first_name = img;
    }
    public int getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }


    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", phone='" + phone + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", gender='" + gender + '\'' +
                ", image_path='" + image_path + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", roleId='" + roleId + '\'' +
                ", hasStore=" + hasStore +
                '}';
    }
}
