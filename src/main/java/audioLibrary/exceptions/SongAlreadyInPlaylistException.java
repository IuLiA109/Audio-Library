package audioLibrary.exceptions;

public class SongAlreadyInPlaylistException extends Exception{
    public SongAlreadyInPlaylistException(Integer id){
        super("Song with id " + id + " already exists in this playlist");
    }
}
