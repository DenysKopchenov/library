package com.dkop.library.dao;

import com.dkop.library.dao.impls.BooksDaoImpl;

import com.dkop.library.entity.Book;
import com.dkop.library.exceptions.NotFoundException;
import com.dkop.library.exceptions.UnableToDeleteException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
    private final Connection connectionMock = mock(Connection.class);
    private final PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
    private final ResultSet resultSetMock = mock(ResultSet.class);

    @Before
    public void setUp() throws SQLException {
        booksDao = new BooksDaoImpl(connectionMock);
        messagesBundle = mock(ResourceBundle.class);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFindAll() {
        booksDao.findAll();
    }

    @Test
    public void testCreate() throws SQLException {
        booksDao.create(createTestBook());
        verify(connectionMock, times(1)).prepareStatement(CREATE_BOOK);
        verify(preparedStatementMock, times(1)).setString(1, "Title");
        verify(preparedStatementMock, times(1)).setString(2, "Author");
        verify(preparedStatementMock, times(1)).setString(3, "Publisher");
        verify(preparedStatementMock, times(1)).setDate(4, Date.valueOf(LocalDate.now()));
        verify(preparedStatementMock, times(1)).setInt(5, 1);
        verify(preparedStatementMock, times(1)).executeUpdate();
    }

    @Test
    public void testDelete() throws SQLException, UnableToDeleteException {
        booksDao.delete(1);
        verify(connectionMock, times(1)).prepareStatement(DELETE_BOOK);
        verify(preparedStatementMock, times(1)).setInt(1, 1);
        verify(preparedStatementMock, times(1)).executeUpdate();

    }

    @Test(expected = UnableToDeleteException.class)
    public void testDeleteThrows() throws UnableToDeleteException, SQLException {
        when(preparedStatementMock.executeUpdate()).thenThrow(new SQLException());
        try {
            booksDao.delete(1);
        } catch (UnableToDeleteException e) {
            verify(connectionMock, times(1)).prepareStatement(DELETE_BOOK);
            verify(preparedStatementMock, times(1)).setInt(1, 1);
            verify(preparedStatementMock, times(1)).executeUpdate();
            throw e;
        }
    }

    @Test
    public void testUpdate() throws SQLException {
        booksDao.update(createTestBook());
        verify(connectionMock, times(1)).prepareStatement(UPDATE_BOOK);
        verify(preparedStatementMock, times(1)).setString(1, "Title");
        verify(preparedStatementMock, times(1)).setString(2, "Author");
        verify(preparedStatementMock, times(1)).setString(3, "Publisher");
        verify(preparedStatementMock, times(1)).setDate(4, Date.valueOf(LocalDate.now()));
        verify(preparedStatementMock, times(1)).setInt(5, 1);
        verify(preparedStatementMock, times(1)).setInt(6, 0);
        verify(preparedStatementMock, times(1)).setInt(7, 1);
        verify(preparedStatementMock, times(1)).executeUpdate();
    }

    @Test
    public void testFindAllSorted() throws SQLException {
        List<Book> allBooks = booksDao.findAllSorted("title", 1, 5);
        verify(connectionMock, times(1)).prepareStatement(String.format(SELECT_BOOKS, "title"));
        verify(preparedStatementMock, times(1)).setInt(1, 1);
        verify(preparedStatementMock, times(1)).setInt(2, 5);
        verify(preparedStatementMock, times(1)).executeQuery();
        Assert.assertTrue(allBooks.isEmpty());
    }

    @Test
    public void testFindAllByAuthor() throws SQLException {
        List<Book> allBooks = booksDao.findAllByAuthor("author");
        verify(connectionMock, times(1)).prepareStatement(SELECT_BOOK_BY_AUTHOR);
        verify(preparedStatementMock, times(1)).setString(1, "%author%");
        verify(preparedStatementMock, times(1)).executeQuery();
        Assert.assertTrue(allBooks.isEmpty());
    }

    @Test
    public void testFindAllByTitle() throws SQLException {
        List<Book> allBooks = booksDao.findAllByTitle("title");
        verify(connectionMock, times(1)).prepareStatement(SELECT_BOOK_BY_TITLE);
        verify(preparedStatementMock, times(1)).setString(1, "%title%");
        verify(preparedStatementMock, times(1)).executeQuery();
        Assert.assertTrue(allBooks.isEmpty());
    }

    @Test
    public void testCountAllRows() throws SQLException {
        int result = booksDao.countAllRows();
        verify(connectionMock, times(1)).prepareStatement(COUNT_ROWS_BOOKS);
        verify(preparedStatementMock, times(1)).executeQuery();
        Assert.assertEquals(0, result);
    }

    @Test(expected = NotFoundException.class)
    public void testFindByIdThrows() throws NotFoundException, SQLException {
        try {
            booksDao.findById(1);
        } catch (NotFoundException e) {
            verify(connectionMock, times(1)).prepareStatement(SELECT_BOOK_BY_ID);
            verify(preparedStatementMock, times(1)).setInt(1, 1);
            verify(preparedStatementMock, times(1)).executeQuery();
            throw e;
        }
    }

    @Test()
    public void testFindById() throws NotFoundException, SQLException {
        when(resultSetMock.next()).thenReturn(Boolean.TRUE);
        when(resultSetMock.getDate("publishing_date")).thenReturn(Date.valueOf(LocalDate.now()));
        booksDao.findById(1);
        verify(connectionMock, times(1)).prepareStatement(SELECT_BOOK_BY_ID);
        verify(preparedStatementMock, times(1)).setInt(1, 1);
        verify(preparedStatementMock, times(1)).executeQuery();
        verify(resultSetMock, times(1)).getInt("id");
        verify(resultSetMock, times(1)).getString("title");
        verify(resultSetMock, times(1)).getString("author");
        verify(resultSetMock, times(1)).getString("publisher");
        verify(resultSetMock, times(1)).getDate("publishing_date");
        verify(resultSetMock, times(1)).getInt("amount");
        verify(resultSetMock, times(1)).getInt("on_order");
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
