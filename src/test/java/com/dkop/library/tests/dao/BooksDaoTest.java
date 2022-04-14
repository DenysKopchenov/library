package com.dkop.library.tests.dao;

import com.dkop.library.dao.BooksDao;
import com.dkop.library.dao.impls.BooksDaoImpl;

import com.dkop.library.entity.Book;
import com.dkop.library.exceptions.NotFoundException;
import com.dkop.library.exceptions.UnableToDeleteException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static com.dkop.library.controller.command.CommandUtils.messagesBundle;
import static com.dkop.library.dao.impls.Queries.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class BooksDaoTest {

    private BooksDao booksDao;
    private final Connection mockConnection = mock(Connection.class);
    private final PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
    private final ResultSet mockResultSet = mock(ResultSet.class);

    @Before
    public void setUp() throws SQLException {
        booksDao = new BooksDaoImpl(mockConnection);
        messagesBundle = mock(ResourceBundle.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFindAll() {
        booksDao.findAll();
    }

    @Test
    public void testCreate() throws SQLException {
        booksDao.create(createTestBook());
        verify(mockConnection, times(1)).prepareStatement(CREATE_BOOK);
        verify(mockPreparedStatement, times(1)).setString(1, "Title");
        verify(mockPreparedStatement, times(1)).setString(2, "Author");
        verify(mockPreparedStatement, times(1)).setString(3, "Publisher");
        verify(mockPreparedStatement, times(1)).setDate(4, Date.valueOf(LocalDate.now()));
        verify(mockPreparedStatement, times(1)).setInt(5, 1);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testDelete() throws SQLException, UnableToDeleteException {
        booksDao.delete(1);
        verify(mockConnection, times(1)).prepareStatement(DELETE_BOOK);
        verify(mockPreparedStatement, times(1)).setInt(1, 1);
        verify(mockPreparedStatement, times(1)).executeUpdate();

    }

    @Test(expected = UnableToDeleteException.class)
    public void testDeleteThrows() throws UnableToDeleteException, SQLException {
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException());
        try {
            booksDao.delete(1);
        } catch (UnableToDeleteException e) {
            verify(mockConnection, times(1)).prepareStatement(DELETE_BOOK);
            verify(mockPreparedStatement, times(1)).setInt(1, 1);
            verify(mockPreparedStatement, times(1)).executeUpdate();
            throw e;
        }
    }

    @Test
    public void testUpdate() throws SQLException {
        booksDao.update(createTestBook());
        verify(mockConnection, times(1)).prepareStatement(UPDATE_BOOK);
        verify(mockPreparedStatement, times(1)).setString(1, "Title");
        verify(mockPreparedStatement, times(1)).setString(2, "Author");
        verify(mockPreparedStatement, times(1)).setString(3, "Publisher");
        verify(mockPreparedStatement, times(1)).setDate(4, Date.valueOf(LocalDate.now()));
        verify(mockPreparedStatement, times(1)).setInt(5, 1);
        verify(mockPreparedStatement, times(1)).setInt(6, 0);
        verify(mockPreparedStatement, times(1)).setInt(7, 1);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testFindAllSorted() throws SQLException {
        List<Book> allBooks = booksDao.findAllSorted("title", 1, 5);
        verify(mockConnection, times(1)).prepareStatement(String.format(SELECT_BOOKS, "title"));
        verify(mockPreparedStatement, times(1)).setInt(1, 1);
        verify(mockPreparedStatement, times(1)).setInt(2, 5);
        verify(mockPreparedStatement, times(1)).executeQuery();
        Assert.assertTrue(allBooks.isEmpty());
    }

    @Test
    public void testFindAllByAuthor() throws SQLException {
        List<Book> allBooks = booksDao.findAllByAuthor("author");
        verify(mockConnection, times(1)).prepareStatement(SELECT_BOOK_BY_AUTHOR);
        verify(mockPreparedStatement, times(1)).setString(1, "%author%");
        verify(mockPreparedStatement, times(1)).executeQuery();
        Assert.assertTrue(allBooks.isEmpty());
    }

    @Test
    public void testFindAllByTitle() throws SQLException {
        List<Book> allBooks = booksDao.findAllByTitle("title");
        verify(mockConnection, times(1)).prepareStatement(SELECT_BOOK_BY_TITLE);
        verify(mockPreparedStatement, times(1)).setString(1, "%title%");
        verify(mockPreparedStatement, times(1)).executeQuery();
        Assert.assertTrue(allBooks.isEmpty());
    }

    @Test
    public void testCountAllRows() throws SQLException {
        int result = booksDao.countAllRows();
        verify(mockConnection, times(1)).prepareStatement(COUNT_ROWS_BOOKS);
        verify(mockPreparedStatement, times(1)).executeQuery();
        Assert.assertEquals(0, result);
    }

    @Test(expected = NotFoundException.class)
    public void testFindById() throws NotFoundException, SQLException {
        booksDao.findById(1);
        verify(mockConnection, times(1)).prepareStatement(SELECT_BOOK_BY_ID);
        verify(mockPreparedStatement, times(1)).setInt(1, 1);
        verify(mockPreparedStatement, times(1)).executeQuery();
    }

    private Book createTestBook() {
        return Book.newBuilder()
                .id(1)
                .title("Title")
                .author("Author")
                .publisher("Publisher")
                .publishingDate(LocalDate.now())
                .amount(1)
                .onOrder(0)
                .build();
    }
}
