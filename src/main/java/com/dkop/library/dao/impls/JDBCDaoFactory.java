package com.dkop.library.dao.impls;

import com.dkop.library.dao.*;

import java.sql.SQLException;

public class JDBCDaoFactory extends DaoFactory {

    @Override
    public UserDao createUserDao() {
        try {
            return new UserDaoImpl(ConnectionPool.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BooksDao createBooksDao() {
        try {
            return new BooksDaoImpl(ConnectionPool.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OrderDao createOrderDao() {
        try {
            return new OrderDaoImpl(ConnectionPool.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
