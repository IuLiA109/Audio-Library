package audioLibrary.user;

public class AnonymousUser extends AbstractUser {
    public AnonymousUser() {
        super(null, null);
    }
    @Override
    public UserType getType() {
        return UserType.Anonymous;
    }
}
