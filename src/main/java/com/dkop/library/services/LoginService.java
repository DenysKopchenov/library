package com.dkop.library.services;

import com.dkop.library.controller.command.CommandUtils;
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

import static com.dkop.library.controller.command.CommandUtils.messagesBundle;

public class LoginService {
    private DaoFactory daoFactory;
    private static final Logger LOGGER = LogManager.getLogger(LoginService.class);

    public LoginService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
        LOGGER.info(LoginService.class.getSimpleName());
    }

    public String login(String email, String password, HttpServletRequest request) {
        try {
            String userRole = authenticateUser(email, password);
            if (CommandUtils.checkIsLogged(request, email)) {
                throw new AlreadyLoggedException(messagesBundle.getString("already.logged"));
            } else {
                CommandUtils.setUserRole(request, email, userRole);
                LOGGER.info("'{}' logged in. Role - '{}'.", email, userRole);
                return resolvePageByRole(userRole);
            }
        } catch (DoesNotExistException | WrongPasswordException | WasBlockedException | AlreadyLoggedException e) {
            LOGGER.error(e, e.getCause());
            request.setAttribute("errorMessage", e.getMessage());
            return "/WEB-INF/login.jsp";
        }
    }

    /**
     * This method authenticate email and password, if user exist return role;
     *
     * @param email    email from request
     * @param password password form request and encode
     * @return User role if user authenticates
     * @throws DoesNotExistException  if email does not exist
     * @throws WrongPasswordException if password is not equal to user password in DB
     * @throws WasBlockedException    if status is blocked
     */
    public String authenticateUser(String email, String password) throws DoesNotExistException, WrongPasswordException, WasBlockedException {
        try (UserDao userDao = daoFactory.createUserDao()) {
            User user = userDao.findByEmail(email);
            if (user.getStatus().equals("active")) {
                if (user.getPassword().equals(DigestUtils.sha256Hex(password))) {
                    return user.getRole();
                } else {
                    throw new WrongPasswordException(messagesBundle.getString("wrong.password"));
                }
            } else {
                throw new WasBlockedException(messagesBundle.getString("was.blocked"));
            }
        }
    }

    /**
     * Return string page to redirect depends on user role;
     *
     * @param userRole after authenticate user
     * @return path to move depends on user role
     */
    public String resolvePageByRole(String userRole) {
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
}
