package com.dkop.library.controller.command;

import com.dkop.library.entity.Book;
import com.dkop.library.entity.User;
import com.dkop.library.exceptions.AlreadyExistException;
import com.dkop.library.exceptions.DoesNotExistException;
import com.dkop.library.exceptions.NotFoundException;
import com.dkop.library.exceptions.UnableToDeleteException;
import com.dkop.library.services.BookService;
import com.dkop.library.services.PaginationService;
import com.dkop.library.services.UserService;
import com.dkop.library.services.Validator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.dkop.library.controller.command.CommandUtils.messagesBundle;

public class AdminCommand implements Command {
    private final Map<String, Consumer<HttpServletRequest>> operations = new HashMap<>();
    private final BookService bookService = BookService.getInstance();
    private final UserService userService = UserService.getInstance();
    private final PaginationService paginationService = PaginationService.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(AdminCommand.class);

    public AdminCommand() {
        init();
        LOGGER.info(AdminCommand.class.getSimpleName());
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
            LOGGER.error(e, e.getCause());
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
                    request.setAttribute("successMessage", messagesBundle.getString("successfully.created"));
                } catch (AlreadyExistException e) {
                    LOGGER.error(e, e.getCause());
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
                request.setAttribute("successMessage", messagesBundle.getString("successfully.deleted"));
                showCatalogBookOperation(request);
            } catch (NotFoundException | UnableToDeleteException e) {
                LOGGER.error(e, e.getCause());
                request.setAttribute("errorMessage", e.getMessage());
            }
        } else {
            request.setAttribute("errorMessage", messagesBundle.getString("id.error"));
        }
    }

    private void updateBookOperation(HttpServletRequest request) {
        String id = request.getParameter("bookId");
        if (StringUtils.isNumeric(id)) {
            try {
                Book book = bookService.findById(Integer.parseInt(id));
                request.setAttribute("operation", "updateBook");
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
                    } else {
                        request.setAttribute("validation", errors);
                    }
                }
            } catch (NotFoundException e) {
                LOGGER.error(e, e.getCause());
                request.setAttribute("errorMessage", e.getMessage());
            }
        } else {
            request.setAttribute("errorMessage", messagesBundle.getString("id.error"));
        }
    }

    private void showAllReadersOperation(HttpServletRequest request) {
        int perPage = paginationService.getRecordsPerPage(request);
        int page = paginationService.getPageNumber(request);

        List<User> usersPerPage = paginationService.paginateUsersByRole("reader", page, perPage);
        int numberOfPages = paginationService.countNumberOfPagesForUsers("reader", perPage);

        request.setAttribute("numberOfPages", numberOfPages);
        request.setAttribute("perPage", perPage);
        request.setAttribute("currentPage", Math.min(page, numberOfPages));
        request.setAttribute("allReaders", usersPerPage);
    }

    private void showAllLibrariansOperation(HttpServletRequest request) {
        int perPage = paginationService.getRecordsPerPage(request);
        int page = paginationService.getPageNumber(request);

        List<User> usersPerPage = paginationService.paginateUsersByRole("librarian", page, perPage);
        int numberOfPages = paginationService.countNumberOfPagesForUsers("librarian", perPage);

        request.setAttribute("numberOfPages", numberOfPages);
        request.setAttribute("perPage", perPage);
        request.setAttribute("currentPage", page);
        request.setAttribute("allLibrarians", usersPerPage);
    }

    private void showCatalogBookOperation(HttpServletRequest request) {
        request.setAttribute("sort", request.getParameter("sort"));
        String sortBy = request.getParameter("sort");
        int perPage = paginationService.getRecordsPerPage(request);
        int page = paginationService.getPageNumber(request);
        List<Book> catalog;
        if (sortBy != null) {
            switch (sortBy) {
                case "author":
                    request.setAttribute("sortBy", "By Author");
                    break;
                case "publisher":
                    request.setAttribute("sortBy", "By Publisher");
                    break;
                case "publishing_date":
                    request.setAttribute("sortBy", "By Publishing date");
                    break;
                default:
                    request.setAttribute("sortBy", "By Title");
            }
            catalog = paginationService.paginateBooks(sortBy, page, perPage);
        } else {
            catalog = paginationService.paginateBooks("title", page, perPage);
        }

        int numberOfPages = paginationService.countNumberOfPagesForBooks(perPage);
        request.setAttribute("numberOfPages", numberOfPages);
        request.setAttribute("perPage", perPage);
        request.setAttribute("currentPage", page);
        request.setAttribute("catalog", catalog);
    }

    private void deleteLibrarianOperation(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        if (StringUtils.isNumeric(userId)) {
            try {
                userService.deleteUser(Integer.parseInt(userId));
                request.setAttribute("successMessage", messagesBundle.getString("successfully.deleted"));
                showAllLibrariansOperation(request);
            } catch (NotFoundException | UnableToDeleteException e) {
                LOGGER.error(e, e.getCause());
                request.setAttribute("errorMessage", e.getMessage());
            }
        } else {
            LOGGER.error("Wrong id format");
            request.setAttribute("errorMessage", messagesBundle.getString("id.error"));
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
                request.setAttribute("successMessage", messagesBundle.getString("successfully.unblocked"));
                showAllReadersOperation(request);
            } catch (NotFoundException e) {
                LOGGER.error(e, e.getCause());
                request.setAttribute("errorMessage", e.getMessage());
                showAllReadersOperation(request);
            }
        } else {
            LOGGER.error("Wrong id format");
            request.setAttribute("errorMessage", messagesBundle.getString("id.error"));
        }
    }

    private void blockUserOperation(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        if (StringUtils.isNumeric(userId)) {
            try {
                userService.changeStatus(Integer.parseInt(userId), "blocked");
                request.setAttribute("successMessage", messagesBundle.getString("successfully.blocked"));
                showAllReadersOperation(request);
            } catch (NotFoundException e) {
                LOGGER.error(e, e.getCause());
                request.setAttribute("errorMessage", e.getMessage());
                showAllReadersOperation(request);
            }
        } else {
            LOGGER.error("Wrong id format");
            request.setAttribute("errorMessage", messagesBundle.getString("id.error"));
        }
    }
}