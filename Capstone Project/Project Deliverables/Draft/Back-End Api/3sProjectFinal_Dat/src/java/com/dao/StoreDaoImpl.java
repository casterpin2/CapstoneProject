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

    private final static String REGISTER_STORE_INSERT = "insert into Store(name,location_id,user_id,phone,status) values (?,?,?,?,1)";
    private final static String REGISTER_LOCATION_INSERT = "insert into Location(apartment_number,street,county,district,city,longitude,latitude) values (?,?,?,?,?,?,?)";
    private final static String GET_LAST_LOCATION_ID = "select MAX(id) as id from Location";

    @Override
    public boolean registerStore(StoreEntites store,LocationEntites location) throws SQLException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement pre = null;
        int count = 0;
        try {
            conn = getConnection();
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
                conn.rollback();
                conn.setAutoCommit(true);
                return check;
            }
            //conn.setAutoCommit(false);
            int locationId = getLocationId();
            if (locationId == -1) {
                return check;
            }
            pre = conn.prepareStatement(REGISTER_STORE_INSERT);

            pre.setString(1, store.getName());
            pre.setString(2,String.valueOf(locationId));
            pre.setInt(3, store.getUser_id());
            pre.setString(4, store.getPhone());
            count = pre.executeUpdate();
            if (count == 0) {
                conn.rollback();
                conn.setAutoCommit(true);
                return check;
            }

        } catch (Exception e) {
            conn.rollback();
            conn.setAutoCommit(true);
            return check;
        } finally {
            closeConnect(conn, pre, null);
            check = true;
        }
        return check;
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
}
