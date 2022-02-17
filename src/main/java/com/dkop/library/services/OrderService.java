package com.dkop.library.services;

import com.dkop.library.dao.DaoFactory;
import com.dkop.library.dao.OrderDao;
import com.dkop.library.model.Book;
import com.dkop.library.model.Order;
import com.dkop.library.model.exceptions.AlreadyExistException;
import com.dkop.library.model.exceptions.NotFoundException;

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


    public void createOrder(int bookId, int userId, String type) throws AlreadyExistException, NotFoundException {
        Order order = Order.newBuilder()
                .userId(userId)
                .bookId(bookId)
                .type(type)
                .build();
        Book book = null;
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            book = bookService.findById(bookId);
            orderDao.create(order);
        } catch (SQLException e) {
            throw new AlreadyExistException("You already order " + book);
        } catch (NotFoundException e) {
            throw new NotFoundException("Book not found");
        }
    }

    public List<Order> findAllApprovedUserOrders(int userId) {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            return orderDao.findAllApprovedUserOrders(userId);
        }
    }

    public long checkForPenalty(Order order) {
        LocalDate expectedReturnDate = order.getExpectedReturnDate();
        LocalDate now = LocalDate.now();
        if (expectedReturnDate.isBefore(now)) {
            int daysDifference = Period.between(expectedReturnDate, now).getDays();
            long penalty = daysDifference * 487;
            return penalty;
        } else {
            return 0;
        }
    }
}
