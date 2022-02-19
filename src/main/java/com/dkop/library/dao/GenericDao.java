package com.dkop.library.dao;

import com.dkop.library.exceptions.NotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface GenericDao<E> extends AutoCloseable {
    void create(E e) throws SQLException;

    List<E> findAll();

    E findById(int id) throws NotFoundException;

    void update(E e);

    void delete(int id);

    void close();
}
