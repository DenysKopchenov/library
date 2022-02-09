package com.dkop.library.controller.command;

import com.dkop.library.dao.DaoFactory;
import com.dkop.library.model.exceptions.AlreadyLoggedException;
import com.dkop.library.model.exceptions.WasBlockedException;
import com.dkop.library.services.LoginService;
import com.dkop.library.dao.UserDao;
import com.dkop.library.model.exceptions.DoesNotExistException;
import com.dkop.library.model.exceptions.WrongPasswordException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class LoginCommand implements Command {
    private static final String LOGIN_JSP = "/WEB-INF/login.jsp";
    @Override
    public String execute(HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        if (password == null || email == null) {
            return LOGIN_JSP;
        }

        LoginService loginService = new LoginService();
        String userRole;
        if (loginService.validate(email, password, errors)) {
            try (UserDao userDao = DaoFactory.getInstance().createUserDao()) {
                userRole = userDao.authenticateUser(email, password);
                //check is logged? if NO -> give role
                if (CommandUtils.checkIsLogged(request, email)) {
                    throw new AlreadyLoggedException("User already LOGGED!");
                } else {
                    switch (userRole) {
                        case "admin":
                            CommandUtils.setUserRole(request, email, userRole);
                            return "redirect:/library/admin";
                        case "librarian":
                            CommandUtils.setUserRole(request, email, userRole);
                            return "redirect:/library/librarian";
                        case "reader":
                            CommandUtils.setUserRole(request, email, userRole);
                            return "redirect:/library/reader";
                        default:
                            return LOGIN_JSP;
                    }
                }
            } catch (DoesNotExistException e) {
                request.setAttribute("doesNotExist", e.getMessage());
                return "/WEB-INF/login.jsp";
            } catch (WrongPasswordException e) {
                request.setAttribute("wrongPassword", e.getMessage());
                return "/WEB-INF/login.jsp";
            } catch (AlreadyLoggedException e) {
                request.setAttribute("alreadyLogged", e.getMessage());
                return "/WEB-INF/login.jsp";
            } catch (WasBlockedException e) {
                request.setAttribute("wasBlocked", e.getMessage());
                return "/WEB-INF/login.jsp";
            }
        } else {
            request.setAttribute("validation", errors);
            return "/WEB-INF/login.jsp";
        }

    }
}
