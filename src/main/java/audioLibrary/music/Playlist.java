package audioLibrary.music;

import audioLibrary.exceptions.InvalidPlaylistNameException;
import audioLibrary.exceptions.InvalidUserTypeException;
import audioLibrary.user.User;
import audioLibrary.user.UserType;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class Playlist {
    private static int idCounter = 1;
    private Integer id;
    private String name;
    private final Collection<Song> songs;
    //private final User user;
    //private static final Gson gson = new Gson();

    public Playlist(String name) {
        this.id = idCounter++;
        this.name = name;
        this.songs = new HashSet<>();
        //this.user = user;
    }

    public static void setIdCounter(int value) {
        idCounter = value;
    }

    /*public void createPlaylist(String name) throws InvalidUserTypeException, InvalidPlaylistNameException {
        if (user.getType() == UserType.Anonymous)
            throw new InvalidUserTypeException();

        for (Playlist playlist : user.getPlaylists()) {
            if (playlist.getName().equals(name)) {
                throw new InvalidPlaylistNameException();
            }
        }
        //Playlist newPlaylist = new Playlist(name, user);
        user.addInPlaylists(this);

        String jsonFilePath = "src/main/java/audioLibrary/music/Playlists/" + user.getUsername() + "_playlists.json";
        try (FileWriter writer = new FileWriter(jsonFilePath)) {
            gson.toJson(user.getPlaylists(), writer);
            System.out.println("Playlist " + name + " was created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
