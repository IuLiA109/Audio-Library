package audioLibrary.music;

import audioLibrary.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.rmi.server.UID;
import java.text.SimpleDateFormat;
import java.util.*;

public class Playlist {
    private static int idCounter = 1;
    private Integer id;
    private String name;
    private Collection<Song> songs;

    public Playlist(String name) {
        this.id = idCounter++;
        this.name = name;
        this.songs = new HashSet<>();
    }

    public static void setIdCounter(int value) {
        idCounter = value;
    }

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

    /**
     * Exports the playlist to the specified format for the given user.
     *
     * @param format   the format in which the playlist will be exported
     * @param username the username of the user who owns the playlist
     */
    public void exportPlaylist(String format, String username){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String currentDate = dateFormat.format(new Date());
        String path = "src/main/java/audioLibrary/export/";
        String fileName = "export_" + username + "_" + this.name + "_" + currentDate + "." + format;
        path = path + fileName;
        if(format.equals("json"))
            try {
                Utils.writeInJson(path, new ArrayList<>(songs), this.name);
                System.out.println("Playlist exported successfully to file: " + fileName);
                return;
            }
            catch (IOException e){
                System.out.println(e.getMessage());
                return;
            }
        if(format.equals("csv"))
            try {
                Utils.writeInCsv(path, songs, this.name);
                System.out.println("Playlist exported successfully to file: " + fileName);
                return;
            }
            catch (IOException e){
                System.out.println(e.getMessage());
                return;
            }

        try (FileWriter writer = new FileWriter(path)) {
            writer.write("Playlist: " + this.name + "\n");
            writer.write("Name, Author, Release Year\n");

            for (Song song : this.songs) {
                writer.write(song.getName() + ", " + song.getAuthor() + ", " + song.getYear() + "\n");
            }

            System.out.println("Playlist exported successfully to file: " + fileName);
        } catch (IOException e) {
            System.out.println("Error exporting playlist: " + e.getMessage());
        }
    }

    @Override
    public String toString(){
        return this.name + " [ID: " + this.id + "]";
    }
}
