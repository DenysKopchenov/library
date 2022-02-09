package com.dkop.library.controller.command;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;

public class LogOutCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        ServletContext context = request.getServletContext();
        HashSet<String> loggedUsers = (HashSet<String>) request.getServletContext().getAttribute("loggedUsers");

        String email = (String) context.getAttribute("email");
        if (loggedUsers != null) {
            loggedUsers.remove(email);
        }
        request.getServletContext().setAttribute("loggedUsers", loggedUsers);

        context.setAttribute("email", null);
        session.setAttribute("role", null);
        return "redirect:/library/";
    }
}
