/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.dao.UserDao;
import com.entites.NearByStore;
import com.entites.ProductAddEntites;
import com.entites.UserEntites;
import java.sql.SQLException;
import java.util.HashMap;
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

    @Override
    public int userHasExists(String username, String email, String phone,String typeSearch) throws SQLException {
        return dao.userHasExists(username,email,phone,typeSearch);
    }

    @Override
    public String registerUser(UserEntites us) throws SQLException {
     return  dao.registerUser(us);
    }
    
    @Override
    public HashMap<String, Object> login(String username, String password) throws SQLException {
        return dao.login(username, password);
    }

    @Override
    public HashMap<String, Object> loginFB(UserEntites user, String FBId) throws SQLException {
        return dao.loginFB(user, FBId);
    }

    @Override
    public List<ProductAddEntites> userSearchProduct(String productName,int page) throws SQLException {
        return dao.userSearchProduct(productName,page);
    }

    @Override
    public List<NearByStore> nearByStore(int productId, String latitude, String longitude) throws SQLException {
        return dao.nearByStore(productId, latitude, longitude);
    }
    
    @Override
    public UserEntites updateInformation(UserEntites user) throws SQLException {
        return dao.updateInformation(user);
}

    @Override
    public UserEntites informationUser(int userId) throws SQLException {
        return dao.informationUser(userId);
    }

    @Override
    public UserEntites getPhoneNumberOfUser(String username) throws SQLException {
        return dao.getPhoneNumberOfUser(username);
    }

    @Override
    public boolean changePassword(String username, String password) throws SQLException {
       return dao.changePassword(username, password);
    }

   
    
}
