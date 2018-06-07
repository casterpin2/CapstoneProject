/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.entites.EmployeeDTO;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TUYEN
 */
@Repository ("employeeDao")
public class EmployeeDAOImpl implements EmployeeDAO {

    @Override
    public EmployeeDTO createNewEmployee() {
        EmployeeDTO e = new EmployeeDTO();
        e.setId(1);
        e.setFirstName("Lokesh");
        e.setLastName("Gupta");
        return e;
    }

}
