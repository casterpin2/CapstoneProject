/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.dao.EmployeeDAO;
import com.entites.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author TUYEN
 */
@Service ("employeeManager")
public class EmployeeManagerImpl implements EmployeeManager {

    @Autowired
    EmployeeDAO dao;

    @Override
    public EmployeeDTO createNewEmployee() {
         return dao.createNewEmployee();
    }

}
