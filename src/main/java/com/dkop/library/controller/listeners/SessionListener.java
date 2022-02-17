package com.dkop.library.controller.listeners;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Set;

public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        Set<String> loggedUsers = (Set<String>) se.getSession().getServletContext().getAttribute("loggedUsers");

        String email = (String) se.getSession().getAttribute("email");
        if (loggedUsers != null) {
            loggedUsers.remove(email);
        }
    }
}
