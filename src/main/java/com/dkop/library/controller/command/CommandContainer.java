package com.dkop.library.controller.command;

import org.springframework.stereotype.Component;

/**
 * Container for all commands
 */
@Component
public class CommandContainer {

    private final LoginCommand loginCommand;
    private final LogOutCommand logOutCommand;
    private final RegistrationCommand registrationCommand;
    private final AdminCommand adminCommand;
    private final LibrarianCommand librarianCommand;
    private final ReaderCommand readerCommand;
    private final CatalogCommand catalogCommand;
    private final StartCommand startCommand;

    public CommandContainer(LoginCommand loginCommand, LogOutCommand logOutCommand, RegistrationCommand registrationCommand, AdminCommand adminCommand, LibrarianCommand librarianCommand, ReaderCommand readerCommand, CatalogCommand catalogCommand, StartCommand noCommand) {
        this.loginCommand = loginCommand;
        this.logOutCommand = logOutCommand;
        this.registrationCommand = registrationCommand;
        this.adminCommand = adminCommand;
        this.librarianCommand = librarianCommand;
        this.readerCommand = readerCommand;
        this.catalogCommand = catalogCommand;
        this.startCommand = noCommand;
    }

    /**
     * Returns command for FrontController
     * @param commandName
     * @return Command by name or start page by default if name dopes not exist
     */
    public Command getCommand(String commandName) {
        switch (commandName){
            case "login":
                return loginCommand;
            case "logout":
                return logOutCommand;
            case "registration":
                return registrationCommand;
            case "admin":
                return adminCommand;
            case "librarian":
                return librarianCommand;
            case "reader":
                return readerCommand;
            case "catalog":
                return catalogCommand;
            default: return startCommand;
        }

    }
}
