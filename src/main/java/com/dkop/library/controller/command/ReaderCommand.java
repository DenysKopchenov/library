package com.dkop.library.controller.command;

import com.dkop.library.entity.Book;
import com.dkop.library.entity.Order;
import com.dkop.library.entity.User;
import com.dkop.library.dto.UserOrderDto;
import com.dkop.library.exceptions.AlreadyExistException;
import com.dkop.library.exceptions.DoesNotExistException;
import com.dkop.library.exceptions.NotFoundException;
import com.dkop.library.services.BookService;
import com.dkop.library.services.OrderService;
import com.dkop.library.services.PaginationService;
import com.dkop.library.services.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.dkop.library.utils.LocalizationUtil.errorMessagesBundle;
import static com.dkop.library.utils.LocalizationUtil.localizationBundle;

public class ReaderCommand implements Command {
    private final Map<String, Consumer<HttpServletRequest>> operations = new HashMap<>();
    private final BookService bookService;
    private final UserService userService;
    private final OrderService orderService;
    private final PaginationService paginationService;
    private static final Logger LOGGER = LogManager.getLogger(ReaderCommand.class);


    public ReaderCommand(BookService bookService, UserService userService, OrderService orderService, PaginationService paginationService) {
        this.bookService = bookService;
        this.userService = userService;
        this.orderService = orderService;
        this.paginationService = paginationService;
        init();
        LOGGER.info(ReaderCommand.class.getSimpleName());
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
        totalPenalty(request);
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
        return "/WEB-INF/roles/reader.jsp";
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
            } catch (AlreadyExistException | DoesNotExistException e) {
                LOGGER.error(e, e.getCause());
                request.setAttribute("errorMessage", e.getMessage());
            }
        }
    }

    private void createOrderIfNotExist(int bookId, int userId, String orderType, HttpServletRequest request) throws AlreadyExistException {
        if (!orderService.isOrderExist(bookId, userId, orderType)) {
            orderService.createOrder(bookId, userId, orderType);
            request.setAttribute("successMessage", errorMessagesBundle.getString("order.created"));
        } else {
            throw new AlreadyExistException(errorMessagesBundle.getString("order.already.exist"));
        }
    }

    private void showCatalogBookOperation(HttpServletRequest request) {
        request.setAttribute("operation", "catalog");
        request.setAttribute("sort", request.getParameter("sort"));
        String sortBy = request.getParameter("sort");
        int page = 1;
        int perPage = 5;
        if (StringUtils.isNumeric(request.getParameter("perPage"))) {
            perPage = Integer.parseInt(request.getParameter("perPage"));
        }
        if (StringUtils.isNumeric(request.getParameter("page"))) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        List<Book> catalog;
        if (sortBy != null) {
            switch (sortBy) {
                case "author":
                    request.setAttribute("sortBy", localizationBundle.getString("catalog.sort.author"));
                    break;
                case "publisher":
                    request.setAttribute("sortBy", localizationBundle.getString("catalog.sort.publisher"));
                    break;
                case "publishing_date":
                    request.setAttribute("sortBy", localizationBundle.getString("catalog.sort.publishing.date"));
                    break;
                default:
                    request.setAttribute("sortBy", localizationBundle.getString("catalog.sort.title"));
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

    private void showApprovedOrders(HttpServletRequest request) {
        try {
            User user = userService.getUserInfo((String) request.getSession().getAttribute("email"));
            List<UserOrderDto> userApprovedOrders = new ArrayList<>();
            int perPage = paginationService.getRecordsPerPage(request);
            int page = paginationService.getPageNumber(request);
            List<Order> allApproved = paginationService.paginateOrdersByUser(user.getId(), page, perPage);
            allApproved.forEach(order -> {
                try {
                    UserOrderDto userOrder = new UserOrderDto();
                    long penalty = orderService.checkForPenalty(order);
                    userOrder.setPenalty(penaltyFormatter(String.valueOf(penalty)));
                    userOrder.setCreateDate(order.getCreateDate());
                    userOrder.setExpectedReturnDate(order.getExpectedReturnDate());
                    userOrder.setBook(bookService.findById(order.getBookId()));
                    userOrder.setOrderId(order.getId());
                    userApprovedOrders.add(userOrder);
                } catch (NotFoundException e) {
                    LOGGER.error(e, e.getCause());
                    request.setAttribute("errorMessage", e.getMessage());
                }
            });
            int numberOfPages = paginationService.countNumberOfPagesForUserApprovedOrders("approved", user.getId(), perPage);
            request.setAttribute("numberOfPages", numberOfPages);
            request.setAttribute("perPage", perPage);
            request.setAttribute("currentPage", page);
            request.setAttribute("userApprovedOrders", userApprovedOrders);
        } catch (DoesNotExistException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }


    }

    private void returnBookOperation(HttpServletRequest request) {
        String orderId = request.getParameter("orderId");
        if (StringUtils.isNumeric(orderId)) {
            try {
                orderService.returnBook(Integer.parseInt(orderId));
                request.setAttribute("successMessage", errorMessagesBundle.getString("successfully.returned"));
                showApprovedOrders(request);
            } catch (NotFoundException e) {
                LOGGER.error(e, e.getCause());
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
            request.setAttribute("user", user);
        } catch (DoesNotExistException e) {
            LOGGER.error(e, e.getCause());
            request.setAttribute("errorMessage", e.getMessage());
        }
    }

    private void totalPenalty(HttpServletRequest request) {
        try {
            User user = userService.getUserInfo((String) request.getSession().getAttribute("email"));
            List<Order> allApproved = orderService.findAllUserApprovedOrders(user.getId(), 0, 0);
            long totalPenalty = allApproved.stream().mapToLong(orderService::checkForPenalty).sum();
            request.setAttribute("totalPenalty", penaltyFormatter(String.valueOf(totalPenalty)));
        } catch (DoesNotExistException e) {
            LOGGER.error(e, e.getCause());
            request.setAttribute("errorMessage", e.getMessage());
        }
    }

    private String penaltyFormatter(String penalty) {
        StringBuilder builder = new StringBuilder();
        if (penalty.length() < 3) {
            builder.append("0.").append(penalty).append(" UAH");
            return builder.toString();
        } else {
            builder.append(penalty, 0, penalty.length() - 2)
                    .append(".")
                    .append(penalty, penalty.length() - 2, penalty.length())
                    .append("UAH");
        }
        return builder.toString();
    }
}
