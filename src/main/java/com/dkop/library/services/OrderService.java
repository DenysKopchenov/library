package com.dkop.library.services;

import com.dkop.library.dao.DaoFactory;
import com.dkop.library.dao.OrderDao;
import com.dkop.library.entity.Book;
import com.dkop.library.entity.Order;
import com.dkop.library.entity.User;
import com.dkop.library.exceptions.DoesNotExistException;
import com.dkop.library.exceptions.NotFoundException;
import com.dkop.library.exceptions.UnableToAcceptOrderException;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class OrderService {
    private static OrderService instance;
    private final BookService bookService;
    private final UserService userService;
    private final DaoFactory daoFactory;

    public static OrderService getInstance() {
        if (instance == null) {
            synchronized (LoginService.class) {
                if (instance == null) {
                    OrderService orderService = new OrderService();
                    instance = orderService;
                }
            }
        }
        return instance;
    }

    private OrderService() {
        daoFactory = DaoFactory.getInstance();
        bookService = BookService.getInstance();
        userService = UserService.getInstance();
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
            e.printStackTrace();
        }
    }

    public List<Order> findAllUserApprovedOrders(int userId) {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            return orderDao.findAllUserApprovedOrders(userId);
        }
    }

    public List<Order> findAllOrdersByStatus(String status) {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            return orderDao.findAllOrdersByStatus(status);
        }
    }


    public void returnBook(int orderId) throws NotFoundException {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            Order order = orderDao.findById(orderId);
//            order.setActualReturnDate(LocalDate.now()); sets automatically on db
            order.setStatus("completed");

            Book book = bookService.findById(order.getBookId());
            book.setAmount(book.getAmount() + 1);
            book.setOnOrder(book.getOnOrder() - 1);

            orderDao.processOrder(order, book);
        }
    }

    public void acceptOrder(Order order) throws NotFoundException, UnableToAcceptOrderException {
        Book book = bookService.findById(order.getBookId());
        int amount = book.getAmount();
        if (amount <= 0) {
            throw new UnableToAcceptOrderException("Unable to accept order, we have no books, reject please");
        }
        book.setAmount(book.getAmount() - 1);
        book.setOnOrder(book.getOnOrder() + 1);
        LocalDate expectedReturnDate = order.getType().equals("home") ? LocalDate.now().plusDays(25) : LocalDate.now();
        order.setApprovedDate(LocalDate.now());
        order.setExpectedReturnDate(expectedReturnDate);
        order.setStatus("approved");

        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            orderDao.processOrder(order, book);
        }
    }

    public void rejectOrder(Order order) throws NotFoundException {
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
}
