package com.dkop.library.controller.command;

import com.dkop.library.services.BookService;
import com.dkop.library.services.UserService;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class ReaderCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        BookService bookService = new BookService();
        UserService userService = new UserService();
        if (StringUtils.isNotBlank(request.getParameter("searchByAuthor"))){
            bookService.searchByAuthor(request);
        }
        if (StringUtils.isNotBlank(request.getParameter("searchByTitle"))){
            bookService.searchByTitle(request);
        }
        if (StringUtils.isNotBlank(request.getParameter("profile"))){
            userService.getUserProfile(request);
        }
        return "/WEB-INF/views/reader.jsp";
    }
}
