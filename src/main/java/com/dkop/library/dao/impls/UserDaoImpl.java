package com.dkop.library.dao.impls;

import com.dkop.library.dao.UserDao;
import com.dkop.library.entity.Book;
import com.dkop.library.entity.User;
import com.dkop.library.exceptions.DoesNotExistException;
import com.dkop.library.exceptions.NotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private final Connection connection;

    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public void create(User user) throws SQLException {
        String INSERT_USERS_SQL = "INSERT INTO users (first_name, last_name, email, password, role, status) VALUES (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getRole());
            preparedStatement.setString(6, user.getStatus());
            preparedStatement.executeUpdate();
        }
    }

    public List<User> findAll() {
        List<User> allUsers = new ArrayList<>();
        String SELECT_USERS = "SELECT * FROM users WHERE role = 'reader';";
        try (ResultSet resultSet = connection.createStatement().executeQuery(SELECT_USERS)) {
            while (resultSet.next()) {
                User user = User.newBuilder()
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .email(resultSet.getString("email"))
                        .role(resultSet.getString("role"))
                        .status(resultSet.getString("status"))
                        .id(resultSet.getInt("id"))
                        .build();
                allUsers.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allUsers;
    }

    public List<User> findAllLibrarians() {
        List<User> allUsers = new ArrayList<>();
        String SELECT_USERS = "SELECT * FROM users WHERE role = 'librarian';";
        try (ResultSet resultSet = connection.createStatement().executeQuery(SELECT_USERS)) {
            while (resultSet.next()) {
                User user = User.newBuilder()
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .email(resultSet.getString("email"))
                        .role(resultSet.getString("role"))
                        .status(resultSet.getString("status"))
                        .id(resultSet.getInt("id"))
                        .build();
                allUsers.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allUsers;
    }

    @Override
    public void changeStatus(int id, String newStatus) {
        String BLOCK_USER = "UPDATE users SET status = ? WHERE id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(BLOCK_USER)) {
            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findById(int id) throws NotFoundException {
        String SELECT_USER = "SELECT * FROM users WHERE id = ?;";
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = User.newBuilder()
                            .firstName(resultSet.getString("first_name"))
                            .lastName(resultSet.getString("last_name"))
                            .email(resultSet.getString("email"))
                            .password(resultSet.getString("password"))
                            .role(resultSet.getString("role"))
                            .status(resultSet.getString("status"))
                            .id(resultSet.getInt("id"))
                            .build();
                } else {
                    throw new NotFoundException("User not found");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void update(User user) {
    }

    @Override
    public void delete(int id) {
        String DELETE_USER = "DELETE FROM users WHERE id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User findByEmail(String email) throws DoesNotExistException {
        String SELECT_USER = "SELECT * FROM users WHERE email = ?;";
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = User.newBuilder()
                            .firstName(resultSet.getString("first_name"))
                            .lastName(resultSet.getString("last_name"))
                            .email(resultSet.getString("email"))
                            .password(resultSet.getString("password"))
                            .role(resultSet.getString("role"))
                            .status(resultSet.getString("status"))
                            .id(resultSet.getInt("id"))
                            .build();
                } else {
                    throw new DoesNotExistException(email + " does not exist!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
