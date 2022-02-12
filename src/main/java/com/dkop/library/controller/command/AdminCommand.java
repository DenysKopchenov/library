package com.dkop.library.controller.command;


import com.dkop.library.model.Book;
import com.dkop.library.model.exceptions.NotFoundException;
import com.dkop.library.services.BookService;
import com.dkop.library.services.UserService;
import com.dkop.library.services.Validator;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class AdminCommand implements Command {
    Map<String, Consumer<HttpServletRequest>> operations = new HashMap<>();
    private final BookService bookService = new BookService();
    private final UserService userService = new UserService();

    public AdminCommand() {
        init();
    }

    private void init() {
        operations.put("createBook", this::createBookOperation);
        operations.put("deleteBook", this::deleteBookOperation);
        operations.put("updateBook", this::updateBookOperation);
        operations.put("catalog", this::showCatalogBookOperation);
    }


    @Override
    public String execute(HttpServletRequest request) {
//        if (StringUtils.isNotBlank(request.getParameter("searchByAuthor"))) {
//            bookService.searchByAuthor(request);
//
//        } else if (StringUtils.isNotBlank(request.getParameter("searchByTitle"))) {
//            bookService.searchByTitle(request);
//        }
        if (StringUtils.isNotBlank(request.getParameter("profile"))) {
            userService.getUserProfile(request);
        }

        if (StringUtils.isNotBlank(request.getParameter("operations"))) {
            String operation = request.getParameter("operations");
            handleOperations(operation, request);

//            switch (operation) {
//                case "catalog":
//                    try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
//                        List<Book> catalog = booksDao.getAllBooks();
//                        request.setAttribute("catalog", catalog);
//                    }
//                    break;
//                case "deleteBook":
//                    bookService.deleteBook(request, Integer.parseInt(request.getParameter("bookId")));
//                    try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
//                        List<Book> catalog = booksDao.getAllBooks();
//                        request.setAttribute("catalog", catalog);
//                    }
//                    break;
//                case "updateBook":
//                    try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
//                        Book book = booksDao.getBookById(Integer.parseInt(request.getParameter("bookId")));
//                        request.setAttribute("operation", "updateBook");
//                        request.setAttribute("updatingBook", book);
//                        if (request.getParameter("updateCurrentBook") != null) {
//                            bookService.updateBook(request, Integer.parseInt(request.getParameter("bookId")));
//                        }
//                    }
//                    break;
//                case "listUsers":
//                    try (UserDao userDao = DaoFactory.getInstance().createUserDao()) {
//                        List<User> users = userDao.getAllUsers();
//                        request.setAttribute("allUsers", users);
//                    }
//                    break;
//                case "createLibrarian":
//                    request.setAttribute("operation", "createLibrarian");
//                    //TODO: createLibrarian
//                    break;
//                case "deleteLibrarian":
//                    request.setAttribute("operation", "deleteLibrarian");
//                    //TODO: deleteLibrarian
//                    break;
//                case "blockUser":
//                    //TODO block user
//                    break;
//                case "unblockUser":
//                    // todo unblock user
//                    break;
//            }
        }
        return "/WEB-INF/views/admin.jsp";
    }

    private void handleOperations(String operation, HttpServletRequest request) {
        if (operations.containsKey(operation)) {
            operations.get(operation).accept(request);
        }
    }

    private void createBookOperation(HttpServletRequest request) {
        request.setAttribute("operation", "createBook");
        if (request.getParameter("createNewBook") != null) {
            String title = request.getParameter("title");
            request.setAttribute("title", title);
            String author = request.getParameter("author");
            String publisher = request.getParameter("publisher");
            String publishingDate = request.getParameter("publishing_date");
            String amount = request.getParameter("amount");
            Map<String, String> errors = Validator.validateBookForm(title, author, publisher, publishingDate, amount);
            if (errors.isEmpty()) {
                try {
                    bookService.addNewBook(title, author, publisher, publishingDate, amount);
                    request.setAttribute("successMessage", "Successfully created");
                } catch (SQLException e) {
                    request.setAttribute("errorMessage", "Creation failed, try to update book!");
                }
            } else {
                request.setAttribute("validation", errors);
            }
        }
    }

    private void deleteBookOperation(HttpServletRequest request) {
        String bookId = request.getParameter("bookId");
        if (StringUtils.isNumeric(bookId)) {
            try {
                bookService.deleteBook(Integer.parseInt(bookId));
                request.setAttribute("successMessage", "Successfully deleted");
            } catch (SQLException e) {
                request.setAttribute("errorMessage", "Failed delete");
            }
        }
    }

    private void showCatalogBookOperation(HttpServletRequest request) {
        request.setAttribute("catalog", bookService.findAll());
    }

    private void updateBookOperation(HttpServletRequest request) {
        request.setAttribute("operation", "updateBook");
        String id = request.getParameter("bookId");

        if (StringUtils.isNumeric(id)) {
            try {
                Book book = bookService.findById(Integer.parseInt(id));
                request.setAttribute("updatingBook", book);
                if (request.getParameter("updateCurrentBook") != null) {
                    String title = request.getParameter("title");
                    String author = request.getParameter("author");
                    String publisher = request.getParameter("publisher");
                    String publishingDate = request.getParameter("publishing_date");
                    String amount = request.getParameter("amount");
                    Map<String, String> errors = Validator.validateBookForm(title, author, publisher, publishingDate, amount);
                    if (errors.isEmpty()) {
                        bookService.updateBook(book, title, author, publisher, publishingDate, amount);
                        request.setAttribute("successMessage", "Successfully updated");
                    } else {
                        request.setAttribute("validation", errors);
                    }
                }
            } catch (NotFoundException e) {
                request.setAttribute("errorMessage", e.getMessage());
            } catch (SQLException e) {
                request.setAttribute("errorMessage", "Update failed");
            }
        }
    }
}