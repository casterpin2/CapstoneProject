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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        HashMap<String, String> Obj = mapper.readValue(storeJSON, HashMap.class);
        String storeObj = Obj.get("store");
        String locationObj = Obj.get("location");
        StoreEntites store = mapper.readValue(storeObj, StoreEntites.class);
        LocationEntites location = mapper.readValue(locationObj, LocationEntites.class);
        StoreEntites result = this.store.registerStore(store, location);
        return result;
    }

    @RequestMapping(value = "/deleteProductInStore", method = RequestMethod.DELETE, produces = "application/json;")
    public boolean deleteProductInStore(@RequestParam("storeId") int storeId, @RequestParam("productId") int productId) throws SQLException, ClassNotFoundException, IOException {
        return store.deleteProductInStore(storeId, productId);
    }

    @RequestMapping(value = "/editProductInStore", method = RequestMethod.PUT, produces = "application/json;")
    public boolean editProductInStore(@RequestParam("storeId") int storeId, @RequestParam("productId") int productId, @RequestParam("price") double price, @RequestParam("promotion") double promotion) throws SQLException, ClassNotFoundException, IOException {
        return store.editProductInStore(storeId, productId, price, promotion);
    }

    @RequestMapping(value = "/getStoreById", method = RequestMethod.GET, produces = "application/json;")
    public StoreEntites getStoreById(@RequestParam("storeId") int storeId) throws SQLException, ClassNotFoundException, IOException {
        return store.getStoreById(storeId);
    }

    @RequestMapping(value = "/updateStore", method = RequestMethod.PUT, produces = "application/json")
    public StoreEntites updateInformationStore(@RequestBody String storeJson) {
        StoreEntites storeData = null;
        LocationEntites locationData = null;
        StoreEntites storeReusult = null;
        try {
            JsonFactory factory = new JsonFactory();
            factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
            ObjectMapper mapper = new ObjectMapper(factory);
            HashMap<String, String> Obj = mapper.readValue(storeJson, HashMap.class);
            String storeObj = Obj.get("store");
            String locationObj = Obj.get("location");
            storeData = mapper.readValue(storeObj, StoreEntites.class);
            locationData = mapper.readValue(locationObj, LocationEntites.class);
            storeReusult = store.updateStore(storeData, locationData);
        } catch (Exception ex) {
            Logger.getLogger(StoreController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return storeReusult;
    }

    @RequestMapping(value = "/informationStore", method = RequestMethod.GET, produces = "application/json;")
    public StoreEntites storeInformation(@RequestParam("storeId") int storeId) throws SQLException, ClassNotFoundException, IOException {
        return store.informationStore(storeId);
    }

    @RequestMapping(value = "/getAllFeedback", method = RequestMethod.GET, produces = "application/json;")
    public List<HashMap<String, Object>> managementFeedback(@RequestParam("storeId") int storeId, @RequestParam("page") int page) throws SQLException, ClassNotFoundException, IOException {
        return store.managementFeedback(storeId, page);
    }

    @RequestMapping(value = "/countFeedback", method = RequestMethod.GET, produces = "application/json;")
    public List<Integer> countFeedback(@RequestParam("storeId") int storeId) throws SQLException, ClassNotFoundException, IOException {
        return store.countFeedback(storeId);
    }

    @RequestMapping(value = "/updateImgStore", method = RequestMethod.PUT, produces = "application/json")
    public Boolean updateImgStore(@RequestBody String storeJson, @RequestParam("imgPath") String newPath) {
        StoreEntites storeData = null;

        StoreEntites storeReusult = null;
        boolean checkUpdate = false;
        try {
            JsonFactory factory = new JsonFactory();
            factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
            ObjectMapper mapper = new ObjectMapper(factory);
            storeData = mapper.readValue(storeJson, StoreEntites.class);
            checkUpdate = store.changeImgByStore(newPath, storeData);

        } catch (Exception ex) {
            Logger.getLogger(StoreController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return checkUpdate;
    }

     @RequestMapping(value = "/vadilateUpdateStore", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public Integer vadilatorStore(@RequestParam("name") String name, @RequestParam("phone") String phone,@RequestParam("typeSearch") String typeSearch) throws SQLException, ClassNotFoundException, IOException {

        return store.validatorStore(name, phone, typeSearch);
    }
}
