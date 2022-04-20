package com.dkop.library.dao.impls;

public final class Queries {

    private Queries() {
    }

    //Books
    public static final String SELECT_BOOKS = "SELECT * FROM books ORDER BY %s LIMIT ?, ?;";
    public static final String CREATE_BOOK = "INSERT INTO books (title, author, publisher, publishing_date, amount) VALUES (?, ?, ?, ?, ?);";
    public static final String DELETE_BOOK = "DELETE FROM books WHERE id = ?;";
    public static final String UPDATE_BOOK = "UPDATE books SET title = ?, author = ?, publisher = ?, publishing_date = ?, amount = ?, on_order = ? WHERE id = ?;";
    public static final String SELECT_BOOK_BY_AUTHOR = "SELECT * FROM books WHERE author LIKE ?;";
    public static final String SELECT_BOOK_BY_ID = "SELECT * FROM books WHERE id = ?;";
    public static final String COUNT_ROWS_BOOKS = "SELECT count(id) AS count FROM books;";
    public static final String SELECT_BOOK_BY_TITLE = "SELECT * FROM books WHERE title LIKE ?;";
    public static final String CREATE_ORDER = "INSERT INTO orders (book_id, user_id, type, create_date) VALUES (?, ?, ?, ?);";
    public static final String SELECT_ALL_ORDERS = "SELECT * FROM orders;";

    //Orders
    public static final String SELECT_ORDER_BY_ID = "SELECT * FROM orders WHERE id = ?;";
    public static final String UPDATE_ORDER = "UPDATE orders SET status = ?, approved_date = ?, expected_return_date = ? WHERE id = ?;";
    public static final String SELECT_ORDERS_BY_STATUS = "SELECT * FROM orders WHERE status = ? LIMIT ?, ?;";
    public static final String SELECT_APPROVED_USER_ORDERS = "SELECT * FROM orders WHERE user_id = ? AND status = 'approved' LIMIT ?, ?;";
    public static final String UPDATE_BOOK_FROM_ORDER = "UPDATE books SET amount = ?, on_order = ? WHERE id = ?;";
    public static final String CHECK_ORDER_EXIST = "SELECT count(id) AS count FROM orders WHERE book_id = ? AND user_id = ? AND type = ? AND (status = 'approved' OR status = 'pending');";
    public static final String CHECK_ORDER_AVAILABLE_TO_DELETE_BOOK = "SELECT count(id) AS count FROM orders WHERE book_id = ? AND (status = 'approved' OR status = 'pending');";
    public static final String COUNT_ROWS_BY_STATUS = "SELECT count(id) AS count FROM orders WHERE status = ?;";
    public static final String COUNT_ROWS_BY_STATUS_AND_READER = "SELECT count(id) AS count FROM orders WHERE status = ? AND user_id = ?;";

    //Users
    public static final String CREATE_USER = "INSERT INTO users (first_name, last_name, email, password, role, status) VALUES (?, ?, ?, ?, ?, ?);";
    public static final String COUNT_ROWS_BY_ROLE = "SELECT count(id) AS count FROM users WHERE role = ?;";
    public static final String SELECT_USERS_BY_ROLE = "SELECT * FROM users WHERE role = ? ORDER BY id limit ?, ?;";
    public static final String UPDATE_USER_STATUS = "UPDATE users SET status = ? WHERE id = ?;";
    public static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?;";
    public static final String SELECT_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?;";
    public static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?;";
}
