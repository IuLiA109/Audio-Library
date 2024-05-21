package audioLibrary.command;

import audioLibrary.exceptions.InvalidArgumentsException;
import audioLibrary.exceptions.InvalidCommandException;
import audioLibrary.exceptions.InvalidUserTypeException;
import audioLibrary.sql.SqlManager;
import audioLibrary.user.AuthenticatedUser;
import audioLibrary.user.User;
import audioLibrary.user.UserType;

import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
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
    public User execute(String[] args, Connection connection, SqlManager manager) throws InvalidArgumentsException, InvalidCommandException {
        if (args.length != 3)
            throw new InvalidCommandException();

        String username = args[1];
        String password = args[2];
        Integer nextId = manager.numberOfRows("Account") + 1;

        Integer userExists = manager.existsInTable(new String[]{"username"}, new String[]{args[1]}, "Account");

        if (userExists == 1) throw new InvalidArgumentsException("User with given username already exists! Please try again!");
        else if(userExists == 0) {
            manager.insertInfoAccount(nextId, username, password);
            System.out.println("Account created");

            String jsonFilePath = "src/main/java/audioLibrary/music/Playlists/" + username + "_playlists.json";
            File jsonFile = new File(jsonFilePath);
            try (FileWriter writer = new FileWriter(jsonFilePath)) {
                writer.write("[]");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return new AuthenticatedUser(username, password);
        }
        else throw new InvalidArgumentsException("PROBLEMA NOUA");
    }
}
