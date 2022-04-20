package com.dkop.library.dao;

import com.dkop.library.entity.User;
import com.dkop.library.exceptions.DoesNotExistException;

import java.util.List;

/**
 * Dao for User entity
 */
public interface UserDao extends GenericDao<User> {

    /**
     * Finds user by specified email
     * @param email
     * @return User entity that was found
     * @throws DoesNotExistException if email does not exists in database
     */
    User findByEmail(String email) throws DoesNotExistException;

    /**
     * Changes user status
     * @param id  id of user to change
     * @param newStatus  Must be 'active' or 'blocked'
     */
    void changeStatus(int id, String newStatus);

    /**
     * Finds all users for a specified role, in the required quantity
     * @param role
     * @param start  parameter for paginating records, start index
     * @param numberOfRecords  parameter for paginating records, number of records per page
     * @return List of users for a specified role, in the required quantity
     */
    List<User> findAllByRole(String role, int start, int numberOfRecords);

    /**
     * Counts all rows in table for specified role
     * @param role
     * @return number of rows was found
     */
    int countAllRowsByRole(String role);
}
