package com.dkop.library.controller.command;


import com.dkop.library.model.Book;
import com.dkop.library.model.User;
import com.dkop.library.model.exceptions.AlreadyExistException;
import com.dkop.library.model.exceptions.DoesNotExistException;
import com.dkop.library.model.exceptions.NotFoundException;
import com.dkop.library.services.BookService;
import com.dkop.library.services.UserService;
import com.dkop.library.services.Validator;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class AdminCommand implements Command {
    private final Map<String, Consumer<HttpServletRequest>> operations = new HashMap<>();
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
        operations.put("showAllUsers", this::showAllUsersOperation);
        operations.put("showAllLibrarians", this::showAllLibrariansOperation);
        operations.put("userInfo", this::showUserInfo);
        operations.put("blockUser", this::blockUserOperation);
        operations.put("unblockUser", this::unblockUserOperation);
        operations.put("createLibrarian", this::createLibrarianOperation);
        operations.put("deleteLibrarian", this::deleteLibrarianOperation);
    }

    @Override
    public String execute(HttpServletRequest request) {
        if (StringUtils.isNotBlank(request.getParameter("operations"))) {
            String operation = request.getParameter("operations");
            handleOperations(operation, request);

//        if (StringUtils.isNotBlank(request.getParameter("searchByAuthor"))) {
//            bookService.searchByAuthor(request);
//
//        } else if (StringUtils.isNotBlank(request.getParameter("searchByTitle"))) {
//            bookService.searchByTitle(request);
//        }
//        if (StringUtils.isNotBlank(request.getParameter("profile"))) {
//            showUserInfo(request);
//        }
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

    private void showUserInfo(HttpServletRequest request) {
        String email = (String) request.getServletContext().getAttribute("email");
        try {
            User user = userService.getUserInfo(email);
            request.setAttribute("userInfo", user);
        } catch (DoesNotExistException e) {
            request.setAttribute("errorMessage", e.getMessage());
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
                    bookService.createBook(title, author, publisher, publishingDate, amount);
                    request.setAttribute("successMessage", "Successfully created");
                } catch (AlreadyExistException e) {
                    request.setAttribute("errorMessage", e.getMessage());
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
                showCatalogBookOperation(request);
            } catch (NotFoundException e) {
                request.setAttribute("errorMessage", e.getMessage());
            }
        }
    }

    private void updateBookOperation(HttpServletRequest request) {
        request.setAttribute("operation", "updateBook");
        String id = request.getParameter("bookId");
        if (StringUtils.isNumeric(id)) {
            try {
                Book book = bookService.findById(Integer.parseInt(id));
                request.setAttribute("updatingBook", book); //shows what book is updating
                if (request.getParameter("updateCurrentBook") != null) {
                    String title = request.getParameter("title");
                    String author = request.getParameter("author");
                    String publisher = request.getParameter("publisher");
                    String publishingDate = request.getParameter("publishing_date");
                    String amount = request.getParameter("amount");
                    Map<String, String> errors = Validator.validateBookForm(title, author, publisher, publishingDate, amount);
                    if (errors.isEmpty()) {
                        bookService.updateBook(Integer.parseInt(id), title, author, publisher, publishingDate, amount);
                        request.setAttribute("successMessage", "Successfully updated");
                    } else {
                        request.setAttribute("validation", errors);
                    }
                }
            } catch (NotFoundException e) {
                request.setAttribute("errorMessage", e.getMessage());
            }
        }
    }

    private void showAllUsersOperation(HttpServletRequest request) {
        request.setAttribute("allUsers", userService.findAll());
    }

    private void showAllLibrariansOperation(HttpServletRequest request) {
        request.setAttribute("allLibrarians", userService.findAllLibrarians());
    }

    private void showCatalogBookOperation(HttpServletRequest request) {
        request.setAttribute("catalog", bookService.findAll());
    }

    private void deleteLibrarianOperation(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        if (StringUtils.isNumeric(userId)) {
            try {
                userService.deleteUser(Integer.parseInt(userId));
                request.setAttribute("successMessage", "Successfully deleted");
                showAllLibrariansOperation(request);
            } catch (NotFoundException e) {
                request.setAttribute("errorMessage", e.getMessage());
            }
        }
    }

    private void createLibrarianOperation(HttpServletRequest request) {
        request.setAttribute("operation", "createLibrarian");
        if (request.getParameter("register") != null) {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");
            String email = request.getParameter("email");

            Map<String, String> errors = Validator.validateRegistrationForm(firstName, lastName, password, confirmPassword, email);
            if (errors.isEmpty()) {
                try {
                    userService.createUser(firstName, lastName, email, password, "librarian", "active");
                    request.setAttribute("successMessage", "Successfully created");
                } catch (AlreadyExistException e) {
                    request.setAttribute("errorMessage", e.getMessage());
                }
            } else {
                request.setAttribute("validation", errors);
            }
        }
    }

    private void unblockUserOperation(HttpServletRequest request) {
    }

    private void blockUserOperation(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        if (StringUtils.isNumeric(userId)) {
            try {
                userService.blockUser(Integer.parseInt(userId));
                request.setAttribute("successMessage", "Successfully blocked");
            } catch (NotFoundException e) {
                request.setAttribute("errorMessage", e.getMessage());
            }
        }
    }

}