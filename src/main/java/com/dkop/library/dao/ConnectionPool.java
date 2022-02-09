package com.dkop.library.dao;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    private static BasicDataSource ds = new BasicDataSource();

    static {
        ds.setUrl("jdbc:mysql://127.0.0.1:3306/users?useUnicode=yes;characterEncoding=UTF-8;autoReconnect=true");
        ds.setUsername("root");
        ds.setPassword("ghbdtnltybc");
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
