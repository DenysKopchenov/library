package com.dkop.library.dao.impls;

import com.dkop.library.dao.BooksDao;
import com.dkop.library.entity.Book;
import com.dkop.library.exceptions.NotFoundException;
import com.dkop.library.exceptions.UnableToDeleteException;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.dkop.library.controller.command.CommandUtils.messagesBundle;

public class BooksDaoImpl implements BooksDao {

    private Connection connection;

    public BooksDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public List<Book> findAll() {
        return findAllSorted("title");
    }

    public List<Book> findAllSorted(String sortBy) {
        if (!StringUtils.equalsAny(sortBy, "title", "author", "publisher", "publishing_date")) {
            sortBy = "title";
        }
        List<Book> allBooks = new ArrayList<>();
        String SELECT_BOOKS = String.format("SELECT * FROM books ORDER BY %s;", sortBy);
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOKS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Book book = Book.newBuilder()
                        .id(resultSet.getInt("id"))
                        .title(resultSet.getString("title"))
                        .author(resultSet.getString("author"))
                        .publisher(resultSet.getString("publisher"))
                        .publishingDate(resultSet.getDate("publishing_date").toLocalDate())
                        .amount(resultSet.getInt("amount"))
                        .onOrder(resultSet.getInt("on_order"))
                        .build();
                allBooks.add(book);
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

    public void delete(int id) throws UnableToDeleteException {
        String DELETE_BOOK = "DELETE FROM books WHERE id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BOOK)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new UnableToDeleteException(messagesBundle.getString("unable.delete"), e);
        }
    }

    public void update(Book book) {
        String UPDATE_BOOK = "UPDATE books SET title = ?, author = ?, publisher = ?, publishing_date = ?, amount = ?, on_order = ? WHERE id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BOOK)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getPublisher());
            preparedStatement.setDate(4, Date.valueOf(book.getPublishingDate()));
            preparedStatement.setInt(5, book.getAmount());
            preparedStatement.setInt(6, book.getOnOrder());
            preparedStatement.setInt(7, book.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Book> findAllByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        String SELECT_BOOK = "SELECT * FROM books WHERE author LIKE ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK)) {
            preparedStatement.setString(1, "%" + author + "%");
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
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public List<Book> findAllByTitle(String title) {
        List<Book> books = new ArrayList<>();
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
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
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
                            .onOrder(resultSet.getInt("on_order"))
                            .build();
                } else {
                    throw new NotFoundException(messagesBundle.getString("book.not.found"));
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