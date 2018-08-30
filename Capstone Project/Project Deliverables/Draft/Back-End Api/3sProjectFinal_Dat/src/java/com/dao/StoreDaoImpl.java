/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import static com.dao.BaseDao.closeConnect;
import com.entites.FeedbackEntites;
import com.entites.LocationEntites;
import com.entites.StoreEntites;
import com.entites.UserEntites;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Repository;

/**
 *
 * @author DatNQ
 */
@Repository("storeDao")
public class StoreDaoImpl extends BaseDao implements StoreDao {

    private final String REGISTER_STORE_INSERT = "update Store set name = ? , location_id = ? , phone = ? , status = 1 , registerLog = ? where user_id = ?";
    private final String UPDATE_HAS_STORE_USER = "update User set hasStore = 1 where id = ?";
    private final String CHECK_PHONE_STORE = "select * from Store where phone = ?";
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
            pre = conn.prepareStatement(CHECK_PHONE_STORE);
            pre.setString(1, store.getPhone());
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                return result;
            }
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
            pre.setInt(5, store.getUser_id());
            pre.setString(4, store.getRegisterLog());
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
                se.setAddress(se.getAddress().replaceAll("0", "").replaceAll("Unnamed Road", "").replaceAll("\\s+", " ").replaceAll("null", "").trim());
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
                pre = conn.prepareStatement("select smile,sad from (select COUNT(*) as smile from Feedback where satisfied = 1 and store_id = ?) a, (select COUNT(*) as sad from Feedback where satisfied = 0 and store_id = ?) b");
                pre.setInt(1, storeId);
                pre.setInt(2, storeId);
                rs = pre.executeQuery();
                rs.next();
                se.setSmile(rs.getInt("smile"));
                se.setSad(rs.getInt("sad"));
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
            int idExist = checkLocationExist(location, conn);
            if (idExist > 0) {
                updateLocationByStore(conn, location, idExist);
                updateUserLocation(conn, idExist, store);
            } else {
                boolean checkNewInsert = insertNewLocation(location, conn);
                int idNew = checkLocationExist(location, conn);
                if (checkNewInsert && idNew > 0) {
                    checkUpdateLocation = updateUserLocation(conn, idNew, store);
                }
            }
            storeData = getStore(conn, store, store.getImage_path());
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
        String sql = "SELECT s.id as storeId,s.user_id,s.location_id,s.name as nameStore,DATE_FORMAT(s.registerLog,\"%d/%m/%Y\")  as registerLogFormat,\n"
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
                    store.setAddress(rs.getString("apartment_number") + " " + rs.getString("street") + " " + rs.getString("county") + " " + rs.getString("district") + " " + rs.getString("city"));
                } else {
                    store.setAddress(rs.getString("street") + " " + rs.getString("county") + " " + rs.getString("district") + " " + rs.getString("city"));
                }
                if (!rs.getString("street").equals("Unnamed Road")) {
                    store.setAddress(rs.getString("apartment_number") + " " + rs.getString("street") + " " + rs.getString("county") + " " + rs.getString("district") + "-" + rs.getString("city"));
                    store.setAddress(store.getAddress().replaceAll("0", "").replaceAll("\\s+", " ").replaceAll("null", "").trim());
                } else {
                    store.setAddress(rs.getString("apartment_number") + " " + rs.getString("county") + " " + rs.getString("district") + " " + rs.getString("city"));
                    store.setAddress(store.getAddress().replaceAll("0", "").replaceAll("\\s+", " ").replaceAll("null", "").trim());
                }
                store.setRegisterLog(rs.getString("registerLogFormat"));
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

    public int checkLocationExist(LocationEntites location, Connection conn) {
        int storeExist = 0;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT id FROM Location where longitude = ? and latitude = ?";
            pre = conn.prepareStatement(sql);
            pre.setString(1, location.getLongitude());
            pre.setString(2, location.getLatitude());
            rs = pre.executeQuery();
            if (rs.next()) {
                storeExist = rs.getInt("id");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnect(null, pre, rs);
        }
        return storeExist;
    }

    public boolean insertNewLocation(LocationEntites locationEntites, Connection conn) {
        String insert = "insert into Location(apartment_number,street,county,district,city,longitude,latitude) values (?,?,?,?,?,?,?)";
        PreparedStatement pre = null;

        try {
            pre = conn.prepareStatement(insert);
            pre.setString(1, locationEntites.getApartment_number());
            pre.setString(2, locationEntites.getStreet());
            pre.setString(3, locationEntites.getCounty());
            pre.setString(4, locationEntites.getDistrict());
            pre.setString(5, locationEntites.getCity());
            pre.setString(6, locationEntites.getLongitude());
            pre.setString(7, locationEntites.getLatitude());
            int countInsert = pre.executeUpdate();
            if (countInsert > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnect(null, pre, null);
        }

        return false;
    }

    public boolean updateUserLocation(Connection conn, int idLocation, StoreEntites store) {
        PreparedStatement pre = null;
        try {
            String update = "Update Store set location_id  = ? where id = ? ";
            pre = conn.prepareStatement(update);
            pre.setInt(1, idLocation);
            pre.setInt(2, store.getId());
            int check = pre.executeUpdate();
            if (check > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnect(null, pre, null);
        }
        return false;
    }

    @Override
    public StoreEntites informationStore(int storeId) throws SQLException {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        StoreEntites store = null;
        String sql = "SELECT s.id as storeId,s.user_id,s.location_id,s.name as nameStore, s.phone,s.status,l.apartment_number,l.street,l.district,l.county, "
                + "l.city,l.latitude,l.longitude,img.path FROM Store s "
                + "JOIN Location as l on s.location_id = l.id JOIN (Image_Store imgS join Image img on imgS.image_id = img.id) on imgS.store_id = s.id where s.id =?";
        try {
            conn = getConnection();
            pre = conn.prepareStatement(sql);
            pre.setInt(1, storeId);
            rs = pre.executeQuery();
            if (rs.next()) {
                store = new StoreEntites();
                store.setId(rs.getInt("storeId"));
                if (rs.getString("apartment_number") != null) {
                    store.setAddress(rs.getString("apartment_number") + "-" + rs.getString("street") + "-" + rs.getString("county") + "-" + rs.getString("city"));
                } else {
                    store.setAddress(rs.getString("street") + "-" + rs.getString("county") + "-" + rs.getString("city"));
                }

                store.setImage_path(rs.getString("path"));
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

    @Override
    public List<HashMap<String, Object>> managementFeedback(int storeId, int page) throws SQLException {
        List<HashMap<String, Object>> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pre = null;
        try {
            conn = getConnection();
            String sql = "select a.user_id,a.email,a.phone,a.full_name,DATE_FORMAT(a.dateOfBirth,\"%d/%m/%Y\")  as dateOfBirth,a.content,a.satisfied,DATE_FORMAT(a.registerLog,\"%d/%m/%Y\")  as registerLog,b.path from\n"
                    + "(select a.id as user_id,a.email,a.phone,a.full_name,a.image_id,a.dateOfBirth,b.content,b.satisfied,b.registerLog from User a , Feedback b where a.id = b.user_id and b.store_id = ?) a , Image b where a.image_id = b.id order by registerLog limit ?,10";
            pre = conn.prepareStatement(sql);
            pre.setInt(1, storeId);
            pre.setInt(2, page * 10);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                FeedbackEntites feedback = new FeedbackEntites();
                UserEntites user = new UserEntites();
                HashMap<String, Object> hashmap = new HashMap<>();
                feedback.setContent(rs.getNString("content"));
                feedback.setIsSatisfied(rs.getInt("satisfied"));
                feedback.setStore_id(storeId);
                feedback.setUser_id(rs.getInt("user_id"));
                feedback.setRegisterLog(rs.getString("registerLog"));
                user.setDisplayName(rs.getNString("full_name"));
                user.setUserID(rs.getInt("user_id"));
                user.setEmail(rs.getNString("email"));
                user.setPhone(rs.getString("phone"));
                user.setDateOfBirth(rs.getString("dateOfBirth"));
                user.setImage_path(rs.getString("path"));
                hashmap.put("user", user);
                hashmap.put("feedback", feedback);
                list.add(hashmap);
            }
        } finally {
            closeConnect(conn, pre, null);
        }
        return list;
    }

    @Override
    public List<Integer> countFeedback(int storeId) throws SQLException {
        String sql = "select smile,sad from (select COUNT(*) as smile from Feedback where satisfied = 1 and store_id = ?) a, (select COUNT(*) as sad from Feedback where satisfied = 0 and store_id = ?) b";
        List<Integer> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pre = null;
        try {
            conn = getConnection();
            pre = conn.prepareStatement(sql);
            pre.setInt(1, storeId);
            pre.setInt(2, storeId);
            ResultSet rs = pre.executeQuery();
            if (rs.next()){
                list.add(rs.getInt("smile"));
                list.add(rs.getInt("sad"));
            }
        } finally {
            closeConnect(conn, pre, null);
        }
        return list;
    }

    @Override
    public boolean changeImgByStore(String imgPath, StoreEntites store) throws SQLException {
        Connection conn = null;
        PreparedStatement pre =null;
        boolean checkUpdate = false;
        try{
            conn = getConnection();
            if(store.getImage_path().equals("Store/default_store.jpg")){
                if(insertNewImgPath(conn, imgPath, store.getId())){
                    int idAfterInsert = getIdAfterInsert(conn,store.getId());
                    checkUpdate = updateToImgStoreTbl(conn,idAfterInsert,store.getId());
                }
            }else{
                int idExist = getIdExist(conn,store.getId());
                checkUpdate = updateImgExist(conn,idExist,imgPath);
            }
        }finally{
            closeConnect(conn, pre, null);
        }
        return checkUpdate;
    }
    public boolean updateToImgStoreTbl(Connection conn,int idImg,int storeId) throws SQLException{
        PreparedStatement pre = null;
        try {
            String update = "Update Image_Store set image_id= ? where store_id =?";
            conn.setAutoCommit(false);
            pre = conn.prepareStatement(update);
            pre.setInt(1, idImg);
            pre.setInt(2, storeId);
            int check = pre.executeUpdate();
            if (check > 0) {
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
    public boolean insertNewImgPath(Connection conn,String newPath,int storeId) throws SQLException{
        PreparedStatement pre =null;
        try{
            String insert = "insert into Image(name,path) values (?,?)";
            conn.setAutoCommit(false);
            pre = conn.prepareStatement(insert);
            pre.setString(1, storeId+"");
            pre.setString(2, newPath);
            int check = pre.executeUpdate();
            if(check>0){
                conn.commit();
                return true;
            }else{
                conn.rollback();
                conn.setAutoCommit(true);
            }
            
        }finally{
            closeConnect(null, pre, null);
        }
        return false;
    }
    public int getIdAfterInsert(Connection conn,int storeId) {
        PreparedStatement pre =null;
        ResultSet rs =null;
        try{
            String sql ="select id from Image where name = ?";
            pre =conn.prepareStatement(sql);
            pre.setString(1, storeId+"");
            rs = pre.executeQuery();
            if(rs.next()){
                return rs.getInt("id");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            closeConnect(null, pre, rs);
        }
        return 0;
    }
    
     public int getIdExist(Connection conn,int storeId) {
        PreparedStatement pre =null;
        ResultSet rs =null;
        try{
            String sql ="select image_id from Image_Store where store_id = ?";
            pre =conn.prepareStatement(sql);
            pre.setInt(1, storeId);
            rs = pre.executeQuery();
            if(rs.next()){
                return rs.getInt("image_id");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            closeConnect(null, pre, rs);
        }
        return 0;
    }
     public boolean updateImgExist(Connection conn, int id,String path){
         PreparedStatement pre =null;
         
         try{
             String update ="Update Image set path= ? where id =?";
             conn.setAutoCommit(false);
             pre = conn.prepareStatement(update);
             pre.setString(1, path);
             pre.setInt(2, id);
             int check = pre.executeUpdate();
             if(check >0){
                 conn.commit();
                 return true;
             }else{
                 conn.rollback();
                 conn.setAutoCommit(true);
             }
         }catch(Exception e){
             
         }finally{
             closeConnect(null, pre, null);
         }
         return false;
     }

    @Override
    public int validatorStore(String nameStore, String phone,String typeSeach) throws SQLException {
        
         Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            int tempSearch = 0;
            String sql = null;
            switch (typeSeach) {
                case "name":
                    sql = "select name from Store where name = ? ";
                    tempSearch = 1;
                    break;
                case "phone":
                    sql = "select phone from Store where phone = ? ";
                    tempSearch = 2;
                    break;
            }

            conn = getConnection();
            //chèn các cho các biến đúng giá trị của nó       
            switch (tempSearch) {
                case 1:
                    pre = conn.prepareStatement(sql);
                    pre.setString(1, nameStore);
                    rs = pre.executeQuery();
                    if (rs.next()) {                       
                        return tempSearch;
                    }
                    break;
                case 2:
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
}
