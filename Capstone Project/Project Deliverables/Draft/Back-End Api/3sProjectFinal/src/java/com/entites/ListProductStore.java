/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entites;

import java.util.List;

/**
 *
 * @author TUYEN
 */
public class ListProductStore {

    private String truck;
    private List<ProductEntites> listProduct;

    public ListProductStore() {
    }

    public ListProductStore(String truck, List<ProductEntites> listProduct) {
        this.truck = truck;
        this.listProduct = listProduct;
    }

    public String getTruck() {
        return truck;
    }

    public void setTruck(String truck) {
        this.truck = truck;
    }

    public List<ProductEntites> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<ProductEntites> listProduct) {
        this.listProduct = listProduct;
    }

}
