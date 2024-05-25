package audioLibrary.command;

import audioLibrary.exceptions.InvalidUserTypeException;
import audioLibrary.user.User;
import audioLibrary.user.UserType;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand {
    private User user;
    private List<String> commandsAvailable;

    public HelpCommand(User user){
        if(user.getType() == UserType.Anonymous)
            commandsAvailable = new ArrayList<>(List.of("register", "login", "help", "exit"));
        if(user.getType() == UserType.Authenticated)
            commandsAvailable = new ArrayList<>(List.of("search", "create playlist", "add", "export playlist"));
        if(user.getType() == UserType.Administrator)
            commandsAvailable = new ArrayList<>(List.of("search", "create playlist", "add", "export playlist", "create song"));
        this.user = user;
    }

    public void execute(){
        System.out.println("Available commands:");
        for (String command : commandsAvailable) {
            System.out.println("- " + command);
        }
    }

}
