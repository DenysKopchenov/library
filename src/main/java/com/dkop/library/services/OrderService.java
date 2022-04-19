package com.dkop.library.services;

import com.dkop.library.dao.BooksDao;
import com.dkop.library.dao.DaoFactory;
import com.dkop.library.dao.OrderDao;
import com.dkop.library.entity.Book;
import com.dkop.library.entity.Order;
import com.dkop.library.exceptions.NotFoundException;
import com.dkop.library.exceptions.UnknownOperationException;
import com.dkop.library.exceptions.UnableToAcceptOrderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static com.dkop.library.utils.LocalizationUtil.localizationBundle;

public class OrderService {

    private static final Logger LOGGER = LogManager.getLogger(OrderService.class);
    private final DaoFactory daoFactory;

    public OrderService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
        LOGGER.info(OrderService.class.getSimpleName());
    }

    public void createOrder(int bookId, int userId, String type) {
        Order order = Order.newBuilder()
                .userId(userId)
                .bookId(bookId)
                .type(type)
                .build();
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            orderDao.create(order);
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
        }
    }

    public List<Order> findAllUserApprovedOrders(int userId, int start, int numberOfRecords) {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            return orderDao.findAllUserApprovedOrders(userId, start, numberOfRecords);
        }
    }

    public List<Order> findAllOrdersByStatus(String status, int start, int numberOfRecords) {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            return orderDao.findAllOrdersByStatus(status, start, numberOfRecords);
        }
    }

    public void returnBook(int orderId) throws NotFoundException {
        try (OrderDao orderDao = daoFactory.createOrderDao();
             BooksDao booksDao = daoFactory.createBooksDao()) {
            Order order = orderDao.findById(orderId);
            if (order.getStatus().equals("approved")) {
                order.setStatus("completed");
            } else {
                LOGGER.error(new RuntimeException());
            }

            Book book = booksDao.findById(order.getBookId());
            book.setAmount(book.getAmount() + 1);
            book.setOnOrder(book.getOnOrder() - 1);

            orderDao.processOrder(order, book);
        }
    }

    public void acceptOrder(Order order) throws NotFoundException, UnableToAcceptOrderException {
        if (!order.getStatus().equals("pending")) {
            LOGGER.error(UnknownOperationException.class);
            throw new UnknownOperationException();
        }
        try (OrderDao orderDao = daoFactory.createOrderDao();
             BooksDao booksDao = daoFactory.createBooksDao()) {
            Book book = booksDao.findById(order.getBookId());
            int amount = book.getAmount();
            if (amount <= 0) {
                throw new UnableToAcceptOrderException(localizationBundle.getString("unable.accept.order"));
            }
            book.setAmount(book.getAmount() - 1);
            book.setOnOrder(book.getOnOrder() + 1);
            LocalDate expectedReturnDate = order.getType().equals("home") ? LocalDate.now().plusDays(25) : LocalDate.now();
            order.setApprovedDate(LocalDate.now());
            order.setExpectedReturnDate(expectedReturnDate);
            order.setStatus("approved");
            orderDao.processOrder(order, book);
        }
    }

    public void rejectOrder(Order order) {
        if (!order.getStatus().equals("pending")) {
            LOGGER.error(UnknownOperationException.class);
            throw new UnknownOperationException();
        }
        order.setStatus("rejected");
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            orderDao.update(order);
        }

    }

    public Order findById(int orderId) throws NotFoundException {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            return orderDao.findById(orderId);
        }
    }

    public long checkForPenalty(Order order) {
        LocalDate expectedReturnDate = order.getExpectedReturnDate();
        LocalDate now = LocalDate.now();
        if (expectedReturnDate.isBefore(now)) {
            int daysDifference = Period.between(expectedReturnDate, now).getDays();
            return daysDifference * 500L;
        } else {
            return 0;
        }
    }

    public boolean isOrderExist(int bookId, int userId, String type) {
        Order order = Order.newBuilder()
                .userId(userId)
                .bookId(bookId)
                .type(type)
                .build();
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            return orderDao.isOrderExist(order);
        }
    }

    public int countAllRowsByStatus(String status) {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            return orderDao.countAllRowsByStatus(status);
        }
    }

    public int countAllRowsByStatusAndUser(String status, int userId) {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            return orderDao.countAllRowsByStatusAndUser(status, userId);
        }
    }
}
