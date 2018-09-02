/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.entites.ProductAddEntites;
import com.entites.TypeEntites;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author AHBP
 */
@Repository("typeDao")
public class TypeDaoImpl extends BaseDao implements TypeDao {

    private String COUNT_PRODUCT_TYPE = "SELECT b.id,b.name from (SELECT a.id as type_id , b.id from (SELECT * FROM Type WHERE id = ?) a , Type_Brand b WHERE a.id = b.type_id) a , Product b WHERE a.id = b.type_brand_id";
    private String QUERY_GET_TYPE = "SELECT a.id,a.name,b.path FROM (SELECT * FROM Type WHERE category_id = ?) a , Image b WHERE a.image_id = b.id";
    private String QUERY_GET_PRODUCT_BY_TYPE = "select p.id,p.name,i.path,t.name as type_name,b.name as brand_name,p.description from Product p join (Type_Brand tb join Brand b on tb.brand_id = b.id) on p.type_brand_id = tb.id join (Type_Brand tl join Type t on tl.type_id = t.id) on p.type_brand_id = tl.id join (Image_Product ip join Image i on ip.image_id = i.id) on p.id = ip.product_id WHERE t.id = ? limit ?,10";
    private String QUERY_GET_PRODUCT_BY_TYPE_AND_NAME = "select p.id,p.name,i.path,t.name as type_name,b.name as brand_name,p.description from Product p join (Type_Brand tb join Brand b on tb.brand_id = b.id) on p.type_brand_id = tb.id join (Type_Brand tl join Type t on tl.type_id = t.id) on p.type_brand_id = tl.id join (Image_Product ip join Image i on ip.image_id = i.id) on p.id = ip.product_id WHERE t.id = ? and p.name LIKE ? limit ?,10";
    @Override
    public List<TypeEntites> getTypebyCategory(int categoryId) throws SQLException {
        List<TypeEntites> listData = null;
        TypeEntites ty = null;
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            listData = new ArrayList<>();
            conn = getConnection();
            pre = conn.prepareStatement(QUERY_GET_TYPE);
            pre.setInt(1, categoryId);
            rs = pre.executeQuery();
            while (rs.next()) {
                ty = new TypeEntites();
                ty.setId(rs.getInt("id"));
                ty.setName(rs.getString("name"));
                ty.setImage_path(rs.getString("path"));
                listData.add(ty);
            }
            for (int i = 0; i < listData.size(); i++) {
                ty = listData.get(i);
                pre = conn.prepareStatement(COUNT_PRODUCT_TYPE);
                pre.setInt(1, ty.getId());
                int count = 0;
                rs = pre.executeQuery();
                while (rs.next()) {
                    count++;
                }
                ty.setProduct_count(count);
            }
        } catch (Exception e) {
            return null;
        } finally {
            closeConnect(conn, pre, rs);
        }

        return listData;
    }

    @Override
    public List<ProductAddEntites> getProductbyType(int typeId, int page) throws SQLException {
        List<ProductAddEntites> listData = null;
        ProductAddEntites pro = null;
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            listData = new ArrayList<>();
            conn = getConnection();
            pre = conn.prepareStatement(QUERY_GET_PRODUCT_BY_TYPE);
            pre.setInt(1, typeId);
            pre.setInt(2, page*10);
            rs = pre.executeQuery();
            while (rs.next()) {
                pro = new ProductAddEntites();
                pro.setProduct_id(rs.getInt("id"));
                pro.setProduct_name(rs.getString("name"));
                pro.setBrand_name(rs.getString("brand_name"));
                pro.setDescription(rs.getString("description"));
                pro.setImage_path(rs.getString("path"));
                pro.setType_name(rs.getString("type_name"));
                listData.add(pro);
            }
        } catch (Exception e) {
            return null;
        } finally {
            closeConnect(conn, pre, rs);
        }
        return listData;
    }

    @Override
    public List<ProductAddEntites> getProductTypebyName(String query, int typeId, int page) throws SQLException {
        List<ProductAddEntites> listData = null;
        ProductAddEntites pro = null;
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            listData = new ArrayList<>();
            conn = getConnection();
            pre = conn.prepareStatement(QUERY_GET_PRODUCT_BY_TYPE_AND_NAME);
            pre.setString(2, "%" + query + "%");
            pre.setInt(1, typeId);
            pre.setInt(3, page*10);
            rs = pre.executeQuery();
            while (rs.next()) {
                pro = new ProductAddEntites();
                pro.setProduct_id(rs.getInt("id"));
                pro.setProduct_name(rs.getString("name"));
                pro.setBrand_name(rs.getString("brand_name"));
                pro.setDescription(rs.getString("description"));
                pro.setImage_path(rs.getString("path"));
                pro.setType_name(rs.getString("type_name"));
                listData.add(pro);
            }
        } catch (Exception e) {
            return null;
        } finally {
            closeConnect(conn, pre, rs);
        }
        return listData;
    }

}
