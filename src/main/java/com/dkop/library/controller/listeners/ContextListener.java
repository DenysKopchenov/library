package com.dkop.library.controller.listeners;

import com.dkop.library.dao.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        String path = context.getRealPath("/WEB-INF/log4j2.log");
        System.setProperty("logFile", path);

        final Logger logger = LogManager.getLogger(ContextListener.class);

        try {
            ConnectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.info("Context initialized");
    }
}
