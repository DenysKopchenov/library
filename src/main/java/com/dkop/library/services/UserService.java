package com.dkop.library.services;

import com.dkop.library.dao.DaoFactory;
import com.dkop.library.dao.UserDao;
import com.dkop.library.entity.User;
import com.dkop.library.exceptions.AlreadyExistException;
import com.dkop.library.exceptions.DoesNotExistException;
import com.dkop.library.exceptions.NotFoundException;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private static UserService instance;
    private final DaoFactory daoFactory;

    public static UserService getInstance() {
        if (instance == null) {
            synchronized (LoginService.class) {
                if (instance == null) {
                    UserService userService = new UserService();
                    instance = userService;
                }
            }
        }
        return instance;
    }

    private UserService() {
        daoFactory = DaoFactory.getInstance();
    }

    public List<User> findAll() {
        try (UserDao userDao = daoFactory.createUserDao()) {
            return userDao.findAll();
        }
    }

    public List<User> findAllLibrarians() {
        try (UserDao userDao = daoFactory.createUserDao()) {
            return userDao.findAllLibrarians();
        }
    }

    //return user
    public User getUserInfo(String email) throws DoesNotExistException {
        User user;
        try (UserDao userDao = daoFactory.createUserDao()) {
            user = userDao.findByEmail(email);
        }
        return user;
    }

    public void createUser(String firstName, String lastName, String email, String password, String role, String status) throws AlreadyExistException {
        try (UserDao userDao = daoFactory.createUserDao()) {
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

    public void deleteUser(int id) throws NotFoundException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.findById(id);
            userDao.delete(id);
        }
    }

    public void changeStatus(int id, String newStatus) throws NotFoundException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.findById(id);
            userDao.changeStatus(id, newStatus);
        } catch (SQLException e) {
            throw new NotFoundException(id + " not found");
        }
    }
}