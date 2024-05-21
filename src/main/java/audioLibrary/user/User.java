package audioLibrary.user;

import audioLibrary.exceptions.InvalidPlaylistNameException;
import audioLibrary.exceptions.InvalidUserTypeException;
import audioLibrary.music.Playlist;

import java.util.List;

public interface User {
    String getUsername();
    String getPassword();
    List<Playlist> getPlaylists();
    void addInPlaylists(Playlist newPlaylist);
    //void loadPlaylists();
    //void createPlaylist(String name) throws InvalidUserTypeException, InvalidPlaylistNameException;
    UserType getType();
}
