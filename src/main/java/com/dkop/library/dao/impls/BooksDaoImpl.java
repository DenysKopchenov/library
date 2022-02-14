package com.dkop.library.dao.impls;

import com.dkop.library.dao.BooksDao;
import com.dkop.library.model.Book;
import com.dkop.library.model.exceptions.NotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BooksDaoImpl implements BooksDao {
    private Connection connection;

    public BooksDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public List<Book> findAll() {
        return findAllSorted("author");
    }

    public List<Book> findAllSorted(String sortBy) {
        List<Book> allBooks = new ArrayList<>();
        String SELECT_BOOKS = "SELECT * FROM books ORDER BY ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOKS)) {
            preparedStatement.setString(1, sortBy);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Book book = Book.newBuilder()
                            .id(resultSet.getInt("id"))
                            .title(resultSet.getString("title"))
                            .author(resultSet.getString("author"))
                            .publisher(resultSet.getString("publisher"))
                            .publishingDate(resultSet.getDate("publishing_date").toLocalDate())
                            .amount(resultSet.getInt("amount"))
                            .build();
                    allBooks.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allBooks;
    }

    public void create(Book book) throws SQLException {
        String INSERT_BOOK = "INSERT INTO books (title, author, publisher, publishing_date, amount) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BOOK)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getPublisher());
            preparedStatement.setDate(4, Date.valueOf(book.getPublishingDate()));
            preparedStatement.setInt(5, book.getAmount());

            preparedStatement.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String DELETE_BOOK = "DELETE FROM books WHERE id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BOOK)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    public void update(Book book) throws SQLException {
        String UPDATE_BOOK = "UPDATE books SET title = ?, author = ?, publisher = ?, publishing_date = ?, amount = ? WHERE id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BOOK)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getPublisher());
            preparedStatement.setDate(4, Date.valueOf(book.getPublishingDate()));
            preparedStatement.setInt(5, book.getAmount());
            preparedStatement.setInt(6, book.getId());
            preparedStatement.executeUpdate();
        }
    }

    public List<Book> findByAuthor(String author) {//todo!
        List<Book> booksByAuthor = new ArrayList<>();
        Book book;
        String SELECT_BOOK = "SELECT * FROM books WHERE author LIKE ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK)) {
            preparedStatement.setString(1, "%" + author + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    book = Book.newBuilder()
                            .id(resultSet.getInt("id"))
                            .title(resultSet.getString("title"))
                            .author(resultSet.getString("author"))
                            .publisher(resultSet.getString("publisher"))
                            .publishingDate(resultSet.getDate("publishing_date").toLocalDate())
                            .amount(resultSet.getInt("amount"))
                            .build();
                    booksByAuthor.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booksByAuthor;
    }

    public List<Book> findByTitle(String title) {
        List<Book> booksByTitle = new ArrayList<>();
        String SELECT_BOOK = "SELECT * FROM books WHERE title LIKE ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK)) {
            preparedStatement.setString(1, "%" + title + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Book book = Book.newBuilder()
                            .id(resultSet.getInt("id"))
                            .title(resultSet.getString("title"))
                            .author(resultSet.getString("author"))
                            .publisher(resultSet.getString("publisher"))
                            .publishingDate(resultSet.getDate("publishing_date").toLocalDate())
                            .amount(resultSet.getInt("amount"))
                            .build();
                    booksByTitle.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booksByTitle;
    }

    public Book findById(int id) throws NotFoundException {
        String SELECT_BOOK = "SELECT * FROM books WHERE id = ?;";
        Book book = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    book = Book.newBuilder()
                            .id(resultSet.getInt("id"))
                            .title(resultSet.getString("title"))
                            .author(resultSet.getString("author"))
                            .publisher(resultSet.getString("publisher"))
                            .publishingDate(resultSet.getDate("publishing_date").toLocalDate())
                            .amount(resultSet.getInt("amount"))
                            .build();
                } else {
                    throw new NotFoundException("Book is not found");
                }
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