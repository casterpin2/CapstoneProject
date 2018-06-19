package project.view.ProductBrandDisplay;

import java.util.ArrayList;

public class ProductBrand {
    private int productID;
    private String productName;
    private String productDesc;
    private String productImageLink;

    public ProductBrand() {}

    public ProductBrand(int productID, String productName, String productDesc, String productImageLink) {
        this.productID = productID;
        this.productName = productName;
        this.productDesc = productDesc;
        this.productImageLink = productImageLink;
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

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductImageLink() {
        return productImageLink;
    }

    public void setProductImageLink(String productImageLink) {
        this.productImageLink = productImageLink;
    }

    public static ArrayList<ProductBrand> getTestingList() {
        ArrayList<ProductBrand> products = new ArrayList<>();
        products.add(new ProductBrand(1,"Bánh Custard","Hello bánh custard","đâsdsadas"));
        products.add(new ProductBrand(1,"Bánh Custard","Hello bánh custard","đâsdsadas"));
        products.add(new ProductBrand(1,"Bánh Custard","Hello bánh custard","đâsdsadas"));
        products.add(new ProductBrand(1,"Bánh Custard","Hello bánh custard","đâsdsadas"));
        products.add(new ProductBrand(1,"Bánh Custard","Hello bánh custard","đâsdsadas"));
        products.add(new ProductBrand(1,"Bánh Custard","Hello bánh custard","đâsdsadas"));
        products.add(new ProductBrand(1,"Bánh Custard","Hello bánh custard","đâsdsadas"));




        return products;

    }
}
