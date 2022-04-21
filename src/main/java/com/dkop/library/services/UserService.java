package com.dkop.library.services;

import com.dkop.library.dao.UserDao;
import com.dkop.library.entity.User;
import com.dkop.library.exceptions.AlreadyExistException;
import com.dkop.library.exceptions.DoesNotExistException;
import com.dkop.library.exceptions.NotFoundException;
import com.dkop.library.exceptions.UnableToDeleteException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

import static com.dkop.library.utils.LocalizationUtil.localizationBundle;

@Service
public class UserService {

    private final UserDao userDao;
    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User findById(int id) throws NotFoundException {
            return userDao.findById(id);
    }

    public List<User> findAllByRole(String role, int offset, int numberOfRecords) {
            return userDao.findAllByRole(role, offset, numberOfRecords);
    }

    public User getUserInfo(String email) throws DoesNotExistException {
            return userDao.findByEmail(email);
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
        try {
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
            throw new AlreadyExistException("Email " + email + " " + localizationBundle.getString("email.already.exist"), e);
        }
    }

    public void deleteUser(int id) throws NotFoundException, UnableToDeleteException {
            userDao.findById(id);
            userDao.delete(id);
    }

    public void changeStatus(int id, String newStatus) throws NotFoundException {
            userDao.findById(id);
            userDao.changeStatus(id, newStatus);
    }

    public int countAllRowsByRole(String role) {
            return userDao.countAllRowsByRole(role);
    }
}