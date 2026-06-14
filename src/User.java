import java.util.HashSet;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class User {
    private String username;
    private String hashedPassword;
    private SecretKey aesKey;
    private HashSet<User> friends = new HashSet<>();

    public User(String username, String hashedPassword) {

        this.username = username;
        this.hashedPassword = hashedPassword;

        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            this.aesKey = keyGenerator.generateKey();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public User(String username, String hashedPassword, SecretKey aesKey) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.aesKey = aesKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public HashSet<User> getFriends() {
        return friends;
    }

    public void setFriends(HashSet<User> friends) {
        this.friends = friends;
    }

    public void addFriends(User friend) {
        this.friends.add(friend);
    }

    public void removeFriend(User friend) {
        this.friends.remove(friend);
    }
    public SecretKey getAesKey() {
        return aesKey;
    }
}