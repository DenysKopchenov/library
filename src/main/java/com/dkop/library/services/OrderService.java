package com.dkop.library.services;

import com.dkop.library.dao.BooksDao;
import com.dkop.library.dao.DaoFactory;
import com.dkop.library.dao.OrderDao;
import com.dkop.library.model.Book;
import com.dkop.library.model.Order;
import com.dkop.library.model.exceptions.AlreadyExistException;
import com.dkop.library.model.exceptions.DoesNotExistException;
import com.dkop.library.model.exceptions.NotFoundException;

import java.sql.SQLException;

public class OrderService {
    BookService bookService = new BookService();

    public void createOrderReadingRoom(int bookId, int userId, String type) throws AlreadyExistException, NotFoundException {
        Order order = Order.newBuilder()
                .userId(userId)
                .bookId(bookId)
                .type(type)
                .build();
        Book book = null;
        try (OrderDao orderDao = DaoFactory.getInstance().createOrderDao()) {
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
