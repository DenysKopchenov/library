package com.dkop.library.dao;

import com.dkop.library.dao.impls.JDBCDaoFactory;

public abstract class DaoFactory {
    private static DaoFactory daoFactory;

    public static DaoFactory getInstance() {
        if (daoFactory == null) {
            synchronized (DaoFactory.class) {
                if (daoFactory == null) {
                    DaoFactory temp = new JDBCDaoFactory();
                    daoFactory = temp;
                }
            }
        }
        return daoFactory;

    }

    public abstract UserDao createUserDao();

    public abstract BooksDao createBooksDao();

    public abstract OrderDao createOrderDao();
}