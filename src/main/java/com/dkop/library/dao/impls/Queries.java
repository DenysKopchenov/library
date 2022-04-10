package com.dkop.library.dao.impls;

public interface Queries {
    //Books
    String SELECT_BOOKS = "SELECT * FROM books ORDER BY %s LIMIT ?, ?;";
    String CREATE_BOOK = "INSERT INTO books (title, author, publisher, publishing_date, amount) VALUES (?, ?, ?, ?, ?);";
    String DELETE_BOOK = "DELETE FROM books WHERE id = ?;";
    String UPDATE_BOOK = "UPDATE books SET title = ?, author = ?, publisher = ?, publishing_date = ?, amount = ?, on_order = ? WHERE id = ?;";
    String SELECT_BOOK_BY_AUTHOR = "SELECT * FROM books WHERE author LIKE ?;";
    String SELECT_BOOK_BY_TITLE = "SELECT * FROM books WHERE title LIKE ?;";
    String COUNT_ROWS_BOOKS = "SELECT count(id) AS count FROM books;";
    String SELECT_BOOK_BY_ID = "SELECT * FROM books WHERE id = ?;";

    //Orders

    //Users
}
