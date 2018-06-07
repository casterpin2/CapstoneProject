/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serviceImpl;

import com.daoImpl.UserDaoImpl;
import com.entities.UserEntities;
import com.service.UserService;
import java.sql.SQLException;
import java.util.List;


/**
 *
 * @author TUYEN
 */

public class UserServiceImpl implements UserService {

    public UserServiceImpl() {
    }
   
    @Override
    public List<UserEntities> listUserForAdmin() throws SQLException {
        return UserDaoImpl.getInstance().listUserForAdmin();
    }

}
