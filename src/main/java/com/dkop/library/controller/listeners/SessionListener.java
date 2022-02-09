package com.dkop.library.controller.listeners;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashSet;

public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HashSet<String> loggedUsers = (HashSet<String>) se.getSession().getServletContext().getAttribute("loggedUsers");

        String email = (String) se.getSession().getServletContext().getAttribute("email");
        if (loggedUsers != null) {
            loggedUsers.remove(email);
        }
        se.getSession().getServletContext().setAttribute("loggedUsers", loggedUsers);
    }
}
