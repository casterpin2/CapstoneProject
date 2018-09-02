/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.entites.ProductAddEntites;
import com.entites.TypeEntites;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author AHBP
 */
public interface TypeDao {
    public List<TypeEntites>  getTypebyCategory(int categoryId) throws SQLException;
    public List<ProductAddEntites>  getProductbyType(int typeId, int page) throws SQLException;
}
