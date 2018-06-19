package project.view.Brand;

public class Brand {
    private int brandID;
    private String brandName;
    private String brandImageLink;
    private int numberOfRecord;

    public Brand() {}

    public Brand(int brandID, String brandName, String brandImageLink, int numberOfRecord) {
        this.brandID = brandID;
        this.brandName = brandName;
        this.brandImageLink = brandImageLink;
        this.numberOfRecord = numberOfRecord;
    }

    public int getBrandID() {
        return brandID;
    }

    public void setBrandID(int brandID) {
        this.brandID = brandID;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandImageLink() {
        return brandImageLink;
    }

    public void setBrandImageLink(String brandImageLink) {
        this.brandImageLink = brandImageLink;
    }

    public int getNumberOfRecord() {
        return numberOfRecord;
    }

    public void setNumberOfRecord(int numberOfRecord) {
        this.numberOfRecord = numberOfRecord;
    }
}
