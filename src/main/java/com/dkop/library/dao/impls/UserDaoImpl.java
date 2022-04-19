package com.dkop.library.dao.impls;

import com.dkop.library.dao.UserDao;
import com.dkop.library.entity.User;
import com.dkop.library.exceptions.DoesNotExistException;
import com.dkop.library.exceptions.NotFoundException;
import com.dkop.library.exceptions.UnableToDeleteException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.dkop.library.utils.Fields.EMAIL;
import static com.dkop.library.utils.LocalizationUtil.localizationBundle;
import static com.dkop.library.dao.impls.Queries.*;

public class UserDaoImpl implements UserDao {

    private static final Logger LOGGER = LogManager.getLogger(UserDaoImpl.class);
    private final Connection connection;

    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public void create(User entity) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_USER)) {
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setString(3, entity.getEmail());
            preparedStatement.setString(4, entity.getPassword());
            preparedStatement.setString(5, entity.getRole());
            preparedStatement.setString(6, entity.getStatus());
            preparedStatement.executeUpdate();
        }
    }

    public List<User> findAll() {
        throw new UnsupportedOperationException();
    }

    public int countAllRowsByRole(String role) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(COUNT_ROWS_BY_ROLE)) {
            preparedStatement.setString(1, role);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count");
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
        }
        return 0;
    }

    public List<User> findAllByRole(String role, int start, int numberOfRecords) {
        List<User> allUsers = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USERS_BY_ROLE)) {
            preparedStatement.setString(1, role);
            preparedStatement.setInt(2, start);
            preparedStatement.setInt(3, numberOfRecords);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    allUsers.add(extractUserFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
        }
        return allUsers;
    }

    @Override
    public void changeStatus(int id, String newStatus) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_STATUS)) {
            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
        }
    }

    @Override
    public User findById(int id) throws NotFoundException {
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = extractUserFromResultSet(resultSet);
                } else {
                    throw new NotFoundException(localizationBundle.getString("user.not.found"));
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
        }
        return user;
    }

    private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        return User.newBuilder()
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .email(resultSet.getString(EMAIL))
                .role(resultSet.getString("role"))
                .status(resultSet.getString("status"))
                .id(resultSet.getInt("id"))
                .build();
    }

    @Override
    public void update(User entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(int id) throws UnableToDeleteException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_BY_ID)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
            throw new UnableToDeleteException(localizationBundle.getString("unable.delete.user"), e);
        }
    }

    public User findByEmail(String email) throws DoesNotExistException {
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_EMAIL)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = User.newBuilder()
                            .firstName(resultSet.getString("first_name"))
                            .lastName(resultSet.getString("last_name"))
                            .email(resultSet.getString(EMAIL))
                            .password(resultSet.getString("password"))
                            .role(resultSet.getString("role"))
                            .status(resultSet.getString("status"))
                            .id(resultSet.getInt("id"))
                            .build();
                } else {
                    throw new DoesNotExistException("Email " + email + localizationBundle.getString("email.does.not.exist"));
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
        }
        return user;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
        }
    }
}
