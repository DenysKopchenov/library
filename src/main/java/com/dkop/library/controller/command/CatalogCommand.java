package com.dkop.library.controller.command;

import com.dkop.library.dao.BooksDao;
import com.dkop.library.dao.DaoFactory;
import com.dkop.library.model.Book;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class CatalogCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        try (BooksDao booksDao = DaoFactory.getInstance().createBooksDao()) {
            List<Book> catalog = booksDao.findAll();
            request.setAttribute("catalog", catalog);
            request.setAttribute("sort", request.getParameter("sort"));
        }
        return "/WEB-INF/catalog.jsp";
    }
}
