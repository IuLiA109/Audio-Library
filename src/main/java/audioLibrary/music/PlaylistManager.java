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
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private User user;

    public PlaylistManager(User user) {
        this.user = user;
    }

    /**
     * Creates a new playlist with the given name for the user.
     *
     * @param parts the array containing the command and the name of the playlist
     * @throws InvalidUserTypeException     if the user is not authenticated
     * @throws InvalidPlaylistNameException if the playlist name already exists
     * @throws InvalidCommandException      if the command is invalid
     */
    public void createPlaylist(String[] parts) throws InvalidUserTypeException, InvalidPlaylistNameException, InvalidCommandException {
        if (parts.length == 2)
            throw new InvalidCommandException();

        String playlistName = "";
        for (int i = 2; i < parts.length; i++) {
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
        try{
            Utils.writeInJson(jsonFilePath, user.getPlaylists());
            System.out.println("Playlist " + playlistName + " was created successfully!");
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
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

    private void updatePlaylistsJson() {
        String jsonFilePath = "src/main/java/audioLibrary/music/Playlists/" + user.getUsername() + "_playlists.json";
        try (FileWriter writer = new FileWriter(jsonFilePath)) {
            gson.toJson(user.getPlaylists(), writer);
            writer.write("\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void printPlaylists() {
        for (Playlist p : user.getPlaylists()) {
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

    /**
     * Adds songs to a playlist specified by name or ID.
     *
     * @param parts the array containing the command and parameters
     * @throws InvalidCommandException        if the command is invalid
     * @throws InvalidUserTypeException       if the user is not authenticated
     * @throws InvalidPlaylistNameException   if the playlist name or ID is invalid
     * @throws InvalidSongIdException         if the song ID is invalid
     * @throws SongAlreadyInPlaylistException if the song is already in the playlist
     */
    public void addSong(String[] parts) throws InvalidCommandException, InvalidUserTypeException, InvalidPlaylistNameException, InvalidSongIdException, SongAlreadyInPlaylistException {
        if (user.getType() == UserType.Anonymous)
            throw new InvalidUserTypeException();

        if (parts[1].equals("byName") || parts[1].equals("byId")) {
            String criterio = parts[1];
            Library library = Library.getInstance("src/main/java/audioLibrary/music/Library.csv");

            String a = "";
            for (String part : parts) {
                a = a + part + " ";
            }

            parts = Utils.trimInput(a, "add " + criterio + " ");

            if (parts.length != 2 || !parts[0].startsWith("\"") || parts[0].length() == 1)
                throw new InvalidCommandException();

            String playlistIndicater = parts[0].substring(1);

            //ArrayList<Integer> songsId = new ArrayList<>();

            for (int i = 0; i < parts[1].length(); i++) {
                Character c = parts[1].charAt(i);
                if (!Character.isDigit(c) && !Character.isWhitespace(c))
                    throw new InvalidCommandException();
            }

            Playlist playlist;
            if (criterio.equals("byName"))
                playlist = getPlaylistByName(playlistIndicater);
            else
                playlist = getPlaylistById(Integer.parseInt(playlistIndicater));

            if (playlist == null) throw new InvalidPlaylistNameException("The desired playlist does not exist");

            String[] songsIdString = parts[1].split(" ");

            for (String number : songsIdString) {
                if (!library.containsSongWithId(Integer.parseInt(number)))
                    throw new InvalidSongIdException();
            }

            for (String number : songsIdString) {
                Boolean success = playlist.addSongToPlaylist(Integer.parseInt(number));
                if (!success)
                    throw new SongAlreadyInPlaylistException(Integer.parseInt(number));
            }

            updatePlaylistsJson();
            System.out.println("The songs have been successfully added to the playlist!");

        } else throw new InvalidCommandException();
    }

    /**
     * Exports a playlist specified by name or ID to the specified format.
     *
     * @param parts the array containing the command and parameters
     * @throws InvalidUserTypeException     if the user is not authenticated
     * @throws InvalidCommandException      if the command is invalid
     * @throws InvalidPlaylistNameException if the playlist name or ID is invalid
     */
    public void exportPlaylist(String [] parts) throws InvalidUserTypeException, InvalidCommandException, InvalidPlaylistNameException{
        Playlist playlist;
        Integer playlistId;
        String playlistName;
        String format;

        if (user.getType() == UserType.Anonymous)
            throw new InvalidUserTypeException();

        if(parts.length < 4)
            throw new InvalidCommandException();

        String playlistIndicator = parts[2]; //name or id
        for(int i = 3; i < parts.length - 1; i++){
            playlistIndicator = playlistIndicator + " " + parts[i];
        }
        format = parts[parts.length-1];

        if(playlistIndicator.matches("\\d+")) {
            playlistId = Integer.parseInt(playlistIndicator);
            playlist = getPlaylistById(playlistId);
        }
        else {
            playlistName = playlistIndicator;
            playlist = getPlaylistByName(playlistName);
        }
        if (playlist == null) throw new InvalidPlaylistNameException("The desired playlist does not exist");



        playlist.exportPlaylist(format, user.getUsername());
    }

    /**
     * Lists the playlists of the authenticated user.
     *
     * @param parts the array containing the command and parameters
     * @throws InvalidCommandException if the command is invalid
     * @throws InvalidUserTypeException if the user is not authenticated
     */
    public void listPlaylists(String[] parts) throws InvalidCommandException, InvalidUserTypeException {
        Integer pageNumber = 1;
        if (parts.length > 3)
            throw new InvalidCommandException();
        if (parts.length == 3 && !parts[2].matches("\\d+"))
            throw new InvalidCommandException();
        if (parts.length == 3) pageNumber = Integer.parseInt(parts[2]);
        if (user.getType() == UserType.Anonymous)
            throw new InvalidUserTypeException();

        Integer pageSize = 5;
        Utils.paginate(user.getPlaylists(), pageSize, pageNumber);
        Integer pages = (int) Math.ceil((double) user.getPlaylists().size() / pageSize);
        if(pages > pageNumber) {
            System.out.println("'list playlists " + (pageNumber + 1) + "'");
        }

        /*List<Playlist> playlists = user.getPlaylists();
        Integer itemsPerPage = 5;
        Integer maxItemsPerPage = 5;
        Integer numberOfPlaylists = playlists.size();
        Integer pages = (int) Math.ceil((double) numberOfPlaylists / itemsPerPage);

        if(pageNumber > pages || pageNumber.equals(0)) {
            System.out.println("Page 0 of 0 (" + maxItemsPerPage +" items per page):");
            return;
        }

        System.out.println("Page " + pageNumber + " of " + pages + " (" + maxItemsPerPage + " items per page):");
        if(itemsPerPage > (numberOfPlaylists - (pageNumber-1) * itemsPerPage)) itemsPerPage = numberOfPlaylists - (pageNumber-1) * itemsPerPage;
        for(int i = 0; i < itemsPerPage; i++){
            Playlist playlist = playlists.get((pageNumber-1) * maxItemsPerPage + i);
            System.out.print(((pageNumber-1) * maxItemsPerPage + i + 1) + ".");
            System.out.println(playlist.toString());
        }

        if(pages > pageNumber) {
            System.out.println("To return the next page run the query as follows:");
            System.out.println("'list playlists " + (pageNumber + 1) + "'");
        }*/

    }

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
        }


         */

    //return;
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
