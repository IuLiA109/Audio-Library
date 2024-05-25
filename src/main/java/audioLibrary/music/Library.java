package audioLibrary.music;

import audioLibrary.Utils;
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
    private  Map<String, List<Song>> songsByName = new HashMap<>();
    private  Map<String, List<Song>> songsByAuthor = new HashMap<>();
    private final String csvFilePath ;

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

    /**
     * Reads song data from the CSV file and populates the library.
     */
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

                List<Song> songList = songsByName.getOrDefault(song.getName(), new ArrayList<>());
                songList.add(song);
                songsByName.put(song.getName(), songList);

                songList = songsByAuthor.getOrDefault(song.getAuthor(), new ArrayList<>());
                songList.add(song);
                songsByAuthor.put(song.getAuthor(), songList);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes song data to the CSV file.
     *
     * @param song the song to be written to the CSV file
     * @throws IOException if an I/O error occurs while writing to the file
     */
    private void writeCSV(Song song) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath, true))) {
            String[] songData = {String.valueOf(song.getId()), song.getName(), song.getAuthor(), String.valueOf(song.getYear())};
            writer.writeNext(songData);

            songsById.put(song.getId(), song);

            List<Song> songList = songsByName.getOrDefault(song.getName(), new ArrayList<>());
            songList.add(song);
            songsByName.put(song.getName(), songList);

            songList = songsByAuthor.getOrDefault(song.getAuthor(), new ArrayList<>());
            songList.add(song);
            songsByAuthor.put(song.getAuthor(), songList);
        }
    }

    /*private String[] trimInput(String input) {
        String[] parts = input.split("\" ");
        List<String> result = new ArrayList<>();
        for (String part : parts) {

            part = part.trim();
            if(!part.isEmpty() && !part.equals("create song"))
                result.add(part);
        }
        return result.toArray(new String[0]);
    }*/

    /**
     * Creates a new song based on the input command.
     *
     * @param input the input command for creating the song
     * @param user  the user attempting to create the song
     * @throws InvalidUserTypeException if the user is not an administrator
     * @throws InvalidCommandException  if the input command is invalid
     */
    public void createSong(String input, User user) throws InvalidUserTypeException, InvalidCommandException {
        if (user.getType() != UserType.Administrator)
            throw new InvalidUserTypeException();

        //String [] args = trimInput(input);
        String [] args = Utils.trimInput(input, "create song ");

        if (args.length != 3)
            throw new InvalidCommandException();
        else{
            //args[0] = args[0].substring(12);
            if(!args[0].startsWith("\"") || args[0].length() == 1 || !args[1].startsWith("\"") || args[1].length() == 1 || !args[2].matches("[0-9]+"))
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
            //System.out.println("Failed to write the song to the CSV file.");
        }
        System.out.println("Added " + name + " by " + author + " to the library");
    }

    public void listSongs(){
        for (Song song : songs) {
            System.out.println(song);
        }
    }

    /**
     * Searches for songs based on the specified criterion and displays the results.
     *
     * @param parts the parts of the input command
     * @param input the input command for searching songs
     * @param user  the user attempting to search for songs
     * @throws InvalidCommandException  if the input command is invalid
     * @throws InvalidUserTypeException if the user is not authenticated
     */
    public void searchSong(String[] parts, String input, User user) throws InvalidCommandException, InvalidUserTypeException{
        if (user.getType() == UserType.Anonymous)
            throw new InvalidUserTypeException();

        if(parts.length < 3)
            throw new InvalidCommandException();
        String criterionType = parts[1];
        Integer pageNumber = 1;

        parts = Utils.trimInput(input, "search " + criterionType + " ");

        String criterion;
        if(parts.length > 2)
            throw new InvalidCommandException();

        if(parts[0].startsWith("\"")) criterion = parts[0].substring(1);
        else throw new InvalidCommandException();

        if(parts.length == 2){
            if(parts[1].matches("\\d+"))
                pageNumber = Integer.parseInt(parts[1]);
            else throw new InvalidCommandException();
        }
        else {
            criterion = criterion.substring(0, criterion.length() - 1);
        }

        Map<String, List<Song>> songsMap;
        if(criterionType.equals("author"))
            songsMap = this.songsByAuthor;
        else if(criterionType.equals("name"))
            songsMap = this.songsByName;
        else throw new InvalidCommandException();

        List<Song> result = new ArrayList<>();
        for (Map.Entry<String, List<Song>> entry : songsMap.entrySet()) {
            String songCriterio = entry.getKey();
            List<Song> songList = entry.getValue();
            if(songCriterio.startsWith(criterion))
                result.addAll(songList);
        }

        Integer pageSize = 5;
        Utils.paginate(result, pageSize, pageNumber);
        Integer pages = (int) Math.ceil((double) result.size() / pageSize);
        if(pages > pageNumber) {
            System.out.println("'search " + criterionType + " \"" + criterion + "\" " + (pageNumber + 1) + "'");
        }
    }

    public boolean containsSongWithId(int id) {
        return songsById.containsKey(id);
    }

    public Map<Integer, Song> getSongsById(){
        return songsById;
    }
}
