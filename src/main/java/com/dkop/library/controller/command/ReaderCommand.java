package com.dkop.library.controller.command;

import com.dkop.library.dao.BooksDao;
import com.dkop.library.dao.DaoFactory;
import com.dkop.library.model.Book;
import com.dkop.library.model.User;
import com.dkop.library.model.exceptions.DoesNotExistException;
import com.dkop.library.services.BookService;
import com.dkop.library.services.UserService;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ReaderCommand implements Command {
    private final BookService bookService = new BookService();
    private final UserService userService = new UserService();

    @Override
    public String execute(HttpServletRequest request) {

//        if (StringUtils.isNotBlank(request.getParameter("searchByAuthor"))){
//            bookService.searchByAuthor(request);
//        }
//        if (StringUtils.isNotBlank(request.getParameter("searchByTitle"))){
//            bookService.searchByTitle(request);
//        }
        if (StringUtils.isNotBlank(request.getParameter("profile"))) {
            showUserInfo(request);
        }
        if (StringUtils.isNotBlank(request.getParameter("operations"))) {
            String operations = request.getParameter("operations");
            if (operations.equals("catalog")) {
                try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
                    List<Book> catalog = booksDao.findAll();
                    request.setAttribute("catalog", catalog);
                }
            }
        }
        return "/WEB-INF/views/reader.jsp";
    }

    private void showUserInfo(HttpServletRequest request) {
        String email = (String) request.getServletContext().getAttribute("email");
        try {
            User user = userService.getUserInfo(email);
            request.setAttribute("userInfo", user);
        } catch (DoesNotExistException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
    }
}
