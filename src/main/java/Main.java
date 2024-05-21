//package org.example;

import audioLibrary.command.*;
import audioLibrary.exceptions.InvalidArgumentsException;
import audioLibrary.exceptions.InvalidCommandException;
import audioLibrary.exceptions.InvalidPlaylistNameException;
import audioLibrary.exceptions.InvalidUserTypeException;
import audioLibrary.music.Library;
import audioLibrary.music.PlaylistManager;
import audioLibrary.sql.SqlManager;
import audioLibrary.user.*;
//import main.java.audioLibrary.command.*;
//import main.java.audioLibrary.user.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//javac Main.java
//java Main

public class Main {

    private static String[] trimInput(String input) {
        String[] parts = input.split("\" "); //input.split("(?<=\") ");
        List<String> result = new ArrayList<>();
        for (String part : parts) {
            part = part.trim();
            if(!part.isEmpty() && !part.equals("create song"))
                result.add(part);
        }
        return result.toArray(new String[0]);
    }

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/laborator";
        String username = "student";
        String password = "student";

        SqlManager manager = new SqlManager(url, username, password);
        //manager.createAccountTable();
        //manager.deleteTable("Account");
        ///*

        Scanner scanner = new Scanner(System.in);
        String input;
        User user = new AnonymousUser();
        User administrator = new Administrator("admin", "admin");
        Library library = Library.getInstance("C:/Users/Iulia/Desktop/Proiect PAO/AudioLibrary/src/main/java/audioLibrary/music/Library.csv");
        //Command com = null;
        PlaylistManager playlistManager =  new PlaylistManager(user);

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            while (true) {
                input = scanner.nextLine();
                String[] parts = input.split("\\s+");
                Command com = null;

                if (parts[0].equals("login")) {
                    try {
                        com = new LoginCommand(user);
                    } catch (InvalidUserTypeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                else if (parts[0].equals("register")) {
                    try {
                        com = new RegisterCommand(user);
                    } catch (InvalidUserTypeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                else if (parts[0].equals("logout")) {
                    try {
                        com = new LogoutCommand(user);
                    } catch (InvalidUserTypeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                else if (parts[0].equals("promote")) {
                    try {
                        com = new PromoteCommand(user);
                    } catch (InvalidUserTypeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                else if (parts[0].equals("create") && parts[1].equals("song")) {
                    try {
                        library.createSong(input, user);
                    } catch (InvalidUserTypeException | InvalidCommandException e) {
                        System.out.println(e.getMessage());
                    }
                }
                else if (parts[0].equals("create") && parts[1].equals("playlist")) {
                    try {
                        playlistManager.createPlaylist(parts);
                    } catch (InvalidUserTypeException | InvalidPlaylistNameException | InvalidCommandException e) {
                        System.out.println(e.getMessage());
                    }
                }
                else if (parts[0].equals("exit")) break;

                if(com != null)
                    try {
                        user = com.execute(parts, connection, manager);
                        playlistManager.setUser(user);
                    } catch (InvalidArgumentsException | InvalidCommandException e) {
                        System.out.println(e.getMessage());
                    }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //library.listSongs();
        scanner.close();
    }
}