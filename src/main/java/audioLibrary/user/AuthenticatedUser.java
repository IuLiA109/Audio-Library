package audioLibrary.user;

public class AuthenticatedUser extends AbstractUser {
    public AuthenticatedUser(String username, String password){
        super(username, password);
    }
    @Override
    public UserType getType() {
        return UserType.Authenticated;
    }
}
