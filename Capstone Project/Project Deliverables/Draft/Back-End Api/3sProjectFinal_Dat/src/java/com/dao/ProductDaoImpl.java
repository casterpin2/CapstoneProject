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

    private final String PRODUCT_QUERY_1 = "SELECT c.product_id,product_name,brand_name,description,category_name,type_name,image_path FROM\n"
            + "(SELECT b.id as product_id,product_name,brand_name,description,category_name,type_name,path as image_path FROM Image i,\n"
            + "(SELECT image_id,id,product_name,brand_name,description,category_name,type_name FROM Image_Product, \n"
            + "(SELECT e.id,e.product_name,e.brand_name,e.description,d.name as category_name , e.type_name FROM Category d , (SELECT c.name as type_name,c.category_id,b.id,b.brand_name,b.name as product_name,description FROM Type c, (SELECT b.name as brand_name , a.id , a.name , a.type_id, description FROM Brand b,(SELECT p.id,name,description,type_brand_id,type_id,brand_id FROM Product p,Type_Brand t WHERE name LIKE ? AND p.type_brand_id = t.id) a WHERE b.id = a.brand_id) b WHERE c.id = b.type_id) e WHERE d.id = e.category_id) a WHERE a.id = product_id) b WHERE  i.id = image_id) c WHERE c.product_id not in (SELECT product_id FROM Product_Store where Store_id = ?) Order BY c.product_id asc LIMIT ?,?";
    private final String PRODUCT_QUERY_2 = "SELECT a.id,a.name as product_name,brand_name,category_name,i.path as image_path,price,promotion FROM Image i INNER JOIN\n"
            + "(SELECT id,name,brand_name,category_name,image_id,price,promotion FROM Image_Product ip INNER JOIN\n"
            + "(SELECT a.id,a.name,brand_name,c.name as category_name,price,promotion FROM Category c INNER JOIN\n"
            + "(SELECT a.id,a.name,brand_name,category_id,price,promotion FROM Type t INNER JOIN \n"
            + "(SELECT c.id,c.name,type_id,b.name as brand_name,price,promotion FROM Brand b INNER JOIN (SELECT b.id,name,type_id,brand_id,price,promotion FROM Type_Brand tb INNER JOIN (SELECT a.product_id as id,name,type_brand_id,price,promotion FROM Product p INNER JOIN (SELECT product_id,price,promotion FROM `Product_Store` WHERE store_id = ?) a ON a.product_id = p.id) b ON tb.id = b.type_brand_id) c ON c.brand_id = b.id) a ON t.id = a.type_id) a ON a.category_id = c.id) a ON a.id = ip.product_id) a ON i.id = a.image_id";

    @Override
    public List<ProductAddEntites> getProductForAdd(String query,int page,int storeId) throws SQLException {
        List<ProductAddEntites> listData = null;
        ProductAddEntites pro = null;
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            listData = new ArrayList();
            conn = getConnection();
            pre = conn.prepareStatement(PRODUCT_QUERY_1);
            pre.setString(1, "%" + query + "%");
            pre.setInt(2, storeId);
            pre.setInt(3, page*10-9);
            pre.setInt(4, 10);
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
    public boolean insertProdcut(ProductAddEntites p, int storeId) throws SQLException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement pre = null;
        int count = 0;
        try {
            String sql = "insert into Product_Store (price,promotion,store_id,product_id) values(?,?,?,?)";
            conn = getConnection();
            conn.setAutoCommit(false);
            pre = conn.prepareStatement(sql);
                pre.setDouble(1, p.getPrice());
                pre.setDouble(2, p.getPromotion());
                pre.setInt(3, storeId);
                pre.setInt(4, p.getProduct_id());
                count++;
                pre.addBatch();
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

            pre = conn.prepareStatement(SQL + "limit 20");
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
    public List<ProductAddEntites> getProductInStore(int storeID) throws SQLException {
        List<ProductAddEntites> listData = null;
        ProductAddEntites pro = null;
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            listData = new ArrayList();
            conn = getConnection();
            pre = conn.prepareStatement(PRODUCT_QUERY_2);
            pre.setInt(1, storeID);
            rs = pre.executeQuery();
            while (rs.next()) {
                pro = new ProductAddEntites();
                pro.setProduct_id(rs.getInt("id"));
                pro.setProduct_name(rs.getString("product_name"));
                pro.setBrand_name(rs.getString("brand_name"));
                pro.setDescription("");
                pro.setCategory_name(rs.getString("category_name"));
                pro.setImage_path(rs.getString("image_path"));
                pro.setType_name("");
                pro.setPromotion(rs.getDouble("promotion"));
                pro.setPrice(rs.getDouble("price"));
                listData.add(pro);
            }
        } finally {
            closeConnect(conn, pre, rs);
        }

        return listData;
    }

    @Override
    public boolean insertData(String name, String brand, String barcode) throws SQLException {
        String sql = "INSERT INTO DataBarcode (name, brand, barcode) VALUES (?, ?, ?);";
        Connection conn = null;
        PreparedStatement pre = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            pre = conn.prepareStatement(sql);
            pre.setString(1, name);
            pre.setString(2, brand);
            pre.setString(3, barcode);
            int temp = pre.executeUpdate();
            if (temp > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                conn.setAutoCommit(true);
                return false;
            }

        } finally {
            closeConnect(conn, pre, null);
        }

    }

    @Override
    public List<ProductAddEntites> getProductWithBarCode(String query, int storeId) throws SQLException {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        List<ProductAddEntites> list = null ;
        try {
            String sql = "select p.id,p.name,b.name as brand_name,c.name as category_name,p.description,i.path ,t.name as type_name from Product p \n"
                    + "				   join(Image_Product ip  join Image i on i.id = ip.image_id) on p.id = ip.product_id\n"
                    + "       			   join(Type_Brand  tb join (Type t join Category c on t.category_id = c.id) on tb.type_id = t.id\n"
                    + "           					   join Brand b on b.id = tb.brand_id)  on p.type_brand_id = tb.id \n"
                    + "                                       where p.barcode=? and p.id not in (select product_id from Product_Store where store_id =?)";
            conn = getConnection();
            list = new ArrayList<>();
            pre = conn.prepareStatement(sql);
            pre.setInt(2, storeId);
            pre.setString(1, query);
            rs = pre.executeQuery();
            if (rs.next()) {
                ProductAddEntites pro = new ProductAddEntites();
                pro.setProduct_id(rs.getInt("id"));
                pro.setProduct_name(rs.getString("name"));
                pro.setBrand_name(rs.getString("brand_name"));
                pro.setDescription(rs.getString("description"));
                pro.setCategory_name(rs.getString("category_name"));
                pro.setImage_path(rs.getString("path"));
                pro.setType_name(rs.getString("type_name"));
                list.add(pro);
            }
        } finally {
            closeConnect(conn, pre, rs);
        }
        return list;
    }

}
