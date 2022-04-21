package com.dkop.library.tags;

import com.dkop.library.dao.UserDao;
import com.dkop.library.entity.User;
import com.dkop.library.exceptions.DoesNotExistException;
import com.dkop.library.exceptions.UnknownOperationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import java.io.IOException;


import static com.dkop.library.config.ApplicationConfig.APPLICATION_CONTEXT;
import static com.dkop.library.utils.Fields.EMAIL;
import static com.dkop.library.utils.LocalizationUtil.localizationBundle;

public class UserInfoTag extends TagSupport {

    private UserDao userDao;
    private static final Logger LOGGER = LogManager.getLogger(UserInfoTag.class);

    @Override
    public int doStartTag() {
        UserDao userDao = APPLICATION_CONTEXT.getBean(UserDao.class);
        HttpSession session = pageContext.getSession();
        String email = (String) session.getAttribute(EMAIL);
        User user;
        try {
            user = userDao.findByEmail(email);
        } catch (DoesNotExistException e) {
            LOGGER.error(e, e.getCause());
            throw new UnknownOperationException();
        }
        JspWriter out = pageContext.getOut();
        StringBuilder table = new StringBuilder();
        table.append("<div class=\"row\">")
                .append("<div class=\"col-4 mx-auto\">")
                .append("<table class=\"table table-bordered table-light\">")
                .append("<thead>")
                .append("<tr>")
                .append("<th>").append(localizationBundle.getString("first.name")).append("</th>")
                .append("<th>").append(localizationBundle.getString("last.name")).append("</th>")
                .append("<th>").append(localizationBundle.getString("email")).append("</th>")
                .append("<th>").append(localizationBundle.getString("role")).append("</th>")
                .append("</tr>")
                .append("</thead>")
                .append("<tbody>");
        if (user != null) {
            table.append("<tr>")
                    .append("<td>").append(user.getFirstName()).append("</td>")
                    .append("<td>").append(user.getLastName()).append("</td>")
                    .append("<td>").append(user.getEmail()).append("</td>")
                    .append("<td>").append(user.getRole()).append("</td>")
                    .append("</tr>");
        }
        table.append("</tbody>")
                .append("</table>")
                .append("</div>")
                .append("</div>");
        try {
            out.print(table);
        } catch (IOException e) {
            LOGGER.error(e, e.getCause());
        }
        return 1;
    }
}
