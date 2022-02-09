package com.dkop.library.controller.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LocaleFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String currentLocale = req.getParameter("currentLocale");
        if (currentLocale != null) {
            if (currentLocale.equals("uk")){
                req.setAttribute("currentLocale", "uk");
                filterChain.doFilter(req, resp);
            } else {
                req.setAttribute("currentLocale", "en");
                filterChain.doFilter(req, resp);
            }
        } else {
            filterChain.doFilter(req, resp);
        }
    }

    @Override
    public void destroy() {
    }
}
