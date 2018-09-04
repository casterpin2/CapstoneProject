/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.entites.CategoryEntities;
import com.entites.ProductAddEntites;
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
            + "            group by c.id,c.name,i.path order by c.name desc";
    private final static String COUNT_PRODUCT = "SELECT COUNT(b.id) from (SELECT a.id as type_id , b.id from (SELECT * FROM Type WHERE category_id = ?) a , Type_Brand b WHERE a.id = b.type_id) a , Product b WHERE a.id = b.type_brand_id";
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
                pre = conn.prepareStatement(COUNT_PRODUCT);
                pre.setInt(1, category.getCategoryId());
                ResultSet rs1 = pre.executeQuery();
                if (rs1.next()){
                    category.setCount(rs1.getInt("COUNT(b.id)"));
                }
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

    @Override
    public List<ProductAddEntites> getProductInCategory(int page, int categoryId) throws SQLException {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        List<ProductAddEntites> list = null;
        try {
            list = new ArrayList<>();
            conn = getConnection();
            pre = conn.prepareStatement("select a.id,a.name,a.description,a.brand_name,a.type_name,b.path from\n" +
"(select a.*,b.image_id from\n" +
"(select a.type_name,a.brand_name,b.id,b.name,b.description from\n" +
"(select a.type_name,a.tb_id,b.name as brand_name from\n" +
"(select a.*,b.id as tb_id,b.brand_id from\n" +
"(select id,name as type_name,category_id from Type where category_id = ?) a, Type_Brand b WHERE a.id =b.type_id) a , Brand b where a.brand_id = b.id) a , Product b where a.tb_id = b.type_brand_id) a , Image_Product b where a.id = b.product_id) a , Image b where a.image_id = b.id order by id limit ?,10");
            pre.setInt(2, page*10);
            pre.setInt(1, categoryId);
            rs = pre.executeQuery();
            while (rs.next()) {
                ProductAddEntites product = new ProductAddEntites();
                product.setProduct_id(rs.getInt("id"));
                product.setProduct_name(rs.getString("name"));
                product.setBrand_name(rs.getString("brand_name"));
                product.setDescription(rs.getNString("description"));
                product.setImage_path(rs.getString("path"));
                product.setType_name(rs.getNString("type_name"));
                list.add(product);
            }
        } finally {
            closeConnect(conn, pre, rs);
        }
        return list;
    }

    @Override
    public List<ProductAddEntites> getProductInCategoryByName(int page, int categoryId, String query) throws SQLException {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        List<ProductAddEntites> list = null;
        try {
            list = new ArrayList<>();
            conn = getConnection();
            pre = conn.prepareStatement("select a.id,a.name,a.description,a.brand_name,a.type_name,b.path from\n" +
"(select a.*,b.image_id from\n" +
"(select a.type_name,a.brand_name,b.id,b.name,b.description from\n" +
"(select a.type_name,a.tb_id,b.name as brand_name from\n" +
"(select a.*,b.id as tb_id,b.brand_id from\n" +
"(select id,name as type_name,category_id from Type where category_id = ?) a, Type_Brand b WHERE a.id =b.type_id) a , Brand b where a.brand_id = b.id) a , Product b where a.tb_id = b.type_brand_id and b.name like ?) a , Image_Product b where a.id = b.product_id) a , Image b where a.image_id = b.id order by id limit ?,10");
            pre.setInt(3, page*10);
            pre.setString(2, "%" + query + "%");
            pre.setInt(1, categoryId);
            rs = pre.executeQuery();
            while (rs.next()) {
                ProductAddEntites product = new ProductAddEntites();
                product.setProduct_id(rs.getInt("id"));
                product.setProduct_name(rs.getString("name"));
                product.setBrand_name(rs.getString("brand_name"));
                product.setDescription(rs.getNString("description"));
                product.setImage_path(rs.getString("path"));
                product.setType_name(rs.getNString("type_name"));
                list.add(product);
            }
        } finally {
            closeConnect(conn, pre, rs);
        }
        return list;
    }
}
