package com.dkop.library.services;

import com.dkop.library.dao.BooksDao;
import com.dkop.library.dao.DaoFactory;
import com.dkop.library.dao.OrderDao;
import com.dkop.library.entity.Book;
import com.dkop.library.exceptions.AlreadyExistException;
import com.dkop.library.exceptions.NotFoundException;
import com.dkop.library.exceptions.UnableToDeleteException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static com.dkop.library.utils.LocalizationUtil.errorMessagesBundle;
import static org.mockito.Mockito.*;

public class BookServiceTest {
    private BookService bookService;
    private DaoFactory daoFactoryMock = mock(DaoFactory.class);
    private BooksDao booksDaoMock = mock(BooksDao.class);
    private OrderDao orderDaoMock = mock(OrderDao.class);

    @Before
    public void setUp() {
        try (MockedStatic<DaoFactory> daoFactoryMockedStatic = mockStatic(DaoFactory.class)) {
            daoFactoryMockedStatic.when(DaoFactory::getInstance).thenReturn(daoFactoryMock);
            when(daoFactoryMock.createBooksDao()).thenReturn(booksDaoMock);
            when(daoFactoryMock.createOrderDao()).thenReturn(orderDaoMock);
            errorMessagesBundle = mock(ResourceBundle.class);
            bookService = new BookService(DaoFactory.getInstance());
        }
    }

    @Test
    public void testCreateBook() throws AlreadyExistException, SQLException {
        Book book = Book.newBuilder()
                .title("Title")
                .author("Author")
                .publisher("Publisher")
                .publishingDate(LocalDate.of(2001, 1, 1))
                .amount(1)
                .build();
        bookService.createBook("Title", "Author", "Publisher", "2001-01-01", "1");
        verify(daoFactoryMock).createBooksDao();
        verify(booksDaoMock).create(book);
    }

    @Test(expected = AlreadyExistException.class)
    public void testCreateBookThrows() throws SQLException, AlreadyExistException {
        Book book = Book.newBuilder()
                .title("Title")
                .author("Author")
                .publisher("Publisher")
                .publishingDate(LocalDate.of(2001, 1, 1))
                .amount(1)
                .build();
        doThrow(SQLException.class).when(booksDaoMock).create(book);
        try {
            bookService.createBook("Title", "Author", "Publisher", "2001-01-01", "1");
        } catch (AlreadyExistException e) {
            verify(daoFactoryMock).createBooksDao();
            verify(booksDaoMock).create(book);
            throw e;
        }
    }

    @Test
    public void testUpdateBook() throws NotFoundException {
        Book book = Book.newBuilder()
                .id(1)
                .title("Title")
                .author("Author")
                .publisher("Publisher")
                .publishingDate(LocalDate.of(2001, 1, 1))
                .amount(1)
                .onOrder(1)
                .build();
        when(booksDaoMock.findById(1)).thenReturn(book);
        bookService.updateBook(1, "Title", "Author", "Publisher", "2001-01-01", "1");
        verify(daoFactoryMock).createBooksDao();
        verify(booksDaoMock).update(book);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateBookThrows() throws NotFoundException {
        when(booksDaoMock.findById(1)).thenThrow(NotFoundException.class);
        try {
            bookService.updateBook(1, "Title", "Author", "Publisher", "2001-01-01", "1");
        } catch (NotFoundException e) {
            verify(daoFactoryMock).createBooksDao();
            throw e;
        }
    }

    @Test
    public void testDeleteBook() throws UnableToDeleteException, NotFoundException {
        when(orderDaoMock.isAvailableToDeleteBook(1)).thenReturn(true);
        bookService.deleteBook(1);
        verify(daoFactoryMock, times(1)).createBooksDao();
        verify(daoFactoryMock, times(1)).createOrderDao();
        verify(booksDaoMock).findById(1);
        verify(orderDaoMock).isAvailableToDeleteBook(1);
        verify(booksDaoMock).delete(1);
    }

    @Test(expected = UnableToDeleteException.class)
    public void testDeleteBookThrows() throws UnableToDeleteException, NotFoundException {
        when(orderDaoMock.isAvailableToDeleteBook(1)).thenReturn(false);
        try {
            bookService.deleteBook(1);
        } catch (UnableToDeleteException e) {
            verify(daoFactoryMock, times(1)).createBooksDao();
            verify(daoFactoryMock, times(1)).createOrderDao();
            verify(booksDaoMock, times(1)).findById(1);
            verify(orderDaoMock, times(1)).isAvailableToDeleteBook(1);
            verify(booksDaoMock, times(0)).delete(1);
            throw e;
        }
    }

    @Test
    public void testFindAllBooksByAuthor() {
        List<Book> allBooksByAuthor = bookService.findAllBooksByAuthor("author");
        verify(daoFactoryMock, times(1)).createBooksDao();
        verify(booksDaoMock, times(1)).findAllByAuthor("author");
        Assert.assertTrue(allBooksByAuthor.isEmpty());
    }

    @Test
    public void testFindAllBooksByTitle() {
        List<Book> allBooksByTitle = bookService.findAllBooksByTitle("title");
        verify(daoFactoryMock, times(1)).createBooksDao();
        verify(booksDaoMock, times(1)).findAllByTitle("title");
        Assert.assertTrue(allBooksByTitle.isEmpty());
    }

    @Test
    public void testFindById() throws NotFoundException {
        when(booksDaoMock.findById(1)).thenReturn(Book.newBuilder().build());
        Book foundedBook = bookService.findById(1);
        verify(daoFactoryMock, times(1)).createBooksDao();
        verify(booksDaoMock, times(1)).findById(1);
        Assert.assertEquals(foundedBook, Book.newBuilder().build());
    }

    @Test(expected = NotFoundException.class)
    public void testFindByIdThrows() throws NotFoundException {
        when(booksDaoMock.findById(1)).thenThrow(NotFoundException.class);
        try {
            bookService.findById(1);
        } catch (NotFoundException e) {
            verify(daoFactoryMock, times(1)).createBooksDao();
            verify(booksDaoMock, times(1)).findById(1);
            throw e;
        }
    }

    @Test
    public void testFindAllSorted() {
        List<Book> allBooks = bookService.findAllSorted("title", 1, 5);
        verify(daoFactoryMock, times(1)).createBooksDao();
        verify(booksDaoMock, times(1)).findAllSorted("title", 1, 5);
        Assert.assertTrue(allBooks.isEmpty());
    }
}
