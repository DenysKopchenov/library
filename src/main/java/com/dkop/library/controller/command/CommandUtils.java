package com.dkop.library.controller.command;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ResourceBundle;
import java.util.Set;

public class CommandUtils {
    public static ResourceBundle messagesBundle;
    public static void setUserRole(HttpServletRequest request, String email, String role) {
        HttpSession session = request.getSession();
        session.setAttribute("email", email);
        session.setAttribute("role", role);
    }

    public static boolean checkIsLogged(HttpServletRequest request, String email) {
        Set<String> loggedUsers = (Set<String>) request.getServletContext().getAttribute("loggedUsers");

        if (loggedUsers.contains(email)) {
            return true;
        }
        loggedUsers.add(email);
        return false;
    }
}
