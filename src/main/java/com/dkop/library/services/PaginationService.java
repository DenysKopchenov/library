package com.dkop.library.services;

import com.dkop.library.entity.Book;
import com.dkop.library.entity.Order;
import com.dkop.library.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@DependsOn({"bookService", "userService", "orderService"})
public class PaginationService {

    private final BookService bookService;
    private final UserService userService;
    private final OrderService orderService;
    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_RECORDS_PER_PAGE = 5;

    public PaginationService(BookService bookService, UserService userService, OrderService orderService) {
        this.bookService = bookService;
        this.userService = userService;
        this.orderService = orderService;
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

    public int countNumberOfPagesForOrdersByStatus(String status, int perPage) {
        return (int) Math.ceil(orderService.countAllRowsByStatus(status) * 1.0 / perPage);
    }

    public List<Order> paginateApprovedOrdersByUser(int userId, int currentPageNumber, int perPage) {
        int startIndex = (currentPageNumber - 1) * perPage;
        return orderService.findAllUserApprovedOrders(userId, startIndex, perPage);
    }

    public int countNumberOfPagesForUserApprovedOrders(String status, int userId, int perPage) {
        return (int) Math.ceil(orderService.countAllRowsByStatusAndUser(status, userId) * 1.0 / perPage);
    }

    public int getRecordsPerPage(HttpServletRequest request) {
        if (StringUtils.isNumeric(request.getParameter("perPage"))) {
            return Integer.parseInt(request.getParameter("perPage"));
        }
        return DEFAULT_RECORDS_PER_PAGE;
    }

    public int getPageNumber(HttpServletRequest request) {
        if (StringUtils.isNumeric(request.getParameter("page"))) {
            return Integer.parseInt(request.getParameter("page"));
        }
        return DEFAULT_PAGE_NUMBER;
    }
}
