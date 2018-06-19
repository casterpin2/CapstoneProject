package com.example.tung.recycleview.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tung.
 */
public class Item {
    private String productCode;
    private String productName;
    private int quantity;
    private String productImage;


    public Item() {

    }

    public Item(String code, String title, int quantity,String productImage) {
        this.productCode = code;
        this.productName = title;
        this.quantity = quantity;
        this.productImage = productImage;
    }

    public String getCode() {
        return productCode;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setCode(String code) {
        this.productCode = code;
    }

    public void setTitle(String title) {
        this.productName = title;
    }

    public void setQuantity(int lyric) {
        this.quantity = lyric;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public List<Item> setItem(){
        List<Item> items = new ArrayList<>();
        items.add(new Item("60696", "Nước khoáng Lavi 500ml", 100, ""));
        items.add(new Item("60701", "Coca zero 330ml", 45, ""));
        items.add(new Item("60696", "Nước khoáng Lavi 500ml", 100, ""));
        items.add(new Item("60701", "Coca zero 330ml", 45, ""));
        items.add(new Item("60696", "Nước khoáng Lavi 500ml", 100, ""));
        items.add(new Item("60701", "Coca zero 330ml", 45, ""));
        items.add(new Item("60696", "Nước khoáng Lavi 500ml", 100, ""));
        items.add(new Item("60701", "Coca zero 330ml", 45, ""));
        items.add(new Item("60696", "Nước khoáng Lavi 500ml", 100, ""));
        items.add(new Item("60701", "Coca zero 330ml", 45, ""));
        items.add(new Item("60696", "Nước khoáng Lavi 500ml", 100, ""));
        items.add(new Item("60701", "Coca zero 330ml", 45, ""));
        return items;
    }
}