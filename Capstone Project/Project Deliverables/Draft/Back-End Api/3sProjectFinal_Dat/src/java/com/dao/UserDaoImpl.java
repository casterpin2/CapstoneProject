/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import static com.dao.BaseDao.closeConnect;
import com.entites.StoreEntites;
import com.entites.UserEntites;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TUYEN
 */
@Repository("userDao")
public class UserDaoImpl extends BaseDao implements UserDao {
    
    private String QUERY_LOGIN = "SELECT a.*,b.path as store_image_path FROM\n"
            + "(SELECT a.*,b.image_id as store_image_id FROM\n"
            + "(SELECT a.*,b.path as user_image_path FROM \n"
            + "(SELECT a.id as user_id,username,a.password,first_name,last_name,email,device_id,role_id,a.phone as user_phone,dateOfBirth,hasStore,gender,a.image_id as user_image_id,b.`location_id`, b.`phone` as store_phone, `status`,b.name,b.id as store_id FROM (SELECT * FROM User WHERE username = ? AND password = ?) as a , Store b WHERE a.id = b.user_id) a , Image b WHERE a.user_image_id = b.id) a , Image_Store b WHERE a.store_id = b.store_id) a , Image b WHERE a.store_image_id = b.id";
    private String QUERY_LOGIN_FB = "SELECT a.*,b.path as store_image_path FROM\n"
            + "(SELECT a.*,b.image_id as store_image_id FROM\n"
            + "(SELECT a.*,b.path as user_image_path FROM \n"
            + "(SELECT a.id as user_id,username,a.password,first_name,last_name,email,device_id,role_id,a.phone as user_phone,dateOfBirth,hasStore,gender,a.image_id as user_image_id,b.`location_id`, b.`phone` as store_phone, `status`,b.name,b.id as store_id FROM (SELECT * FROM User WHERE facebook_id = ?) as a , Store b WHERE a.id = b.user_id) a , Image b WHERE a.user_image_id = b.id) a , Image_Store b WHERE a.store_id = b.store_id) a , Image b WHERE a.store_image_id = b.id";

    private String INSERT_ACCOUNT_FB = "INSERT INTO User(first_name,last_name,email,hasStore,role_id,facebook_id,image_id) VALUES (?,?,?,0,2,?,?)";

    private String QUERY_GET_MAX_USER_ID = "SELECT MAX(id) FROM User";

    private String QUERY_GET_MAX_STORE_ID = "SELECT MAX(id) FROM Store";

    private String QUERY_GET_MAX_IMAGE_ID = "SELECT MAX(id) FROM Image";

    private String INSERT_STORE = "insert into Store(user_id) values (?)";

    private String INSERT_IMAGE = "INSERT INTO Image(name,path) VALUES (?,?)";

    private String INSERT_IMAGE_STORE = "INSERT INTO Image_Store(image_id,store_id) VALUES (53,?)";
    
    @Override
    public List<UserEntites> getAllUserForAdmin() throws SQLException {
        List<UserEntites> listData = null;
        UserEntites us = null;
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            listData = new ArrayList();
            conn = getConnection();
            String sql = "select username, password,device_id,role_id,first_name,last_name,location_id,role_id,dateOfBirth from User";
            pre = conn.prepareStatement(sql);
            rs = pre.executeQuery();
            while (rs.next()) {
                us = new UserEntites();
                us.setUserName(rs.getString("username"));
                us.setPassword(rs.getString("password"));
                us.setDeviceId(rs.getString("device_id"));
                us.setFirstName(rs.getString("first_name"));
                us.setLastName(rs.getString("last_name"));
                us.setDateOfBirth(rs.getString("dateOfBirth"));
                listData.add(us);
            }
        } finally {
            closeConnect(conn, pre, rs);
        }

        return listData;

    }

    @Override
    public int userHasExists(String username, String email, String phone, String typeSearch) throws SQLException {

        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            int tempSearch = 0;
            String sql = null;
            switch (typeSearch) {
                case "username":
                    sql = "select username,email,phone from User where username = ? ";
                    tempSearch = 1;
                    break;
                case "email":
                    sql = "select username,email,phone from User where email = ? ";
                    tempSearch = 2;
                    break;
                case "phone":
                    sql = "select username,email,phone from User where phone = ? ";
                    tempSearch = 3;
                    break;
            }

            conn = getConnection();
            //chèn các cho các biến đúng giá trị của nó       
            switch (tempSearch) {
                case 1:
                    pre = conn.prepareStatement(sql);
                    pre.setString(1, username);
                    rs = pre.executeQuery();
                    if (rs.next()) {
                        return tempSearch;
                    }
                    break;
                case 2:
                    pre = conn.prepareStatement(sql);
                    pre.setString(1, email);
                    rs = pre.executeQuery();
                    if (rs.next()) {
                        return tempSearch;
                    }
                    break;
                case 3:
                    pre = conn.prepareStatement(sql);
                    pre.setString(1, phone);
                    rs = pre.executeQuery();
                    if (rs.next()) {
                        return tempSearch;
                    }
                    break;
            }

        } finally {
            closeConnect(conn, pre, rs);
        }
        return 0;
    }

    @Override
    public Boolean registerUser(UserEntites us) throws SQLException {
        Connection conn = null;
        PreparedStatement pre = null;
        String message;
        try {
            String sql = "INSERT INTO User (username, password, first_name, last_name, email, location_id, role_id, phone, hasStore, gender) VALUES (?,?,?,?,?,?,?,?,?,?)";
            conn = getConnection();
            conn.setAutoCommit(false);
            pre = conn.prepareStatement(sql);
            pre.setString(1, us.getUserName());
            pre.setString(2, us.getPassword());
            pre.setString(3, "Tuyen");
            pre.setString(4, "Ngo");
            pre.setString(5, us.getEmail());
            pre.setInt(6, 1);
            pre.setInt(7, 1);
            pre.setString(8, us.getPhone());
            pre.setInt(9, 1);
            pre.setString(10, "Name");
            int countInsert = pre.executeUpdate();
            if (countInsert > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                conn.setAutoCommit(true);
                return false;
            }
        } finally {
            closeConnect(conn, pre, null);
        }

    }
    @Override
    public HashMap<String, Object> login(String username, String password) throws SQLException {
        HashMap<String, Object> hashMap = new HashMap<>();
        StoreEntites store = null;
        UserEntites us = null;
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pre = conn.prepareStatement(QUERY_LOGIN);
            pre.setString(1, username);
            pre.setString(2, password);
            rs = pre.executeQuery();
            while (rs.next()) {
                us = new UserEntites();
                us.setUserID(rs.getInt("user_id"));
                us.setUserName(rs.getString("username"));
                us.setDeviceId(rs.getString("device_id"));
                us.setFirstName(rs.getString("first_name"));
                us.setLastName(rs.getString("last_name"));
                us.setEmail(rs.getString("email"));
                us.setHasStore(rs.getInt("hasStore"));
                us.setGender(rs.getString("gender"));
                us.setDateOfBirth(rs.getString("dateOfBirth"));
                us.setPhone(rs.getString("user_phone"));
                us.setImage_path(rs.getString("user_image_path"));
                hashMap.put("user", us);
                store = new StoreEntites();
                store.setImage_path(rs.getString("store_image_path"));
                store.setName(rs.getString("name"));
                store.setId(rs.getInt("store_id"));
                store.setPhone(rs.getString("store_phone"));
                store.setStatus(rs.getInt("status"));
                store.setUser_id(rs.getInt("user_id"));
                hashMap.put("store", store);
            }
        } finally {
            closeConnect(conn, pre, rs);
        }
        return hashMap;
    }

    @Override
    public HashMap<String, Object> loginFB(UserEntites user, String FBId) throws SQLException {
        HashMap<String, Object> hashMap = new HashMap<>();
        StoreEntites store = null;
        UserEntites us = null;
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pre = conn.prepareStatement(QUERY_LOGIN_FB);
            pre.setString(1, FBId);
            rs = pre.executeQuery();
            if (rs.next()) {
                us = new UserEntites();
                us.setUserID(rs.getInt("user_id"));
                us.setUserName(rs.getString("username"));
                us.setDeviceId(rs.getString("device_id"));
                us.setFirstName(rs.getString("first_name"));
                us.setLastName(rs.getString("last_name"));
                us.setEmail(rs.getString("email"));
                us.setHasStore(rs.getInt("hasStore"));
                us.setGender(rs.getString("gender"));
                us.setDateOfBirth(rs.getString("dateOfBirth"));
                us.setPhone(rs.getString("user_phone"));
                us.setImage_path(rs.getString("user_image_path"));
                hashMap.put("user", us);
                store = new StoreEntites();
                store.setImage_path(rs.getString("store_image_path"));
                store.setName(rs.getString("name"));
                store.setId(rs.getInt("store_id"));
                store.setPhone(rs.getString("store_phone"));
                store.setStatus(rs.getInt("status"));
                store.setUser_id(rs.getInt("user_id"));
                hashMap.put("store", store);
            } else {
                int user_id = 0;
                int image_id = 0;
                int store_id = 0;
                pre = conn.prepareStatement(INSERT_IMAGE);
                pre.setString(1, "Ảnh của user FBId : " + FBId);
                pre.setString(2, user.getImage_path());
                if (pre.executeUpdate() == 0) {
                    conn.rollback();
                    conn.setAutoCommit(true);
                    return null;
                }
                pre = conn.prepareStatement(QUERY_GET_MAX_IMAGE_ID);
                rs = pre.executeQuery();
                if (rs.next()) {
                    image_id = rs.getInt("MAX(id)");
                }
                pre = conn.prepareStatement(INSERT_ACCOUNT_FB);
                pre.setString(1, user.getFirstName());
                pre.setString(2, user.getLastName());
                pre.setString(3, user.getEmail());
                pre.setString(4, FBId);
                pre.setInt(5, image_id);
                if (pre.executeUpdate() == 0) {
                    conn.rollback();
                    conn.setAutoCommit(true);
                    return null;
                } else {
                    pre = conn.prepareStatement(QUERY_GET_MAX_USER_ID);
                    rs = pre.executeQuery();
                    if (rs.next()) {
                        user_id = rs.getInt("MAX(id)");
                    }
                }
                pre = conn.prepareStatement(INSERT_STORE);
                pre.setInt(1, user_id);
                if (pre.executeUpdate() == 0) {
                    conn.rollback();
                    conn.setAutoCommit(true);
                    return null;
                } else {
//                    user.setId(user_id);
//                    hashMap.put("user", user);
//                    hashMap.put("store", new StoreEntites(store_id, "", user_id, "", "", 0));
                    pre = conn.prepareStatement(QUERY_GET_MAX_STORE_ID);
                    rs = pre.executeQuery();
                    if (rs.next()) {
                        store_id = rs.getInt("MAX(id)");
                    }
                    pre = conn.prepareStatement(INSERT_IMAGE_STORE);
                    pre.setInt(1, store_id);
                    if (pre.executeUpdate() == 0) {
                        conn.rollback();
                        conn.setAutoCommit(true);
                        return null;
                    }
                }
                hashMap.put("user", user);
                hashMap.put("store", new StoreEntites(store_id, "", user_id, "", "", 0));
            }
        } finally {
            closeConnect(conn, pre, rs);
        }
        return hashMap;
    }
}
