/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.daoImpl;

import com.dao.BaseDao;
import com.dao.UserDao;
import com.entities.UserEntities;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TUYEN
 */
public class UserDaoImpl extends BaseDao implements UserDao {

    private static UserDaoImpl instance;

    private UserDaoImpl() {

    }

    public static UserDaoImpl getInstance() {
        if (instance == null) {
            instance = new UserDaoImpl();
        }
        return instance;
    }

    @Override
    public List<UserEntities> listUserForAdmin() throws SQLException {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        List<UserEntities> listData = null;
        UserEntities us = null;
        try {
            listData = new ArrayList<>();
            conn = getConnection();
            String sql = "SELECT id,username,password,first_name,last_name,location_id,device_id,role_id FROM user where role_id = ?";
            pre = conn.prepareStatement(sql);
            pre.setInt(1, 1);
            rs = pre.executeQuery();
            while (rs.next()) {
                us = new UserEntities();
                us.setId(rs.getInt("id"));
                us.setUsername(rs.getString("username"));
                us.setPassword(rs.getString("password"));
                us.setFirstName(rs.getNString("first_name"));
                us.setLastName(rs.getNString("last_name"));
                us.setLocationID(rs.getString("location_id"));
                us.setDeviceId(rs.getInt("device_id"));
                us.setRoleId(rs.getInt("role_id"));
                listData.add(us);
            }
        } finally {
            closeConnect(conn, pre, rs);
        }
        return listData;
    }

}
