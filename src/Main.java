import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        HashMap<String, User> users = UserStorage.loadUsers();
        HashMap<String, Chat> chats = new HashMap<>();

        String username ="";
        boolean loggedIn = false;

        while(true){
            if(loggedIn){
                break;
            }
            System.out.println("=".repeat(50));
            System.out.println("QUIBBI CHAT APP");
            System.out.println("=".repeat(50));
            System.out.println();
            System.out.println("1. Register Account");
            System.out.println("2. Log into an existing Account");
            System.out.println("3. Exit");
            System.out.println();
            System.out.print("Enter Choice: ");

            int c1 = scanner.nextInt();

            if(c1 == 3) break;
            else if(c1 == 1){
                System.out.println("-".repeat(50));
                System.out.println("Sign Up Page");
                System.out.println("-".repeat(50));
                String password;

                while(true){
                    System.out.println();
                    System.out.print("Enter your desired Username: ");
                    username = scanner.next();
                    System.out.print("Enter an 8 digit password: ");
                    password = scanner.next();

                    boolean usernameTaken = false;

                    for (String u : users.keySet()) {
                        if (u.equals(username)) {
                            usernameTaken = true;
                            break;
                        }
                    }

                    if (usernameTaken) {
                        System.out.println("Username taken. Try again!");
                    }
                    else if (password.length() < 8) {
                        System.out.println("Password too short. Try again");
                    }
                    else {
                        System.out.println("Registered. Please log in now");

                        String hashedPassword = Auth.hashPassword(password);

                        users.put(
                                username,
                                new User(username, hashedPassword)
                        );

                        UserStorage.saveUsers(users);

                        break;
                    }
                }

            }
            else if(c1 == 2){
                System.out.println("-".repeat(50));
                System.out.println("Login Page");
                System.out.println("-".repeat(50));
                String password;

                while(true) {
                    System.out.println();
                    System.out.print("Enter your Username: ");
                    username = scanner.next();
                    System.out.print("Enter your password: ");
                    password = scanner.next();

                    if(users.containsKey(username) && users.get(username).getHashedPassword().equals(Auth.hashPassword(password))){
                        loggedIn = true;
                        System.out.println("Successful Login");
                        break;
                    } else{
                        System.out.println("Invalid credentials. Try again");
                    }
                }
            }
        }

        if(loggedIn){
            User sessionUser;
            sessionUser = users.get(username);

            while(true){
                System.out.println();
                System.out.println("Welcome back, " + sessionUser.getUsername());

                System.out.println();
                System.out.println("1. Message a Friend");
                System.out.println("2. View Friends");
                System.out.println("3. Add friends");
                System.out.println("4. Remove friend");
                System.out.println("5. View Chat History");
                System.out.println("6. Exit");
                System.out.println();

                System.out.print("Enter Choice: ");
                int c1 = scanner.nextInt();

                if(c1 == 6){
                    break;
                }

                else if(c1 == 1){

                    if(sessionUser.getFriends().isEmpty()){
                        System.out.println("No friends added");
                        continue;
                    }

                    System.out.println();

                    for(User u : sessionUser.getFriends()){
                        System.out.println(u.getUsername());
                    }

                    System.out.println();
                    System.out.print("Enter username: ");
                    String friendUsername = scanner.next();

                    User friend = users.get(friendUsername);

                    if(friend == null || !sessionUser.getFriends().contains(friend)){
                        System.out.println("Friend not found");
                        continue;
                    }

                    scanner.nextLine();

                    System.out.print("Message: ");
                    String text = scanner.nextLine();

                    String key1 = sessionUser.getUsername() + "-" + friend.getUsername();
                    String key2 = friend.getUsername() + "-" + sessionUser.getUsername();

                    Chat chat;

                    if(chats.containsKey(key1)){
                        chat = chats.get(key1);
                    }
                    else if(chats.containsKey(key2)){
                        chat = chats.get(key2);
                    }
                    else{
                        chat = new Chat(sessionUser, friend);

                        ChatStorage.loadChat(chat);

                        chats.put(key1, chat);
                    }

                    String encryptedText = Crypto.encrypt(text, friend.getAesKey());

                    Message message = new Message(
                            sessionUser.getUsername(),
                            friend.getUsername(),
                            encryptedText,
                            System.currentTimeMillis()
                    );

                    chat.addMessage(message);
                    ChatStorage.saveMessage(message);

                    System.out.println();
                    System.out.println("-".repeat(50));
                    System.out.println("Chat History");
                    System.out.println("-".repeat(50));

                    for(Message m : chat.getMessages()){
                        String decrypted;

                        if(m.getReceiver().equals(sessionUser.getUsername())){
                            decrypted = Crypto.decrypt(m.getEncryptedText(), sessionUser.getAesKey());
                        }
                        else{
                            User otherUser = users.get(m.getReceiver());

                            decrypted = Crypto.decrypt(m.getEncryptedText(), otherUser.getAesKey());
                        }

                        System.out.println(
                                m.getSender() + ": " + decrypted
                        );
                    }
                }

                else if(c1 == 2){

                    System.out.println();

                    if(sessionUser.getFriends().isEmpty()){
                        System.out.println("No friends");
                    }

                    for(User u : sessionUser.getFriends()){
                        System.out.println(u.getUsername());
                    }
                }

                else if(c1 == 3){

                    System.out.print("Enter username: ");
                    String friendUsername = scanner.next();

                    if(users.containsKey(friendUsername)){

                        User friend = users.get(friendUsername);

                        if(friend != sessionUser){
                            sessionUser.addFriends(friend);
                            friend.addFriends(sessionUser);

                            UserStorage.saveUsers(users);

                            System.out.println("-".repeat(50));
                            System.out.println("Friend added");
                            System.out.println("-".repeat(50));
                        }
                    }
                    else{
                        System.out.println("User not found");
                    }
                }

                else if(c1 == 4){

                    System.out.println("-".repeat(50));
                    System.out.print("Enter username: ");
                    String friendUsername = scanner.next();

                    if(users.containsKey(friendUsername)){

                        User friend = users.get(friendUsername);

                        sessionUser.removeFriend(friend);
                        friend.removeFriend(sessionUser);
                        UserStorage.saveUsers(users);

                        System.out.println("Friend removed");
                    }
                    else{
                        System.out.println("User not found");
                    }
                }
                else if(c1 == 5){
                    System.out.print("Enter friend's username: ");
                    String friendUsername = scanner.next();

                    User friend = users.get(friendUsername);

                    if(friend == null || !sessionUser.getFriends().contains(friend)){
                        System.out.println("Friend not found");
                        continue;
                    }

                    Chat chat = new Chat(sessionUser, friend);

                    ChatStorage.loadChat(chat);

                    System.out.println();
                    System.out.println("-".repeat(50));
                    System.out.println("Chat History");
                    System.out.println("-".repeat(50));

                    if(chat.getMessages().isEmpty()){
                        System.out.println("No messages");
                        continue;
                    }

                    for(Message m : chat.getMessages()){

                        String decrypted;

                        if(m.getReceiver()
                                .equals(sessionUser.getUsername())){

                            decrypted = Crypto.decrypt(
                                    m.getEncryptedText(),
                                    sessionUser.getAesKey()
                            );
                        }
                        else{

                            User receiver =
                                    users.get(m.getReceiver());

                            decrypted = Crypto.decrypt(
                                    m.getEncryptedText(),
                                    receiver.getAesKey()
                            );
                        }

                        System.out.println(
                                m.getSender() + ": " + decrypted
                        );
                    }
                }
            }
        }
    }
}