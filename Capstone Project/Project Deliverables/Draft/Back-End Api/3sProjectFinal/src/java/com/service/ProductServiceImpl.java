/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.dao.ProductDao;
import com.entites.ProductEntites;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author TUYEN
 */
@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao dao;

    @Override
    public boolean insertProductStore(ProductEntites p) throws SQLException {
        return dao.insertProductStore(p);
    }

}
