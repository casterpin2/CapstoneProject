/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.dao.StoreDao;
import com.entites.LocationEntites;
import com.entites.StoreEntites;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author DatNQ
 */
@Service("storeService")
public class StoreServiceImpl implements StoreService{
    @Autowired
    StoreDao dao;
    @Override
    public StoreEntites registerStore(StoreEntites store,LocationEntites location) throws SQLException {
        return dao.registerStore(store,location);
    }

    @Override
    public boolean deleteProductInStore(int storeId, int productId) throws SQLException {
        return dao.deleteProductInStore(storeId, productId);
    }

    @Override
    public boolean editProductInStore(int storeId, int productId, double price, double promotion) throws SQLException {
        return dao.editProductInStore(storeId, productId,price,promotion);
    }
    
}
