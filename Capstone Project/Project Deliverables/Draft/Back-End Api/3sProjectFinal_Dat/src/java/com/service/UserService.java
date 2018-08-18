/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.entites.FeedbackEntites;
import com.entites.NearByStore;
import com.entites.ProductAddEntites;
import com.entites.UserEntites;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author TUYEN
 */
public interface UserService {
    //User tim kiem san pham
    public List<ProductAddEntites> userSearchProduct(String productName,int page) throws SQLException;
    //Business Core
    public List<NearByStore> nearByStore(int productId, String latitude, String longitude) throws SQLException;
    
    public List<UserEntites> getAllUserForAdmin() throws SQLException;
    //lấy user theo người dùng nhập lúc đăng ký và đăng nhập
    public int userHasExists( String username, String email, String phone,String typeSearch) throws SQLException;
    public String registerUser(UserEntites us) throws SQLException;
    //Đăng nhập
    public HashMap<String,Object> login(String username,String password) throws SQLException;
    public HashMap<String,Object> loginFB(UserEntites user,String FBId) throws SQLException;
    public HashMap<String,Object> loginG(UserEntites user,String GId) throws SQLException;
    public boolean getFeedback(FeedbackEntites feedback) throws SQLException;
    //update 
    public UserEntites updateInformation(UserEntites user) throws SQLException;
    public UserEntites informationUser(int userId) throws SQLException;
    public UserEntites getPhoneNumberOfUser(String username) throws SQLException;
    public boolean changePassword(String username,String password) throws SQLException;
}
