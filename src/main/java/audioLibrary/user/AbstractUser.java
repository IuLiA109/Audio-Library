package audioLibrary.user;

import audioLibrary.exceptions.InvalidPlaylistNameException;
import audioLibrary.exceptions.InvalidUserTypeException;
import audioLibrary.music.Playlist;
import audioLibrary.music.PlaylistManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractUser implements User {
    private final String username;
    private final String password;
    private List<Playlist> playlists;
    private static final Gson gson = new Gson();

    /**
     * Constructs a new AbstractUser with the specified username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     */
    public AbstractUser(String username, String password) {
        this.username = username;
        this.password = password;
        //if (username != null) {
        if(getType() != UserType.Anonymous){
            this.playlists = new ArrayList<>();
            PlaylistManager manager = new PlaylistManager(this);
            playlists = manager.loadPlaylists();
        }
    }

    /**
     * Gets the username of the user.
     *
     * @return the username of the user
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password of the user.
     *
     * @return the password of the user
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Gets the list of playlists of the user.
     *
     * @return the list of playlists
     */
    @Override
    public List<Playlist> getPlaylists(){
        return playlists;
    }

    /**
     * Adds a new playlist to the user's list of playlists.
     *
     * @param newPlaylist the new playlist to be added
     */
    @Override
    public void addInPlaylists(Playlist newPlaylist){
        playlists.add(newPlaylist);
    }

    /**
     * Adds an audit entry with the given input command for the user, if the user is not anonymous.
     *
     * @param input the input command to be added to the audit
     */
    public void addAudit(String input){
        if(this.getType() != UserType.Anonymous) {
            String fileName = "src/main/java/audioLibrary/audit/" + username + "_audit.myformat";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
                writer.write(input);
                //writer.newLine();
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file: " + e.getMessage());
            }
        }
    }

    /**
     * Adds an audit entry indicating whether the command execution was successful or unsuccessful,
     * if the user is not anonymous.
     *
     * @param success true if the command was successful, false otherwise
     */
    public void addAuditSuccess(Boolean success){
        if(this.getType() != UserType.Anonymous) {
            String toWrite;
            if(success.equals(true))
                toWrite = "    -    successful";
            else toWrite = "    -    unsuccessful";
            String fileName = "src/main/java/audioLibrary/audit/" + username + "_audit.myformat";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
                writer.write(toWrite);
                writer.newLine();
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file: " + e.getMessage());
            }
        }
    }

}
