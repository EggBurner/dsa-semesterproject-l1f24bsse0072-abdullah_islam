import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class DashboardFrame extends JFrame {

    private User currentUser;
    private HashMap<String, User> users;

    private DefaultListModel<String> friendModel;
    private JList<String> friendList;

    public DashboardFrame(User currentUser,
                          HashMap<String, User> users) {

        this.currentUser = currentUser;
        this.users = users;

        setTitle("Dashboard");
        setSize(500,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        friendModel = new DefaultListModel<>();

        refreshFriends();

        friendList = new JList<>(friendModel);

        JButton addBtn = new JButton("Add Friend");
        JButton removeBtn = new JButton("Remove Friend");
        JButton chatBtn = new JButton("Open Chat");

        JPanel buttons = new JPanel();

        buttons.add(addBtn);
        buttons.add(removeBtn);
        buttons.add(chatBtn);

        add(new JScrollPane(friendList),
                BorderLayout.CENTER);

        add(buttons,
                BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addFriend());

        removeBtn.addActionListener(e -> removeFriend());

        chatBtn.addActionListener(e -> openChat());

        setVisible(true);
    }

    private void refreshFriends() {

        friendModel.clear();

        for(User u : currentUser.getFriends()) {

            friendModel.addElement(
                    u.getUsername()
            );
        }
    }

    private void addFriend() {

        String username =
                JOptionPane.showInputDialog(
                        "Enter username");

        if(username == null) return;

        User friend = users.get(username);

        if(friend == null){

            JOptionPane.showMessageDialog(this,
                    "User not found");

            return;
        }

        currentUser.addFriends(friend);
        friend.addFriends(currentUser);

        UserStorage.saveUsers(users);

        refreshFriends();
    }

    private void removeFriend() {

        String username =
                friendList.getSelectedValue();

        if(username == null) return;

        User friend = users.get(username);

        currentUser.removeFriend(friend);
        friend.removeFriend(currentUser);

        UserStorage.saveUsers(users);

        refreshFriends();
    }

    private void openChat() {

        String username =
                friendList.getSelectedValue();

        if(username == null) return;

        new ChatFrame(
                currentUser,
                users.get(username)
        );
    }
}