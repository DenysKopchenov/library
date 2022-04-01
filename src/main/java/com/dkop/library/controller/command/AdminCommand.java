package com.dkop.library.controller.command;

import com.dkop.library.entity.Book;
import com.dkop.library.entity.User;
import com.dkop.library.exceptions.AlreadyExistException;
import com.dkop.library.exceptions.DoesNotExistException;
import com.dkop.library.exceptions.NotFoundException;
import com.dkop.library.exceptions.UnableToDeleteException;
import com.dkop.library.services.BookService;
import com.dkop.library.services.UserService;
import com.dkop.library.services.Validator;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class AdminCommand implements Command {
    private final Map<String, Consumer<HttpServletRequest>> operations = new HashMap<>();
    private final BookService bookService = BookService.getInstance();
    private final UserService userService = UserService.getInstance();

    public AdminCommand() {
        init();
    }

    private void init() {
        operations.put("createBook", this::createBookOperation);
        operations.put("deleteBook", this::deleteBookOperation);
        operations.put("updateBook", this::updateBookOperation);
        operations.put("catalog", this::showCatalogBookOperation);
        operations.put("showAllReaders", this::showAllReadersOperation);
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
        }
        return "/WEB-INF/roles/admin.jsp";
    }

    private void handleOperations(String operation, HttpServletRequest request) {
        if (operations.containsKey(operation)) {
            operations.get(operation).accept(request);
        }
    }

    private void showUserInfo(HttpServletRequest request) {
        String email = (String) request.getSession().getAttribute("email");
        try {
            User user = userService.getUserInfo(email);
            request.setAttribute("user", user);
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
            } catch (NotFoundException | UnableToDeleteException e) {
                request.setAttribute("errorMessage", e.getMessage());
                e.getCause().printStackTrace();
            }
        } else {
            request.setAttribute("errorMessage", "ID may contains only digits");
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
                        request.setAttribute("operation", "");
                    } else {
                        request.setAttribute("validation", errors);
                    }
                }
            } catch (NotFoundException e) {
                request.setAttribute("errorMessage", e.getMessage());
            }
        } else {
            request.setAttribute("errorMessage", "ID may contains only digits");
        }
    }

    private void showAllReadersOperation(HttpServletRequest request) {
        int page = 1;
        int perPage = 1;
        if (request.getParameter("perPage") != null) {
            perPage = Integer.parseInt(request.getParameter("perPage"));
        }
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        List<User> allUsers = userService.findAll();
        int records = allUsers.size();
        int numberOfPages = (int) Math.ceil(records * 1.0 / perPage);
        List<User> usersPerPage = (List<User>) getCurrentRecordsPerPage(allUsers, page, perPage);

        request.setAttribute("numberOfPages", numberOfPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("allReaders", usersPerPage);
    }

    private List<?> getCurrentRecordsPerPage(List<?> all, int currentPageNumber, int perPage) {
        int startIndex = (currentPageNumber - 1) * perPage;
        int endIndex = (Math.min(startIndex + perPage, all.size()));
        if (endIndex > all.size() || startIndex > all.size()) {
            return all;
        }
        return all.subList(startIndex, endIndex);
    }

    private void showAllLibrariansOperation(HttpServletRequest request) {
        request.setAttribute("allLibrarians", userService.findAllByRole("librarian"));
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
            } catch (NotFoundException | UnableToDeleteException e) {
                request.setAttribute("errorMessage", e.getMessage());
            }
        } else {
            request.setAttribute("errorMessage", "ID may contains only digits");
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
                    request.setAttribute("operation", "");
                } catch (AlreadyExistException e) {
                    request.setAttribute("errorMessage", e.getMessage());
                }
            } else {
                request.setAttribute("validation", errors);
            }
        }
    }

    private void unblockUserOperation(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        if (StringUtils.isNumeric(userId)) {
            try {
                userService.changeStatus(Integer.parseInt(userId), "active");
                request.setAttribute("successMessage", "Successfully unblocked");
                showAllReadersOperation(request);
            } catch (NotFoundException e) {
                request.setAttribute("errorMessage", e.getMessage());
                showAllReadersOperation(request);
            }
        } else {
            request.setAttribute("errorMessage", "ID may contains only digits");
        }
    }

    private void blockUserOperation(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        if (StringUtils.isNumeric(userId)) {
            try {
                userService.changeStatus(Integer.parseInt(userId), "blocked");
                request.setAttribute("successMessage", "Successfully blocked");
                showAllReadersOperation(request);
            } catch (NotFoundException e) {
                request.setAttribute("errorMessage", e.getMessage());
                showAllReadersOperation(request);
            }
        } else {
            request.setAttribute("errorMessage", "ID may contains only digits");
        }
    }
}