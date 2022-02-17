package com.dkop.library.controller.command;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Set;

public class LogOutCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Set<String> loggedUsers = (Set<String>) request.getServletContext().getAttribute("loggedUsers");

        String email = (String) session.getAttribute("email");
        if (loggedUsers != null) {
            loggedUsers.remove(email);
        }

        session.setAttribute("email", null);
        session.setAttribute("role", null);
        return "redirect:/library/";
    }
}
