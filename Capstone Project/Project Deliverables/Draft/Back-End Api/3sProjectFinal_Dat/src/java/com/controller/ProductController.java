/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entites.JsonUtil;
import com.entites.ProductList;
import com.entites.ProductAddEntites;
import com.entites.ProductEntities;
import com.service.ProductService;
import java.io.IOException;
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
 * @author AHBP
 */
@RestController
@RequestMapping(value = "/api")
@EnableWebMvc
public class ProductController {
     @Autowired
    ProductService product;

    @RequestMapping(value = "/getProductForAdd", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public List<ProductAddEntites> getProductForAdd(@RequestParam("query") String query) throws SQLException{
        return product.getProductForAdd(query);
    }
    
    @RequestMapping(value = "/posts",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public boolean getData(@RequestParam("jsonString") String jsonString,@RequestParam("storeId") int storeId) throws SQLException, ClassNotFoundException, IOException{
        System.out.println(jsonString+" "+ storeId);
        List<ProductAddEntites> list = JsonUtil.converJsonToJava(jsonString, ProductAddEntites.class);
        
        return product.insertProdcut(list, storeId);
}
}
