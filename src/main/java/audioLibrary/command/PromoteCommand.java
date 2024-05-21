package audioLibrary.command;

import audioLibrary.exceptions.InvalidArgumentsException;
import audioLibrary.exceptions.InvalidCommandException;
import audioLibrary.exceptions.InvalidUserTypeException;
import audioLibrary.sql.SqlManager;
import audioLibrary.user.Administrator;
import audioLibrary.user.AuthenticatedUser;
import audioLibrary.user.User;
import audioLibrary.user.UserType;

import java.sql.Connection;
import java.util.Arrays;

public class PromoteCommand implements Command{
    private User user;

    public PromoteCommand(User user) throws InvalidUserTypeException {
        if (user.getType() != UserType.Administrator)
            throw new InvalidUserTypeException();
        this.user = user;
    }

    @Override
    public User execute(String[] args, Connection connection, SqlManager manager) throws InvalidArgumentsException, InvalidCommandException {
        if (args.length != 2)
            throw new InvalidCommandException();

        String username = args[1];

        Integer userExists = manager.existsInTable(new String[]{"username"}, new String[]{args[1]}, "Account");

        if (userExists == 0) throw new InvalidArgumentsException("Specified user does not exist!");
        else if (userExists == 1){
            manager.promoteAccount(username);
            System.out.println(username + " is now an administrator!");
            return user;
        }
        else throw new InvalidArgumentsException("PROBLEMA NOUA");
    }
}
