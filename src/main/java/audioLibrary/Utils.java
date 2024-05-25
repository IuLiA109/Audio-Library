package audioLibrary;

import audioLibrary.exceptions.InvalidCommandException;
import audioLibrary.exceptions.InvalidUserTypeException;
import audioLibrary.music.Playlist;
import audioLibrary.music.Song;
import audioLibrary.user.User;
import audioLibrary.user.UserType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Utils {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Trims the input command and splits it into parts after quotation mark + space.
     *
     * @param input   the input command to be trimmed and split
     * @param command the command to be removed from the input
     * @return an array of parts representing the trimmed input command
     */
    public static String[] trimInput(String input, String command) {
        String[] parts = input.split("\" ");
        List<String> result = new ArrayList<>();
        for (String part : parts) {

            part = part.trim();
            if(!part.isEmpty())
                result.add(part);
        }
        result.set(0, result.getFirst().substring(command.length()));
        return result.toArray(new String[0]);
    }

    /**
     * Paginates a list of items and displays them.
     *
     * @param items       the list of items to be paginated
     * @param pageSize    the number of items per page
     * @param pageNumber  the current page number
     * @param <T>         the type of items in the list
     */
    public static <T> void paginate(List<T> items, Integer pageSize, Integer pageNumber) {
        //List<Playlist> playlists = user.getPlaylists();
        Integer itemsPerPage = pageSize;
        Integer maxItemsPerPage = pageSize;
        Integer numberOfPlaylists = items.size();
        Integer pages = (int) Math.ceil((double) numberOfPlaylists / itemsPerPage);

        if(pageNumber > pages || pageNumber.equals(0)) {
            System.out.println("Page 0 of 0 (" + maxItemsPerPage +" items per page):");
            return;
        }

        System.out.println("Page " + pageNumber + " of " + pages + " (" + maxItemsPerPage + " items per page):");
        if(itemsPerPage > (numberOfPlaylists - (pageNumber-1) * itemsPerPage)) itemsPerPage = numberOfPlaylists - (pageNumber-1) * itemsPerPage;
        for(int i = 0; i < itemsPerPage; i++){
            T item = items.get((pageNumber-1) * maxItemsPerPage + i);
            System.out.print(((pageNumber-1) * maxItemsPerPage + i + 1) + ".");
            System.out.println(item.toString());
        }

        if(pages > pageNumber) {
            System.out.println("To return the next page run the query as follows:");
        }
    }

    /**
     * Writes a list of objects to a JSON file.
     *
     * @param jsonFilePath the path of the JSON file to write
     * @param objects      the list of objects to write
     * @param <T>          the type of objects in the list
     * @throws IOException if an I/O error occurs while writing to the file
     */
    public static <T> void writeInJson(String jsonFilePath, List<T> objects) throws IOException{
        try (FileWriter writer = new FileWriter(jsonFilePath)) {

            gson.toJson(objects, writer);
            writer.write("\n");
        }
    }

    /**
     * Writes a list of objects to a JSON file with a specified playlist name.
     *
     * @param jsonFilePath the path of the JSON file to write
     * @param objects      the list of objects to write
     * @param playlistName the name of the playlist
     * @param <T>          the type of objects in the list
     * @throws IOException if an I/O error occurs while writing to the file
     */
    public static <T> void writeInJson(String jsonFilePath, List<T> objects, String playlistName) throws IOException{
        try (FileWriter writer = new FileWriter(jsonFilePath)) {
            writer.write("{\n");
            writer.write("\"name\": \"" + playlistName + "\",\n");
            writer.write("\"objects\": ");
            gson.toJson(objects, writer);
            writer.write("\n}");
        }
    }

    public static void writeInCsv(String csvFilePath, String [] data) throws IOException{
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath, true))) {
            writer.writeNext(data);
        }
    }

    /**
     * Writes a collection of songs to a CSV file with a specified playlist name.
     *
     * @param path         the path of the CSV file to write
     * @param songs        the collection of songs to write
     * @param playlistName the name of the playlist
     * @throws IOException if an I/O error occurs while writing to the file
     */
    public static void writeInCsv(String path, Collection<Song> songs, String playlistName) throws IOException{
        try (CSVWriter writer = new CSVWriter(new FileWriter(path, true))) {
            writer.writeNext(new String[]{"Name:", playlistName});

            for(Song song: songs) {
                String[] data = {String.valueOf(song.getId()), song.getName(), song.getAuthor(), String.valueOf(song.getYear())};
                writer.writeNext(data);
            }
        }
    }

    /**
     * Reads data from a custom format file.
     *
     * @param path     the path of the file to read
     * @param username the username associated with the file
     * @return a list of strings read from the file
     */
    public static List<String> readMyFormat(String path, String username){
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(username + "    -    " +line);
                //System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
        return lines;
    }

    /**
     * Audits user activity and paginates the audit logs.
     *
     * @param user  the user whose activity is being audited
     * @param parts an array containing the command and username
     * @throws InvalidUserTypeException if the user is not an administrator
     * @throws InvalidCommandException  if the command is invalid
     */
    public static void audit(User user, String[] parts) throws InvalidUserTypeException, InvalidCommandException{
        Integer pageNumber = 1;
        if(user.getType() != UserType.Administrator)
            throw new InvalidUserTypeException();
        if(parts.length > 3)
            throw new InvalidCommandException();
        if(parts.length == 3 && !parts[2].matches("\\d+"))
            throw new InvalidCommandException();
        if(parts.length == 3 && parts[2].matches("\\d+"))
            pageNumber = Integer.parseInt(parts[2]);

        String username = parts[1];
        String path = "src/main/java/audioLibrary/audit/" + username + "_audit.myformat";
        List<String> lines = readMyFormat(path, username);

        Integer pageSize = 5;
        paginate(lines, 5, pageNumber);
        Integer pages = (int) Math.ceil((double) lines.size() / pageSize);
        if(pages > pageNumber) {
            System.out.println("'audit " + username + " " + (pageNumber + 1) + "'");
        }
    }

}
