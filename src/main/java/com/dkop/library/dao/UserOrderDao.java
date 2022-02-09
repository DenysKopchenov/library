package com.dkop.library.dao;

import java.sql.Connection;

public class UserOrderDao implements AutoCloseable{
    Connection connection;

    public UserOrderDao(Connection connection) {
        this.connection = connection;
    }

    public void createUserOrder(int userId, int bookId){

    }

    @Override
    public void close() throws Exception {

    }
}
