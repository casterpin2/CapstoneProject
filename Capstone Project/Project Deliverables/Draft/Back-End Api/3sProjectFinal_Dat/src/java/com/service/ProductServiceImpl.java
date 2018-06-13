/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.dao.ProductDao;
import com.entites.ProductAddEntites;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author AHBP
 */
@Service("productService")
public class ProductServiceImpl implements ProductService{
    @Autowired
    ProductDao dao;

    @Override
    public List<ProductAddEntites> getProductForAdd(String query) throws SQLException {
        return dao.getProductForAdd(query);
    }

    @Override
    public boolean insertProdcut(List<ProductAddEntites> productList, int storeId) throws SQLException {
       return dao.insertProdcut(productList, storeId);
    }


}
