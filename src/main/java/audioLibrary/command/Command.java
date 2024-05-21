package audioLibrary.command;

import audioLibrary.exceptions.InvalidArgumentsException;
import audioLibrary.exceptions.InvalidCommandException;
import audioLibrary.sql.SqlManager;
import audioLibrary.user.User;
//import audioLibrary.user.User;

import java.sql.Connection;

public interface Command {
    User execute(String[] args, Connection connection, SqlManager manager) throws InvalidCommandException, InvalidArgumentsException;
}
