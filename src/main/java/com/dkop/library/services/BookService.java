package com.dkop.library.services;

import com.dkop.library.dao.BooksDao;
import com.dkop.library.dao.DaoFactory;
import com.dkop.library.model.Book;
import com.dkop.library.model.exceptions.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class BookService {

    public void addNewBook(String title, String author, String publisher, String publishingDate, String amount) throws SQLException {
        LocalDate date = LocalDate.parse(publishingDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Book book = new Book(title, author, publisher, date, Integer.parseInt(amount));
        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
            booksDao.createBook(book);
        }
    }

    public void deleteBook(int bookId) throws SQLException {
        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
            booksDao.deleteBook(bookId);
        }

    }

//    public void searchByAuthor(HttpServletRequest request) {
//        String author = request.getParameter("searchByAuthor");
//        List<Book> booksByAuthor;
//        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
//            booksByAuthor = booksDao.searchByAuthor(author);
//            if (booksByAuthor.isEmpty()) {
//                request.setAttribute("notFound", String.format("%s was not found", author));
//            } else {
//                request.setAttribute("booksByAuthor", booksByAuthor);
//            }
//        }
//
//    }
//
//    public void searchByTitle(HttpServletRequest request) {
//        String title = request.getParameter("searchByTitle");
//        List<Book> booksByTitle;
//        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
//            booksByTitle = booksDao.searchByTitle(title);
//            if (booksByTitle.isEmpty()) {
//                request.setAttribute("notFound", String.format("%s was not found", title));
//            } else {
//                request.setAttribute("booksByTitle", booksByTitle);
//            }
//        }
//
//    }

    public List<Book> findAll() {
        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
            return booksDao.findAll();
        }
    }

    public Book findById(int id) throws NotFoundException {
        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
            return booksDao.findById(id);
        } catch (SQLException e) {
            throw new NotFoundException("Book not found");
        }
    }

    public void updateBook(Book book) throws SQLException {
        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
            booksDao.updateBook(book);
        }
    }
}
