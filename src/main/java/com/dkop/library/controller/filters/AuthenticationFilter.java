package com.dkop.library.controller.filters;

import com.dkop.library.controller.command.*;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        HttpSession session = req.getSession();
        String role = (String) session.getAttribute("role");
        String requestURI = req.getRequestURI();

        if (requestURI.contains("admin")) {
            if (role != null && role.equals("admin")) {
                filterChain.doFilter(req, resp);
            } else {
                new LogOutCommand().execute(req);
                resp.sendRedirect("/app/library/login");
            }
        } else if (requestURI.contains("librarian")) {
            if (role != null && role.equals("librarian")) {
                filterChain.doFilter(req, resp);
            } else {
                new LogOutCommand().execute(req);
                resp.sendRedirect("/app/library/login");
            }
        } else if (requestURI.contains("reader")) {
            if (role != null && role.equals("reader")) {
                filterChain.doFilter(req, resp);
            } else {
                new LogOutCommand().execute(req);
                resp.sendRedirect("/app/library/login");
            }
        } else if (requestURI.contains("catalog")) {
            filterChain.doFilter(req, resp);
        } else {
            new LogOutCommand().execute(req);
            filterChain.doFilter(req, resp);
        }


    }

    @Override
    public void destroy() {
    }
}
