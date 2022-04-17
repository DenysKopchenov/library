package com.dkop.library.controller.filters;

import com.dkop.library.controller.command.Command;
import com.dkop.library.controller.command.CommandContainer;
import com.dkop.library.dao.DaoFactory;
import com.dkop.library.exceptions.DoesNotExistException;
import com.dkop.library.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.dkop.library.utils.Fields.EMAIL;

public class CheckStatusFilter implements Filter {
    private final Command logout = CommandContainer.getCommand("logout");
    private final UserService userService = new UserService(DaoFactory.getInstance());
    private static final Logger LOGGER = LogManager.getLogger(CheckStatusFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String email = (String) req.getSession().getAttribute(EMAIL);
        if (email != null) {
            try {
                String status = userService.getUserInfo(email).getStatus();
                if (status.equals("blocked")) {
                    LOGGER.info("{} was blocked.", email);
                    logout.execute(req);
                    resp.sendRedirect("/app/library");
                } else {
                    chain.doFilter(req, resp);
                }
            } catch (DoesNotExistException e) {
                LOGGER.error(e, e.getCause());
            }
        } else {
            chain.doFilter(req, resp);
        }
    }

    @Override
    public void destroy() {
        //do nothing
    }
}
