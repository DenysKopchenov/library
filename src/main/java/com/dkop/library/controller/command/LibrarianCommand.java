package com.dkop.library.controller.command;

import com.dkop.library.controller.command.Command;

import javax.servlet.http.HttpServletRequest;

public class LibrarianCommand  implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return "/WEB-INF/views/librarian.jsp";
    }
}
