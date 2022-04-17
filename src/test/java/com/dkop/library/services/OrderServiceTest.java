package com.dkop.library.services;

import com.dkop.library.dao.BooksDao;
import com.dkop.library.dao.DaoFactory;
import com.dkop.library.dao.OrderDao;
import com.dkop.library.entity.Book;
import com.dkop.library.entity.Order;
import com.dkop.library.exceptions.NotFoundException;
import com.dkop.library.exceptions.UnknownOperationException;
import com.dkop.library.exceptions.UnableToAcceptOrderException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static com.dkop.library.utils.LocalizationUtil.errorMessagesBundle;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class OrderServiceTest {

    private OrderService orderService;
    private DaoFactory daoFactoryMock = mock(DaoFactory.class);
    private BooksDao booksDaoMock = mock(BooksDao.class);
    private OrderDao orderDaoMock = mock(OrderDao.class);

    @Before
    public void setUp() {
        try (MockedStatic<DaoFactory> daoFactoryMockedStatic = mockStatic(DaoFactory.class)) {
            daoFactoryMockedStatic.when(DaoFactory::getInstance).thenReturn(daoFactoryMock);
            when(daoFactoryMock.createBooksDao()).thenReturn(booksDaoMock);
            when(daoFactoryMock.createOrderDao()).thenReturn(orderDaoMock);
            errorMessagesBundle = mock(ResourceBundle.class);
            orderService = new OrderService(DaoFactory.getInstance());
        }
    }

    @Test
    public void testCreateOrder() throws SQLException {
        Order order = Order.newBuilder()
                .userId(1)
                .bookId(1)
                .type("type")
                .build();
        orderService.createOrder(1, 1, "type");
        verify(daoFactoryMock, times(1)).createOrderDao();
        verify(orderDaoMock, times(1)).create(order);
    }

    @Test
    public void testFindAllUserApprovedOrders() {
        List<Order> orders = orderService.findAllUserApprovedOrders(1, 1, 5);
        verify(daoFactoryMock, times(1)).createOrderDao();
        verify(orderDaoMock, times(1)).findAllUserApprovedOrders(1, 1, 5);
        Assert.assertTrue(orders.isEmpty());
    }

    @Test
    public void testFindAllOrdersByStatus() {
        List<Order> orders = orderService.findAllOrdersByStatus("status", 1, 5);
        verify(daoFactoryMock, times(1)).createOrderDao();
        verify(orderDaoMock, times(1)).findAllOrdersByStatus("status", 1, 5);
        Assert.assertTrue(orders.isEmpty());
    }

    @Test(expected = RuntimeException.class)
    public void testReturnBookThrowsRuntimeEx() throws NotFoundException {
        try {
            orderService.returnBook(1);
        } catch (RuntimeException e) {
            verify(daoFactoryMock, times(1)).createOrderDao();
            verify(daoFactoryMock, times(1)).createBooksDao();
            verify(orderDaoMock, times(1)).findById(1);
            throw e;
        }
    }

    @Test
    public void testReturnBook() throws NotFoundException {
        Order testOrder = Order.newBuilder()
                .bookId(1)
                .status("approved")
                .build();
        Book testBook = Book.newBuilder().build();
        when(orderDaoMock.findById(1)).thenReturn(testOrder);
        when(booksDaoMock.findById(1)).thenReturn(testBook);
        orderService.returnBook(1);
        verify(daoFactoryMock, times(1)).createOrderDao();
        verify(daoFactoryMock, times(1)).createBooksDao();
        verify(orderDaoMock, times(1)).findById(1);
        verify(booksDaoMock, times(1)).findById(1);
        verify(orderDaoMock, times(1)).processOrder(testOrder, testBook);
    }

    @Test(expected = UnknownOperationException.class)
    public void testAcceptOrderThrowsRuntimeEx() throws UnableToAcceptOrderException, NotFoundException {
        Order testOrder = Order.newBuilder()
                .bookId(1)
                .status("approved")
                .build();
        orderService.acceptOrder(testOrder);
    }

    @Test(expected = UnableToAcceptOrderException.class)
    public void testAcceptOrderThrowsUnableToAccept() throws UnableToAcceptOrderException, NotFoundException {
        Order testOrder = Order.newBuilder()
                .bookId(1)
                .status("pending")
                .build();
        Book testBook = Book.newBuilder().build();
        when(booksDaoMock.findById(1)).thenReturn(testBook);
        try {
            orderService.acceptOrder(testOrder);
        } catch (UnableToAcceptOrderException e) {
            verify(daoFactoryMock, times(1)).createOrderDao();
            verify(daoFactoryMock, times(1)).createBooksDao();
            verify(booksDaoMock, times(1)).findById(1);
            throw e;
        }
    }

    @Test
    public void testAcceptOrder() throws UnableToAcceptOrderException, NotFoundException {
        Order testOrder = Order.newBuilder()
                .bookId(1)
                .type("home")
                .status("pending")
                .build();
        Book testBook = Book.newBuilder()
                .amount(1)
                .build();
        when(booksDaoMock.findById(1)).thenReturn(testBook);
        orderService.acceptOrder(testOrder);
        verify(daoFactoryMock, times(1)).createOrderDao();
        verify(daoFactoryMock, times(1)).createBooksDao();
        verify(booksDaoMock, times(1)).findById(1);
        verify(orderDaoMock, times(1)).processOrder(testOrder, testBook);
    }

    @Test(expected = UnknownOperationException.class)
    public void testRejectOrderThrowsRuntimeEx() {
        Order testOrder = Order.newBuilder()
                .status("approved")
                .build();
        orderService.rejectOrder(testOrder);
    }

    @Test
    public void testRejectOrder() {
        Order testOrder = Order.newBuilder()
                .status("pending")
                .build();
        orderService.rejectOrder(testOrder);
        verify(daoFactoryMock, times(1)).createOrderDao();
        verify(orderDaoMock, times(1)).update(testOrder);
    }

    @Test(expected = NotFoundException.class)
    public void testFindByIdThrowsNotFound() throws NotFoundException {
        when(orderDaoMock.findById(1)).thenThrow(NotFoundException.class);
        try {
            orderService.findById(1);
        } catch (NotFoundException e) {
            verify(daoFactoryMock, times(1)).createOrderDao();
            verify(orderDaoMock, times(1)).findById(1);
            throw e;
        }
    }

    @Test
    public void testFindById() throws NotFoundException {
        when(orderDaoMock.findById(1)).thenReturn(Order.newBuilder().build());
        Order foundedOrder = orderService.findById(1);
        verify(daoFactoryMock, times(1)).createOrderDao();
        verify(orderDaoMock, times(1)).findById(1);
        Assert.assertEquals(foundedOrder, Order.newBuilder().build());
    }

    @Test
    public void testCheckForPenaltyOneDay() {
        Order testOrder = Order.newBuilder()
                .expectedReturnDate(LocalDate.now().minusDays(1)).build();
        long penalty = orderService.checkForPenalty(testOrder);
        Assert.assertEquals(500, penalty);
    }

    @Test
    public void testIsOrderExist() {
        Order testOrder = Order.newBuilder()
                .bookId(1)
                .userId(1)
                .type("type")
                .build();
        boolean isExist = orderService.isOrderExist(1, 1, "type");
        verify(daoFactoryMock, times(1)).createOrderDao();
        verify(orderDaoMock, times(1)).isOrderExist(testOrder);
        Assert.assertFalse(isExist);
    }

    @Test
    public void testCountAllRowsByStatus() {
        int count = orderService.countAllRowsByStatus("status");
        verify(daoFactoryMock, times(1)).createOrderDao();
        verify(orderDaoMock, times(1)).countAllRowsByStatus("status");
        Assert.assertEquals(0, count);
    }

    @Test
    public void testCountAllRowsByStatusAndUser() {
        int count = orderService.countAllRowsByStatusAndUser("status", 1);
        verify(daoFactoryMock, times(1)).createOrderDao();
        verify(orderDaoMock, times(1)).countAllRowsByStatusAndUser("status", 1);
        Assert.assertEquals(0, count);
    }
}
