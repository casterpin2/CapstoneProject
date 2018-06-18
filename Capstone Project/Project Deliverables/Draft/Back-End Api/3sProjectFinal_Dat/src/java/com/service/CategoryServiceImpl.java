/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.dao.CategoryDao;
import com.entites.CategoryEntities;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author TUYEN
 */
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    CategoryDao dao;
    @Override
    public List<CategoryEntities> listCategory() throws SQLException {
        return dao.listCategory();
    }

    @Override
    public List<CategoryEntities> listCategoryTop10() throws SQLException {
        return dao.listCategoryTop10();
    }
    
}
