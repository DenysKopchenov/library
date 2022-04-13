package com.dkop.library.tests.dao;

import com.dkop.library.dao.OrderDao;
import com.dkop.library.dao.UserDao;
import com.dkop.library.dao.impls.OrderDaoImpl;
import com.dkop.library.entity.Book;
import com.dkop.library.entity.Order;
import com.dkop.library.exceptions.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.Or;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static com.dkop.library.controller.command.CommandUtils.messagesBundle;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class OrderDaoTest {

    private OrderDao orderDao;
    private final Connection mockConnection = mock(Connection.class);
    private final PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
    private final ResultSet mockResultSet = mock(ResultSet.class);

    @Before
    public void setUp() throws SQLException {
        orderDao = new OrderDaoImpl(mockConnection);
        messagesBundle = mock(ResourceBundle.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    public void testCreate() throws SQLException {
        orderDao.create(createTestOrder());
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(2)).setInt(anyInt(), anyInt());
        verify(mockPreparedStatement, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStatement, times(1)).setDate(anyInt(), any(Date.class));
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testFindAll() throws SQLException {
        List<Order> allOrders = orderDao.findAll();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeQuery();
        Assert.assertTrue(allOrders.isEmpty());
    }

    @Test(expected = NotFoundException.class)
    public void testFindById() throws SQLException, NotFoundException {
        try {
            orderDao.findById(1);
        } catch (NotFoundException e) {
            verify(mockConnection, times(1)).prepareStatement(anyString());
            verify(mockPreparedStatement, times(1)).setInt(anyInt(), anyInt());
            verify(mockPreparedStatement, times(1)).executeQuery();
            throw e;
        }
    }

    @Test
    public void testUpdate() throws SQLException {
        orderDao.update(createTestOrder());
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStatement, times(2)).setDate(anyInt(), any(Date.class));
        verify(mockPreparedStatement, times(1)).setInt(anyInt(), anyInt());
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testFindAllOrdersByStatus() throws SQLException {
        List<Order> ordersByStatus = orderDao.findAllOrdersByStatus(anyString(), 1, 5);
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStatement, times(2)).setInt(anyInt(), anyInt());
        verify(mockPreparedStatement, times(1)).executeQuery();
        Assert.assertTrue(ordersByStatus.isEmpty());
    }

    @Test
    public void testFindAllUserApprovedOrders() throws SQLException {
        List<Order> allApprovedOrders = orderDao.findAllUserApprovedOrders(anyInt(), 1, 5);
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(3)).setInt(anyInt(), anyInt());
        verify(mockPreparedStatement, times(1)).executeQuery();
        Assert.assertTrue(allApprovedOrders.isEmpty());
    }

    @Test
    public void testProcessOrder() throws SQLException {
        orderDao.processOrder(createTestOrder(), createTestBook());
        verify(mockConnection, times(2)).prepareStatement(anyString());
        verify(mockConnection, times(1)).setAutoCommit(false);
        verify(mockPreparedStatement, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStatement, times(2)).setDate(anyInt(), any(Date.class));
        verify(mockPreparedStatement, times(4)).setInt(anyInt(), anyInt());
        verify(mockPreparedStatement, times(2)).executeUpdate();
        verify(mockConnection, times(1)).commit();
    }

    @Test
    public void testProcessOrderThrows() throws SQLException {
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException());
        orderDao.processOrder(createTestOrder(), createTestBook());
        verify(mockConnection, times(2)).prepareStatement(anyString());
        verify(mockConnection, times(1)).setAutoCommit(false);
        verify(mockPreparedStatement, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStatement, times(2)).setDate(anyInt(), any(Date.class));
        verify(mockPreparedStatement, times(1)).setInt(anyInt(), anyInt());
        verify(mockPreparedStatement, times(1)).executeUpdate();
        verify(mockConnection, times(1)).rollback();
    }

    @Test
    public void testIsOrderExist() throws SQLException {
        boolean isExist = orderDao.isOrderExist(createTestOrder());
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(2)).setInt(anyInt(), anyInt());
        verify(mockPreparedStatement, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStatement, times(1)).executeQuery();
        Assert.assertFalse(isExist);
    }

    @Test
    public void testCountAllRowsByStatus() throws SQLException {
        int count = orderDao.countAllRowsByStatus(anyString());
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStatement, times(1)).executeQuery();
        Assert.assertEquals(0, count);
    }

    @Test
    public void testCountAllRowsByStatusAndUser() throws SQLException {
        int count = orderDao.countAllRowsByStatusAndUser(anyString(), 1);
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStatement, times(1)).setInt(anyInt(), anyInt());
        verify(mockPreparedStatement, times(1)).executeQuery();
        Assert.assertEquals(0, count);
    }

    private Order createTestOrder() {
        return Order.newBuilder()
                .id(1)
                .bookId(1)
                .userId(1)
                .type("any")
                .status("any")
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
