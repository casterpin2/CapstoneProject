/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entites;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author TUYEN
 */
public class ProductEntites implements Serializable{
    private int id;
    private int productId;
    private String pathImg;
    private double promotion;
    private int price;
    private int storeId;
    private List<ImgEntites> listImg;
    private int typeBrand;
    private String nameProduct;
    public ProductEntites() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getPathImg() {
        return pathImg;
    }

    public void setPathImg(String pathImg) {
        this.pathImg = pathImg;
    }

    public double getPromotion() {
        return promotion;
    }

    public void setPromotion(double promotion) {
        this.promotion = promotion;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
  
    public int getTypeBrand() {
        return typeBrand;
    }

    public void setTypeBrand(int typeBrand) {
        this.typeBrand = typeBrand;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public List<ImgEntites> getListImg() {
        return listImg;
    }

    public void setListImg(List<ImgEntites> listImg) {
        this.listImg = listImg;
    }
    
    
}
