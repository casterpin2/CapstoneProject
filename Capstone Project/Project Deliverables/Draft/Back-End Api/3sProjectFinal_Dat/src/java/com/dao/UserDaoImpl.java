/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.entites.UserEntites;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TUYEN
 */
@Repository("userDao")
public class UserDaoImpl extends BaseDao implements UserDao {

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
            String sql = "select username, password,device_id,role_id,first_name,last_name,location_id,role_id from User";
            pre = conn.prepareStatement(sql);
            rs = pre.executeQuery();
            while (rs.next()) {
                us = new UserEntites();
                us.setUserName(rs.getString("username"));
                us.setPassword(rs.getString("password"));
                us.setDeviceId(rs.getString("device_id"));
                us.setFirstName(rs.getString("first_name"));
                us.setLastName(rs.getString("last_name"));
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

}
