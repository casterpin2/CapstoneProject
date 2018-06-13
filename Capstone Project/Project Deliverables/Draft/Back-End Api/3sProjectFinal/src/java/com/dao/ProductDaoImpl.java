/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.entites.ImgEntites;
import com.entites.ProductEntites;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TUYEN
 */
@Repository ("productDao")
public class ProductDaoImpl extends BaseDao implements ProductDao{

    @Override
    public boolean insertProductStore(ProductEntites p) throws SQLException {
        boolean insertProduct = false;
        boolean insertImg = false;
        Connection conn  = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try{
            String sql = "Insert into product_store values()";
            conn = getConnection();
            
            
        }finally{
            closeConnect(conn, pre, rs);
        }
        
       return false;
    }
    public boolean insertImgProductStore(Connection conn ,int productId,List<ImgEntites> listImg){
        return false;
    }
}