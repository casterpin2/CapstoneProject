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
    
}
