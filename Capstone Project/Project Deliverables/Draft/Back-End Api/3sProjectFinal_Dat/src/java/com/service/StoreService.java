/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.entites.LocationEntites;
import com.entites.StoreEntites;
import java.sql.SQLException;

/**
 *
 * @author AHBP
 */
public interface StoreService {
    public StoreEntites  registerStore(StoreEntites store,LocationEntites location) throws SQLException;
    public StoreEntites  getStoreById(int storeId) throws SQLException;
    public boolean deleteProductInStore(int storeId,int productId) throws SQLException;
    public boolean editProductInStore(int storeId,int productId,double price,double promotion) throws SQLException;
    public StoreEntites updateStore(StoreEntites store,LocationEntites location) throws SQLException;
}
