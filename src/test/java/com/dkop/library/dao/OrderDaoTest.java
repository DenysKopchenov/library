package com.dkop.library.dao;

import com.dkop.library.dao.impls.OrderDaoImpl;
import com.dkop.library.entity.Book;
import com.dkop.library.entity.Order;
import com.dkop.library.exceptions.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static com.dkop.library.utils.LocalizationUtil.errorMessagesBundle;
import static com.dkop.library.dao.impls.Queries.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class OrderDaoTest {

    private OrderDao orderDao;
    private final Connection connectionMock = mock(Connection.class);
    private final PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
    private final ResultSet resultSetMock = mock(ResultSet.class);

    @Before
    public void setUp() throws SQLException {
        orderDao = new OrderDaoImpl(connectionMock);
        errorMessagesBundle = mock(ResourceBundle.class);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
    }

    @Test
    public void testCreate() throws SQLException {
        orderDao.create(createTestOrder());
        verify(connectionMock, times(1)).prepareStatement(CREATE_ORDER);
        verify(preparedStatementMock, times(1)).setInt(1, 1);
        verify(preparedStatementMock, times(1)).setInt(2, 1);
        verify(preparedStatementMock, times(1)).setString(3, "type");
        verify(preparedStatementMock, times(1)).setDate(4, Date.valueOf(LocalDate.now()));
        verify(preparedStatementMock, times(1)).executeUpdate();
    }

    @Test
    public void testFindAll() throws SQLException {
        when(resultSetMock.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        when(resultSetMock.getDate(anyString())).thenReturn(Date.valueOf(LocalDate.now()));
        List<Order> allOrders = orderDao.findAll();
        verify(connectionMock, times(1)).prepareStatement(SELECT_ALL_ORDERS);
        verify(preparedStatementMock, times(1)).executeQuery();
        verify(resultSetMock, times(1)).getInt("id");
        verify(resultSetMock, times(1)).getInt("book_id");
        verify(resultSetMock, times(1)).getInt("user_id");
        verify(resultSetMock, times(1)).getString("type");
        verify(resultSetMock, times(1)).getString("status");
        verify(resultSetMock, times(1)).getDate("create_date");
        verify(resultSetMock, times(1)).getDate("approved_date");
        verify(resultSetMock, times(1)).getDate("expected_return_date");
        verify(resultSetMock, times(1)).getDate("actual_return_date");
        Assert.assertEquals(1, allOrders.size());
    }

    @Test(expected = NotFoundException.class)
    public void testFindById() throws SQLException, NotFoundException {
        try {
            orderDao.findById(1);
        } catch (NotFoundException e) {
            verify(connectionMock, times(1)).prepareStatement(SELECT_ORDER_BY_ID);
            verify(preparedStatementMock, times(1)).setInt(1, 1);
            verify(preparedStatementMock, times(1)).executeQuery();
            throw e;
        }
    }

    @Test
    public void testUpdate() throws SQLException {
        orderDao.update(createTestOrder());
        verify(connectionMock, times(1)).prepareStatement(UPDATE_ORDER);
        verify(preparedStatementMock, times(1)).setString(1, "status");
        verify(preparedStatementMock, times(1)).setDate(2, Date.valueOf(LocalDate.now()));
        verify(preparedStatementMock, times(1)).setDate(3, Date.valueOf(LocalDate.now()));
        verify(preparedStatementMock, times(1)).setInt(4, 1);
        verify(preparedStatementMock, times(1)).executeUpdate();
    }

    @Test
    public void testFindAllOrdersByStatus() throws SQLException {
        List<Order> ordersByStatus = orderDao.findAllOrdersByStatus("status", 1, 5);
        verify(connectionMock, times(1)).prepareStatement(SELECT_ORDERS_BY_STATUS);
        verify(preparedStatementMock, times(1)).setString(1, "status");
        verify(preparedStatementMock, times(1)).setInt(2, 1);
        verify(preparedStatementMock, times(1)).setInt(3, 5);
        verify(preparedStatementMock, times(1)).executeQuery();
        Assert.assertTrue(ordersByStatus.isEmpty());
    }

    @Test
    public void testFindAllUserApprovedOrders() throws SQLException {
        List<Order> allApprovedOrders = orderDao.findAllUserApprovedOrders(1, 1, 5);
        verify(connectionMock, times(1)).prepareStatement(SELECT_APPROVED_USER_ORDERS);
        verify(preparedStatementMock, times(1)).setInt(1, 1);
        verify(preparedStatementMock, times(1)).setInt(2, 1);
        verify(preparedStatementMock, times(1)).setInt(3, 5);
        verify(preparedStatementMock, times(1)).executeQuery();
        Assert.assertTrue(allApprovedOrders.isEmpty());
    }

    @Test
    public void testProcessOrder() throws SQLException {
        orderDao.processOrder(createTestOrder(), createTestBook());
        verify(connectionMock, times(1)).prepareStatement(UPDATE_BOOK_FROM_ORDER);
        verify(connectionMock, times(1)).prepareStatement(UPDATE_ORDER);
        verify(connectionMock, times(1)).setAutoCommit(false);
        verify(preparedStatementMock, times(1)).setString(1, "status");
        verify(preparedStatementMock, times(1)).setDate(2, Date.valueOf(LocalDate.now()));
        verify(preparedStatementMock, times(1)).setDate(3, Date.valueOf(LocalDate.now()));
        verify(preparedStatementMock, times(1)).setInt(4, 1);
        verify(preparedStatementMock, times(1)).setInt(1, 1);
        verify(preparedStatementMock, times(1)).setInt(2, 0);
        verify(preparedStatementMock, times(1)).setInt(3, 1);
        verify(preparedStatementMock, times(2)).executeUpdate();
        verify(connectionMock, times(1)).commit();
    }

    @Test
    public void testProcessOrderTransactionFailed() throws SQLException {
        when(preparedStatementMock.executeUpdate()).thenThrow(new SQLException());
        orderDao.processOrder(createTestOrder(), createTestBook());
        verify(connectionMock, times(1)).prepareStatement(UPDATE_BOOK_FROM_ORDER);
        verify(connectionMock, times(1)).prepareStatement(UPDATE_ORDER);
        verify(connectionMock, times(1)).setAutoCommit(false);
        verify(preparedStatementMock, times(1)).setString(1, "status");
        verify(preparedStatementMock, times(1)).setDate(2, Date.valueOf(LocalDate.now()));
        verify(preparedStatementMock, times(1)).setDate(3, Date.valueOf(LocalDate.now()));
        verify(preparedStatementMock, times(1)).setInt(4, 1);
        verify(preparedStatementMock, times(1)).executeUpdate();
        verify(connectionMock, times(1)).rollback();
    }

    @Test
    public void testIsOrderExist() throws SQLException {
        boolean isExist = orderDao.isOrderExist(createTestOrder());
        verify(connectionMock, times(1)).prepareStatement(CHECK_ORDER_EXIST);
        verify(preparedStatementMock, times(1)).setInt(1, 1);
        verify(preparedStatementMock, times(1)).setInt(2, 1);
        verify(preparedStatementMock, times(1)).setString(3, "type");
        verify(preparedStatementMock, times(1)).executeQuery();
        Assert.assertFalse(isExist);
    }

    @Test
    public void testIsOrderAvailableToDeleteBook() throws SQLException {
        boolean isExist = orderDao.isAvailableToDeleteBook(1);
        verify(connectionMock, times(1)).prepareStatement(CHECK_ORDER_AVAILABLE_TO_DELETE_BOOK);
        verify(preparedStatementMock, times(1)).setInt(1, 1);
        verify(preparedStatementMock, times(1)).executeQuery();
        Assert.assertFalse(isExist);
    }

    @Test
    public void testCountAllRowsByStatus() throws SQLException {
        int count = orderDao.countAllRowsByStatus("status");
        verify(connectionMock, times(1)).prepareStatement(COUNT_ROWS_BY_STATUS);
        verify(preparedStatementMock, times(1)).setString(1, "status");
        verify(preparedStatementMock, times(1)).executeQuery();
        Assert.assertEquals(0, count);
    }

    @Test
    public void testCountAllRowsByStatusAndUser() throws SQLException {
        int count = orderDao.countAllRowsByStatusAndUser("status", 1);
        verify(connectionMock, times(1)).prepareStatement(COUNT_ROWS_BY_STATUS_AND_READER);
        verify(preparedStatementMock, times(1)).setString(1, "status");
        verify(preparedStatementMock, times(1)).setInt(2, 1);
        verify(preparedStatementMock, times(1)).executeQuery();
        Assert.assertEquals(0, count);
    }

    private Order createTestOrder() {
        return Order.newBuilder()
                .id(1)
                .bookId(1)
                .userId(1)
                .type("type")
                .status("status")
                .createDate(LocalDate.now())
                .approvedDate(LocalDate.now())
                .expectedReturnDate(LocalDate.now())
                .actualReturnDate(LocalDate.now())
                .build();
    }

    private Book createTestBook() {
        return Book.newBuilder()
                .id(1)
                .title("Title")
                .author("Author")
                .publisher("Publisher")
                .publishingDate(LocalDate.now())
                .amount(1)
                .onOrder(0)
                .build();
    }
}
