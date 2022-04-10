package com.dkop.library.dao.impls;

import com.dkop.library.dao.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class JDBCDaoFactory extends DaoFactory {
    private static final Logger LOGGER = LogManager.getLogger(JDBCDaoFactory.class);

    @Override
    public UserDao createUserDao() {
        try {
            return new UserDaoImpl(ConnectionPool.getConnection());
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
            throw new RuntimeException(e);
        }
    }

    @Override
    public BooksDao createBooksDao() {
        try {
            return new BooksDaoImpl(ConnectionPool.getConnection());
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
            throw new RuntimeException(e);
        }
    }

    @Override
    public OrderDao createOrderDao() {
        try {
            return new OrderDaoImpl(ConnectionPool.getConnection());
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
            throw new RuntimeException(e);
        }
    }
}
