package com.dkop.library.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Set;

import static com.dkop.library.utils.Fields.EMAIL;

public class LogOutCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(LogOutCommand.class);

    public LogOutCommand() {
        LOGGER.info(LogOutCommand.class.getSimpleName());
    }

    @Override
    public String execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Set<String> loggedUsers = (Set<String>) request.getServletContext().getAttribute("loggedUsers");

        String email = (String) session.getAttribute(EMAIL);
        if (loggedUsers != null) {
            boolean removed = loggedUsers.remove(email);
            if (removed) {
                LOGGER.info("'{}' logged out.", email);
            }
        }

        session.setAttribute(EMAIL, null);
        session.setAttribute("role", null);
        return "redirect:/library/";
    }
}
