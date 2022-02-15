package com.dkop.library.dao;

import com.dkop.library.model.User;
import com.dkop.library.model.exceptions.DoesNotExistException;

import java.sql.SQLException;
import java.util.List;

public interface UserDao extends GenericDao<User> {
    User findByEmail(String email) throws DoesNotExistException;

    void changeStatus(int id, String newStatus) throws SQLException;

    List<User> findAllLibrarians();
}
