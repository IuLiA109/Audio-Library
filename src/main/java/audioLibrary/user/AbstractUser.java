package audioLibrary.user;

public abstract class AbstractUser implements User {
      private final String username;
      private final String password;

      public AbstractUser(String username, String password){
          this.username = username;
          this.password = password;
      }
      @Override
      public String getUsername(){
          return username;
      }
      @Override
      public String getPassword(){
          return password;
      }
}
