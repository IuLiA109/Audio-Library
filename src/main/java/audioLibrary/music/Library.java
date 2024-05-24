package audioLibrary.music;

import audioLibrary.exceptions.InvalidArgumentsException;
import audioLibrary.exceptions.InvalidCommandException;
import audioLibrary.exceptions.InvalidUserTypeException;
import audioLibrary.user.User;
import audioLibrary.user.UserType;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Library {
    private static Library instance;
    private  Collection<Song> songs;
    private  Map<Integer, Song> songsById = new HashMap<>();
    private  Map<String, Song> songsByName = new HashMap<>();
    private final String csvFilePath;

    private Library(String csvFilePath) {
        this.csvFilePath = csvFilePath;
        this.songs = new HashSet<>();
        readCSV();
    }

    public static synchronized Library getInstance(String csvFilePath) {
        if (instance == null) {
            instance = new Library(csvFilePath);
        }
        return instance;
    }

    private void readCSV() {
        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            String[] nextRecord;
            while ((nextRecord = reader.readNext()) != null) {
                if (nextRecord[0].equals("ID")) {
                    continue;
                }
                Song song = new Song(nextRecord[1], nextRecord[2], Integer.parseInt(nextRecord[3]));
                songs.add(song);
                songsById.put(song.getId(), song);
                songsByName.put(song.getName(), song);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    private void writeCSV(Song song) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath, true))) {
            String[] songData = {String.valueOf(song.getId()), song.getName(), song.getAuthor(), String.valueOf(song.getYear())};
            writer.writeNext(songData);
        }
    }

    private String[] trimInput(String input) {
        String[] parts = input.split("\" ");
        List<String> result = new ArrayList<>();
        for (String part : parts) {

            part = part.trim();
            if(!part.isEmpty() && !part.equals("create song"))
                result.add(part);
        }
        return result.toArray(new String[0]);
    }

    //public void createSong(String name, String author, String year, User user) throws InvalidUserTypeException{
    public void createSong(String input, User user) throws InvalidUserTypeException, InvalidCommandException {
        if (user.getType() != UserType.Administrator)
            throw new InvalidUserTypeException();

        String [] args = trimInput(input);

        if (args.length != 3)
            throw new InvalidCommandException();
        else{
            args[0] = args[0].substring(12);
            if(!args[0].startsWith("\"") || args[0].length()==1 || !args[1].startsWith("\"") || args[1].length()==1 || !args[2].matches("[0-9]+"))
                throw new InvalidCommandException();
            args[0] = args[0].substring(1);
            args[1] = args[1].substring(1);
        }

        String name = args[0];
        String author = args[1];
        String year = args[2];

        Song song = new Song(name, author, Integer.parseInt(year));
        if (!songs.add(song)) {
            System.out.println("This song is already part of the library!");
            return;
        }
        try {
            writeCSV(song);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to write the song to the CSV file.");
        }
        System.out.println("Added " + name + " by " + author + " to the library");
    }

    public void listSongs(){
        for (Song song : songs) {
            System.out.println(song);
        }
    }

    public boolean containsSongWithId(int id) {
        return songsById.containsKey(id);
    }

    public Map<Integer, Song> getSongsById(){
        return songsById;
    }
}
