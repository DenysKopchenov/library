package com.dkop.library.controller.command;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface for all commands, to handle request
 */
public interface Command {

    /**
     * Executes the request
     * @param request
     * @return String path to forward or redirect
     */
    String execute(HttpServletRequest request);
}
