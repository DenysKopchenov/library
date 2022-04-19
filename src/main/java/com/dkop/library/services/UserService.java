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

import static com.dkop.library.utils.LocalizationUtil.localizationBundle;

public class UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);
    private final DaoFactory daoFactory;

    public UserService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
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

    /**
     * Creates a user entity and a password hash using SHA256HEX, then inserts it into the database
     *
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     * @param role
     * @param status
     * @throws AlreadyExistException if the user with specified email already exists in the database
     */
    public void createUser(String firstName, String lastName, String email, String password, String role, String status) throws AlreadyExistException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            String passwordHash = DigestUtils.sha256Hex(password);
            User user = User.newBuilder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .password(passwordHash)
                    .role(role)
                    .status(status)
                    .build();
            userDao.create(user);
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
            throw new AlreadyExistException("Email " + email + localizationBundle.getString("email.already.exist"), e);
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
        }
    }

    public int countAllRowsByRole(String role) {
        try (UserDao userDao = daoFactory.createUserDao()) {
            return userDao.countAllRowsByRole(role);
        }
    }
}