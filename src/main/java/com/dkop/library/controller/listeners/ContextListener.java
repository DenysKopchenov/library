package com.dkop.library.controller.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final Logger logger = LogManager.getLogger(ContextListener.class);
        ServletContext context = sce.getServletContext();
        String path = context.getRealPath("/WEB-INF/log4j2.log");
        System.setProperty("logFile", path);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.error(e, e.getCause());
        }
        logger.info("Initialized");
    }
}
