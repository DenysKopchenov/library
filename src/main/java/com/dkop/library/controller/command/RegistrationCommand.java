package com.dkop.library.controller.command;

import com.dkop.library.exceptions.AlreadyExistException;
import com.dkop.library.services.UserService;
import com.dkop.library.utils.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.dkop.library.utils.Fields.*;

/**
 * Command for Registration page
 */
public class RegistrationCommand implements Command {

    private static final String REGISTRATION_JSP = "/WEB-INF/registration.jsp";
    private final UserService userService;
    private static final Logger LOGGER = LogManager.getLogger(RegistrationCommand.class);

    public RegistrationCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String email = request.getParameter(EMAIL);
        if (firstName == null
                || lastName == null
                || password == null
                || confirmPassword == null
                || email == null) {
            return REGISTRATION_JSP;
        }

        Map<String, String> errors = Validator.validateRegistrationForm(firstName, lastName, password, confirmPassword, email);
        if (errors.isEmpty()) {
            try {
                userService.createUser(firstName, lastName, email, password, "reader", "active");
            } catch (AlreadyExistException e) {
                LOGGER.error(e, e.getCause());
                request.setAttribute(ERROR_MESSAGE, e.getMessage());
                return REGISTRATION_JSP;
            }
        } else {
            request.setAttribute(VALIDATION, errors);
            return REGISTRATION_JSP;
        }
        return "redirect:/library/login";
    }
}
