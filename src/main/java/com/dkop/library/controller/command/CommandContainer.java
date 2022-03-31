package com.dkop.library.controller.command;

import java.util.HashMap;
import java.util.Map;

public class CommandContainer {
    private static final Map<String, Command> commands = new HashMap<>();

    static {
        commands.put("login", new LoginCommand());
        commands.put("logout", new LogOutCommand());
        commands.put("registration", new RegistrationCommand());
        commands.put("admin", new AdminCommand());
        commands.put("librarian", new LibrarianCommand());
        commands.put("reader", new ReaderCommand());
        commands.put("catalog", new CatalogCommand());

    }

    public static Command getCommand(String commandName){
        return commands.getOrDefault(commandName, p -> "/start.jsp");
    }
}
