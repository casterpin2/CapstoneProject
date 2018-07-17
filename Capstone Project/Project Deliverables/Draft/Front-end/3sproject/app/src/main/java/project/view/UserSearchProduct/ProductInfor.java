package project.view.UserSearchProduct;

public class ProductInfor {
    private int productID;
    private String productName;
    private String productImagePath;
    private String brandName;

    public void ProductInfor(){}

    public ProductInfor(int productID, String productName, String productImagePath, String brandName) {
        this.productID = productID;
        this.productName = productName;
        this.productImagePath = productImagePath;
        this.brandName = brandName;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImagePath() {
        return productImagePath;
    }

    public void setProductImagePath(String productImagePath) {
        this.productImagePath = productImagePath;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
