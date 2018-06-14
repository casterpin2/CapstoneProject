/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.entites.ProductAddEntites;
import com.entites.UserEntites;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author AHBP
 */
public interface ProductDao {
     public List<ProductAddEntites> getProductForAdd(String query) throws SQLException;
      public boolean  insertProdcut(List<ProductAddEntites> productList,int storeId) throws SQLException;
}
