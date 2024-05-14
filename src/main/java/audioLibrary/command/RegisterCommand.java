package audioLibrary.command;

import audioLibrary.exceptions.InvalidArgumentsException;
import audioLibrary.exceptions.InvalidUserTypeException;
import audioLibrary.sql.SqlManager;
import audioLibrary.user.AuthenticatedUser;
import audioLibrary.user.User;
import audioLibrary.user.UserType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class RegisterCommand implements Command{
    private User user;

    public RegisterCommand(User user) throws InvalidUserTypeException {
        if (user.getType() != UserType.Anonymous)
            throw new InvalidUserTypeException();
        this.user = user;
    }

    @Override
    public User execute(String[] args, Connection connection, SqlManager manager) throws InvalidArgumentsException {
        if (args.length != 3)
            throw new InvalidArgumentsException("Invalid command!");

        String username = args[1];
        String password = args[2];
        Integer nextId = manager.numberOfRows("Account") + 1;

        Integer userExists = manager.existsInTable(new String[]{"username"}, new String[]{args[1]}, "Account");

        if (userExists == 1) throw new InvalidArgumentsException("User with given username already exists! Please try again!");
        else if(userExists == 0) {
            manager.insertInfoAccount(nextId, username, password);
            System.out.println("Account created");
            return new AuthenticatedUser(username, password);
        }
        else throw new InvalidArgumentsException("PROBLEMA NOUA");
    }
}
