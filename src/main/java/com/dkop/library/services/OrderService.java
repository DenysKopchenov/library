package com.dkop.library.services;

import com.dkop.library.dao.BooksDao;
import com.dkop.library.dao.OrderDao;
import com.dkop.library.entity.Book;
import com.dkop.library.entity.Order;
import com.dkop.library.exceptions.NotFoundException;
import com.dkop.library.exceptions.UnknownOperationException;
import com.dkop.library.exceptions.UnableToAcceptOrderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static com.dkop.library.utils.LocalizationUtil.localizationBundle;

@Service
public class OrderService {

    private final BooksDao booksDao;
    private final OrderDao orderDao;
    private static final Logger LOGGER = LogManager.getLogger(OrderService.class);

    public OrderService(BooksDao booksDao, OrderDao orderDao) {
        this.booksDao = booksDao;
        this.orderDao = orderDao;
    }

    public void createOrder(int bookId, int userId, String type) {
        Order order = Order.newBuilder()
                .userId(userId)
                .bookId(bookId)
                .type(type)
                .build();
        try {
            orderDao.create(order);
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
        }
    }

    public List<Order> findAllUserApprovedOrders(int userId, int start, int numberOfRecords) {
            return orderDao.findAllUserApprovedOrders(userId, start, numberOfRecords);
    }

    public List<Order> findAllOrdersByStatus(String status, int start, int numberOfRecords) {
            return orderDao.findAllOrdersByStatus(status, start, numberOfRecords);
    }

    /**
     * Changes order status to the completed, update specified book: increases amount on 1, decreases onOrder on 1
     *
     * @param orderId
     * @throws NotFoundException         if order or book was not found
     * @throws UnknownOperationException if order status is not 'approved'
     */
    public void returnOrder(int orderId) throws NotFoundException {
        Order order = orderDao.findById(orderId);
        if (order.getStatus().equals("approved")) {
            order.setStatus("completed");
        } else {
            LOGGER.error("Unable to return not approved order");
            throw new UnknownOperationException();
        }

        Book book = booksDao.findById(order.getBookId());
        book.setAmount(book.getAmount() + 1);
        book.setOnOrder(book.getOnOrder() - 1);

        orderDao.processOrder(order, book);
    }

    /**
     * Changes order status to the approved, update specified book: increases onOrder on 1, decreases amount on 1
     *
     * @param order
     * @throws NotFoundException            if book was not found
     * @throws UnableToAcceptOrderException if book amount <= 0
     * @throws UnknownOperationException    if order status is not 'pending'
     */
    public void acceptOrder(Order order) throws NotFoundException, UnableToAcceptOrderException {
        if (!order.getStatus().equals("pending")) {
            LOGGER.error("Unable to accept not pending order");
            throw new UnknownOperationException();
        }
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

    /**
     * Changes order status to the rejected
     *
     * @param order
     * @throws UnknownOperationException if order status is not 'pending'
     */
    public void rejectOrder(Order order) {
        if (!order.getStatus().equals("pending")) {
            LOGGER.error("Unable to accept not pending order");
            throw new UnknownOperationException();
        }
        order.setStatus("rejected");
        orderDao.update(order);
    }

    public Order findById(int orderId) throws NotFoundException {
        return orderDao.findById(orderId);
    }

    /**
     * Checks is the user has penalty. 5 UAH for each day difference between expected return date
     *
     * @param order
     * @return penalty size
     */
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
        return orderDao.isOrderExist(order);
    }

    public int countAllRowsByStatus(String status) {
        return orderDao.countAllRowsByStatus(status);
    }

    public int countAllRowsByStatusAndUser(String status, int userId) {
        return orderDao.countAllRowsByStatusAndUser(status, userId);
    }
}
