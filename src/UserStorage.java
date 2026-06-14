import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Base64;
import java.util.HashMap;

public class UserStorage {

    public static void saveUsers(HashMap<String, User> users) {

        try {

            FileWriter writer = new FileWriter("users.txt");
            for (User user : users.values()) {
                StringBuilder friendList = new StringBuilder();

                for (User f : user.getFriends()) {
                    friendList.append(f.getUsername()).append(";");
                }

                if (!friendList.isEmpty()) {
                    friendList.setLength(friendList.length() - 1);
                }
                String keyString = Base64.getEncoder().encodeToString(user.getAesKey().getEncoded());

                writer.write(user.getUsername().trim() + "," + user.getHashedPassword().trim() + "," + keyString + "," + friendList.toString().trim() + "\n");
            }

            writer.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static HashMap<String, User> loadUsers() {

        HashMap<String, User> users = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("users.txt"));

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", -1);

                String username = parts[0].trim();
                String hashedPassword = parts.length > 1 ? parts[1].trim() : "";

                String keyString = parts.length > 2 ? parts[2].trim() : "";

                byte[] keyBytes = Base64.getDecoder().decode(keyString);

                SecretKey aesKey = new SecretKeySpec(keyBytes, "AES");

                users.put(username, new User(username, hashedPassword, aesKey)
                );
            }

            reader.close();

            reader = new BufferedReader(new FileReader("users.txt"));

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", -1);

                String username = parts[0].trim();

                if(parts.length < 4) continue;

                String friendsPart = parts[3].trim();

                User user = users.get(username);
                if (user == null) continue;

                if (!friendsPart.isEmpty()) {

                    String[] friends = friendsPart.split(";");

                    for (String f : friends) {

                        String friendName = f.trim();

                        if (!friendName.isEmpty() && users.containsKey(friendName)) {
                            user.addFriends(users.get(friendName));
                        }
                    }
                }
            }

            reader.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return users;
    }
}