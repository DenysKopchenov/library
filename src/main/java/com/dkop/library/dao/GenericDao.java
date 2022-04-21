package com.dkop.library.dao;

import com.dkop.library.exceptions.NotFoundException;
import com.dkop.library.exceptions.UnableToDeleteException;

import java.sql.SQLException;
import java.util.List;

/**
 * Base interface for all dao classes
 */
public interface GenericDao<E> {

    /**
     * Creates an entity in database
     * @param entity
     * @throws SQLException
     */
    void create(E entity) throws SQLException;

    /**
     * Find all records in database
     * @return List of entities
     */
    List<E> findAll();

    /**
     * Find entity by id
     * @param id
     * @return
     * @throws NotFoundException if id does not exist in database
     */
    E findById(int id) throws NotFoundException;

    /**
     * Update record in database
     * @param entity
     */
    void update(E entity);

    /**
     * Delete record in database
     * @param id
     * @throws UnableToDeleteException
     */
    void delete(int id) throws UnableToDeleteException;

    /**
     * Close connection to database
     */
}
