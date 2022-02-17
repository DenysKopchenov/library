package com.dkop.library.controller.filters;

import com.dkop.library.controller.command.Command;
import com.dkop.library.controller.command.LogOutCommand;
import com.dkop.library.model.exceptions.DoesNotExistException;
import com.dkop.library.services.UserService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CheckStatusFilter implements Filter {
    private final Command logout = new LogOutCommand();
    private final UserService userService = UserService.getInstance();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String email = (String) request.getServletContext().getAttribute("email");
        if (email != null) {
            try {
                String status = userService.getUserInfo(email).getStatus();
                if (status.equals("blocked")) {
                    logout.execute(req);
                    resp.sendRedirect("/app/library");
                } else {
                    chain.doFilter(req, resp);
                }
            } catch (DoesNotExistException e) {
                e.printStackTrace();
            }
        } else {
            chain.doFilter(req, resp);
        }
    }

    @Override
    public void destroy() {

    }
}
