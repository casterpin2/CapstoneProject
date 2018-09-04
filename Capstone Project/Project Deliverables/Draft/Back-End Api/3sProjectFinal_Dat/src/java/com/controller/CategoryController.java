/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entites.CategoryEntities;
import com.entites.ProductAddEntites;
import com.service.CategoryService;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 *
 * @author TUYEN
 */
@RestController
@RequestMapping(value = "/api")
@EnableWebMvc
public class CategoryController {

    @Autowired
    CategoryService category;

    @RequestMapping(value = "/category", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<CategoryEntities> getCategoryList() throws SQLException {
        return category.listCategory();
    }

    @RequestMapping(value = "/category/top10", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<CategoryEntities> getCategoryListTop10() throws SQLException {
        return category.listCategoryTop10();
    }
    
    @RequestMapping(value = "/category/products", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<ProductAddEntites> getProductInCategory(@RequestParam("page")int page,@RequestParam("categoryId") int categoryId) throws SQLException {
        return category.getProductInCategory(page, categoryId);
    }
    
    @RequestMapping(value = "/category/productsByName", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<ProductAddEntites> getProductInCategoryByName(@RequestParam("page")int page,@RequestParam("categoryId") int categoryId, @RequestParam("query")String query) throws SQLException {
        return category.getProductInCategoryByName(page, categoryId,query);
    }
}
