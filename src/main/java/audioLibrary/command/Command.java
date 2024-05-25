package audioLibrary.command;

import audioLibrary.exceptions.InvalidArgumentsException;
import audioLibrary.exceptions.InvalidCommandException;
import audioLibrary.sql.SqlManager;
import audioLibrary.user.User;
//import audioLibrary.user.User;

import java.sql.Connection;

public interface Command {
    /**
     * Executes the command with the given arguments.
     *
     * @param args the split input after spaces
     * @param connection the connection to the database
     * @param manager the SQL manager to manage SQL operations
     * @return the user object resulting from the command execution
     * @throws InvalidCommandException if the command is invalid
     * @throws InvalidArgumentsException if the user data provided in the arguments are invalid
     */
    User execute(String[] args, Connection connection, SqlManager manager) throws InvalidCommandException, InvalidArgumentsException;
}
