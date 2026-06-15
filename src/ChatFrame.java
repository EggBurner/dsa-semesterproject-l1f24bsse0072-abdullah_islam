import javax.swing.*;
import java.awt.*;

public class ChatFrame extends JFrame {

    private User currentUser;
    private User friend;

    private JTextArea chatArea;
    private JTextField messageField;

    private Chat chat;

    public ChatFrame(User currentUser,
                     User friend) {

        this.currentUser = currentUser;
        this.friend = friend;

        setTitle("Chat - " +
                friend.getUsername());

        setSize(600,500);

        setLocationRelativeTo(null);

        chatArea = new JTextArea();
        chatArea.setEditable(false);

        messageField = new JTextField();

        JButton sendBtn = new JButton("Send");

        JPanel bottom =
                new JPanel(new BorderLayout());

        bottom.add(messageField,
                BorderLayout.CENTER);

        bottom.add(sendBtn,
                BorderLayout.EAST);

        add(new JScrollPane(chatArea),
                BorderLayout.CENTER);

        add(bottom,
                BorderLayout.SOUTH);

        chat = new Chat(currentUser, friend);

        ChatStorage.loadChat(chat);

        loadMessages();

        sendBtn.addActionListener(e -> sendMessage());

        setVisible(true);
    }

    private void sendMessage() {

        String text = messageField.getText();

        if(text.isEmpty()) return;

        String encrypted =
                Crypto.encrypt(
                        text,
                        friend.getAesKey()
                );

        Message msg =
                new Message(
                        currentUser.getUsername(),
                        friend.getUsername(),
                        encrypted,
                        System.currentTimeMillis()
                );

        chat.addMessage(msg);

        ChatStorage.saveMessage(msg);

        chatArea.append(
                currentUser.getUsername()
                        + ": "
                        + text
                        + "\n"
        );

        messageField.setText("");
    }

    private void loadMessages() {

        for(Message m : chat.getMessages()) {

            String decrypted;

            if(m.getReceiver()
                    .equals(currentUser.getUsername())) {

                decrypted =
                        Crypto.decrypt(
                                m.getEncryptedText(),
                                currentUser.getAesKey()
                        );
            }
            else {

                User receiver = friend;

                decrypted =
                        Crypto.decrypt(
                                m.getEncryptedText(),
                                receiver.getAesKey()
                        );
            }

            chatArea.append(
                    m.getSender()
                            + ": "
                            + decrypted
                            + "\n"
            );
        }
    }
}