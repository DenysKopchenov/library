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
        ds.setMinIdle(25);
        ds.setMaxIdle(100);
        ds.setMaxOpenPreparedStatements(150);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
