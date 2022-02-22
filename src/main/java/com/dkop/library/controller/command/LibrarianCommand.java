package com.dkop.library.controller.command;

import com.dkop.library.entity.Order;
import com.dkop.library.entity.User;
import com.dkop.library.dto.UserOrderDto;
import com.dkop.library.exceptions.DoesNotExistException;
import com.dkop.library.exceptions.NotFoundException;
import com.dkop.library.exceptions.UnableToAcceptOrderException;
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

public class LibrarianCommand implements Command {
    private final Map<String, Consumer<HttpServletRequest>> operations = new HashMap<>();
    private final BookService bookService;
    private final UserService userService;
    private final OrderService orderService;

    public LibrarianCommand() {
        bookService = BookService.getInstance();
        userService = UserService.getInstance();
        orderService = OrderService.getInstance();
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
        String email = (String) request.getSession().getAttribute("email");
        try {
            User user = userService.getUserInfo(email);
            request.setAttribute("userInfo", user);
        } catch (DoesNotExistException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
    }

    private void showReaders(HttpServletRequest request) {
        request.setAttribute("allReaders", userService.findAll());
    }

    private void showReadersApprovedOrders(HttpServletRequest request) {
        if (StringUtils.isNumeric(request.getParameter("userId"))) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            List<UserOrderDto> userApprovedOrders = new ArrayList<>();
            List<Order> allApproved = orderService.findAllUserApprovedOrders(userId);
            allApproved.forEach(order -> {
                try {
                    UserOrderDto userOrder = new UserOrderDto();
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
            request.setAttribute("userApprovedOrders", userApprovedOrders);
        }
    }

    private void showPendingOrders(HttpServletRequest request) {
        List<UserOrderDto> pendingOrders = new ArrayList<>();
        List<Order> orders = orderService.findAllOrdersByStatus("pending");
        orders.forEach(order -> {
            try {
                UserOrderDto userOrder = new UserOrderDto();
                userOrder.setCreateDate(order.getCreateDate());
                userOrder.setBook(bookService.findById(order.getBookId()));
                userOrder.setUserId(order.getUserId());
                userOrder.setOrderId(order.getId());
                pendingOrders.add(userOrder);
            } catch (NotFoundException e) {
                request.setAttribute("errorMessage", e.getMessage());
            }
        });
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
                request.setAttribute("errorMessage", e.getMessage());
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
                request.setAttribute("errorMessage", e.getMessage());
            }
        }
    }
}
