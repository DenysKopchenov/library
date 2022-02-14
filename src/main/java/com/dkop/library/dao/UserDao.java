package com.dkop.library.dao;

import com.dkop.library.model.User;
import com.dkop.library.model.exceptions.DoesNotExistException;

import java.sql.SQLException;

public interface UserDao extends GenericDao<User> {
    User findByEmail(String email) throws DoesNotExistException;

    void blockUserById(int id) throws SQLException;
}
