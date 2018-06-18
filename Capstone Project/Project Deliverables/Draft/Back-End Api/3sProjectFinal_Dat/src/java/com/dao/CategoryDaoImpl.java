/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.entites.CategoryEntities;
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
@Repository("categoryDao")
public class CategoryDaoImpl extends BaseDao implements CategoryDao {

    private final static String SQL = "select c.id,c.name,i.path as categoryImage,count(p.id) as countProduct from Category c\n"
            + "            left join ( Type t left join ( Type_Brand tb left join Product p on tb.id = p.id) on t.id = tb.type_id\n"
            + "            ) on c.id = t.category_id\n"
            + "            left join (Image_Category ic left join Image i on i.id = ic.Image_id) on ic.Category_id = c.id\n"
            + "            group by c.id,c.name,i.path ";

    @Override
    public List<CategoryEntities> listCategory() throws SQLException {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        List<CategoryEntities> list = null;
        try {
            list = new ArrayList<>();
            conn = getConnection();
            pre = conn.prepareStatement(SQL);
            rs = pre.executeQuery();
            while (rs.next()) {
                CategoryEntities category = new CategoryEntities();
                category.setCategoryId(rs.getInt("id"));
                category.setCategoryName(rs.getNString("name"));
                category.setCategoryPath(rs.getNString("categoryImage"));
                category.setCount(rs.getInt("countProduct"));
                list.add(category);
            }
        } finally {
            closeConnect(conn, pre, rs);
        }
        return list;
    }

    @Override
    public List<CategoryEntities> listCategoryTop10() throws SQLException {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        List<CategoryEntities> list = null;
        try {
            list = new ArrayList<>();
            conn = getConnection();
            pre = conn.prepareStatement(SQL + "limit 10");
            rs = pre.executeQuery();
            while (rs.next()) {
                CategoryEntities category = new CategoryEntities();
                category.setCategoryId(rs.getInt("id"));
                category.setCategoryName(rs.getNString("name"));
                category.setCategoryPath(rs.getNString("categoryImage"));
                category.setCount(rs.getInt("countProduct"));
                list.add(category);
            }
        } finally {
            closeConnect(conn, pre, rs);
        }
        return list;
    }

}
