package project.view.UserInformation;

public class UserInformation {
    private int userID;
    private String userName;
    private String DOB;
    private String address;
    private String phone;
    private String email;
    private String gender;
    private String password;
    private String userImg;
    private int numberofOrder;
    private String storeName;

    public UserInformation(int userID, String userName, String DOB, String address, String phone, String email, String gender, String password, String userImg, int numberofOrder, String storeName) {
        this.userID = userID;
        this.userName = userName;
        this.DOB = DOB;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.password = password;
        this.userImg = userImg;
        this.numberofOrder = numberofOrder;
        this.storeName = storeName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public int getNumberofOrder() {
        return numberofOrder;
    }

    public void setNumberofOrder(int numberofOrder) {
        this.numberofOrder = numberofOrder;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
