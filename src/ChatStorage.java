import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ChatStorage {

    public static void saveMessage(Message message) {

        try {

            FileWriter writer = new FileWriter("chat_history.txt", true);
            writer.write(message.getTimestamp() + "," +message.getSender() + "," + message.getReceiver() + "," + message.getEncryptedText() + "\n");
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadChat(Chat chat) {

        try {

            BufferedReader reader = new BufferedReader(new FileReader("chat_history.txt"));
            String line;

            while((line = reader.readLine()) != null) {

                String[] parts = line.split(",", 4);

                long timestamp = Long.parseLong(parts[0]);

                String sender = parts[1];
                String receiver = parts[2];
                String encryptedText = parts[3];

                boolean sameUsers = (sender.equals(chat.getUser1().getUsername()) && receiver.equals(chat.getUser2().getUsername())) || (sender.equals(chat.getUser2().getUsername()) && receiver.equals(chat.getUser1().getUsername()));

                if(sameUsers) {
                    chat.addMessage(new Message(sender, receiver, encryptedText, timestamp));
                }
            }

            reader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}