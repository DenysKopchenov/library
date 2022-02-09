package com.dkop.library.controller.command;


import com.dkop.library.dao.BooksDao;
import com.dkop.library.dao.DaoFactory;
import com.dkop.library.dao.UserDao;
import com.dkop.library.model.Book;
import com.dkop.library.model.User;
import com.dkop.library.services.BookService;
import com.dkop.library.services.UserService;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class AdminCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        BookService bookService = new BookService();
        UserService userService = new UserService();
        if (StringUtils.isNotBlank(request.getParameter("searchByAuthor"))) {
            bookService.searchByAuthor(request);
        }
        if (StringUtils.isNotBlank(request.getParameter("searchByTitle"))) {
            bookService.searchByTitle(request);
        }
        if (StringUtils.isNotBlank(request.getParameter("profile"))) {
            userService.getUserProfile(request);
        }
        if (StringUtils.isNotBlank(request.getParameter("operations"))) {
            String operations = request.getParameter("operations");
            switch (operations) {
                case "createBook":
                    request.setAttribute("operation", "createBook");
                    if (request.getParameter("createNewBook") != null) {
                        bookService.addNewBook(request);
                    }
                    break;
                case "catalog":
                    try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
                        List<Book> catalog = booksDao.getAllBooks();
                        request.setAttribute("catalog", catalog);
                    }
                    break;
                case "deleteBook":
                    bookService.deleteBook(request, Integer.parseInt(request.getParameter("bookId")));
                    break;
                case "updateBook":
                    try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
                        Book book = booksDao.getBookById(Integer.parseInt(request.getParameter("bookId")));
                        request.setAttribute("operation", "updateBook");
                        request.setAttribute("updatingBook", book);
                        if (request.getParameter("updateCurrentBook") != null) {
                            bookService.updateBook(request, Integer.parseInt(request.getParameter("bookId")));
                        }
                    }
                    break;
                case "listUsers":
                    try (UserDao userDao = DaoFactory.getInstance().createUserDao()){
                        List<User> users = userDao.getAllUsers();
                        request.setAttribute("allUsers", users);
                    }
                    break;
                case "createLibrarian":
                    request.setAttribute("operation", "createLibrarian");
                    //TODO: createLibrarian
                    break;
                case "deleteLibrarian":
                    request.setAttribute("operation", "deleteLibrarian");
                    //TODO: deleteLibrarian
                    break;
                case "blockUser":
                    //TODO block user
                    break;
                case "unblockUser":
                    // todo unblock user
                    break;
            }
        }
        return "/WEB-INF/views/admin.jsp";
    }
}