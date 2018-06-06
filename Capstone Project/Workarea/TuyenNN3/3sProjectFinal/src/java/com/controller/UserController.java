/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.daoImpl.UserDaoImpl;
import com.entities.UserEntities;
import com.service.UserService;
import com.serviceImpl.UserServiceImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(value = "/getAllUser", method = RequestMethod.GET, produces = "application/json")
    public List<UserEntities> listUserForAdmin() throws ClassNotFoundException, SQLException {

        return UserDaoImpl.getInstance().listUserForAdmin();
    }

}
