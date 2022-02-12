package com.dkop.library.services;

import com.dkop.library.dao.DaoFactory;
import com.dkop.library.dao.UserDao;
import com.dkop.library.model.User;
import com.dkop.library.model.exceptions.DoesNotExistException;

import javax.servlet.http.HttpServletRequest;

public class UserService {
    //return user
    public void getUserProfile(HttpServletRequest request) {
        String email = (String) request.getServletContext().getAttribute("email");
        try (UserDao userDao = DaoFactory.getInstance().createUserDao()) {
            User user = userDao.findUser(email);
            request.setAttribute("userInfo", user);
        } catch (DoesNotExistException e) {
            request.setAttribute("error", e.getMessage());
        }
    }
}