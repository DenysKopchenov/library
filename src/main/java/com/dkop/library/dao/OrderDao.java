package com.dkop.library.dao;

import com.dkop.library.entity.Book;
import com.dkop.library.entity.Order;

import java.util.List;

public interface OrderDao extends GenericDao<Order> {
    List<Order> findAllUserApprovedOrders(int userId, int start, int numberOfRecords);

    List<Order> findAllOrdersByStatus(String status, int start, int numberOfRecords);

    void processOrder(Order order, Book book);

    boolean isOrderExist(Order order);

    int countAllRowsByStatus(String status);

    int countAllRowsByStatusAndUser(String status, int userId);
}
