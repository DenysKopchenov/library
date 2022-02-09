package com.dkop.library.dao;

import com.dkop.library.model.Book;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BooksDao implements AutoCloseable {
    private Connection connection;

    public BooksDao(Connection connection) {
        this.connection = connection;
    }

    public List<Book> getAllBooks() { //todo add String sort "author, title, publisher, publishing date" depends on it ORDER BY ?
        List<Book> allBooks = new ArrayList<>();
        String SELECT_BOOKS = "SELECT * FROM books;";
        try {
            ResultSet resultSet = connection.prepareStatement(SELECT_BOOKS).executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String publisher = resultSet.getString("publisher");
                LocalDate publishing_date = resultSet.getDate("publishing_date").toLocalDate();
                Integer amount = Integer.valueOf(resultSet.getString("amount"));
                Book book = new Book(id, title, author, publisher, publishing_date, amount);
                allBooks.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allBooks;
    }

    public boolean checkBook(Book book) {
        String SELECT_BOOK = "SELECT title, author, publisher, publishing_date FROM books WHERE title = ? AND author = ? AND publisher = ? AND publishing_date = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK);
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getPublisher());
            preparedStatement.setDate(4, java.sql.Date.valueOf(book.getPublishingDate()));

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //for admin
    public void createBook(Book book) {
        String INSERT_BOOK = "INSERT INTO books (title, author, publisher, publishing_date, amount) VALUES (?, ?, ?, ?, ?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BOOK);
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getPublisher());
            preparedStatement.setDate(4, java.sql.Date.valueOf(book.getPublishingDate()));
            preparedStatement.setInt(5, book.getAmount());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //for admin
    public void deleteBook(int id) {
        String DELETE_BOOK = "DELETE FROM books WHERE id = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BOOK);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //for admin TODO!!!

    public void updateBook(Book book) throws SQLException {
        String UPDATE_BOOK = "UPDATE books SET title = ?, author = ?, publisher = ?, publishing_date = ?, amount = ? WHERE id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BOOK);
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getPublisher());
            preparedStatement.setDate(4, Date.valueOf(book.getPublishingDate()));
            preparedStatement.setInt(5, book.getAmount());
            preparedStatement.setInt(6, book.getId());
            preparedStatement.executeUpdate();
    }

    public List<Book> searchByAuthor(String author) {
        List<Book> booksByAuthor = new ArrayList<>();
        Book book;
        String SELECT_BOOK = "SELECT * FROM books WHERE author LIKE ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK);
            preparedStatement.setString(1, "%" + author + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String authorFromDB = resultSet.getString("author");
                String publisher = resultSet.getString("publisher");
                LocalDate publishing_date = resultSet.getDate("publishing_date").toLocalDate();
                Integer amount = Integer.valueOf(resultSet.getString("amount"));
                book = new Book(id, title, authorFromDB, publisher, publishing_date, amount);
                booksByAuthor.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booksByAuthor;
    }

    public List<Book> searchByTitle(String title) {
        List<Book> booksByTitle = new ArrayList<>();
        Book book;
        String SELECT_BOOK = "SELECT * FROM books WHERE title LIKE ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK);
            preparedStatement.setString(1, "%" + title + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int idFromDB = resultSet.getInt("id");
                String titleFromDB = resultSet.getString("title");
                String authorFromDB = resultSet.getString("author");
                String publisherFromDB = resultSet.getString("publisher");
                LocalDate publishing_dateFromDB = resultSet.getDate("publishing_date").toLocalDate();
                Integer amountFromDB = Integer.valueOf(resultSet.getString("amount"));
                book = new Book(idFromDB, titleFromDB, authorFromDB, publisherFromDB, publishing_dateFromDB, amountFromDB);
                booksByTitle.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booksByTitle;
    }

    public Book getBookById(int id) {
        Book book = null;
        String SELECT_BOOK = "SELECT * FROM books WHERE id = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String titleFromDB = resultSet.getString("title");
                String authorFromDB = resultSet.getString("author");
                String publisherFromDB = resultSet.getString("publisher");
                LocalDate publishing_dateFromDB = resultSet.getDate("publishing_date").toLocalDate();
                Integer amountFromDB = Integer.valueOf(resultSet.getString("amount"));
                book = new Book(id, titleFromDB, authorFromDB, publisherFromDB, publishing_dateFromDB, amountFromDB);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}