package audioLibrary.exceptions;

public class InvalidSongIdException extends Exception{
    public InvalidSongIdException(){
        super("Song with this id not found in library");
    }
}
