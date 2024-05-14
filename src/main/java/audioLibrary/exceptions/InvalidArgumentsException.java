package audioLibrary.exceptions;

public class InvalidArgumentsException extends Exception{
    public InvalidArgumentsException(){
        super("Username or password is invalid. Please try again!");
    }
    public InvalidArgumentsException(String message){
        super(message);
    }
}