package com.dkop.library.controller.command;

import com.dkop.library.services.ServiceContainer;

import java.util.HashMap;
import java.util.Map;

public class CommandContainer {

    private static final Map<String, Command> commands = new HashMap<>();

    private CommandContainer() {
    }

    static {
        final ServiceContainer sc = new ServiceContainer();
        commands.put("login", new LoginCommand(sc.getLoginService()));
        commands.put("logout", new LogOutCommand());
        commands.put("registration", new RegistrationCommand(sc.getUserService()));
        commands.put("admin", new AdminCommand(sc.getBookService(), sc.getUserService(), sc.getPaginationService()));
        commands.put("librarian", new LibrarianCommand(sc.getBookService(), sc.getUserService(), sc.getOrderService(), sc.getPaginationService()));
        commands.put("reader", new ReaderCommand(sc.getBookService(), sc.getUserService(), sc.getOrderService(), sc.getPaginationService()));
        commands.put("catalog", new CatalogCommand(sc.getPaginationService()));
    }

    public static Command getCommand(String commandName) {
        return commands.getOrDefault(commandName, p -> "/start.jsp");
    }
}
