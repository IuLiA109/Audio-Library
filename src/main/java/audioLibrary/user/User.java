package audioLibrary.user;

import audioLibrary.music.Playlist;

import java.util.List;

public interface User {
    String getUsername();
    String getPassword();
    List<Playlist> getPlaylists();
    void addInPlaylists(Playlist newPlaylist);
    void addAudit(String input);
    void addAuditSuccess(Boolean success);
    UserType getType();
}
