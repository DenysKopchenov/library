package com.dkop.library.services;

import com.dkop.library.dao.UserDao;
import com.dkop.library.entity.User;
import com.dkop.library.exceptions.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Set;

import static com.dkop.library.utils.Fields.EMAIL;
import static com.dkop.library.utils.Fields.ERROR_MESSAGE;
import static com.dkop.library.utils.LocalizationUtil.localizationBundle;

@Service
public class LoginService {

    private final UserDao userDao;
    private static final Logger LOGGER = LogManager.getLogger(LoginService.class);

    public LoginService(UserDao userDao) {
        this.userDao = userDao;
    }

    public String login(String email, String password, HttpServletRequest request) {
        try {
            User user = authenticateUser(email, password);
            String userRole = user.getRole();
            if (checkIsLogged(request, email)) {
                throw new AlreadyLoggedException(localizationBundle.getString("already.logged"));
            }
            setUserRole(request, email, userRole);
            LOGGER.info("'{}' logged in. Role - '{}'.", email, userRole);
            return resolvePageByRole(userRole);

        } catch (DoesNotExistException | WrongPasswordException | WasBlockedException | AlreadyLoggedException e) {
            LOGGER.error(e, e.getCause());
            request.setAttribute(ERROR_MESSAGE, e.getMessage());
            return "/WEB-INF/login.jsp";
        }
    }

    private User authenticateUser(String email, String password) throws DoesNotExistException, WrongPasswordException, WasBlockedException {
            User user = userDao.findByEmail(email);
            if (user.getStatus().equals("active")) {
                String passwordHash = DigestUtils.sha256Hex(password);
                if (user.getPassword().equals(passwordHash)) {
                    return user;
                }
                throw new WrongPasswordException(localizationBundle.getString("wrong.password"));
            }
            throw new WasBlockedException(localizationBundle.getString("was.blocked"));
    }

    private String resolvePageByRole(String userRole) {
        switch (userRole) {
            case "admin":
                return "redirect:/library/admin";
            case "librarian":
                return "redirect:/library/librarian";
            case "reader":
                return "redirect:/library/reader";
            default:
                throw new UnknownOperationException("Unknown role: " + userRole);
        }
    }

    private void setUserRole(HttpServletRequest request, String email, String role) {
        HttpSession session = request.getSession();
        session.setAttribute(EMAIL, email);
        session.setAttribute("role", role);
    }

    private boolean checkIsLogged(HttpServletRequest request, String email) {
        Set<String> loggedUsers = (Set<String>) request.getServletContext().getAttribute("loggedUsers");

        if (loggedUsers.contains(email)) {
            return true;
        }
        loggedUsers.add(email);
        return false;
    }
}
