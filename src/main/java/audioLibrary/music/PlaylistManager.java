package audioLibrary.music;

import audioLibrary.exceptions.InvalidCommandException;
import audioLibrary.exceptions.InvalidPlaylistNameException;
import audioLibrary.exceptions.InvalidUserTypeException;
import audioLibrary.user.User;
import audioLibrary.user.UserType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlaylistManager {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private User user;

    public PlaylistManager(User user) {
        this.user = user;
    }

    public void createPlaylist(String [] parts) throws InvalidUserTypeException, InvalidPlaylistNameException, InvalidCommandException {
        if (parts.length == 2)
            throw new InvalidCommandException();

        String playlistName = "";
        for(int i = 2; i < parts.length; i++){
            playlistName = playlistName + parts[i] + " ";
        }
        playlistName = playlistName.substring(0, playlistName.length() - 1);

        if (user.getType() == UserType.Anonymous)
            throw new InvalidUserTypeException();

        for (Playlist playlist : user.getPlaylists()) {
            if (playlist.getName().equals(playlistName)) {
                throw new InvalidPlaylistNameException();
            }
        }
        Playlist newPlaylist = new Playlist(playlistName);
        user.addInPlaylists(newPlaylist);

        String jsonFilePath = "src/main/java/audioLibrary/music/Playlists/" + user.getUsername() + "_playlists.json";
        try (FileWriter writer = new FileWriter(jsonFilePath)) {
            gson.toJson(user.getPlaylists(), writer);
            writer.write("\n");
            System.out.println("Playlist " + playlistName + " was created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Playlist> loadPlaylists() {
        List<Playlist> playlists;
        String jsonFilePath = "src/main/java/audioLibrary/music/Playlists/" + user.getUsername() + "_playlists.json";
        try (FileReader reader = new FileReader(jsonFilePath)) {
            Type playlistListType = new TypeToken<ArrayList<Playlist>>() {
            }.getType();
            playlists = gson.fromJson(reader, playlistListType);
            if (playlists == null) {
                playlists = new ArrayList<>();
            }
            Playlist.setIdCounter(playlists.size() + 1);
        } catch (IOException e) {
            playlists = new ArrayList<>();
        }

        return playlists;
    }

    public void setUser(User user){
        this.user = user;
    }

}
