/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entites.JsonUtil;
import com.entites.UserEntites;
import com.service.UserService;
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

    @RequestMapping(value = "/registerUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Boolean registerUser(@RequestParam("jsonString") String jsonString) throws SQLException, ClassNotFoundException, IOException {
        List<UserEntites> list = JsonUtil.converJsonToJava(jsonString, UserEntites.class);    
        return user.registerUser(list.get(0));
    }

    @RequestMapping(value = "/vadilateRegisterUser", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public Integer vadilatorUser(@RequestParam("username") String username, @RequestParam("email") String email, @RequestParam("phone") String phone, @RequestParam("typeSearch") String typeSearch) throws SQLException, ClassNotFoundException, IOException {

        return user.userHasExists(username, email, phone, typeSearch);
    }
}
