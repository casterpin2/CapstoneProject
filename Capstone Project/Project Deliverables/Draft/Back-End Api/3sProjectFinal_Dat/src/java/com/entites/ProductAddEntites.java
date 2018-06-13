/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entites;

/**
 *
 * @author AHBP
 */
public class ProductAddEntites {
    private int product_id;
    private String product_name;
    private String brand_name;
    private String description;
    private String category_name;
    private String type_name;
    private String image_path;
    private Double price;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ProductAddEntites(int product_id, String product_name, String brand_name, String description, String category_name, String type_name, String image_path) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.brand_name = brand_name;
        this.description = description;
        this.category_name = category_name;
        this.type_name = type_name;
        this.image_path = image_path;
    }

    public ProductAddEntites(int product_id, String product_name, String brand_name, String description, String category_name, String type_name, String image_path, Double price) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.brand_name = brand_name;
        this.description = description;
        this.category_name = category_name;
        this.type_name = type_name;
        this.image_path = image_path;
        this.price = price;
    }

    public ProductAddEntites() {
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
    
    
    
}
