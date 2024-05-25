/*package audioLibrary;
import audioLibrary.user.*;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {
    private Map<String, User> loggedInUsers;
    private Map<String, User> registeredUsers;

    public AuthenticationService() {
        loggedInUsers = new HashMap<>();
        registeredUsers = new HashMap<>();
    }

    public boolean login(String username, String password) {
        User user = registeredUsers.get(username);
        if (loggedInUsers.size() == 1) {
            System.out.println("cant run this command bc you are already logged in");
            return false;
        }
        if (user != null && user.getPassword().equals(password)) {
            loggedInUsers.put(username, user);
            System.out.println("You are successfully logged in");
            return true;
        }
        System.out.println("Username or password is invalid. Please try again!");
        return false;
    }

    public boolean logout() {
        if (loggedInUsers.size() == 0) {
            System.out.println("cant run this command bc you are not logged in");
            return false;
        }

        loggedInUsers.clear();
        System.out.println("Successfully logged out.");
        return true;
    }

    public boolean register(String username, String password) {
        if (loggedInUsers.size() == 1) {
            System.out.println("cant run this command bc you are already logged in");
            return false;
        }
        if (!registeredUsers.containsKey(username)) {
            User user = new AuthenticatedUser(username, password);
            registeredUsers.put(username, user);
            loggedInUsers.put(username, user);
            System.out.println("Successful registration");
            return true;
        }
        System.out.println("User with given username already exists! Please try again!");
        return false;
    }

    public boolean isUserLoggedIn(String username) {
        return loggedInUsers.containsKey(username);
    }

    public void printRegisteredUsers(){
        for (Map.Entry<String, User> entry : registeredUsers.entrySet()) {
            String username = entry.getKey();
            User user = entry.getValue();
            System.out.println("Username: " + username);
            System.out.println("Password: " + user.getPassword());
            System.out.println();
        }
    }

    public void printLoggedInUsers(){
        for (Map.Entry<String, User> entry : loggedInUsers.entrySet()) {
            String username = entry.getKey();
            User user = entry.getValue();
            System.out.println("Username: " + username);
            System.out.println("Password: " + user.getPassword());
            System.out.println();
        }
    }
}
*/
