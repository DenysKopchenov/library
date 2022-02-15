package com.dkop.library.controller.filters;

import com.dkop.library.controller.command.Command;
import com.dkop.library.controller.command.LogOutCommand;
import com.dkop.library.controller.command.LoginCommand;
import com.dkop.library.model.exceptions.DoesNotExistException;
import com.dkop.library.services.UserService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ProfileStatusFilter implements Filter {
    private final Command logout = new LogOutCommand();
    private final UserService userService = new UserService();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        try {
            String status = userService.getUserInfo((String) request.getServletContext().getAttribute("email")).getStatus();
            if (status.equals("blocked")) {
                logout.execute(req);
                resp.sendRedirect("/app/library");
            } else {
                chain.doFilter(request, response);
            }
        } catch (DoesNotExistException e) {
            e.printStackTrace();
        }


    }
}
