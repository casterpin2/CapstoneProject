/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entites.ProductAddEntites;
import com.entites.TypeEntites;
import com.service.TypeService;
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
public class TypeController {
    @Autowired
    TypeService service;
    
    @RequestMapping(value = "/getType", method = RequestMethod.GET, produces = "application/json")
    public List<TypeEntites> getTypebyCategory(@RequestParam("categoryId") int categoryId) throws SQLException, IOException {
        return service.getTypebyCategory(categoryId);
    }
    
    @RequestMapping(value = "/getProductbyType", method = RequestMethod.GET, produces = "application/json")
    public List<ProductAddEntites> getProductbyType(@RequestParam("typeId") int typeId) throws SQLException, IOException {
        return service.getProductbyType(typeId);
    }
}
