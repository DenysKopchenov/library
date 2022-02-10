package com.dkop.library.controller.command;

import com.dkop.library.services.LoginService;
import com.dkop.library.services.Validator;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class LoginCommand implements Command {
    private static final String LOGIN_JSP = "/WEB-INF/login.jsp";
    private final LoginService loginService = new LoginService();

    @Override
    public String execute(HttpServletRequest request) {
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        if (password == null || email == null) {
            return LOGIN_JSP;
        }

        Map<String, String> errors = Validator.validateLoginForm(email, password);
        if (errors.isEmpty()) {
            return loginService.login(email, password, request);
        } else {
            request.setAttribute("validation", errors);
            return LOGIN_JSP;
        }
    }
}
