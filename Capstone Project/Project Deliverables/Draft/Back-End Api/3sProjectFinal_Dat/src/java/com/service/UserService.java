/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.entites.UserEntites;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author TUYEN
 */
public interface UserService {
    public List<UserEntites> getAllUserForAdmin() throws SQLException;
}
