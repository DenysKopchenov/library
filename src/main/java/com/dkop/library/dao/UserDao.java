package com.dkop.library.dao;

import com.dkop.library.model.User;
import com.dkop.library.model.exceptions.AlreadyExistException;
import com.dkop.library.model.exceptions.DoesNotExistException;
import com.dkop.library.model.exceptions.WasBlockedException;
import com.dkop.library.model.exceptions.WrongPasswordException;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements AutoCloseable {
    private Connection connection;

    public UserDao(Connection connection) {
        this.connection = connection;
    }

    public void createUser(User user) {
        String INSERT_USERS_SQL = "INSERT INTO users" +
                " (first_name, last_name, password, email, role, status) VALUES " +
                " (?, ?, ?, ?, ?, ?);";
        try {
            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getRole());
            preparedStatement.setString(6, user.getStatus());
            // Step 3: Execute the query or update query
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void checkUser(String email) throws AlreadyExistException {
        String SELECT_EMAIL_SQL = "SELECT email FROM users WHERE email = ?;";
        try  {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_EMAIL_SQL);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String emailFromDB = resultSet.getString("email");
                if (emailFromDB.equals(email)) {
                    throw new AlreadyExistException("Email " + email + " already exist!");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        String SELECT_USERS = "SELECT * FROM users WHERE role NOT LIKE 'admin';";

        try {
            ResultSet resultSet = connection.createStatement().executeQuery(SELECT_USERS);
            while (resultSet.next()) {
                User user = new User();
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setEmail(resultSet.getString("email"));
                user.setRole(resultSet.getString("role")); // mb enum? Role.valueOf(resultSet.getString("role").toUpperCase())
                user.setStatus(resultSet.getString("status")); // mb enum? Status.valueOf(resultSet.getString("status").toUpperCase())
                user.setId(resultSet.getInt("id"));
                allUsers.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allUsers;
    }

    public String authenticateUser(String email, String password) throws DoesNotExistException, WrongPasswordException, WasBlockedException {
        String userRole = "";
        String SELECT_LOGIN_INFO = "SELECT email, password, role, status FROM users WHERE email = ?;";
        password = DigestUtils.sha256Hex(password);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LOGIN_INFO);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String status = resultSet.getString("status");
                if (!status.equals("active")){
                    throw new WasBlockedException("Your account was blocked. Contact administrator");
                }
                String passwordFromDB = resultSet.getString("password");
                if (password.equals(passwordFromDB)) {
                    userRole = resultSet.getString("role");
                } else {
                    throw new WrongPasswordException("Wrong Password!");
                }
            } else {
                throw new DoesNotExistException(email + " Does not exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userRole;
    }

    public User findUser(String email) {
        String SELECT_USER = "SELECT * FROM users WHERE email = ?;";
        User user = new User();
        try  {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setEmail(resultSet.getString("email"));
                user.setRole(resultSet.getString("role")); // mb enum? Role.valueOf(resultSet.getString("role").toUpperCase())
                user.setStatus(resultSet.getString("status")); // mb enum Status.valueOf(resultSet.getString("status").toUpperCase())
                user.setId(resultSet.getInt("id"));
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
