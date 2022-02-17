package com.dkop.library.controller.command;

import com.dkop.library.model.Book;
import com.dkop.library.services.BookService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class CatalogCommand implements Command {
    private final BookService bookService = BookService.getInstance();
    @Override
    public String execute(HttpServletRequest request) {
        request.setAttribute("sort", request.getParameter("sort"));
        String sortBy = request.getParameter("sort");
        List<Book> catalog;
        if (sortBy != null) {
            catalog = bookService.findAllSorted(sortBy);
        } else {
            catalog = bookService.findAll();
        }
        request.setAttribute("catalog", catalog);
        return "/WEB-INF/catalog.jsp";
    }
}
