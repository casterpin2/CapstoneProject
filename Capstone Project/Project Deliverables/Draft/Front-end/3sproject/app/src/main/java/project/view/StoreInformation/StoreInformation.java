package project.view.StoreInformation;

public class StoreInformation {
    public int storeID;
    public String storeName;
    public String ownerName;
    public String address;
    public String registerDate;
    public String phone;

    public StoreInformation(){}

    public StoreInformation(int storeID, String storeName, String ownerName, String address, String registerDate, String phone) {
        this.storeID = storeID;
        this.storeName = storeName;
        this.ownerName = ownerName;
        this.address = address;
        this.registerDate = registerDate;
        this.phone = phone;
    }

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

