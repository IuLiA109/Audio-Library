//package org.example;

import audioLibrary.command.*;
import audioLibrary.exceptions.InvalidArgumentsException;
import audioLibrary.exceptions.InvalidUserTypeException;
import audioLibrary.sql.SqlManager;
import audioLibrary.user.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
//javac Main.java
//java Main

public class Main {
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

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            while (true) {
                input = scanner.nextLine();
                String[] parts = input.split("\\s+");
                if (parts[0].equals("login")) {
                    try {
                        LoginCommand log = new LoginCommand(user);
                        try {
                            user = log.execute(parts, connection, manager);
                            //sigleton
                        } catch (InvalidArgumentsException e) {
                            System.out.println(e.getMessage());
                        }
                    } catch (InvalidUserTypeException e) {
                        System.out.println(e.getMessage());
                    }

                }
                else if (parts[0].equals("register")) {
                    try {
                        RegisterCommand reg = new RegisterCommand(user);
                        try {
                            user = reg.execute(parts, connection, manager);
                        } catch (InvalidArgumentsException e) {
                            System.out.println(e.getMessage());
                        }
                    } catch (InvalidUserTypeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                else if (parts[0].equals("exit")) break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    scanner.close();

    //*/

        /*
        Scanner scanner = new Scanner(System.in);
        String input;
        AuthenticationService authService = new AuthenticationService();

        while (true) {
            input = scanner.nextLine();
            String[] parts = input.split("\\s+");

            if (parts[0].equals("login")) {
                authService.login(parts[1], parts[2]);
            } else if (parts[0].equals("register")) {
                authService.register(parts[1], parts[2]);
            } else if (parts[0].equals("logout")) {
                authService.logout();
            } else if (input.equalsIgnoreCase("exit")) {
                break;
            } else {
                System.out.println("Comanda necunoscută. Te rog să introduci buna, rea sau exit.");
            }
        }

        scanner.close();
        */

        /*

        AuthenticationService authService = new AuthenticationService();
        boolean registrationSuccess = authService.register("john", "password123");
        authService.register("iulia", "parola1");
        authService.register("mia", "parola2");
        authService.register("iulia", "parola3");
        authService.register("milan", "parola2");

        //System.out.println("RegisteredUsers: ");
        //authService.printRegisteredUsers();

        //System.out.println("LoggedInUsers: ");
        //authService.printLoggedInUsers();

        authService.login("john", "incorrectPassword");
        authService.login("john", "password123");

        */


        /*
        System.out.println("Registration successful: " + registrationSuccess);

        boolean loginSuccess = authService.login("john", "password123");
        System.out.println("Login successful: " + loginSuccess);

        loginSuccess = authService.login("john", "incorrectPassword");
        System.out.println("Login successful: " + loginSuccess);

        boolean logoutSuccess = authService.logout("john");
        System.out.println("Logout successful: " + logoutSuccess);

        System.out.println("Is 'john' logged in? " + authService.isUserLoggedIn("john"));

        System.out.println("RegisteredUsers: ");
        authService.printRegisteredUsers();

        System.out.println("LoggedInUsers: ");
        authService.printLoggedInUsers();
        */

}
}