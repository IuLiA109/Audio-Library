package audioLibrary;

import java.util.ArrayList;
import java.util.List;

public class Utils {
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
            System.out.println("'list playlists " + (pageNumber + 1) + "'");
        }
    }
}
