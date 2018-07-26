
package project.view.model;

public class RegisterStoreInformation {
    private int userID;
    private String storeName;
    private String phone;
    private int cityID;
    private int townID;
    private int communeID;
    private String detailAddress;
    private double longtitude;
    private double latitude;

    public RegisterStoreInformation(){}

    public RegisterStoreInformation(int userID, String storeName, String phone, int cityID, int townID, int communeID, String detailAddress, double longtitude, double latitude) {
        this.userID = userID;
        this.storeName = storeName;
        this.phone = phone;
        this.cityID = cityID;
        this.townID = townID;
        this.communeID = communeID;
        this.detailAddress = detailAddress;
        this.longtitude = longtitude;
        this.latitude = latitude;
    }

    public int getUserID() { return userID; }

    public void setUserID(int userID) { this.userID = userID; }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public int getTownID() {
        return townID;
    }

    public void setTownID(int townID) {
        this.townID = townID;
    }

    public int getCommuneID() {
        return communeID;
    }

    public void setCommuneID(int communeID) {
        this.communeID = communeID;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}

