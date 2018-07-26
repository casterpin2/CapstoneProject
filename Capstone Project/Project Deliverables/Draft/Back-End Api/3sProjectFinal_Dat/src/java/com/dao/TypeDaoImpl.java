/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

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
            while (rs.next()){
                ty = new TypeEntites();
                ty.setId(rs.getInt("id"));
                ty.setName(rs.getString("name"));
                ty.setImage_path(rs.getString("path"));
                listData.add(ty);
            }
            for (int i =0 ; i < listData.size() ; i++){
                ty = listData.get(i);
                pre = conn.prepareStatement(COUNT_PRODUCT_TYPE);
                pre.setInt(1, ty.getId());
                int count = 0;
                rs = pre.executeQuery();
                while (rs.next()) count ++;
                ty.setProduct_count(count);
            }
        } catch (Exception e) {
            return null;
        } finally {
            closeConnect(conn, pre, rs);
        }

        return listData;
    }

}
