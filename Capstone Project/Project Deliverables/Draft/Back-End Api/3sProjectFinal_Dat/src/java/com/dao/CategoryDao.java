/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.entites.CategoryEntities;
import com.entites.ProductAddEntites;
import java.sql.SQLException;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author TUYEN
 */
public interface CategoryDao {
    public List<CategoryEntities> listCategory() throws SQLException;
    public List<CategoryEntities> listCategoryTop10() throws SQLException;
    public List<ProductAddEntites> getProductInCategory(@RequestParam("page")int page,@RequestParam("categoryId") int categoryId) throws SQLException;
}
