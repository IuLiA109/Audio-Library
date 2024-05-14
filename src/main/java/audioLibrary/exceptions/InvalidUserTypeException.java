package audioLibrary.exceptions;

public class InvalidUserTypeException extends Exception{
    public InvalidUserTypeException(){
        super("Your user type is not authorized to perform this command");
    }
    public InvalidUserTypeException(String message){
        super(message);
    }
}
