/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entites.JsonUtil;
import com.entites.NearByStore;
import com.entites.ProductAddEntites;
import com.entites.UserEntites;
import com.service.UserService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
public class UserController {

    @Autowired
    UserService user;

    @RequestMapping(value = "/getAllUser", method = RequestMethod.GET, produces = "application/json")
    public List<UserEntites> createNewEmployee() throws SQLException {

        return user.getAllUserForAdmin();
    }

    @RequestMapping(value = "/registerUser", method = RequestMethod.POST, produces = "application/json")
    public String registerUser(@RequestBody String jsonString) throws SQLException, ClassNotFoundException, IOException {
        JsonFactory factory = new JsonFactory();
        factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        ObjectMapper mapper = new ObjectMapper(factory);
        UserEntites us = mapper.readValue(jsonString, UserEntites.class);
        //String result = 
        return "{\"result\":" + "\"" + user.registerUser(us) + "\"}";
    }

    @RequestMapping(value = "/vadilateRegisterUser", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public Integer vadilatorUser(@RequestParam("username") String username, @RequestParam("email") String email, @RequestParam("phone") String phone, @RequestParam("typeSearch") String typeSearch) throws SQLException, ClassNotFoundException, IOException {

        return user.userHasExists(username, email, phone, typeSearch);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = "application/json")
    public HashMap<String, Object> login(@RequestParam("username") String username, @RequestParam("password") String password) throws SQLException {

        return user.login(username, password);
    }

    @RequestMapping(value = "/loginFB", method = RequestMethod.POST, produces = "application/json")
    public HashMap<String, Object> loginFB(@RequestBody String userJSON, @RequestParam("FBId") String FBid) throws SQLException, IOException {
        JsonFactory factory = new JsonFactory();
        factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        ObjectMapper mapper = new ObjectMapper(factory);
        UserEntites us = mapper.readValue(userJSON, UserEntites.class);
        return user.loginFB(us, FBid);
    }

    @RequestMapping(value = "/userSearchProduct", method = RequestMethod.GET, produces = "application/json")
    public List<ProductAddEntites> userSearchProduct(@RequestParam("productName") String productName) throws SQLException, IOException {
        return user.userSearchProduct(productName);
    }

    @RequestMapping(value = "/findStore", method = RequestMethod.GET, produces = "application/json")
    public List<NearByStore> nearByStore(@RequestParam("productId") int productId, @RequestParam("latitude") String latitude, @RequestParam("longitude") String longitude) throws SQLException, IOException {
        return user.nearByStore(productId, latitude, longitude);
    }

    @RequestMapping(value = "/informationUser", method = RequestMethod.GET, produces = "application/json")
    public UserEntites getUserInformatiopn(@RequestParam("userId") int userId) throws SQLException, IOException {
        return user.informationUser(userId);
    }
    @RequestMapping(value = "/updateInformation", method = RequestMethod.PUT, produces = "application/json")
    public UserEntites updateInformation(@RequestBody String userJson) throws SQLException, IOException {
        JsonFactory factory = new JsonFactory();
        factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        ObjectMapper mapper = new ObjectMapper(factory);
        UserEntites us = mapper.readValue(userJson, UserEntites.class);
        return user.updateInformation(us);
    }
    
}
