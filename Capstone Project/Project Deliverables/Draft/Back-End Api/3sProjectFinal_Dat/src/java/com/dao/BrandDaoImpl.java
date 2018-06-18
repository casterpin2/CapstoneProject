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

    private final static String SQL = "select b.id,b.name,i.path,p.name,count(p.id) as countProduct from Brand b left join (Image_Brand ib left join Image i on ib.image_id = i.id) on b.id = ib.brand_id \n"
            + "left join (Type_Brand tb left join Product p on tb.id = p.id) on b.id = tb.brand_id \n"
            + "group by b.id,b.name,i.path,p.name";

    @Override
    public List<BrandEntities> listBrand() throws SQLException {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        List<BrandEntities> list = null;
        try {
            list = new ArrayList<>();
            conn = getConnection();
            ;
            pre = conn.prepareStatement(SQL);
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
    public List<BrandEntities> listBrandTop10() throws SQLException {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        List<BrandEntities> list = null;
        try {
            list = new ArrayList<>();
            conn = getConnection();

            pre = conn.prepareStatement(SQL + " limit 10");
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
    public List<ProductAddEntites> listProductWithBrand(int brandId) throws SQLException {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        List<ProductAddEntites> list = null;
        try {
            list = new ArrayList<>();
            conn = getConnection();
            String sql = "select p.id,p.name,i.path,p.description from Product p left join (Type_Brand tb left join Brand b on tb.brand_id = b.id) on p.id = tb.id \n"
                    + "left join (Image_Product ip left join Image i on ip.image_id = i.id) on p.id = ip.product_id where p.type_brand_id = ?";
            pre = conn.prepareStatement(sql);
            pre.setInt(1, brandId);
            rs = pre.executeQuery();
            while (rs.next()) {
                ProductAddEntites product = new ProductAddEntites();
                product.setProduct_id(rs.getInt("id"));
                product.setProduct_name(rs.getNString("name"));
                product.setImage_path(rs.getNString("path"));
                product.setDescription(rs.getNString("description"));
                list.add(product);
            }
        } finally {
            closeConnect(conn, pre, rs);
        }
        return list;
    }

}
