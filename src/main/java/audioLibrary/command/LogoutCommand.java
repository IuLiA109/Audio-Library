package audioLibrary.command;

import audioLibrary.exceptions.InvalidArgumentsException;
import audioLibrary.exceptions.InvalidUserTypeException;
import audioLibrary.sql.SqlManager;
import audioLibrary.user.AnonymousUser;
import audioLibrary.user.AuthenticatedUser;
import audioLibrary.user.User;
import audioLibrary.user.UserType;

import java.sql.Connection;
import java.util.Arrays;

public class LogoutCommand implements Command{
    private User user;

    public LogoutCommand(User user) throws InvalidUserTypeException {
        if (user.getType() == UserType.Anonymous)
            throw new InvalidUserTypeException();
        this.user = user;
    }

    @Override
    public User execute(String[] args, Connection connection, SqlManager manager) throws InvalidArgumentsException {
        if (args.length != 1)
            throw new InvalidArgumentsException("Invalid command!");
        System.out.println("Successfully logged out");
        return new AnonymousUser();
    }
}
