package audioLibrary.music;

import audioLibrary.Utils;
import audioLibrary.exceptions.*;
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

    private void updatePlaylistJson(Playlist playlist) {
        String jsonFilePath = "src/main/java/audioLibrary/music/Playlists/" + user.getUsername() + "_playlists.json";
        try (FileWriter writer = new FileWriter(jsonFilePath)) {
            gson.toJson(user.getPlaylists(), writer);
            writer.write("\n");
            //System.out.println("Playlist updated successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUser(User user){
        this.user = user;
    }

    public void printPlaylists(){
        for(Playlist p: user.getPlaylists()){
            System.out.println(p.toString());
        }
    }

    public Playlist getPlaylistByName(String playlistName) {
        for (Playlist playlist : user.getPlaylists()) {
            if (playlist.getName().equalsIgnoreCase(playlistName)) {
                return playlist;
            }
        }
        return null;
    }

    public Playlist getPlaylistById(Integer playlistId) {
        for (Playlist playlist : user.getPlaylists()) {
            if (playlist.getId().equals(playlistId)) {
                return playlist;
            }
        }
        return null;
    }

    public void addSong(String [] parts) throws InvalidCommandException, InvalidUserTypeException, InvalidPlaylistNameException, InvalidSongIdException, SongAlreadyInPlaylistException {
        if (user.getType() == UserType.Anonymous)
            throw new InvalidUserTypeException();

        if (parts[1].equals("byName") || parts[1].equals("byId")) {
            String criterion = parts[1];
            Library library = Library.getInstance("src/main/java/audioLibrary/music/Library.csv");

            String a = "";
            for (String part : parts) {
                a = a + part + " ";
            }
            //System.out.println(a);

            parts = Utils.trimInput(a, "add " + criterion + " ");
            //parts[0] = parts[0].substring(11);

            /*for (String part : parts) {
                System.out.println(part);
            }
            */
            if (parts.length != 2 || !parts[0].startsWith("\"") || parts[0].length() == 1)
                throw new InvalidCommandException();

            String playlistIndicater = parts[0].substring(1);

            ArrayList<Integer> songsId = new ArrayList<>();

            for (int i = 0; i < parts[1].length(); i++) {
                Character c = parts[1].charAt(i);
                if (!Character.isDigit(c) && !Character.isWhitespace(c))
                    throw new InvalidCommandException();
            }

            Playlist playlist;
            if (criterion.equals("byName"))
                playlist = getPlaylistByName(playlistIndicater);
            else
                playlist = getPlaylistById(Integer.parseInt(playlistIndicater));

            if (playlist == null) throw new InvalidPlaylistNameException("The desired playlist does not exist");

            String[] songsIdString = parts[1].split(" ");

            for (String number : songsIdString) {
                if(!library.containsSongWithId(Integer.parseInt(number)))
                    throw new InvalidSongIdException();
            }

            for (String number : songsIdString) {
                Boolean success = playlist.addSongToPlaylist(Integer.parseInt(number));
                if (!success)
                    throw new SongAlreadyInPlaylistException(Integer.parseInt(number));
            }

            updatePlaylistJson(playlist);

        }
        else throw new InvalidCommandException();

        /*
        parts =  a.split("\" ");

        for(String part: parts){
            part = part.trim();
        }

        if (parts.length != 3)
            throw new InvalidCommandException();


        ArrayList<Integer> songsId = new ArrayList<>();

        for(int i = 0; i < parts[2].length(); i++){
            Character c = parts[2].charAt(i);
            if(!Character.isDigit(c) && !Character.isWhitespace(c))
                throw new InvalidCommandException();
        }

         */

        return;
/*
        if(parts[1].equals("byName")){
            if(parts[2].startsWith("\"")) {
                String playlistName = parts[2].substring(1);
                int cnt = 2;
                if (parts[2].endsWith("\"")){
                    playlistName = playlistName.substring(0, playlistName.length() - 1);
                    System.out.println(playlistName);
                }
                else {
                    System.out.println(playlistName);
                    while (cnt < parts.length - 1 && !parts[cnt].endsWith("\"")) {
                        playlistName += parts[cnt];
                        cnt++;
                    }
                }
                    System.out.println(playlistName);
                    if (cnt == parts.length - 1) throw new InvalidCommandException();
                    else if (cnt > 2) playlistName += parts[cnt].substring(0, parts[cnt].length() - 1);
                    System.out.println(playlistName);

            }
        }
        else if(parts[1].equals("byId")){

        }
        else throw new InvalidCommandException();
*/
    }

}
