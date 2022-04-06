package com.dkop.library.services;

import com.dkop.library.controller.command.LibrarianCommand;
import com.dkop.library.dao.DaoFactory;
import com.dkop.library.entity.Book;
import com.dkop.library.entity.User;

import java.util.List;

public class PaginationService {
    private final DaoFactory daoFactory;
    private static PaginationService instance;
    private final BookService bookService;
    private final UserService userService;

    public static PaginationService getInstance() {
        if (instance == null) {
            synchronized (BookService.class) {
                if (instance == null) {
                    PaginationService paginationService = new PaginationService();
                    instance = paginationService;
                }
            }
        }
        return instance;
    }

    private PaginationService() {
        daoFactory = DaoFactory.getInstance();
        bookService = BookService.getInstance();
        userService = UserService.getInstance();
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

}
