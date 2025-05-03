import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JButton registerButton;
    private UserDAO userDAO;

    public RegistrationPage() {
        userDAO = new UserDAO();
        setTitle("Registration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(20);
        panel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        panel.add(emailField, gbc);

        // Register Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText();

                if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(RegistrationPage.this, "Please fill in all fields!");
                    return;
                }

                if (userDAO.isUsernameTaken(username)) {
                    JOptionPane.showMessageDialog(RegistrationPage.this, "Username already taken!");
                    return;
                }

                User newUser = new User(username, password, email);
                if (userDAO.registerUser(newUser)) {
                    JOptionPane.showMessageDialog(RegistrationPage.this, "Registration successful!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(RegistrationPage.this, "Registration failed!");
                }
            }
        });
        panel.add(registerButton, gbc);

        add(panel);
    }
} 