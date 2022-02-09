package com.dkop.library.controller.command;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;

public class CommandUtils {
    public static void setUserRole(HttpServletRequest request, String email, String role) {
        HttpSession session = request.getSession();
        ServletContext context = request.getServletContext();
        context.setAttribute("email", email);
        session.setAttribute("role", role);
    }

    public static boolean checkIsLogged(HttpServletRequest request, String email) {
            HashSet<String> loggedUsers = (HashSet<String>) request.getServletContext().getAttribute("loggedUsers");

            if (loggedUsers.contains(email)) {
                return true;
            }
            loggedUsers.add(email);
            request.getServletContext().setAttribute("loggedUsers", loggedUsers);
        return false;
    }

}
