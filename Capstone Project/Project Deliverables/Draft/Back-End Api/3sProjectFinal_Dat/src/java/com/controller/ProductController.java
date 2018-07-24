/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entites.JsonUtil;
import com.entites.ProductAddEntites;
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

    @RequestMapping(value = "/getProductForAdd", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<ProductAddEntites> getProductForAdd(@RequestParam("query") String query) throws SQLException {
        return product.getProductForAdd(query);
    }

    @RequestMapping(value = "/posts", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public boolean getData(@RequestParam("jsonString") StringBuilder jsonString, @RequestParam("storeId") int storeId) throws SQLException, ClassNotFoundException, IOException {
        //Chuyển JSON thành Object
        List<ProductAddEntites> list = JsonUtil.converJsonToJava(jsonString.toString(), ProductAddEntites.class);

        return product.insertProdcut(list, storeId);
    }

    @RequestMapping(value = "/productSales", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<ProductAddEntites> getSaleList() throws SQLException {
        return product.getProductSaleList(0);
    }

    @RequestMapping(value = "/productSales/top20", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<ProductAddEntites> getSaleListTop20() throws SQLException {
        return product.getProductSaleListTop20(0);
    }

    @RequestMapping(value = "/getProductInStore", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<ProductAddEntites> getProductInStore(@RequestParam("storeID") int storeID) throws SQLException {
        return product.getProductInStore(storeID);
    }

    @RequestMapping(value = "/insertData", method = RequestMethod.POST, produces = "application/json")
    public boolean insertData(@RequestParam("name") String name, @RequestParam("brand") String brand, @RequestParam("barcode") String barcode) throws SQLException {

        return product.insertData(name, brand, barcode);
    }

    @RequestMapping(value = "/getProductWithBarcode", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<ProductAddEntites> getProductWithBarcode(@RequestParam("barcode") String query, @RequestParam("store") int storeId) throws SQLException {
        return product.getProductWithBarCode(query, storeId);
    }

}
