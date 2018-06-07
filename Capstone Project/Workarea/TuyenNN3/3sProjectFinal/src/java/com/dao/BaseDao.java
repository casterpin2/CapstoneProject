/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author TUYEN
 */
public class BaseDao {

    private final String FILE_PATH = "../application.properties";


    private Properties readPropertiesFile() throws FileNotFoundException, IOException {
        Properties p = new Properties();
        InputStream is = getClass().getClassLoader().getResourceAsStream(FILE_PATH);
        if (is == null) {
            throw new FileNotFoundException("File config is not found!");
        } else {
            p.load(is);
        }
        return p;

    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            Properties properties = readPropertiesFile();
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            String databaseName = properties.getProperty("databaseName");
            String url = properties.getProperty("url") + databaseName;
            Class.forName(properties.getProperty("driver"));
            connection = DriverManager.getConnection(url, username, password);
        } catch (IOException | ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage() + "EXCEPTION");
        }

        return connection;
    }

    public static void closeConnect(Connection conn, PreparedStatement pre, ResultSet rs) {
        try {

            if (pre != null) {
                pre.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
