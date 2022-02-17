package com.dkop.library.dao.impls;

import com.dkop.library.dao.OrderDao;
import com.dkop.library.model.Order;
import com.dkop.library.model.exceptions.NotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    private Connection connection;

    public OrderDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Order order) throws SQLException {
        String CREATE_ORDER = "INSERT INTO orders (book_id, user_id, type) VALUES (?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_ORDER)) {
            preparedStatement.setInt(1, order.getBookId());
            preparedStatement.setInt(2, order.getUserId());
            preparedStatement.setString(3, order.getType());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Order> findAll() {
        String SELECT_ORDERS = "SELECT * FROM orders;";
        List<Order> allOrders = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ORDERS)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = Order.newBuilder()
                            .id(resultSet.getInt("id"))
                            .bookId(resultSet.getInt("book_id"))
                            .userId(resultSet.getInt("user_id"))
                            .type(resultSet.getString("type"))
                            .status(resultSet.getString("status"))
                            .approvedDate(resultSet.getDate("approved_date").toLocalDate())
                            .expectedReturnDate(resultSet.getDate("expected_return_date").toLocalDate())
                            .actualReturnDate(resultSet.getDate("actual_return_date").toLocalDate())
                            .build();
                    allOrders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allOrders;
    }

    @Override
    public Order findById(int id) throws NotFoundException {
        String SELECT_ORDER = "SELECT * FROM orders WHERE id = ?;";
        Order order = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ORDER)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    order = Order.newBuilder()
                            .id(resultSet.getInt("id"))
                            .bookId(resultSet.getInt("book_id"))
                            .userId(resultSet.getInt("user_id"))
                            .type(resultSet.getString("type"))
                            .status(resultSet.getString("status"))
                            .approvedDate(resultSet.getDate("approved_date").toLocalDate())
                            .expectedReturnDate(resultSet.getDate("expected_return_date").toLocalDate())
                            .actualReturnDate(resultSet.getDate("actual_return_date").toLocalDate())
                            .build();
                } else {
                    throw new NotFoundException("Order not found");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    @Override
    public void update(Order order) throws SQLException {
        String UPDATE_ORDER = "UPDATE orders SET status = ? , approved_date = ?, expected_return_date = ?, actual_return_date = ? WHERE id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ORDER)) {
            preparedStatement.setString(1, order.getStatus());
            preparedStatement.setDate(2, Date.valueOf(order.getApprovedDate()));
            preparedStatement.setDate(3, Date.valueOf(order.getExpectedReturnDate()));
            preparedStatement.setDate(4, Date.valueOf(order.getActualReturnDate()));
            preparedStatement.setInt(5, order.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {

    }

    @Override
    public List<Order> findAllOrdersBasedOnStatus(int userId, String status) {
        String SELECT_ORDERS = "SELECT * FROM orders WHERE user_id = ? AND status = ?;";
        List<Order> allApprovedOrders = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ORDERS)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, status);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = Order.newBuilder()
                            .id(resultSet.getInt("id"))
                            .bookId(resultSet.getInt("book_id"))
                            .userId(resultSet.getInt("user_id"))
                            .type(resultSet.getString("type"))
                            .status(resultSet.getString("status"))
                            .approvedDate(resultSet.getDate("approved_date").toLocalDate())
                            .expectedReturnDate(resultSet.getDate("expected_return_date").toLocalDate())
                            .build();
                    allApprovedOrders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allApprovedOrders;
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
