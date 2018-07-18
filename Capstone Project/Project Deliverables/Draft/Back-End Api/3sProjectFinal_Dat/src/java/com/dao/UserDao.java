/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.entites.UserEntites;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author TUYEN
 */
public interface UserDao {
    public List<UserEntites> getAllUserForAdmin() throws SQLException;
    //lấy user theo người dùng nhập lúc đăng ký và đăng nhập
    public int userHasExists( String username, String email, String phone,String typeSearch) throws SQLException;
    //danh ky
    public Boolean registerUser(UserEntites us) throws SQLException;
    //Đăng nhập
    public HashMap<String,Object> login(String username,String password) throws SQLException;
    public HashMap<String,Object> loginFB(UserEntites user,String FBId) throws SQLException;
}
