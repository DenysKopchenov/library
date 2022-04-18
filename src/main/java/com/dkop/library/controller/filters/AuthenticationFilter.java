package com.dkop.library.controller.filters;

import com.dkop.library.exceptions.UnknownOperationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

import static com.dkop.library.utils.Fields.HOME_PAGE;

public class AuthenticationFilter implements Filter {

    private static final Logger LOGGER = LogManager.getLogger(AuthenticationFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
        // Do nothing
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        HttpSession session = req.getSession();
        String role = (String) session.getAttribute("role");
        String requestURI = req.getRequestURI();

        Set<String> publicAvailablePages = Set.of("/registration", "/login", "/catalog", "/logout", "library/");
        boolean isPublicAvailablePage = isPublicAvailablePage(requestURI, publicAvailablePages);
        if (role == null) {
            if (isPublicAvailablePage) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                resp.sendRedirect(HOME_PAGE);
            }
            return;
        }

        switch (role) {
            case "admin":
                if (requestURI.contains("admin") || requestURI.contains("logout")) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    LOGGER.info("Unauthorized access");
                    resp.sendRedirect(HOME_PAGE + role);
                }
                break;
            case "librarian":
                if (requestURI.contains("librarian") || requestURI.contains("logout")) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    LOGGER.info("Unauthorized access");
                    resp.sendRedirect(HOME_PAGE + role);
                }
                break;
            case "reader":
                if (requestURI.contains("reader") || requestURI.contains("logout")) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    LOGGER.info("Unauthorized access");
                    resp.sendRedirect(HOME_PAGE + role);
                }
                break;
            default:
                throw new UnknownOperationException("Unknown role: " + role);
        }

    }

    private boolean isPublicAvailablePage(String requestURI, Set<String> publicAvailablePages) {
        for (String publicAvailablePage : publicAvailablePages) {
            if (requestURI.endsWith(publicAvailablePage)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        // Do nothing
    }
}
