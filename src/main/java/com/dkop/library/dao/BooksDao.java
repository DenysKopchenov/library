package com.dkop.library.dao;

import com.dkop.library.model.Book;

import java.util.List;

public interface BooksDao extends GenericDao<Book> {
    List<Book> findAllSorted(String sortBy);

    List<Book> findAllByAuthor(String author);

    List<Book> findAllByTitle(String title);


}
