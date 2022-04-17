package com.dkop.library.controller.command;

import com.dkop.library.services.LoginService;
import com.dkop.library.utils.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.dkop.library.utils.Fields.EMAIL;
import static com.dkop.library.utils.Fields.VALIDATION;

public class LoginCommand implements Command {
    private static final String LOGIN_JSP = "/WEB-INF/login.jsp";
    private final LoginService loginService;
    private static final Logger LOGGER = LogManager.getLogger(LoginCommand.class);

    public LoginCommand(LoginService loginService) {
        this.loginService = loginService;
        LOGGER.info(LoginCommand.class.getSimpleName());
    }

    @Override
    public String execute(HttpServletRequest request) {
        String password = request.getParameter("password");
        String email = request.getParameter(EMAIL);
        if (password == null || email == null) {
            return LOGIN_JSP;
        }

        Map<String, String> errors = Validator.validateLoginForm(email, password);
        if (errors.isEmpty()) {
            return loginService.login(email, password, request);
        } else {
            request.setAttribute(VALIDATION, errors);
            return LOGIN_JSP;
        }
    }
}
