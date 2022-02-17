package com.dkop.library.controller.command;

import com.dkop.library.model.Book;
import com.dkop.library.model.User;
import com.dkop.library.model.exceptions.AlreadyExistException;
import com.dkop.library.model.exceptions.DoesNotExistException;
import com.dkop.library.model.exceptions.NotFoundException;
import com.dkop.library.services.BookService;
import com.dkop.library.services.OrderService;
import com.dkop.library.services.UserService;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ReaderCommand implements Command {
    private final Map<String, Consumer<HttpServletRequest>> operations = new HashMap<>();
    private final BookService bookService = BookService.getInstance();
    private final UserService userService = new UserService();
    private final OrderService orderService = new OrderService();

    public ReaderCommand() {
        init();
    }

    private void init() {
        operations.put("catalog", this::showCatalogBookOperation);
        operations.put("userInfo", this::showUserInfo);
        operations.put("orderBook", this::orderBookOperation);
    }

    @Override
    public String execute(HttpServletRequest request) {
        if (StringUtils.isNotBlank(request.getParameter("searchByAuthor"))) {
            String author = (request.getParameter("searchByAuthor"));
            List<Book> books = bookService.findAllBooksByAuthor(author);
            isAnyFounded(books, request, "by Author", author);
        }
        if (StringUtils.isNotBlank(request.getParameter("searchByTitle"))) {
            String title = request.getParameter("searchByTitle");
            List<Book> books = bookService.findAllBooksByTitle(title);
            isAnyFounded(books, request, "by Title", title);
        }
        if (StringUtils.isNotBlank(request.getParameter("operations"))) {
            String operation = request.getParameter("operations");
            handleOperations(operation, request);
        }
        return "/WEB-INF/views/reader.jsp";
    }

    private void handleOperations(String operation, HttpServletRequest request) {
        if (operations.containsKey(operation)) {
            operations.get(operation).accept(request);
        }
    }

    private void orderBookOperation(HttpServletRequest request) {
        if (StringUtils.isNotBlank(request.getParameter("order"))) {
            String orderType = request.getParameter("order");
            String email = (String) request.getServletContext().getAttribute("email");
            try {
                int userId = userService.getUserInfo(email).getId();
                int bookId = Integer.parseInt(request.getParameter("bookId"));
                switch (orderType) {
                    case "readingRoom":
                        orderService.createOrderReadingRoom(bookId, userId, "readingRoom");
                        break;
                    case "home":
                        orderService.createOrderHome();
                        break;
                }
            } catch (AlreadyExistException | NotFoundException | DoesNotExistException e) {
                request.setAttribute("errorMessage", e.getMessage());
                showCatalogBookOperation(request);
            }
        }
    }

    private void showCatalogBookOperation(HttpServletRequest request) {
        request.setAttribute("operation", "catalog");
        request.setAttribute("sort", request.getParameter("sort"));
        String sortBy = request.getParameter("sort");
        List<Book> catalog;
        if (sortBy != null) {
            catalog = bookService.findAllSorted(sortBy);
        } else {
            catalog = bookService.findAll();
        }
        request.setAttribute("catalog", catalog);
    }

    private void isAnyFounded(List<Book> books, HttpServletRequest request, String by, String parameter) {
        if (!books.isEmpty()) {
            request.setAttribute("foundedBooks", books);
            request.setAttribute("successMessage", String.format("Books founded %s '%s':", by, parameter));
        } else {
            request.setAttribute("errorMessage", String.format("No books found %s '%s':", by, parameter));
        }
    }

    private void showUserInfo(HttpServletRequest request) {
        String email = (String) request.getServletContext().getAttribute("email");
        try {
            User user = userService.getUserInfo(email);
            request.setAttribute("userInfo", user);
        } catch (DoesNotExistException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
    }
}
