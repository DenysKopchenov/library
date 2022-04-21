package com.dkop.library.controller;

import com.dkop.library.config.ApplicationConfig;
import com.dkop.library.controller.command.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;

import static com.dkop.library.config.ApplicationConfig.APPLICATION_CONTEXT;


/**
 * Main servlet that mapping request to the command
 */
public class FrontController extends HttpServlet {

    private CommandContainer commandContainer;

    @Override
    public void init(ServletConfig servletConfig) {
        commandContainer = APPLICATION_CONTEXT.getBean(CommandContainer.class);
        servletConfig.getServletContext().setAttribute("loggedUsers", new HashSet<>());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp, req.getMethod());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp, req.getMethod());
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response, String type) throws ServletException, IOException {
        String path = request.getRequestURI();
        String pathRegex = ".*/library/";
        path = path.replaceAll(pathRegex, "");
        Command command = commandContainer.getCommand(path);
        String pageToGo = command.execute(request);
        if (pageToGo.contains("redirect:")) {
            response.sendRedirect(pageToGo.replaceAll(".*redirect:", "/app"));
        } else {
            if (type.equals("POST") && StringUtils.containsAny(pageToGo, "admin", "reader", "librarian")) {
                response.sendRedirect(request.getRequestURI() + "?" + request.getQueryString());
            } else {
                request.getRequestDispatcher(pageToGo).forward(request, response);
            }
        }
    }
}