/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import static com.dao.BaseDao.closeConnect;
import com.entites.LocationEntites;
import com.entites.StoreEntites;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Repository;

/**
 *
 * @author DatNQ
 */
@Repository("storeDao")
public class StoreDaoImpl extends BaseDao implements StoreDao {

    private final static String REGISTER_STORE_INSERT = "update Store set name = ? , location_id = ? , phone = ? , status = 1 where user_id = ?";
    private final static String UPDATE_HAS_STORE_USER = "update User set hasStore = 1 where id = ?";
    private final static String REGISTER_LOCATION_INSERT = "insert into Location(apartment_number,street,county,district,city,longitude,latitude) values (?,?,?,?,?,?,?)";
    private final static String GET_LAST_LOCATION_ID = "select MAX(id) as id from Location";
    private final static String GET_LOCATION_ID = "select * from Location where latitude = ? and longitude = ?";

    @Override
    public String registerStore(StoreEntites store, LocationEntites location) throws SQLException {
        String result = "";
        Connection conn = null;
        PreparedStatement pre = null;
        int count = 0;
        int locationId = -1;
        try {
            conn = getConnection();
            locationId = getLocaionIdIfExist(location.getLatitude(), location.getLongitude());
            if (locationId == -1) {
                pre = conn.prepareStatement(REGISTER_LOCATION_INSERT);
                pre.setInt(1, location.getApartment_number());
                pre.setString(2, location.getStreet());
                pre.setString(3, location.getCounty());
                pre.setString(4, location.getDistrict());
                pre.setString(5, location.getCity());
                pre.setString(6, location.getLongitude());
                pre.setString(7, location.getLatitude());
                count = pre.executeUpdate();
                if (count == 0) {
                    conn.setAutoCommit(false);
                    conn.rollback();
                    return "FAILED REGISTER_LOCATION_INSERT";
                }
                locationId = getLocationId();
                result = "LOCATION NOT EXSIST";
                if (locationId == -1) {
                    return "FAILED";
                }
            } else {
                result = "LOCATION EXSIST";
            }
            pre = conn.prepareStatement(REGISTER_STORE_INSERT);

            pre.setString(1, store.getName());
            pre.setInt(2, locationId);
            pre.setInt(4, store.getUser_id());
            pre.setString(3, store.getPhone());
            count = pre.executeUpdate();
            if (count == 0) {
                conn.setAutoCommit(false);
                conn.rollback();
                return "FAILED";
            }
               pre = conn.prepareStatement(UPDATE_HAS_STORE_USER);
               pre.setInt(1, store.getUser_id());
               count = pre.executeUpdate();
               if (count == 0) {
                conn.setAutoCommit(false);
                conn.rollback();
                return "FAILED";
            }
        } catch (Exception e) {
            conn.setAutoCommit(false);
            conn.rollback();
            return "FAILED";
        } finally {
            closeConnect(conn, pre, null);
        }
        return result;
    }

    private int getLocationId() {
        Connection conn = null;
        PreparedStatement pre = null;
        try {
            conn = getConnection();
            pre = conn.prepareStatement(GET_LAST_LOCATION_ID);

            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            closeConnect(conn, pre, null);
            return -1;
        } finally {
            closeConnect(conn, pre, null);
        }
        return -1;
    }

    private int getLocaionIdIfExist(String latitude, String longtitude) {
        Connection conn = null;
        PreparedStatement pre = null;
        try {
            conn = getConnection();
            pre = conn.prepareStatement(GET_LOCATION_ID);
            pre.setString(1, latitude);
            pre.setString(2, longtitude);

            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            closeConnect(conn, pre, null);
            return -1;
        } finally {
            closeConnect(conn, pre, null);
        }
        return -1;
    }
}
