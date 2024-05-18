package audioLibrary.music;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Library {
    private static Library instance;
    private final Collection<Song> songs;
    private final Map<Integer, Song> songsById = new HashMap<>();
    private final Map<String, Song> songsByNameAndAuthor = new HashMap<>();
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

    public void createSong(String name, String author, String year) {
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
}
