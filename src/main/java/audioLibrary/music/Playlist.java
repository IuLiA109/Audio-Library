package audioLibrary.music;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class Playlist {
    private static int idCounter = 1;
    private Integer id;
    private String name;
    private Collection<Song> songs;
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

    public Boolean addSongToPlaylist(Integer songId) {
        Library library = Library.getInstance("src/main/java/audioLibrary/music/Library.csv");
        Song song = library.getSongsById().get(songId);
        if (!songs.contains(song)) {
            songs.add(song);
            return true;
        } else {
            return false;
        }
    }


    @Override
    public String toString(){
        return this.name + " [ID: " + this.id + "]";
    }
}
