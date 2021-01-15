package fastPg3.types;

/**
 *
 * @author Édson Fischborn
 */
public class UserLogin {
    private String username;
    private String password;

    public UserLogin(String username, String password){
        setUsername(username);
        setPassword(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
