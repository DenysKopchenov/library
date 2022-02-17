package com.dkop.library.services;

import com.dkop.library.dao.BooksDao;
import com.dkop.library.dao.DaoFactory;
import com.dkop.library.model.Book;
import com.dkop.library.model.exceptions.AlreadyExistException;
import com.dkop.library.model.exceptions.NotFoundException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class BookService {
    private final DaoFactory daoFactory;
    private static BookService instance;

    public static BookService getInstance() {
        if (instance == null) {
            synchronized (BookService.class) {
                if (instance == null) {
                    BookService temp = new BookService();
                    instance = temp;
                }
            }
        }
        return instance;
    }

    private BookService() {
        daoFactory = DaoFactory.getInstance();
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
            throw new AlreadyExistException("Book already exist, you can update it!");
        }
    }

    public void updateBook(int id, String title, String author, String publisher, String publishingDate, String amount) throws NotFoundException {
        LocalDate date = LocalDate.parse(publishingDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Book updatingBook = Book.newBuilder()
                .id(id)
                .title(title)
                .author(author)
                .publisher(publisher)
                .publishingDate(date)
                .amount(Integer.parseInt(amount))
                .build();
        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
            booksDao.update(updatingBook);
        } catch (SQLException e) {
            throw new NotFoundException("Book not found");
        }
    }

    public void deleteBook(int id) throws NotFoundException {
        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
            try {
                booksDao.delete(id);
            } catch (SQLException e) {
                throw new NotFoundException("Book not found");
            }
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

    public List<Book> findAll() {
        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
            return booksDao.findAll();
        }
    }

    public Book findById(int id) throws NotFoundException {
        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
            return booksDao.findById(id);
        }
    }

    public List<Book> findAllSorted(String sortBy) {
        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
            return booksDao.findAllSorted(sortBy);
        }
    }
}
