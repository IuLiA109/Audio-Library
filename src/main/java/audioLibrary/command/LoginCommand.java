package audioLibrary.command;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import audioLibrary.exceptions.InvalidUserTypeException;
import audioLibrary.sql.SqlManager;
import audioLibrary.user.*;
import audioLibrary.exceptions.*;

public class LoginCommand implements Command{
    private User user;

    public LoginCommand(User user) throws InvalidUserTypeException{
        if (user.getType() != UserType.Anonymous)
            throw new InvalidUserTypeException();
        this.user = user;
    }

    @Override
    public User execute(String[] args, Connection connection, SqlManager manager) throws InvalidArgumentsException{
        if (args.length != 3)
            throw new InvalidArgumentsException("Invalid command!");

        String username = args[1];
        String password = args[2];

        Integer userExists = manager.existsInTable(new String[]{"username", "password"}, Arrays.copyOfRange(args, 1, args.length), "Account");

        if (userExists == 0) throw new InvalidArgumentsException();
        else if (userExists == 1){
            System.out.println("User logged in");
            if (manager.isAdmin(username))
                return new Administrator(username, password);
            return new AuthenticatedUser(username, password);
        }
        else throw new InvalidArgumentsException("PROBLEMA NOUA");
    }
}








