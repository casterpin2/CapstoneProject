/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.entites.ProductAddEntites;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author AHBP
 */
public interface ProductDao {
     public List<ProductAddEntites> getProductForAdd(String query,int page,int storeId) throws SQLException;
      public boolean  insertProdcut(ProductAddEntites p,int storeId) throws SQLException;
      public List<ProductAddEntites> getProductSaleList(int number) throws SQLException;
      public List<ProductAddEntites> getProductSaleListTop20(int number) throws SQLException;
      public List<ProductAddEntites> getProductInStore(int storeID) throws SQLException;
      public boolean insertData(String name,String brand,String barcode) throws SQLException;
      public List<ProductAddEntites> getProductWithBarCode(String query, int storeId) throws SQLException;
}
