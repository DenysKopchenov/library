package com.dkop.library.dao.impls;

import com.dkop.library.dao.OrderDao;
import com.dkop.library.entity.Book;
import com.dkop.library.entity.Order;
import com.dkop.library.exceptions.NotFoundException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    private Connection connection;

    public OrderDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Order order) throws SQLException {
        String CREATE_ORDER = "INSERT INTO orders (book_id, user_id, type, create_date) VALUES (?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_ORDER)) {
            preparedStatement.setInt(1, order.getBookId());
            preparedStatement.setInt(2, order.getUserId());
            preparedStatement.setString(3, order.getType());
            preparedStatement.setDate(4, Date.valueOf(LocalDate.now()));
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
                            .createDate(resultSet.getDate("create_date").toLocalDate())
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
                            .createDate(resultSet.getDate("create_date").toLocalDate())
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
    public void update(Order order) {
        String UPDATE_ORDER = "UPDATE orders SET status = ? , approved_date = ?, expected_return_date = ? WHERE id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ORDER)) {
            preparedStatement.setString(1, order.getStatus());
            preparedStatement.setDate(2, Date.valueOf(order.getApprovedDate()));
            preparedStatement.setDate(3, Date.valueOf(order.getExpectedReturnDate()));
//            preparedStatement.setDate(4, Date.valueOf(order.getActualReturnDate()));
            preparedStatement.setInt(4, order.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public List<Order> findAllOrdersByStatus(String status) {
        String SELECT_ORDERS = "SELECT * FROM orders WHERE status = ?;";
        List<Order> ordersByStatus = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ORDERS)) {
            preparedStatement.setString(1, status);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = Order.newBuilder()
                            .id(resultSet.getInt("id"))
                            .bookId(resultSet.getInt("book_id"))
                            .userId(resultSet.getInt("user_id"))
                            .type(resultSet.getString("type"))
                            .status(resultSet.getString("status"))
                            .createDate(resultSet.getDate("create_date").toLocalDate())
                            .approvedDate(resultSet.getDate("approved_date").toLocalDate())
                            .expectedReturnDate(resultSet.getDate("expected_return_date").toLocalDate())
                            .actualReturnDate(resultSet.getDate("actual_return_date").toLocalDate())
                            .build();
                    ordersByStatus.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ordersByStatus;
    }

    @Override
    public List<Order> findAllUserApprovedOrders(int userId) {
        String SELECT_ORDERS = "SELECT * FROM orders WHERE user_id = ? AND status = 'approved'";
        List<Order> allApprovedOrders = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ORDERS)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = Order.newBuilder()
                            .id(resultSet.getInt("id"))
                            .bookId(resultSet.getInt("book_id"))
                            .userId(resultSet.getInt("user_id"))
                            .type(resultSet.getString("type"))
                            .status(resultSet.getString("status"))
                            .createDate(resultSet.getDate("create_date").toLocalDate())
                            .approvedDate(resultSet.getDate("approved_date").toLocalDate())
                            .expectedReturnDate(resultSet.getDate("expected_return_date").toLocalDate())
                            .actualReturnDate(resultSet.getDate("actual_return_date").toLocalDate())
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
    public void processOrder(Order order, Book book) {
        String UPDATE_ORDER = "UPDATE orders SET status = ? , approved_date = ?, expected_return_date = ? WHERE id = ?;";
        String UPDATE_BOOK = "UPDATE books SET amount = ?, on_order = ? WHERE id = ?;";
        try (PreparedStatement updateBook = connection.prepareStatement(UPDATE_BOOK);
             PreparedStatement updateOrder = connection.prepareStatement(UPDATE_ORDER)) {
            connection.setAutoCommit(false);
            updateOrder.setString(1, order.getStatus());
            updateOrder.setDate(2, Date.valueOf(order.getApprovedDate()));
            updateOrder.setDate(3, Date.valueOf(order.getExpectedReturnDate()));
//            updateOrder.setDate(4, Date.valueOf(order.getActualReturnDate()));
            updateOrder.setInt(4, order.getId());
            updateOrder.executeUpdate();

//            updateBook.setString(1, book.getTitle());
//            updateBook.setString(2, book.getAuthor());
//            updateBook.setString(3, book.getPublisher());
//            updateBook.setDate(4, Date.valueOf(book.getPublishingDate()));
            updateBook.setInt(1, book.getAmount());
            updateBook.setInt(2, book.getOnOrder());
            updateBook.setInt(3, book.getId());
            updateBook.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    @Override
    public boolean isOrderExist(Order order) {
        String CHECK_ORDER = "SELECT count(id) AS count FROM orders WHERE status = 'approved' OR status = 'pending' AND book_id = ? AND user_id = ? AND type = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_ORDER)) {
            preparedStatement.setInt(1, order.getBookId());
            preparedStatement.setInt(2, order.getUserId());
            preparedStatement.setString(3, order.getType());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    long count = resultSet.getLong("count");
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
