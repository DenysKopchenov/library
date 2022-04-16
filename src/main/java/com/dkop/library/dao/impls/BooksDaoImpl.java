package com.dkop.library.dao.impls;

import com.dkop.library.dao.BooksDao;
import com.dkop.library.entity.Book;
import com.dkop.library.exceptions.NotFoundException;
import com.dkop.library.exceptions.UnableToDeleteException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


import static com.dkop.library.utils.LocalizationUtil.errorMessagesBundle;
import static com.dkop.library.dao.impls.Queries.*;


public class BooksDaoImpl implements BooksDao {

    private Connection connection;
    private static final Logger LOGGER = LogManager.getLogger(BooksDaoImpl.class);

    public BooksDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public List<Book> findAll() {
        throw new UnsupportedOperationException();
    }

    public List<Book> findAllSorted(String sortBy, int start, int numberOfRecords) {
        if (!StringUtils.equalsAny(sortBy, "title", "author", "publisher", "publishing_date")) {
            sortBy = "title";
        }
        List<Book> allBooks = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(String.format(SELECT_BOOKS, sortBy))) {
            preparedStatement.setInt(1, start);
            preparedStatement.setInt(2, numberOfRecords);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    allBooks.add(extractBooksFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
        }
        return allBooks;
    }

    public void create(Book book) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_BOOK)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getPublisher());
            preparedStatement.setDate(4, Date.valueOf(book.getPublishingDate()));
            preparedStatement.setInt(5, book.getAmount());

            preparedStatement.executeUpdate();
        }
    }

    public void delete(int id) throws UnableToDeleteException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BOOK)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
            throw new UnableToDeleteException(errorMessagesBundle.getString("unable.delete.book"), e);
        }
    }

    public void update(Book book) {
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
            LOGGER.error(e, e.getCause());
        }
    }

    @Override
    public List<Book> findAllByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK_BY_AUTHOR)) {
            preparedStatement.setString(1, "%" + author + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    books.add(extractBooksFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
        }
        return books;
    }

    @Override
    public List<Book> findAllByTitle(String title) {
        List<Book> books = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK_BY_TITLE)) {
            preparedStatement.setString(1, "%" + title + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    books.add(extractBooksFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
        }
        return books;
    }

    @Override
    public int countAllRows() {
        try (ResultSet resultSet = connection.prepareStatement(COUNT_ROWS_BOOKS).executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
        }
        return 0;
    }

    public Book findById(int id) throws NotFoundException {
        Book book = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK_BY_ID)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    book = extractBooksFromResultSet(resultSet);
                } else {
                    throw new NotFoundException(errorMessagesBundle.getString("book.not.found"));
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
        }
        return book;
    }

    private Book extractBooksFromResultSet(ResultSet resultSet) throws SQLException {
        return Book.newBuilder()
                .id(resultSet.getInt("id"))
                .title(resultSet.getString("title"))
                .author(resultSet.getString("author"))
                .publisher(resultSet.getString("publisher"))
                .publishingDate(resultSet.getDate("publishing_date").toLocalDate())
                .amount(resultSet.getInt("amount"))
                .onOrder(resultSet.getInt("on_order"))
                .build();
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
        }
    }
}