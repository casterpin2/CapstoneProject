/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import static com.dao.BaseDao.closeConnect;
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
 * @author AHBP
 */
@Repository("productDao")
public class ProductDaoImpl extends BaseDao implements ProductDao {

    private final String PRODUCT_QUERY = "SELECT c.product_id,product_name,brand_name,description,category_name,type_name,image_path FROM\n"
            + "(SELECT b.id as product_id,product_name,brand_name,description,category_name,type_name,path as image_path FROM Image i,\n"
            + "(SELECT image_id,id,product_name,brand_name,description,category_name,type_name FROM Image_Product, \n"
            + "(SELECT e.id,e.product_name,e.brand_name,e.description,d.name as category_name , e.type_name FROM Category d , (SELECT c.name as type_name,c.category_id,b.id,b.brand_name,b.name as product_name,description FROM Type c, (SELECT b.name as brand_name , a.id , a.name , a.type_id, description FROM Brand b,(SELECT p.id,name,description,type_brand_id,type_id,brand_id FROM Product p,Type_Brand t WHERE name LIKE ? AND p.type_brand_id = t.id) a WHERE b.id = a.brand_id) b WHERE c.id = b.type_id) e WHERE d.id = e.category_id) a WHERE a.id = product_id) b WHERE  i.id = image_id) c WHERE c.product_id not in (SELECT product_id FROM Product_Store where Store_id = 1) Order BY c.product_id asc";

    @Override
    public List<ProductAddEntites> getProductForAdd(String query) throws SQLException {
        List<ProductAddEntites> listData = null;
        ProductAddEntites pro = null;
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            listData = new ArrayList();
            conn = getConnection();
            pre = conn.prepareStatement(PRODUCT_QUERY);
            pre.setString(1, "%" + query + "%");
            rs = pre.executeQuery();
            while (rs.next()) {
                pro = new ProductAddEntites();
                pro.setProduct_id(rs.getInt("product_id"));
                pro.setProduct_name(rs.getString("product_name"));
                pro.setBrand_name(rs.getString("brand_name"));
                pro.setDescription(rs.getString("description"));
                pro.setCategory_name(rs.getString("category_name"));
                pro.setImage_path(rs.getString("image_path"));
                pro.setType_name(rs.getString("type_name"));
                listData.add(pro);
            }
        } finally {
            closeConnect(conn, pre, rs);
        }

        return listData;
    }

    @Override
    public boolean insertProdcut(List<ProductAddEntites> productList, int storeId) throws SQLException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement pre = null;
        int count = 0;
        try {
            String sql = "insert into Product_Store (price,promotion,store_id,product_id) values(?,?,?,?)";
            conn = getConnection();
            conn.setAutoCommit(false);
            pre = conn.prepareStatement(sql);
            for (ProductAddEntites p : productList) {
                pre.setDouble(1, p.getPrice());
                pre.setDouble(2, p.getPromotion());
                pre.setInt(3, storeId);
                pre.setInt(4, p.getProduct_id());
                count++;
                pre.addBatch();
            }
            int temp[] = pre.executeBatch();
            if (temp.length == count) {
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
    private final static String SQL = "select p.id,p.name,i.path,s.name as storeName,ps.price,ps.promotion from Product p \n"
            + "                  join (Product_Store ps  join Store s on ps.store_id = s.id) on p.id = ps.product_id\n"
            + "                  join (Image_Product ip  join Image i on ip.image_id = i.id) on p.id = ip.product_id where promotion > ? ";

    @Override
    public List<ProductAddEntites> getProductSaleList(int number) throws SQLException {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        List<ProductAddEntites> list = null;
        try {
            list = new ArrayList<>();
            conn = getConnection();
            
            pre = conn.prepareStatement(SQL);
            pre.setInt(1, number);
            rs = pre.executeQuery();
            while (rs.next()) {
                ProductAddEntites product = new ProductAddEntites();
                product.setProduct_id(rs.getInt("id"));
                product.setProduct_name(rs.getNString("name"));
                product.setImage_path(rs.getNString("path"));
                product.setStoreName(rs.getNString("storeName"));
                product.setPrice(rs.getDouble("price"));
                product.setPromotion(rs.getDouble("promotion"));
                list.add(product);
            }
        } finally {
            closeConnect(conn, pre, rs);
        }
        return list;
    }

    @Override
    public List<ProductAddEntites> getProductSaleListTop20(int number) throws SQLException {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        List<ProductAddEntites> list = null;
        try {
            list = new ArrayList<>();
            conn = getConnection();
            
            pre = conn.prepareStatement(SQL+ "limit 20");
            pre.setInt(1, number);
            rs = pre.executeQuery();
            while (rs.next()) {
                ProductAddEntites product = new ProductAddEntites();
                product.setProduct_id(rs.getInt("id"));
                product.setProduct_name(rs.getNString("name"));
                product.setImage_path(rs.getNString("path"));
                product.setStoreName(rs.getNString("storeName"));
                product.setPrice(rs.getDouble("price"));
                product.setPromotion(rs.getDouble("promotion"));
                list.add(product);
            }
        } finally {
            closeConnect(conn, pre, rs);
        }
        return list;
    }

}
