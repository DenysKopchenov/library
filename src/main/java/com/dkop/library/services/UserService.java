package com.dkop.library.services;

import com.dkop.library.dao.DaoFactory;
import com.dkop.library.dao.UserDao;
import com.dkop.library.model.User;
import com.dkop.library.model.exceptions.AlreadyExistException;
import com.dkop.library.model.exceptions.DoesNotExistException;
import com.dkop.library.model.exceptions.NotFoundException;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    public List<User> findAll() {
        try (UserDao userDao = DaoFactory.getInstance().createUserDao()) {
            return userDao.findAll();
        }
    }

    public List<User> findAllLibrarians() {
        try (UserDao userDao = DaoFactory.getInstance().createUserDao()) {
            return userDao.findAllLibrarians();
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

    public void deleteUser(int id) throws NotFoundException {
        try (UserDao userDao = DaoFactory.getInstance().createUserDao()) {
            userDao.delete(id);
        } catch (SQLException e) {
            throw new NotFoundException(id + " not found");
        }
    }

    public void changeStatus(int id, String newStatus) throws NotFoundException {
        try (UserDao userDao = DaoFactory.getInstance().createUserDao()) {
            userDao.changeStatus(id, newStatus);
        } catch (SQLException e) {
            throw new NotFoundException(id + " not found");
        }
    }
}