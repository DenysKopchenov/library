package com.dkop.library.services;

import com.dkop.library.dao.DaoFactory;
import com.dkop.library.dao.UserDao;
import com.dkop.library.model.User;
import com.dkop.library.model.exceptions.DoesNotExistException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class UserService {
    public List<User> findAll() {
        try (UserDao userDao = DaoFactory.getInstance().createUserDao()) {
            return userDao.findAll();
        }
    }

    //return user
    public User getUserInfo(String email) throws DoesNotExistException {
        User user;
        try (UserDao userDao = DaoFactory.getInstance().createUserDao()) {
            user = userDao.findByEmail(email);
        }
        return user;
    }
}