package com.dkop.library.services;

import com.dkop.library.dao.BooksDao;
import com.dkop.library.dao.DaoFactory;
import com.dkop.library.entity.Book;
import com.dkop.library.exceptions.AlreadyExistException;
import com.dkop.library.exceptions.NotFoundException;
import com.dkop.library.exceptions.UnableToDeleteException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


import static com.dkop.library.controller.command.CommandUtils.messagesBundle;


public class BookService {
    private final DaoFactory daoFactory;
    private static BookService instance;
    private static final Logger LOGGER = LogManager.getLogger(BookService.class);

    public static BookService getInstance() {
        if (instance == null) {
            synchronized (BookService.class) {
                if (instance == null) {
                    BookService bookService = new BookService();
                    instance = bookService;
                }
            }
        }
        return instance;
    }

    private BookService() {
        daoFactory = DaoFactory.getInstance();
        LOGGER.info(BookService.class.getSimpleName());
    }

    public void createBook(String title, String author, String publisher, String publishingDate, String amount) throws AlreadyExistException {
        LocalDate date = LocalDate.parse(publishingDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Book book = Book.newBuilder()
                .title(title)
                .author(author)
                .publisher(publisher)
                .publishingDate(date)
                .amount(Integer.parseInt(amount))
                .build();
        try (BooksDao booksDao = daoFactory.createBooksDao()) {
            booksDao.create(book);
        } catch (SQLException e) {
            LOGGER.error(e, e.getCause());
            throw new AlreadyExistException(messagesBundle.getString("book.already.exist"), e);
        }
    }

    public void updateBook(int id, String title, String author, String publisher, String publishingDate, String amount) throws NotFoundException {
        Book bookFromDB = findById(id);
        LocalDate date = LocalDate.parse(publishingDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Book updatingBook = Book.newBuilder()
                .id(id)
                .title(title)
                .author(author)
                .publisher(publisher)
                .publishingDate(date)
                .amount(Integer.parseInt(amount))
                .onOrder(bookFromDB.getOnOrder())
                .build();
        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
            booksDao.update(updatingBook);
        }
    }

    public void deleteBook(int id) throws NotFoundException, UnableToDeleteException {
        findById(id);
        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
            booksDao.delete(id);
        }
    }

    public List<Book> findAllBooksByAuthor(String author) {
        List<Book> books;
        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
            books = booksDao.findAllByAuthor(author);
        }
        return books;
    }

    public List<Book> findAllBooksByTitle(String title) {
        List<Book> books;
        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
            books = booksDao.findAllByTitle(title);
        }
        return books;
    }

    public Book findById(int id) throws NotFoundException {
        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
            return booksDao.findById(id);
        }
    }

    public List<Book> findAllSorted(String sortBy, int start, int numberOfRecords) {
        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
            return booksDao.findAllSorted(sortBy, start, numberOfRecords);
        }
    }

    public int countAllRows() {
        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
            return booksDao.countAllRows();
        }
    }
}
