package com.dkop.library.dao;

import com.dkop.library.dao.impls.JDBCDaoFactory;

public abstract class DaoFactory {

    private static DaoFactory instance;

    public static DaoFactory getInstance() {
        if (instance == null) {
            synchronized (DaoFactory.class) {
                if (instance == null) {
                    DaoFactory daoFactory = new JDBCDaoFactory();
                    instance = daoFactory;
                }
            }
        }
        return instance;
    }

    public abstract UserDao createUserDao();

    public abstract BooksDao createBooksDao();

    public abstract OrderDao createOrderDao();
}