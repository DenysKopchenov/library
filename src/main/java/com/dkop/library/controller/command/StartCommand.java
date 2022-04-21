package com.dkop.library.controller.command;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class StartCommand implements Command{
    @Override
    public String execute(HttpServletRequest request) {
        return "/start.jsp";
    }
}
