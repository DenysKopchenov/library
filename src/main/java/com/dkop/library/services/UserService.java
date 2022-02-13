package com.dkop.library.services;

import com.dkop.library.dao.DaoFactory;
import com.dkop.library.dao.UserDao;
import com.dkop.library.model.User;
import com.dkop.library.model.exceptions.AlreadyExistException;
import com.dkop.library.model.exceptions.DoesNotExistException;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.SQLException;
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

    public void createUser(String firstName, String lastName, String email, String password, String role, String status) throws AlreadyExistException {
        try (UserDao userDao = DaoFactory.getInstance().createUserDao()) {
            User user = User.newBuilder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .password(DigestUtils.sha256Hex(password))
                    .role(role)
                    .status(status)
                    .build();
            userDao.create(user);
        } catch (SQLException e) {
            throw new AlreadyExistException("Email " + email + " already exist!");
        }
    }
}