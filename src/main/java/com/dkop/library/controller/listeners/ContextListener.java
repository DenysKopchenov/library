package com.dkop.library.controller.listeners;

import com.dkop.library.config.ApplicationConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        String path = context.getRealPath("/WEB-INF/log4j2.log");
        System.setProperty("logFile", path);

        final Logger logger = LogManager.getLogger(ContextListener.class);
        context.setAttribute("applicationContext", new AnnotationConfigApplicationContext(ApplicationConfig.class));
        logger.info("Context initialized");
    }
}
