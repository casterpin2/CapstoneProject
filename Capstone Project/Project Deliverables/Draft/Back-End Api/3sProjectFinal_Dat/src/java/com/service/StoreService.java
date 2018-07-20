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
}
