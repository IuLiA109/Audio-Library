package audioLibrary.exceptions;

public class InvalidUserTypeException extends Exception{
    public InvalidUserTypeException(){
        super("You do not have the permission to use this!");
    }
    public InvalidUserTypeException(String message){
        super(message);
    }
}
