package audioLibrary.user;

import audioLibrary.exceptions.InvalidPlaylistNameException;
import audioLibrary.exceptions.InvalidUserTypeException;
import audioLibrary.music.Playlist;
import audioLibrary.music.PlaylistManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

    public AbstractUser(String username, String password) {
        this.username = username;
        this.password = password;
        if (username != null) {
            this.playlists = new ArrayList<>();
            PlaylistManager manager = new PlaylistManager(this);
            playlists = manager.loadPlaylists();
        }
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public List<Playlist> getPlaylists(){
        return playlists;
    }
    @Override
    public void addInPlaylists(Playlist newPlaylist){
        playlists.add(newPlaylist);
    }

}
