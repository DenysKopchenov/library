package com.dkop.library.services;

import com.dkop.library.dao.DaoFactory;
import com.dkop.library.dao.OrderDao;
import com.dkop.library.model.Book;
import com.dkop.library.model.Order;
import com.dkop.library.model.exceptions.AlreadyExistException;
import com.dkop.library.model.exceptions.NotFoundException;

import java.sql.SQLException;

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


    public void createOrderReadingRoom(int bookId, int userId, String type) throws AlreadyExistException, NotFoundException {
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

    public void createOrderHome() {

    }
}
