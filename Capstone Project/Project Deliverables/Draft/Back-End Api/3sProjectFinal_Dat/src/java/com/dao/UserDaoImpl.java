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
@Repository ("userDao")
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
                us.setUsername(rs.getString("username"));
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

}
