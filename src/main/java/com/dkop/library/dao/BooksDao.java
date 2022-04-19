package com.dkop.library.dao;

import com.dkop.library.entity.Book;

import java.util.List;

/**
 * Dao for Book entity
 */
public interface BooksDao extends GenericDao<Book> {

    /**
     * Finds all sorted books in table in the required quantity
     * @param sortBy  parameter for sorting
     * @param start  parameter for paginating records, start index
     * @param numberOfRecords  parameter for paginating records, number of records per page
     * @return List of Books in the required quantity
     */
    List<Book> findAllSorted(String sortBy, int start, int numberOfRecords);

    /**
     * Finds all records in table that contains parameter in 'author' column
     * @param author parameter for searching
     * @return List of founded books or empty list of no one founded
     */
    List<Book> findAllByAuthor(String author);

    /**
     * Finds all records in table that contains parameter in 'title' column
     * @param title
     * @return
     */
    List<Book> findAllByTitle(String title);

    /**
     * Count all rows in table
     * @return number of rows was founded
     */
    int countAllRows();
}
