package com.dkop.library.dao;

import com.dkop.library.entity.Book;
import com.dkop.library.entity.Order;

import java.util.List;

public interface OrderDao extends GenericDao<Order> {
    List<Order> findAllUserApprovedOrders(int userId);

    List<Order> findAllOrdersByStatus(String status);

    void processOrder(Order order, Book book);
}
