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
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private final String GET_STORE_WITH_USER_ID = "select a.id,a.name,location_id,user_id,phone,status,registerLogFormat,b.path from\n"
            + "(select a.*,b.image_id from\n"
            + "(select *, DATE_FORMAT(registerLog,\"%d/%m/%Y\")  as registerLogFormat from Store where user_id = ?) a , Image_Store b where a.id = b.store_id) a , Image b where a.image_id = b.id";
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

    private StoreEntites getStore(int userId) {
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
        } catch (SQLException e) {
            closeConnect(conn, pre, null);
            return null;
        } finally {
            closeConnect(conn, pre, null);
        }
        return null;
    }

    @Override
    public boolean deleteProductInStore(int storeId, int productId) throws SQLException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement pre = null;
        int count = 0;
        try {
            String sql = "delete from Product_Store where product_id = ? and store_id = ?";
            conn = getConnection();
            conn.setAutoCommit(false);
            pre = conn.prepareStatement(sql);
            pre.setInt(2, storeId);
            pre.setInt(1, productId);
            count++;
            if (pre.executeUpdate() == count) {
                conn.commit();
                check = true;
            } else {
                conn.rollback();
                conn.setAutoCommit(true);
            }
        } finally {
            closeConnect(conn, pre, null);
        }
        return check;
    }

    @Override
    public boolean editProductInStore(int storeId, int productId, double price, double promotion) throws SQLException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement pre = null;
        int count = 0;
        try {
            String sql = "update Product_Store set price = ? , promotion = ? where product_id = ? and store_id = ?";
            conn = getConnection();
            conn.setAutoCommit(false);
            pre = conn.prepareStatement(sql);
            pre.setDouble(1, price);
            pre.setDouble(2, promotion);
            pre.setInt(4, storeId);
            pre.setInt(3, productId);
            count++;
            if (pre.executeUpdate() == count) {
                conn.commit();
                check = true;
            } else {
                conn.rollback();
                conn.setAutoCommit(true);
            }
        } finally {
            closeConnect(conn, pre, null);
        }
        return check;
    }

    @Override
    public StoreEntites getStoreById(int storeId) throws SQLException {
        Connection conn = null;
        PreparedStatement pre = null;
        StoreEntites se = null;
        try {
            se = new StoreEntites();
            conn = getConnection();
            pre = conn.prepareStatement("select s.*,u.full_name,i.path,DATE_FORMAT(registerLog,\"%d/%m/%Y\")  as registerLogFormat,l.* from Store s join (Image i join Image_Store it on i.id = it.image_id) on it.store_id = s.id and s.id = ? join User u on s.user_id = u.id join Location l on l.id = s.location_id");
            pre.setInt(1, storeId);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                se.setId(rs.getInt("id"));
                se.setName(rs.getString("name"));
                se.setLocation_id(rs.getInt("location_id"));
                se.setUser_id(rs.getInt("user_id"));
                se.setPhone(rs.getString("phone"));
                se.setImage_path(rs.getString("path"));
                se.setRegisterLog(rs.getString("registerLogFormat"));
                se.setUser_name(rs.getNString("full_name"));
                se.setAddress(rs.getString("apartment_number") + " " + rs.getString("street") + " " + rs.getString("county") + " " + rs.getString("district") + " " + rs.getString("city"));
                se.setAddress(se.getAddress().replaceAll("0", "").replaceAll("Unnamed Road", "").replaceAll("\\s+", " ").replaceAll("null", "").trim());
                return se;
            }
        } catch (SQLException e) {
            closeConnect(conn, pre, null);
            return null;
        } finally {
            closeConnect(conn, pre, null);
        }
        return null;
    }

    @Override
    public StoreEntites updateStore(StoreEntites store, LocationEntites location) throws SQLException {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        boolean checkUpdateStore = false;
        boolean checkUpdateLocation = false;
        StoreEntites storeData = null;
        int locationId = 0;
        try {
            String sql = "Update Store set name =? ,phone =? where id = ?";
            conn = getConnection();
            conn.setAutoCommit(false);
            pre = conn.prepareStatement(sql);
            pre.setString(1, store.getName());
            pre.setString(2, store.getPhone());
            pre.setInt(3, store.getId());
            int updateSuccess = pre.executeUpdate();
            if (updateSuccess > 0) {
                conn.commit();
                checkUpdateStore = true;

            } else {
                conn.rollback();
                conn.setAutoCommit(true);
            }
            if (checkLocation(location)) {
                checkUpdateLocation = updateLocationByStore(conn, location, store.getLocation_id());
            } else {
                checkUpdateLocation = true;
            }

            if (checkUpdateLocation && checkUpdateStore) {
                storeData = getStore(conn, store, store.getImage_path());
            }
        } finally {
            closeConnect(conn, pre, rs);
        }

        return storeData;
    }

    public boolean updateLocationByStore(Connection conn, LocationEntites location, int locationId) throws SQLException {
        PreparedStatement pre = null;
        try {
            String updateLocation = "Update Location set apartment_number =? , street =?, county =? , district =?,city =? , longitude =?,latitude=? where id =?";
            conn.setAutoCommit(false);
            pre = conn.prepareStatement(updateLocation);
            pre.setString(1, location.getApartment_number());
            pre.setString(2, location.getStreet());
            pre.setString(3, location.getCounty());
            pre.setString(4, location.getDistrict());
            pre.setString(5, location.getCity());
            pre.setString(6, location.getLongitude());
            pre.setString(7, location.getLatitude());
            pre.setInt(8, locationId);
            int checkUpdateLocation = pre.executeUpdate();
            if (checkUpdateLocation > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                conn.setAutoCommit(true);
            }
        } finally {
            closeConnect(null, pre, null);
        }
        return false;
    }

    public StoreEntites getStore(Connection conn, StoreEntites storeData, String imgPath) {
        PreparedStatement pre = null;
        ResultSet rs = null;
        StoreEntites store = null;
        String sql = "SELECT s.id as storeId,s.user_id,s.location_id,s.name as nameStore,\n"
                + "                 s.phone,s.status,l.apartment_number,l.street,l.district,l.county,\n"
                + "                l.city,l.latitude,l.longitude \n"
                + "                FROM Store s JOIN Location as l on s.location_id = l.id where s.id =?";
        try {
            pre = conn.prepareStatement(sql);
            pre.setInt(1, storeData.getId());
            rs = pre.executeQuery();
            if (rs.next()) {
                store = new StoreEntites();
                store.setId(rs.getInt("storeId"));
                if (rs.getString("apartment_number") != null) {
                    store.setAddress(rs.getString("apartment_number") + " " + rs.getString("street") + " " + rs.getString("county") + " " + rs.getString("city"));
                } else {
                    store.setAddress(rs.getString("street") + " " + rs.getString("county") + " " + rs.getString("city"));
                }

                store.setImage_path(imgPath);
                store.setLatitude(rs.getString("latitude"));
                store.setLongtitude(rs.getString("longitude"));
                store.setName(rs.getString("nameStore"));
                store.setPhone(rs.getString("phone"));
                store.setStatus(rs.getInt("status"));
                store.setUser_id(rs.getInt("user_id"));
                store.setLocation_id(rs.getInt("location_id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoreDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnect(null, pre, rs);
        }
        return store;

    }

    public boolean checkLocation(LocationEntites location) {
        boolean check = false;
        if(location.getApartment_number()!=null){
            check = true;
        }
        if(location.getCity()!=null){
            check = true;
        }
        if(location.getCounty()!=null){
            check = true;
        }
        if(location.getDistrict()!=null){
            check = true;
        }
        if(location.getLatitude()!=null){
            check = true;
        }
        if(location.getLongitude()!=null){
            check = true;
        }
        if(location.getStreet()!=null){
            check = true;
        }
        
        return check;
    }
}
