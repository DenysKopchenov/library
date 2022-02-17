package com.dkop.library.dao;

import com.dkop.library.model.Order;

import java.util.List;

public interface OrderDao extends GenericDao<Order> {
    List<Order> findAllOrdersBasedOnStatus(int userId, String status);
}
