/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entites.BrandEntities;
import com.entites.ProductAddEntites;
import com.service.BrandService;
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
public class BrandController {
    
    @Autowired
    BrandService brand;
    
    @RequestMapping(value = "/brands", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<BrandEntities> brandList(@RequestParam("page") int page) throws SQLException {
        return brand.listBrand(page);
    }
    
    @RequestMapping(value = "/brands/top5", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<BrandEntities> brandListTop5() throws SQLException {
        return brand.listBrandTop5();
    }

    @RequestMapping(value = "/brands/productWithBrand", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<ProductAddEntites> productWithBrand(@RequestParam("brandId") int brandId, @RequestParam("page") int page) throws SQLException {
        return brand.listProductWithBrand(brandId,page);
    }
    
    @RequestMapping(value = "/brands/brandByType", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<BrandEntities> listBrandByType(@RequestParam("typeId") int typeId) throws SQLException {
        return brand.listBrandByType(typeId);
    }
    
    @RequestMapping(value = "/brands/productWithBrandType", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<ProductAddEntites> listProductWithBrandType(@RequestParam("brandId") int brandId, @RequestParam("typeId") int typeId, @RequestParam("page") int page) throws SQLException {
        return brand.listProductWithBrandType(brandId,typeId,page);
    }
    
    @RequestMapping(value = "/brands/getBrandsByName", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<BrandEntities> getBrandsByName(@RequestParam("query") String query, @RequestParam("page") int page) throws SQLException {
        return brand.getBrandsByName(query,page);
    }
}
