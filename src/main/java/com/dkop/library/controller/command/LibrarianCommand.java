package com.dkop.library.controller.command;

import com.dkop.library.entity.Order;
import com.dkop.library.entity.User;
import com.dkop.library.dto.UserOrderDto;
import com.dkop.library.exceptions.DoesNotExistException;
import com.dkop.library.exceptions.NotFoundException;
import com.dkop.library.exceptions.UnableToAcceptOrderException;
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

import static com.dkop.library.utils.Fields.EMAIL;
import static com.dkop.library.utils.Fields.ERROR_MESSAGE;

/**
 * Librarian command
 * Handle operations on librarian page
 */
public class LibrarianCommand implements Command {

    private final Map<String, Consumer<HttpServletRequest>> operations = new HashMap<>();
    private final BookService bookService;
    private final UserService userService;
    private final OrderService orderService;
    private final PaginationService paginationService;
    private static final Logger LOGGER = LogManager.getLogger(LibrarianCommand.class);

    public LibrarianCommand(BookService bookService, UserService userService, OrderService orderService, PaginationService paginationService) {
        this.bookService = bookService;
        this.userService = userService;
        this.orderService = orderService;
        this.paginationService = paginationService;
        init();
    }

    private void init() {
        operations.put("userInfo", this::showUserInfo);
        operations.put("showPendingOrders", this::showPendingOrders);
        operations.put("showReadersApprovedOrders", this::showReadersApprovedOrders);
        operations.put("showAllReaders", this::showReaders);
        operations.put("acceptOrder", this::acceptOrder);
        operations.put("rejectOrder", this::rejectOrder);
    }

    @Override
    public String execute(HttpServletRequest request) {
        if (StringUtils.isNotBlank(request.getParameter("operations"))) {
            String operation = request.getParameter("operations");
            handleOperations(operation, request);
        }
        return "/WEB-INF/roles/librarian.jsp";
    }

    private void handleOperations(String operation, HttpServletRequest request) {
        if (operations.containsKey(operation)) {
            operations.get(operation).accept(request);
        }
    }

    private void showUserInfo(HttpServletRequest request) {
        String email = (String) request.getSession().getAttribute(EMAIL);
        try {
            User user = userService.getUserInfo(email);
            request.setAttribute("user", user);
        } catch (DoesNotExistException e) {
            LOGGER.error(e, e.getCause());
            request.setAttribute(ERROR_MESSAGE, e.getMessage());
        }
    }

    private void showReaders(HttpServletRequest request) {
        int perPage = paginationService.getRecordsPerPage(request);
        int page = paginationService.getPageNumber(request);

        List<User> usersPerPage = paginationService.paginateUsersByRole("reader", page, perPage);
        int numberOfPages = paginationService.countNumberOfPagesForUsers("reader", perPage);

        request.setAttribute("numberOfPages", numberOfPages);
        request.setAttribute("perPage", perPage);
        request.setAttribute("currentPage", Math.min(page, numberOfPages));
        request.setAttribute("allReaders", usersPerPage);
    }

    private void showReadersApprovedOrders(HttpServletRequest request) {
        if (StringUtils.isNumeric(request.getParameter("userId"))) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            int perPage = paginationService.getRecordsPerPage(request);
            int page = paginationService.getPageNumber(request);
            List<UserOrderDto> userApprovedOrders = new ArrayList<>();
            List<Order> allApproved = paginationService.paginateApprovedOrdersByUser(userId, page, perPage);
            allApproved.forEach(order -> {
                try {
                    UserOrderDto userOrder = prepareUserOrderForView(order);
                    userApprovedOrders.add(userOrder);
                } catch (NotFoundException e) {
                    LOGGER.error(e, e.getCause());
                    request.setAttribute(ERROR_MESSAGE, e.getMessage());
                }
            });

            int numberOfPages = paginationService.countNumberOfPagesForUserApprovedOrders("approved", userId, perPage);
            request.setAttribute("numberOfPages", numberOfPages);
            request.setAttribute("perPage", perPage);
            request.setAttribute("currentPage", Math.min(page, numberOfPages));
            request.setAttribute("userApprovedOrders", userApprovedOrders);
            request.setAttribute("userId", userId);
        }
    }

    private UserOrderDto prepareUserOrderForView(Order order) throws NotFoundException {
        UserOrderDto userOrder = new UserOrderDto();
        long penalty = orderService.checkForPenalty(order);
        userOrder.setPenalty(penaltyFormatter(String.valueOf(penalty)));
        userOrder.setCreateDate(order.getCreateDate());
        userOrder.setExpectedReturnDate(order.getExpectedReturnDate());
        userOrder.setType(StringUtils.capitalize(order.getType()));
        userOrder.setStatus(StringUtils.capitalize(order.getStatus()));
        userOrder.setBook(bookService.findById(order.getBookId()));
        userOrder.setUser(userService.findById(order.getUserId()));
        userOrder.setOrderId(order.getId());
        return userOrder;
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
                    .append(" UAH");
        }
        return builder.toString();
    }

    private void showPendingOrders(HttpServletRequest request) {
        int perPage = paginationService.getRecordsPerPage(request);
        int page = paginationService.getPageNumber(request);
        List<UserOrderDto> pendingOrders = new ArrayList<>();
        List<Order> orders = paginationService.paginateOrdersByStatus("pending", page, perPage);
        orders.forEach(order -> {
            try {
                UserOrderDto userOrder = prepareUserOrderForView(order);
                pendingOrders.add(userOrder);
            } catch (NotFoundException e) {
                LOGGER.error(e, e.getCause());
                request.setAttribute(ERROR_MESSAGE, e.getMessage());
            }
        });

        int numberOfPages = paginationService.countNumberOfPagesForOrdersByStatus("pending", perPage);
        request.setAttribute("numberOfPages", numberOfPages);
        request.setAttribute("perPage", perPage);
        request.setAttribute("currentPage", Math.min(page, numberOfPages));
        request.setAttribute("pendingOrders", pendingOrders);
    }

    private void acceptOrder(HttpServletRequest request) {
        if (StringUtils.isNumeric(request.getParameter("orderId"))) {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            try {
                Order order = orderService.findById(orderId);
                orderService.acceptOrder(order);
                showPendingOrders(request);
            } catch (NotFoundException | UnableToAcceptOrderException e) {
                LOGGER.error(e, e.getCause());
                request.setAttribute(ERROR_MESSAGE, e.getMessage());
            }
        }
    }

    private void rejectOrder(HttpServletRequest request) {
        if (StringUtils.isNumeric(request.getParameter("orderId"))) {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            try {
                Order order = orderService.findById(orderId);
                orderService.rejectOrder(order);
                showPendingOrders(request);
            } catch (NotFoundException e) {
                LOGGER.error(e, e.getCause());
                request.setAttribute(ERROR_MESSAGE, e.getMessage());
            }
        }
    }
}
