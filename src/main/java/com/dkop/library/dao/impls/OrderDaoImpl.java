package com.dkop.library.dao.impls;

import com.dkop.library.dao.OrderDao;
import com.dkop.library.model.Order;
import com.dkop.library.model.exceptions.NotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        return null;
    }

    @Override
    public Order findById(int id) throws NotFoundException {
        return null;
    }

    @Override
    public void update(Order order) throws SQLException {

    }

    @Override
    public void delete(int id) throws SQLException {

    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Order> findAllApprovedUserOrders(int userId) {
        String SELECT_ORDERS = "SELECT book_id, user_id, expected_return_date FROM orders WHERE user_id = ? AND status = 'approved';";
        List<Order> allApprovedOrders = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ORDERS)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = Order.newBuilder()
                            .bookId(resultSet.getInt("book_id"))
                            .userId(resultSet.getInt("user_id"))
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
}
