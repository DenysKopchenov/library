package com.dkop.library.dao.impls;

import com.dkop.library.dao.BooksDao;
import com.dkop.library.dao.DaoFactory;
import com.dkop.library.dao.UserDao;

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
}
