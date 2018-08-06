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
    public List<ProductAddEntites> getProductForAdd(String query,int page,int storeId) throws SQLException {
        return dao.getProductForAdd(query,page,storeId);
    }

    @Override
    public boolean insertProdcut(ProductAddEntites p, int storeId) throws SQLException {
       return dao.insertProdcut(p, storeId);
    }

    @Override
    public List<ProductAddEntites> getProductSaleList(int number) throws SQLException {
        return dao.getProductSaleList(number);
    }

    @Override
    public List<ProductAddEntites> getProductSaleListTop20(int number) throws SQLException {
         return dao.getProductSaleListTop20(number);
    }

    @Override
    public List<ProductAddEntites> getProductInStore(int storeID) throws SQLException{
        return dao.getProductInStore(storeID);
    }

    @Override
    public boolean insertData(String name, String brand, String barcode) throws SQLException {
        return dao.insertData(name, brand, barcode);
    }

    @Override
    public List<ProductAddEntites> getProductWithBarCode(String query, int storeId) throws SQLException {
        return dao.getProductWithBarCode(query, storeId);
    }

    @Override
    public List<ProductAddEntites> findProductWithUser(String query) throws SQLException {
        return dao.findProductWithUser(query);
    }


}
