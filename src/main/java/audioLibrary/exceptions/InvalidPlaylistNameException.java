package audioLibrary.exceptions;

public class InvalidPlaylistNameException extends Exception{
    public InvalidPlaylistNameException(){
        super("You already have a playlist with this name!");
    }
    public InvalidPlaylistNameException(String message){
        super(message);
    }
}
