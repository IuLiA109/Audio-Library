package audioLibrary.user;

public class Administrator extends AbstractUser {
    public Administrator(String username, String password) {
        super(username, password);
    }
    @Override
    public UserType getType() {
        return UserType.Administrator;
    }
}
