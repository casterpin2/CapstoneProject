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

    private final String REGISTER_STORE_INSERT = "update Store set name = ? , location_id = ? , phone = ? , status = 1 where user_id = ?";
    private final String UPDATE_HAS_STORE_USER = "update User set hasStore = 1 where id = ?";
    private final String REGISTER_LOCATION_INSERT = "insert into Location(apartment_number,street,county,district,city,longitude,latitude) values (?,?,?,?,?,?,?)";
    private final String GET_LAST_LOCATION_ID = "select MAX(id) as id from Location";
    private final String GET_LOCATION_ID = "select * from Location where latitude = ? and longitude = ?";
    private final String GET_STORE_WITH_USER_ID = "select a.id,a.name,location_id,user_id,phone,status,registerLogFormat,b.path from\n" +
                                                  "(select a.*,b.image_id from\n" +
                                                  "(select *, DATE_FORMAT(registerLog,\"%d/%m/%Y\")  as registerLogFormat from Store where user_id = ?) a , Image_Store b where a.id = b.store_id) a , Image b where a.image_id = b.id";
    private final String QUERY_LOCATION = "SELECT * FROM Location WHERE id = ?";
    @Override
    public StoreEntites registerStore(StoreEntites store, LocationEntites location) throws SQLException {
        StoreEntites result = new StoreEntites();
        Connection conn = null;
        PreparedStatement pre = null;
        int count = 0;
        int locationId = -1;
        try {
            conn = getConnection();
            locationId = getLocaionIdIfExist(location.getLatitude(), location.getLongitude());
            if (locationId == -1) {
                pre = conn.prepareStatement(REGISTER_LOCATION_INSERT);
                pre.setString(1, location.getApartment_number());
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
                    return null;
                }
                locationId = getLocationId();
                if (locationId == -1) {
                    return null;
                }
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
                return null;
            }
               pre = conn.prepareStatement(UPDATE_HAS_STORE_USER);
               pre.setInt(1, store.getUser_id());
               count = pre.executeUpdate();
               if (count == 0) {
                conn.setAutoCommit(false);
                conn.rollback();
                return null;
            }
        } catch (Exception e) {
            conn.setAutoCommit(false);
            conn.rollback();
            return null;
        } finally {
            result = getStore(store.getUser_id());
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
    
    private StoreEntites getStore(int userId){
        Connection conn = null;
        PreparedStatement pre = null;
        StoreEntites se = null;
        try {
            se = new StoreEntites();
            conn = getConnection();
            pre = conn.prepareStatement(GET_STORE_WITH_USER_ID);
            pre.setInt(1, userId);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                se.setId(rs.getInt("id"));
                se.setName(rs.getString("name"));
                se.setLocation_id(rs.getInt("location_id"));
                se.setUser_id(rs.getInt("user_id"));
                se.setPhone(rs.getString("phone"));
                se.setImage_path(rs.getString("path"));
                se.setRegisterLog(rs.getString("registerLogFormat"));
                pre = conn.prepareStatement(QUERY_LOCATION);
                pre.setInt(1, se.getLocation_id());
                rs = pre.executeQuery();
                rs.next();
                se.setAddress(rs.getString("apartment_number") + " " + rs.getString("street") + " " + rs.getString("county") + " " + rs.getString("district") + " " + rs.getString("city"));
                se.setLatitude(rs.getString("latitude"));
                se.setLongtitude(rs.getString("longitude"));
                return se;
            }
        }
        catch (SQLException e) {
            closeConnect(conn, pre, null);
            return null;
        } finally {
            closeConnect(conn, pre, null);
        }
        return null;
    }
}
