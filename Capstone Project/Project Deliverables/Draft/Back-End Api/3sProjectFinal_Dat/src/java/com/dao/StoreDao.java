/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.entites.LocationEntites;
import com.entites.StoreEntites;
import java.sql.SQLException;

/**
 *
 * @author DatNQ
 */
public interface StoreDao {
     public StoreEntites  registerStore(StoreEntites store,LocationEntites location) throws SQLException;
     public boolean deleteProductInStore(int storeId,int productId) throws SQLException;
     public boolean editProductInStore(int storeId,int productId,double price,double promotion) throws SQLException;
}
