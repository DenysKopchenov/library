package com.dkop.library.services;

import com.dkop.library.dao.DaoFactory;
import com.dkop.library.dao.UserDao;
import com.dkop.library.entity.User;
import com.dkop.library.exceptions.AlreadyExistException;
import com.dkop.library.exceptions.DoesNotExistException;
import com.dkop.library.exceptions.NotFoundException;
import com.dkop.library.exceptions.UnableToDeleteException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

import static com.dkop.library.controller.command.CommandUtils.messagesBundle;

public class UserService {
    private static final Logger LOGGER = LogManager.getLogger(UserService.class);
    private final DaoFactory daoFactory;

    public UserService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
        LOGGER.info(UserService.class.getSimpleName());
    }

    public User findById(int id) throws NotFoundException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            return userDao.findById(id);
        }
    }

    public List<User> findAllByRole(String role, int offset, int numberOfRecords) {
        try (UserDao userDao = daoFactory.createUserDao()) {
            return userDao.findAllByRole(role, offset, numberOfRecords);
        }
    }

    public User getUserInfo(String email) throws DoesNotExistException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            return userDao.findByEmail(email);
        }
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
            LOGGER.error(e, e.getCause());
            throw new AlreadyExistException("Email " + email + messagesBundle.getString("email.already.exist"), e);
        }
    }

    public void deleteUser(int id) throws NotFoundException, UnableToDeleteException {
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
            LOGGER.error(e, e.getCause());
            throw new NotFoundException(messagesBundle.getString("user.not.found"));
        }
    }

    public int countAllRowsByRole(String role) {
        try (UserDao userDao = daoFactory.createUserDao()) {
            return userDao.countAllRowsByRole(role);
        }
    }
}