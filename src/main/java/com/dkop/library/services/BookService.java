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

    public void createBook(String title, String author, String publisher, String publishingDate, String amount) throws AlreadyExistException {
        LocalDate date = LocalDate.parse(publishingDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Book book = Book.newBuilder()
                .title(title)
                .author(author)
                .publisher(publisher)
                .publishingDate(date)
                .amount(Integer.parseInt(amount))
                .build();
        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
            booksDao.create(book);
        } catch (SQLException e) {
            throw new AlreadyExistException("Book already exist, you can update it!");
        }
    }

    public void deleteBook(int id) throws SQLException {
        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
            booksDao.delete(id);
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
        }
    }

    public void updateBook(int id, String title, String author, String publisher, String publishingDate, String amount) throws SQLException {
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
            booksDao.updateBook(updatingBook);
        }
    }
}
