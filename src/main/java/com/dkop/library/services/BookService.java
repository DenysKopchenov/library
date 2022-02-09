package com.dkop.library.services;

import com.dkop.library.dao.BooksDao;
import com.dkop.library.dao.DaoFactory;
import com.dkop.library.model.Book;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dkop.library.services.RegexContainer.*;

public class BookService {
    public boolean validate(String title, String author, String publisher, String publishingDate, String amount, Map<String, String> errors) {
        boolean isValid = true;
        if (!title.matches(BOOK_VALIDATION)) {
            errors.put("title", "Not Valid");
            isValid = false;
        }
        if (!author.matches(BOOK_VALIDATION)) {
            errors.put("author", "Not Valid");
            isValid = false;
        }
        if (!publisher.matches(BOOK_VALIDATION)) {
            errors.put("publisher", "Not Valid");
            isValid = false;
        }
        if (!amount.matches("\\d+")) {
            errors.put("amount", "Can not be negative, 0, or fraction");
            isValid = false;
        }
        if (!publishingDate.matches(DATE_FORMAT_VALIDATION)) {
            errors.put("publishingDate", "Not Valid. Input in format yyyy.MM.dd");
            isValid = false;
        } else {
            if (getPublishingDate(publishingDate).isAfter(LocalDate.now())) {
                errors.put("publishingDate", "Can not be after today");
                isValid = false;
            }
        }
        return isValid;
    }

    public void addNewBook(HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String publisher = request.getParameter("publisher");
        String publishingDate = request.getParameter("publishing_date");
        String amount = request.getParameter("amount");

        if (validate(title, author, publisher, publishingDate, amount, errors)) {
            Book book = new Book(title, author, publisher, getPublishingDate(publishingDate), Integer.parseInt(amount));
            try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
                if (!booksDao.checkBook(book)) {
                    booksDao.createBook(book);
                    request.setAttribute("successCreate", "Successfully created");
                } else {
                    request.setAttribute("failedCreate", "Book already exist, UPDATE if you want!");
                }
            }
        } else {
            request.setAttribute("validation", errors);
        }
    }

    public void deleteBook(HttpServletRequest request, int bookId) {
        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
            booksDao.deleteBook(bookId);
            request.setAttribute("successDelete", "Successfully deleted");
        }

    }


    public void searchByAuthor(HttpServletRequest request) {
        String author = request.getParameter("searchByAuthor");
        List<Book> booksByAuthor;
        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
            booksByAuthor = booksDao.searchByAuthor(author);
            if (booksByAuthor.isEmpty()) {
                request.setAttribute("notFound", String.format("%s was not found", author));
            } else {
                request.setAttribute("booksByAuthor", booksByAuthor);
            }
        }

    }

    public void searchByTitle(HttpServletRequest request) {
        String title = request.getParameter("searchByTitle");
        List<Book> booksByTitle;
        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
            booksByTitle = booksDao.searchByTitle(title);
            if (booksByTitle.isEmpty()) {
                request.setAttribute("notFound", String.format("%s was not found", title));
            } else {
                request.setAttribute("booksByTitle", booksByTitle);
            }
        }

    }

    public void updateBook(HttpServletRequest request, int id) {
        Map<String, String> errors = new HashMap<>();
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String publisher = request.getParameter("publisher");
        String publishingDate = request.getParameter("publishing_date");
        String amount = request.getParameter("amount");

        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
            if (validate(title, author, publisher, publishingDate, amount, errors)) {
                Book book = new Book(id, title, author, publisher, getPublishingDate(publishingDate), Integer.parseInt(amount));
                booksDao.updateBook(book);
                request.setAttribute("successUpdate", "Successfully updated");
            } else {
                request.setAttribute("validation", errors);
            }
        } catch (SQLException e) {
            request.setAttribute("failed", "Failed update");
        }
    }

    public LocalDate getPublishingDate(String publishingDate) {
        publishingDate = publishingDate.replaceAll("\\W", "");
        int year = Integer.parseInt(publishingDate.substring(0, 4));
        int month = Integer.parseInt(publishingDate.substring(4, 6));
        int day = Integer.parseInt(publishingDate.substring(6, 8));
        return LocalDate.of(year, month, day);
    }
}
