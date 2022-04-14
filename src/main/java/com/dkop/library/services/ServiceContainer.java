package com.dkop.library.services;

import com.dkop.library.dao.DaoFactory;

public class ServiceContainer {
    private BookService bookService;
    private LoginService loginService;
    private OrderService orderService;
    private UserService userService;
    private PaginationService paginationService;

    public ServiceContainer() {
        init();
    }

    private void init() {
        bookService = new BookService(DaoFactory.getInstance());
        loginService = new LoginService(DaoFactory.getInstance());
        orderService = new OrderService(new BookService(DaoFactory.getInstance()), DaoFactory.getInstance());
        userService = new UserService(DaoFactory.getInstance());
        paginationService = new PaginationService(bookService, userService, orderService);
    }

    public BookService getBookService() {
        return bookService;
    }

    public LoginService getLoginService() {
        return loginService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public PaginationService getPaginationService() {
        return paginationService;
    }

    public UserService getUserService() {
        return userService;
    }
}
