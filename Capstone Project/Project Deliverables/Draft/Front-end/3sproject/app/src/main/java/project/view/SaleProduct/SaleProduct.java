package project.view.SaleProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lincoln on 18/05/16.
 */
public class SaleProduct {
    private String productID;
    private String productName;
    private double productPrice;
    private double productPromotionPercent;
    private String storeName;



    public SaleProduct() {
    }

    public SaleProduct(String productID, String productName, double productPrice, double producrPromotionPercent, String storeName) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productPromotionPercent = producrPromotionPercent;
        this.storeName = storeName;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public double getProducrPromotionPercent() {
        return productPromotionPercent;
    }

    public void setProducrPromotionPercent(double producrPromotionPercent) {
        this.productPromotionPercent = producrPromotionPercent;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    public static List<SaleProduct> setListProduct(){

        List<SaleProduct> saleProductList = new ArrayList<>();
        SaleProduct a = new SaleProduct("123","Banh Custard", 120000, 10,"Thang Store");
        saleProductList.add(a);

        a = new SaleProduct("123","Banh Custard", 120000, 10,"Thang Store");
        saleProductList.add(a);

        a = new SaleProduct("123","Banh Custard", 120000, 10,"Thang Store");
        saleProductList.add(a);

        a = new SaleProduct("123","Banhs Custard", 1000000, 20,"Thang Store");
        saleProductList.add(a);

        a = new SaleProduct("123","Bánh Custard", 200000, 15,"Thang Store");
        saleProductList.add(a);

        a = new SaleProduct("123","Banh Custard", 120000, 10,"Thang Store");
        saleProductList.add(a);

        a = new SaleProduct("123","Banh Custard", 120000, 10,"Thang Store");
        saleProductList.add(a);

        a = new SaleProduct("123","Banh Custard", 120000, 10,"Thang Store");
        saleProductList.add(a);

        a = new SaleProduct("123","Banh Custard", 120000, 10,"Thang Store");
        saleProductList.add(a);
        return saleProductList;
    }
}