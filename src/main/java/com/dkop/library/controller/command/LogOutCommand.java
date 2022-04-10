package com.dkop.library.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Set;

public class LogOutCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(LoginCommand.class);

    public LogOutCommand() {
        LOGGER.info(LogOutCommand.class.getSimpleName());
    }

    @Override
    public String execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Set<String> loggedUsers = (Set<String>) request.getServletContext().getAttribute("loggedUsers");

        String email = (String) session.getAttribute("email");
        if (loggedUsers != null) {
            loggedUsers.remove(email);
            LOGGER.info("{} logged out", email);
        }

        session.setAttribute("email", null);
        session.setAttribute("role", null);
        return "redirect:/library/";
    }
}
