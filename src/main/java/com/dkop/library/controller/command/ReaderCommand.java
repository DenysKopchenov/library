package com.dkop.library.controller.command;

import com.dkop.library.entity.Book;
import com.dkop.library.entity.Order;
import com.dkop.library.entity.User;
import com.dkop.library.dto.UserOrderDTO;
import com.dkop.library.exceptions.AlreadyExistException;
import com.dkop.library.exceptions.DoesNotExistException;
import com.dkop.library.exceptions.NotFoundException;
import com.dkop.library.services.BookService;
import com.dkop.library.services.OrderService;
import com.dkop.library.services.UserService;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ReaderCommand implements Command {
    private final Map<String, Consumer<HttpServletRequest>> operations = new HashMap<>();
    private final BookService bookService;
    private final UserService userService;
    private final OrderService orderService;

    public ReaderCommand() {
        bookService = BookService.getInstance();
        userService = UserService.getInstance();
        orderService = OrderService.getInstance();
        init();
    }

    private void init() {
        operations.put("catalog", this::showCatalogBookOperation);
        operations.put("userInfo", this::showUserInfo);
        operations.put("orderBook", this::orderBookOperation);
        operations.put("showApprovedOrders", this::showApprovedOrders);
        operations.put("returnBook", this::returnBookOperation);
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
            String email = (String) request.getSession().getAttribute("email");
            try {
                int userId = userService.getUserInfo(email).getId();
                int bookId = Integer.parseInt(request.getParameter("bookId"));
                createOrderIfNotExist(bookId, userId, orderType, request);
            } catch (AlreadyExistException | NotFoundException | DoesNotExistException e) {
                request.setAttribute("errorMessage", e.getMessage());
            }
        }
    }

    private void createOrderIfNotExist(int bookId, int userId, String orderType, HttpServletRequest request) throws NotFoundException, AlreadyExistException {
        if (!orderService.isOrderExist(bookId, userId, orderType)) {
            orderService.createOrder(bookId, userId, orderType);
            request.setAttribute("successMessage", "Order created, after approve you find it on 'Show orders'");
        } else {
            throw new AlreadyExistException("You already order this book");
        }
    }

    private void showCatalogBookOperation(HttpServletRequest request) {
        request.setAttribute("operation", "catalog");
        String sortBy = request.getParameter("sort");
        List<Book> catalog;
        if (sortBy != null) {
            switch (sortBy) {
                case "author":
                    request.setAttribute("sort", "By Author");
                    break;
                case "publisher":
                    request.setAttribute("sort", "By Publisher");
                    break;
                case "publishing_date":
                    request.setAttribute("sort", "By Publishing date");
                    break;
                default:
                    request.setAttribute("sort", "By Title");
            }
            catalog = bookService.findAllSorted(sortBy);
        } else {
            catalog = bookService.findAll();
        }
        request.setAttribute("catalog", catalog);
    }

    private void showApprovedOrders(HttpServletRequest request) {
        try {
            User user = userService.getUserInfo((String) request.getSession().getAttribute("email"));
            List<UserOrderDTO> userApprovedOrders = new ArrayList<>();
            List<Order> allApproved = orderService.findAllUserApprovedOrders(user.getId());
            allApproved.forEach(order -> {
                try {
                    UserOrderDTO userOrder = new UserOrderDTO();
                    long penalty = orderService.checkForPenalty(order);
                    userOrder.setPenalty(String.valueOf(penalty));
                    userOrder.setCreateDate(order.getCreateDate());
                    userOrder.setExpectedReturnDate(order.getExpectedReturnDate());
                    userOrder.setBook(bookService.findById(order.getBookId()));
                    userOrder.setOrderId(order.getId());
                    userApprovedOrders.add(userOrder);
                } catch (NotFoundException e) {
                    request.setAttribute("errorMessage", e.getMessage());
                }
            });
            request.setAttribute("userOrders", userApprovedOrders);
        } catch (DoesNotExistException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
    }

    private void returnBookOperation(HttpServletRequest request) {
        String orderId = request.getParameter("orderId");
        if (StringUtils.isNumeric(orderId)) {
            try {
                orderService.returnBook(Integer.parseInt(orderId));
                request.setAttribute("successMessage", "Successfully returned");
                showApprovedOrders(request);
            } catch (NotFoundException e) {
                request.setAttribute("errorMessage", e.getMessage());
            }
        }
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
        String email = (String) request.getSession().getAttribute("email");
        try {
            User user = userService.getUserInfo(email);
            request.setAttribute("userInfo", user);
        } catch (DoesNotExistException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
    }
}
