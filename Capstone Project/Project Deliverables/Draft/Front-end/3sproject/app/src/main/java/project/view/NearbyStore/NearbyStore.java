package project.view.NearbyStore;

public class NearbyStore {
    String storeName;
    String storeAddress;
    double distance;
    long productPrice;
    double promotionPercent;
    double longtitude;
    double latitude;

    public NearbyStore(){}

    public NearbyStore(String storeName, String storeAddress, double distance, long productPrice, double promotionPercent, double longtitude, double latitude) {
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.distance = distance;
        this.productPrice = productPrice;
        this.promotionPercent = promotionPercent;
        this.longtitude = longtitude;
        this.latitude = latitude;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(long productPrice) {
        this.productPrice = productPrice;
    }

    public double getPromotionPercent() {
        return promotionPercent;
    }

    public void setPromotionPercent(double promotionPercent) {
        this.promotionPercent = promotionPercent;
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
