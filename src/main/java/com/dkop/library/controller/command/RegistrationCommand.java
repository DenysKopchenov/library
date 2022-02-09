package com.dkop.library.controller.command;

import com.dkop.library.dao.DaoFactory;
import com.dkop.library.services.RegistrationService;
import com.dkop.library.dao.UserDao;
import com.dkop.library.model.User;
import com.dkop.library.model.exceptions.AlreadyExistException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class RegistrationCommand implements Command {
    private static final String REGISTRATION_JSP = "/WEB-INF/registration.jsp";
    @Override
    public String execute(HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String email = request.getParameter("email");

        if (firstName == null
                || lastName == null
                || password == null
                || confirmPassword == null
                || email == null) {
            return REGISTRATION_JSP;
        }

        RegistrationService registrationService = new RegistrationService();
        User user = new User();
        if (registrationService.validate(firstName, lastName, password, confirmPassword, email, user, errors)) {
            try (UserDao userDao = DaoFactory.getInstance().createUserDao()){
                userDao.checkUser(email);
                userDao.createUser(user);
            } catch (AlreadyExistException e) {
                request.setAttribute("alreadyExist", e.getMessage());
                return REGISTRATION_JSP;
            }
        } else {
            request.setAttribute("validation", errors);
            return REGISTRATION_JSP;
        }
        return "redirect:/library/login";
    }
}
