/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entites.LocationEntites;
import com.entites.StoreEntites;
import com.service.StoreService;
import java.io.IOException;
import java.sql.SQLException;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import java.util.HashMap;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author AHBP
 */
@RestController
@RequestMapping(value = "/api")
@EnableWebMvc
public class StoreController {

    @Autowired
    StoreService store;

    @RequestMapping(value = "/registerStore", method = RequestMethod.POST, produces = "application/json;")
    public StoreEntites registerStore(@RequestBody String storeJSON) throws SQLException, ClassNotFoundException, IOException {
        JsonFactory factory = new JsonFactory();
        factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        ObjectMapper mapper = new ObjectMapper(factory);
        HashMap<String,String> Obj = mapper.readValue(storeJSON, HashMap.class);
        String storeObj = Obj.get("store");
        String locationObj = Obj.get("location");
        StoreEntites store = mapper.readValue(storeObj, StoreEntites.class);
        LocationEntites location = mapper.readValue(locationObj, LocationEntites.class);
        StoreEntites result = this.store.registerStore(store,location);
        return result;
    }
    @RequestMapping(value = "/deleteProductInStore", method = RequestMethod.DELETE, produces = "application/json;")
    public boolean deleteProductInStore(@RequestParam("storeId") int storeId,@RequestParam("productId") int productId) throws SQLException, ClassNotFoundException, IOException {
        return store.deleteProductInStore(storeId, productId);
    }
    
    @RequestMapping(value = "/editProductInStore", method = RequestMethod.PUT, produces = "application/json;")
    public boolean editProductInStore(@RequestParam("storeId") int storeId,@RequestParam("productId") int productId,@RequestParam("price") double price,@RequestParam("promotion") double promotion) throws SQLException, ClassNotFoundException, IOException {
        return store.editProductInStore(storeId, productId, price, promotion);
    }
    
    @RequestMapping(value = "/getStoreById", method = RequestMethod.GET, produces = "application/json;")
    public StoreEntites getStoreById(@RequestParam("storeId") int storeId) throws SQLException, ClassNotFoundException, IOException {
        return store.getStoreById(storeId);
    }
}
