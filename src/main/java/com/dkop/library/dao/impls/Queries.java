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
    String CREATE_ORDER = "INSERT INTO orders (book_id, user_id, type, create_date) VALUES (?, ?, ?, ?);";
    String SELECT_ORDERS = "SELECT * FROM orders;";
    String SELECT_ORDER = "SELECT * FROM orders WHERE id = ?;";
    String UPDATE_ORDER = "UPDATE orders SET status = ?, approved_date = ?, expected_return_date = ? WHERE id = ?;";
    String SELECT_ORDERS_BY_STATUS = "SELECT * FROM orders WHERE status = ? LIMIT ?, ?;";
    String SELECT_APPROVED_USER_ORDERS = "SELECT * FROM orders WHERE user_id = ? AND status = 'approved' LIMIT ?, ?;";
    String UPDATE_BOOK_FROM_ORDER = "UPDATE books SET amount = ?, on_order = ? WHERE id = ?;";
    String CHECK_ORDER = "SELECT count(id) AS count FROM orders WHERE book_id = ? AND user_id = ? AND type = ? AND (status = 'approved' OR status = 'pending');";
    String COUNT_ROWS_BY_STATUS = "SELECT count(id) AS count FROM orders WHERE status = ?;";
    String COUNT_ROWS_BY_STATUS_AND_READER = "SELECT count(id) AS count FROM orders WHERE status = ? AND user_id = ?;";

    //Users
    String CREATE_USER = "INSERT INTO users (first_name, last_name, email, password, role, status) VALUES (?, ?, ?, ?, ?, ?);";
    String COUNT_ROWS_BY_ROLE = "SELECT count(id) AS count FROM users WHERE role = ?;";
    String SELECT_USERS_BY_ROLE = "SELECT * FROM users WHERE role = ? ORDER BY id limit ?, ?;";
    String BLOCK_USER = "UPDATE users SET status = ? WHERE id = ?;";
    String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?;";
    String SELECT_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?;";
    String DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?;";
}
