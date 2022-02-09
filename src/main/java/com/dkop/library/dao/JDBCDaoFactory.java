package com.dkop.library.dao;

import java.sql.SQLException;

public class JDBCDaoFactory extends DaoFactory {
    @Override
    public UserDao createUserDao() {
        try {
            return new UserDao(ConnectionPool.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BooksDao createBooksDao() {
        try {
            return new BooksDao(ConnectionPool.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserOrderDao createUserOrderDao() {
        try {
            return new UserOrderDao(ConnectionPool.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
