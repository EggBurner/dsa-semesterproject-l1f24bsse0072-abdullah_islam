

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    private HashMap<String, User> users;

    public LoginFrame() {

        users = UserStorage.loadUsers();

        setTitle("Quibbi Chat");
        setSize(400,250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4,2,10,10));

        panel.add(new JLabel("Username"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        panel.add(loginBtn);
        panel.add(registerBtn);

        add(panel);

        loginBtn.addActionListener(e -> login());

        registerBtn.addActionListener(e -> {
            dispose();
            new RegisterFrame();
        });

        setVisible(true);
    }

    private void login() {

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if(users.containsKey(username)
                && users.get(username).getHashedPassword()
                .equals(Auth.hashPassword(password))) {

            JOptionPane.showMessageDialog(this,"Login Successful");

            dispose();

            new DashboardFrame(users.get(username), users);
        }
        else{
            JOptionPane.showMessageDialog(this,
                    "Invalid Credentials");
        }
    }
}