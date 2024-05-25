//package org.example;

import audioLibrary.Pair;
import audioLibrary.Utils;
import audioLibrary.command.*;
import audioLibrary.exceptions.*;
import audioLibrary.music.Library;
import audioLibrary.music.PlaylistManager;
import audioLibrary.sql.SqlManager;
import audioLibrary.user.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/laborator";
        String username = "student";
        String password = "student";

        SqlManager manager = new SqlManager(url, username, password);
        //manager.createAccountTable();
        //manager.deleteTable("Account");

        Scanner scanner = new Scanner(System.in);
        String input;
        User user = new AnonymousUser();
        User administrator = new Administrator("admin", "admin");
        Library library = Library.getInstance("src/main/java/audioLibrary/music/Library.csv");
        PlaylistManager playlistManager =  new PlaylistManager(user);

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            while (true) {
                input = scanner.nextLine();
                String[] parts = input.split("\\s+");

                if(input.equals("exit")) break;

                Pair<Command, Boolean> pair = CommandManager.execute(input, parts, user, library, playlistManager);
                Command com = pair.getKey();
                Boolean success = pair.getValue();

                if(com != null)
                    try {
                        User previousUser = user;
                        user = com.execute(parts, connection, manager);
                        previousUser.addAuditSuccess(success);
                        playlistManager.setUser(user);
                    } catch (InvalidArgumentsException | InvalidCommandException e) {
                        success = false;
                        user.addAuditSuccess(success);
                        System.out.println(e.getMessage());
                    }
                else user.addAuditSuccess(success);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        scanner.close();
    }
}