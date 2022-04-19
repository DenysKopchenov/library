package com.dkop.library.dao;

import com.dkop.library.entity.Book;
import com.dkop.library.entity.Order;

import java.util.List;

/**
 * Dao for Order entity
 */
public interface OrderDao extends GenericDao<Order> {

    /**
     * Finds approved orders for a specific user, in the required quantity
     * @param userId
     * @param start  parameter for paginating records, start index
     * @param numberOfRecords  parameter for paginating records, number of records per page
     * @return List of approved orders for a specific user, in the required quantity
     */
    List<Order> findAllUserApprovedOrders(int userId, int start, int numberOfRecords);

    /**
     * Finds orders depends on order status, in the required quantity
     * @param status may be 'approved', 'pending', 'rejected', ''completed'
     * @param start  parameter for paginating records, start index
     * @param numberOfRecords  parameter for paginating records, number of records per page
     * @return List of orders depends on order status, in the required quantity
     */
    List<Order> findAllOrdersByStatus(String status, int start, int numberOfRecords);

    /**
     * Processing order. Accept order by librarian or return book by reader
     * @param order  Order for processing
     * @param book Book from order for processing
     */
    void processOrder(Order order, Book book);

    /**
     * Checks is order exist in database
     * @param order  order for checking
     * @return True if order with status 'approved' or 'pending' exist
     */
    boolean isOrderExist(Order order);

    /**
     * Checks if the book can be deleted
     * @param bookId
     * @return True if no orders in the approved or pending status for the specific ID
     */
    boolean isAvailableToDeleteBook(int bookId);

    /**
     * Counts all rows in table depends on status
     * @param status
     * @return number of rows was found
     */
    int countAllRowsByStatus(String status);

    /**
     * Counts all rows in table depends on status for specified user
     * @param status
     * @param userId
     * @return number of rows was found
     */
    int countAllRowsByStatusAndUser(String status, int userId);
}
