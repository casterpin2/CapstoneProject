/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.entites.BrandEntities;
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
@Repository("brandDao")
public class BrandDaoImpl extends BaseDao implements BrandDao {

    private final static String SQL = " select b.id,b.name,i.path,count(p.id) as countProduct from Brand b  join (Image_Brand ib  join Image i on ib.image_id = i.id) on b.id = ib.brand_id \n"
            + "            join (Type_Brand tb  join Product p on tb.id = p.type_brand_id) on b.id = tb.brand_id \n"
            + "            group by b.id,b.name,i.path limit ?,10";
    private final static String SQL1 = " select b.id,b.name,i.path,count(p.id) as countProduct from Brand b  join (Image_Brand ib  join Image i on ib.image_id = i.id) on b.id = ib.brand_id and ib.brand_id IN (1,9,60,62,68)\n" +
"                        join (Type_Brand tb  join Product p on tb.id = p.type_brand_id) on b.id = tb.brand_id\n" +
"                        group by b.id,b.name,i.path";
    private final static String SQL2 = " select b.id,b.name,i.path,count(p.id) as countProduct from Brand b  join (Image_Brand ib  join Image i on ib.image_id = i.id) on b.id = ib.brand_id\n" +
"                      join (Type_Brand tb  join Product p on tb.id = p.type_brand_id and tb.type_id = ?) on b.id = tb.brand_id\n" +
"                      group by b.id,b.name,i.path";
    
    private final static String SQL3 = "select p.id,p.name,p.description,i.path,b.name as brand_name,t.name as type_name from Product p join (Type_Brand tb join Brand b on tb.brand_id = b.id) on p.type_brand_id = tb.id join (Type_Brand tl join Type t on tl.type_id = t.id) on p.type_brand_id = tl.id join (Image_Product ip join Image i on ip.image_id = i.id) on p.id = ip.product_id where tb.brand_id = ? and tb.type_id = ? order by p.id limit ?,10";
    
    private final static String SQL4 = " select b.id,b.name,i.path,count(p.id) as countProduct from Brand b  join (Image_Brand ib  join Image i on ib.image_id = i.id) on b.id = ib.brand_id and b.name like ?\n"
            + "            join (Type_Brand tb  join Product p on tb.id = p.type_brand_id) on b.id = tb.brand_id \n"
            + "            group by b.id,b.name,i.path limit ?,10";
    @Override
    public List<BrandEntities> listBrand(int page) throws SQLException {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        List<BrandEntities> list = null;
        try {
            list = new ArrayList<>();
            conn = getConnection();
            pre = conn.prepareStatement(SQL);
            pre.setInt(1, page*10);
            rs = pre.executeQuery();
            while (rs.next()) {
                BrandEntities brand = new BrandEntities();
                brand.setBrandId(rs.getInt("id"));
                brand.setBrandName(rs.getNString("name"));
                brand.setBrandImg(rs.getNString("path"));
                brand.setNumberProduct(rs.getInt("countProduct"));
                list.add(brand);
            }
        } finally {
            closeConnect(conn, pre, rs);
        }
        return list;
    }

    @Override
    public List<BrandEntities> listBrandTop5() throws SQLException {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        List<BrandEntities> list = null;
        try {
            list = new ArrayList<>();
            conn = getConnection();

            pre = conn.prepareStatement(SQL1);
            rs = pre.executeQuery();
            while (rs.next()) {
                BrandEntities brand = new BrandEntities();
                brand.setBrandId(rs.getInt("id"));
                brand.setBrandName(rs.getNString("name"));
                brand.setBrandImg(rs.getNString("path"));
                brand.setNumberProduct(rs.getInt("countProduct"));
                list.add(brand);
            }
        } finally {
            closeConnect(conn, pre, rs);
        }
        return list;
    }

    @Override
    public List<ProductAddEntites> listProductWithBrand(int brandId, int page) throws SQLException {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        List<ProductAddEntites> list = null;
        try {
            list = new ArrayList<>();
            conn = getConnection();
            String sql = "select p.id,p.name,p.description,i.path,b.name as brand_name,t.name as type_name from Product p join (Type_Brand tb join Brand b on tb.brand_id = b.id) on p.type_brand_id = tb.id join (Type_Brand tl join Type t on tl.type_id = t.id) on p.type_brand_id = tl.id join (Image_Product ip join Image i on ip.image_id = i.id) on p.id = ip.product_id where b.id = ? limit ?,5";
            pre = conn.prepareStatement(sql);
            pre.setInt(1, brandId);
            pre.setInt(2, page*5);
            rs = pre.executeQuery();
            while (rs.next()) {
                ProductAddEntites product = new ProductAddEntites();
                product.setProduct_id(rs.getInt("id"));
                product.setProduct_name(rs.getNString("name"));
                product.setImage_path(rs.getNString("path"));
                product.setDescription(rs.getNString("description"));
                product.setBrand_name(rs.getNString("brand_name"));
                product.setType_name(rs.getNString("type_name"));
                list.add(product);
            }
        } finally {
            closeConnect(conn, pre, rs);
        }
        return list;
    }

    @Override
    public List<BrandEntities> listBrandByType(int typeId) throws SQLException {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        List<BrandEntities> list = null;
        try {
            list = new ArrayList<>();
            conn = getConnection();
            pre = conn.prepareStatement(SQL2);
            pre.setInt(1, typeId);
            rs = pre.executeQuery();
            while (rs.next()) {
                BrandEntities brand = new BrandEntities();
                brand.setBrandId(rs.getInt("id"));
                brand.setBrandName(rs.getNString("name"));
                brand.setBrandImg(rs.getNString("path"));
                brand.setNumberProduct(rs.getInt("countProduct"));
                list.add(brand);
            }
        } finally {
            closeConnect(conn, pre, rs);
        }
        return list;
    }
    
    @Override
    public List<ProductAddEntites> listProductWithBrandType(int brandId, int typeId, int page) throws SQLException {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        List<ProductAddEntites> list = null;
        try {
            list = new ArrayList<>();
            conn = getConnection();
            pre = conn.prepareStatement(SQL3);
            pre.setInt(1, brandId);
            pre.setInt(2, typeId);
            pre.setInt(3, page*10);
            rs = pre.executeQuery();
            while (rs.next()) {
                ProductAddEntites product = new ProductAddEntites();
                product.setProduct_id(rs.getInt("id"));
                product.setProduct_name(rs.getNString("name"));
                product.setImage_path(rs.getNString("path"));
                product.setDescription(rs.getNString("description"));
                product.setBrand_name(rs.getNString("brand_name"));
                product.setType_name(rs.getNString("type_name"));
                list.add(product);
            }
        } finally {
            closeConnect(conn, pre, rs);
        }
        return list;
    }

    @Override
    public List<BrandEntities> getBrandsByName(String query,int page) throws SQLException {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        List<BrandEntities> list = null;
        try {
            list = new ArrayList<>();
            conn = getConnection();
            pre = conn.prepareStatement(SQL4);
            pre.setString(1, "%" + query + "%");
            pre.setInt(2, page*10);
            rs = pre.executeQuery();
            while (rs.next()) {
                BrandEntities brand = new BrandEntities();
                brand.setBrandId(rs.getInt("id"));
                brand.setBrandName(rs.getNString("name"));
                brand.setBrandImg(rs.getNString("path"));
                brand.setNumberProduct(rs.getInt("countProduct"));
                list.add(brand);
            }
        } finally {
            closeConnect(conn, pre, rs);
        }
        return list;
    }
}
