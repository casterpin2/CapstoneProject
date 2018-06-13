/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.entites.ProductEntites;
import java.sql.SQLException;

/**
 *
 * @author TUYEN
 */
public interface ProductService {
    public boolean insertProductStore(ProductEntites p) throws SQLException;
    
}
