package com.dkop.library.services;

import com.dkop.library.dao.DaoFactory;
import com.dkop.library.dao.UserDao;
import com.dkop.library.entity.User;
import com.dkop.library.exceptions.AlreadyLoggedException;
import com.dkop.library.exceptions.DoesNotExistException;
import com.dkop.library.exceptions.WasBlockedException;
import com.dkop.library.exceptions.WrongPasswordException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Set;

import static com.dkop.library.utils.LocalizationUtil.errorMessagesBundle;

public class LoginService {
    private DaoFactory daoFactory;
    private static final Logger LOGGER = LogManager.getLogger(LoginService.class);

    public LoginService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
        LOGGER.info(LoginService.class.getSimpleName());
    }

    public String login(String email, String password, HttpServletRequest request) {
        try {
            User user = authenticateUser(email, password);
            String userRole = user.getRole();
            if (checkIsLogged(request, email)) {
                throw new AlreadyLoggedException(errorMessagesBundle.getString("already.logged"));
            } else {
                setUserRole(request, email, userRole);
                LOGGER.info("'{}' logged in. Role - '{}'.", email, userRole);
                return resolvePageByRole(userRole);
            }
        } catch (DoesNotExistException | WrongPasswordException | WasBlockedException | AlreadyLoggedException e) {
            LOGGER.error(e, e.getCause());
            request.setAttribute("errorMessage", e.getMessage());
            return "/WEB-INF/login.jsp";
        }
    }

    public User authenticateUser(String email, String password) throws DoesNotExistException, WrongPasswordException, WasBlockedException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            User user = userDao.findByEmail(email);
            if (user.getStatus().equals("active")) {
                if (user.getPassword().equals(DigestUtils.sha256Hex(password))) {
                    return user;
                } else {
                    throw new WrongPasswordException(errorMessagesBundle.getString("wrong.password"));
                }
            } else {
                throw new WasBlockedException(errorMessagesBundle.getString("was.blocked"));
            }
        }
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
                throw new RuntimeException("Unknown role: " + userRole);
        }
    }

    private void setUserRole(HttpServletRequest request, String email, String role) {
        HttpSession session = request.getSession();
        session.setAttribute("email", email);
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
