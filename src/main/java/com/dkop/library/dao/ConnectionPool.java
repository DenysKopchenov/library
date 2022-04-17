package com.dkop.library.dao;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConnectionPool {
    private static BasicDataSource ds = new BasicDataSource();

    private ConnectionPool() {
    }

    static {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("db");

        ds.setUrl(resourceBundle.getString("db.url"));
        ds.setUsername(resourceBundle.getString("db.username"));
        ds.setPassword(resourceBundle.getString("db.password"));
        ds.setDriverClassName(resourceBundle.getString("db.driver"));
        ds.setMinIdle(Integer.parseInt(resourceBundle.getString("db.min.idle")));
        ds.setMaxIdle(Integer.parseInt(resourceBundle.getString("db.max.idle")));
        ds.setMaxOpenPreparedStatements(Integer.parseInt(resourceBundle.getString("db.max.opened.statements")));
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
