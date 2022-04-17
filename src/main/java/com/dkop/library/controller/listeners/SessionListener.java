package com.dkop.library.controller.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Set;

import static com.dkop.library.utils.Fields.EMAIL;

public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // do nothing
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        final Logger logger = LogManager.getLogger(SessionListener.class);
        Set<String> loggedUsers = (Set<String>) httpSessionEvent.getSession().getServletContext().getAttribute("loggedUsers");

        String email = (String) httpSessionEvent.getSession().getAttribute(EMAIL);
        if (loggedUsers != null) {
            loggedUsers.remove(email);
            logger.info("'{}' logged out.", email);
        }
    }
}
