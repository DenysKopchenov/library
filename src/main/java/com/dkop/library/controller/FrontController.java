package com.dkop.library.controller;

import com.dkop.library.controller.command.*;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;

/**
 * Main servlet that mapping request to the command
 */
public class FrontController extends HttpServlet {

    @Override
    public void init(ServletConfig servletConfig) {
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
        Command command = CommandContainer.getCommand(path);
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