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
    public boolean registerStore(@RequestBody String storeJSON) throws SQLException, ClassNotFoundException, IOException {
        JsonFactory factory = new JsonFactory();
        factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        ObjectMapper mapper = new ObjectMapper(factory);
        HashMap<String,String> Obj = mapper.readValue(storeJSON, HashMap.class);
        String storeObj = Obj.get("store");
        String locationObj = Obj.get("location");
        StoreEntites store = mapper.readValue(storeObj, StoreEntites.class);
        LocationEntites location = mapper.readValue(locationObj, LocationEntites.class);
        boolean check = this.store.registerStore(store,location);
        return check;
    }
}
