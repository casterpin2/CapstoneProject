/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.dao.UserDao;
import com.entites.UserEntites;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author TUYEN
 */
@Service("userService")
public class UserServiceImpl implements UserService{

    @Autowired
    UserDao dao;

    @Override
    public List<UserEntites> getAllUserForAdmin() throws SQLException {
       return dao.getAllUserForAdmin();
    }
}
