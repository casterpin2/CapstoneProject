/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.entites.EmployeeDTO;
import com.service.EmployeeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
public class EmployeeController {

    @Autowired
    EmployeeManager manager;
    
    @RequestMapping(value = "/login",method = RequestMethod.GET,produces="application/json")
    public EmployeeDTO createNewEmployee() {
        return manager.createNewEmployee();
    }
}
