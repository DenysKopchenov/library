package com.dkop.library.services;

import com.dkop.library.dao.DaoFactory;
import com.dkop.library.entity.Book;
import com.dkop.library.entity.Order;
import com.dkop.library.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class PaginationService {

    private static final Logger LOGGER = LogManager.getLogger(PaginationService.class);
    private final BookService bookService;
    private final UserService userService;
    private final OrderService orderService;

    public PaginationService(BookService bookService, UserService userService, OrderService orderService) {
        this.bookService = bookService;
        this.userService = userService;
        this.orderService = orderService;
        LOGGER.info(PaginationService.class.getSimpleName());
    }

    public List<User> paginateUsersByRole(String role, int currentPageNumber, int perPage) {
        int startIndex = (currentPageNumber - 1) * perPage;
        return userService.findAllByRole(role, startIndex, perPage);
    }

    public int countNumberOfPagesForUsers(String role, int perPage) {
        return (int) Math.ceil(userService.countAllRowsByRole(role) * 1.0 / perPage);
    }

    public List<Book> paginateBooks(String sortedBy, int currentPageNumber, int perPage) {
        int startIndex = (currentPageNumber - 1) * perPage;
        return bookService.findAllSorted(sortedBy, startIndex, perPage);
    }

    public int countNumberOfPagesForBooks(int perPage) {
        return (int) Math.ceil(bookService.countAllRows() * 1.0 / perPage);
    }

    public List<Order> paginateOrdersByStatus(String status, int currentPageNumber, int perPage) {
        int startIndex = (currentPageNumber - 1) * perPage;
        return orderService.findAllOrdersByStatus(status, startIndex, perPage);
    }

    public List<Order> paginateOrdersByUser(int userId, int currentPageNumber, int perPage) {
        int startIndex = (currentPageNumber - 1) * perPage;
        return orderService.findAllUserApprovedOrders(userId, startIndex, perPage);
    }

    public int countNumberOfPagesForOrdersByStatus(String status, int perPage) {
        return (int) Math.ceil(orderService.countAllRowsByStatus(status) * 1.0 / perPage);
    }

    public int countNumberOfPagesForUserApprovedOrders(String status, int userId, int perPage) {
        return (int) Math.ceil(orderService.countAllRowsByStatusAndUser(status, userId) * 1.0 / perPage);
    }

    public int getRecordsPerPage(HttpServletRequest request) {
        if (StringUtils.isNumeric(request.getParameter("perPage"))) {
            return Integer.parseInt(request.getParameter("perPage"));
        }
        return 5;
    }

    public int getPageNumber(HttpServletRequest request) {
        if (StringUtils.isNumeric(request.getParameter("page"))) {
            return Integer.parseInt(request.getParameter("page"));
        }
        return 1;
    }
}
