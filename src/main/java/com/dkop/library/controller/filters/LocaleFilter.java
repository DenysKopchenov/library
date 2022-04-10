package com.dkop.library.controller.filters;

import com.dkop.library.controller.command.CommandUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocaleFilter implements Filter {
    private static final String LANGUAGE = "language";
    private static final Locale DEFAULT_LOCALE = new Locale("en");

    @Override
    public void init(FilterConfig filterConfig) {
        // Do nothing
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String userSelectedLanguage = req.getParameter("lang");
        Locale locale = DEFAULT_LOCALE;
        if (userSelectedLanguage != null) {
            req.getSession().setAttribute(LANGUAGE, userSelectedLanguage);
            locale = new Locale(userSelectedLanguage);
        } else if (req.getSession().getAttribute(LANGUAGE) == null) {
            req.getSession().setAttribute(LANGUAGE, "en");
        } else {
            req.getSession().setAttribute(LANGUAGE, req.getSession().getAttribute(LANGUAGE));
            locale = new Locale((String) req.getSession().getAttribute(LANGUAGE));
        }

        CommandUtils.setMessagesBundle(ResourceBundle.getBundle("messages", locale));
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // Do nothing
    }
}
