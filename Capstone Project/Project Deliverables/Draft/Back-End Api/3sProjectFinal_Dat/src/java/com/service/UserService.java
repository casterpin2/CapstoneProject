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
    //lấy user theo người dùng nhập lúc đăng ký và đăng nhập
    public int userHasExists( String username, String email, String phone,String typeSearch) throws SQLException;
    public Boolean registerUser(UserEntites us) throws SQLException;
}
