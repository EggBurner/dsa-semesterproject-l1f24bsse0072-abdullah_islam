

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class RegisterFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    private HashMap<String, User> users;

    public RegisterFrame() {

        users = UserStorage.loadUsers();

        setTitle("Register");
        setSize(400,250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4,2,10,10));

        panel.add(new JLabel("Username"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton registerBtn = new JButton("Create Account");

        panel.add(registerBtn);

        add(panel);

        registerBtn.addActionListener(e -> register());

        setVisible(true);
    }

    private void register() {

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if(users.containsKey(username)){

            JOptionPane.showMessageDialog(this,
                    "Username already exists");

            return;
        }

        if(password.length() < 8){

            JOptionPane.showMessageDialog(this,
                    "Password must be at least 8 characters");

            return;
        }

        User user = new User(
                username,
                Auth.hashPassword(password)
        );

        users.put(username,user);

        UserStorage.saveUsers(users);

        JOptionPane.showMessageDialog(this,
                "Registration Successful");

        dispose();

        new LoginFrame();
    }
}