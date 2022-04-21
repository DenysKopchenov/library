package com.dkop.library.dao.impls;

import com.dkop.library.dao.OrderDao;
import com.dkop.library.entity.Book;
import com.dkop.library.entity.Order;
import com.dkop.library.exceptions.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.dkop.library.utils.LocalizationUtil.localizationBundle;
import static com.dkop.library.dao.impls.Queries.*;

@Component
public class OrderDaoImpl implements OrderDao {

    private final DataSource dataSource;
    private static final Logger LOGGER = LogManager.getLogger(OrderDaoImpl.class);

    public OrderDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void create(Order order) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_ORDER)) {
                preparedStatement.setInt(1, order.getBookId());
                preparedStatement.setInt(2, order.getUserId());
                preparedStatement.setString(3, order.getType());
                preparedStatement.setDate(4, Date.valueOf(LocalDate.now()));
                preparedStatement.executeUpdate();
            }
        }
    }

    @Override
    public List<Order> findAll() {
        List<Order> allOrders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_ORDERS)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        allOrders.add(extractOrderFromResultSet(resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
        }
        return allOrders;
    }

    private Order extractOrderFromResultSet(ResultSet resultSet) throws SQLException {
        return Order.newBuilder()
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
    }

    @Override
    public Order findById(int id) throws NotFoundException {
        Order order = null;
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ORDER_BY_ID)) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        order = extractOrderFromResultSet(resultSet);
                    } else {
                        throw new NotFoundException(localizationBundle.getString("order.not.found"));
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
        }
        return order;
    }

    @Override
    public void update(Order order) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ORDER)) {
                preparedStatement.setString(1, order.getStatus());
                preparedStatement.setDate(2, Date.valueOf(order.getApprovedDate()));
                preparedStatement.setDate(3, Date.valueOf(order.getExpectedReturnDate()));
                preparedStatement.setInt(4, order.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
        }
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Order> findAllOrdersByStatus(String status, int start, int numberOfRecords) {
        List<Order> ordersByStatus = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ORDERS_BY_STATUS)) {
                preparedStatement.setString(1, status);
                preparedStatement.setInt(2, start);
                preparedStatement.setInt(3, numberOfRecords);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        ordersByStatus.add(extractOrderFromResultSet(resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
        }
        return ordersByStatus;
    }

    @Override
    public List<Order> findAllUserApprovedOrders(int userId, int start, int numberOfRecords) {
        List<Order> allApprovedOrders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_APPROVED_USER_ORDERS)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, start);
                preparedStatement.setInt(3, numberOfRecords);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        allApprovedOrders.add(extractOrderFromResultSet(resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
        }
        return allApprovedOrders;
    }

    @Override
    public void processOrder(Order order, Book book) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement updateBook = connection.prepareStatement(UPDATE_BOOK_FROM_ORDER);
                 PreparedStatement updateOrder = connection.prepareStatement(UPDATE_ORDER)) {
                connection.setAutoCommit(false);
                updateOrder.setString(1, order.getStatus());
                updateOrder.setDate(2, Date.valueOf(order.getApprovedDate()));
                updateOrder.setDate(3, Date.valueOf(order.getExpectedReturnDate()));
                updateOrder.setInt(4, order.getId());
                updateOrder.executeUpdate();

                updateBook.setInt(1, book.getAmount());
                updateBook.setInt(2, book.getOnOrder());
                updateBook.setInt(3, book.getId());
                updateBook.executeUpdate();

                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    LOGGER.error(ex, ex.getCause());
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
        }
    }

    @Override
    public boolean isOrderExist(Order order) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_ORDER_EXIST)) {
                preparedStatement.setInt(1, order.getBookId());
                preparedStatement.setInt(2, order.getUserId());
                preparedStatement.setString(3, order.getType());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        long count = resultSet.getLong("count");
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
        }
        return false;
    }

    @Override
    public boolean isAvailableToDeleteBook(int bookId) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_ORDER_AVAILABLE_TO_DELETE_BOOK)) {
                preparedStatement.setInt(1, bookId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        long count = resultSet.getLong("count");
                        return count == 0;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
        }
        return false;
    }

    @Override
    public int countAllRowsByStatus(String status) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(COUNT_ROWS_BY_STATUS)) {
                preparedStatement.setString(1, status);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("count");
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
        }
        return 0;
    }

    @Override
    public int countAllRowsByStatusAndUser(String status, int userId) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(COUNT_ROWS_BY_STATUS_AND_READER)) {
                preparedStatement.setString(1, status);
                preparedStatement.setInt(2, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("count");
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
        }
        return 0;
    }
}
