package ui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.sql.PreparedStatement;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField username;
    private JPasswordField password;
    private JButton loginButton;
    private JButton registerButton;
    private JButton forgotPasswordButton;

    public LoginFrame() {// constructor
        setTitle("Student Management System - Login");
        setSize(400, 330);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 50, 100, 30);
        add(usernameLabel);
        username = new JTextField();
        username.setBounds(150, 50, 180, 30);
        add(username);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 100, 100, 30);
        add(passwordLabel);
        password = new JPasswordField();
        password.setBounds(150, 100, 180, 30);
        add(password);

        loginButton = new JButton("Login");
        loginButton.setBounds(150, 150, 90, 30);
        add(loginButton);
        forgotPasswordButton = new JButton("Forgot Password?");
        forgotPasswordButton.setBounds(130, 240, 140, 30);
        add(forgotPasswordButton);
        registerButton = new JButton("Register");
        registerButton.setBounds(150, 195, 90, 30);
        add(registerButton);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterFrame registerFrame = new RegisterFrame();
                registerFrame.setVisible(true);
                setVisible(false);
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredUsername = username.getText();
                String enteredPassword = new String(password.getPassword());
                if (enteredUsername.trim().isEmpty() ||
                        enteredPassword.trim().isEmpty()) {

                    JOptionPane.showMessageDialog(
                            LoginFrame.this,
                            "Please enter username and password.");

                    return;
                }
                try {
                    Connection connection = DatabaseConnection.getConnection();
                    String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, enteredUsername);
                    statement.setString(2, enteredPassword);
                    ResultSet result = statement.executeQuery();
                    if (result.next()) {
                        String loggedInUsername = result.getString("username");
                        JOptionPane.showMessageDialog(LoginFrame.this, "Login successful!");
                        DashboardFrame dashboardFrame = new DashboardFrame(loggedInUsername);

                        dashboardFrame.setVisible(true);

                        setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(
                                LoginFrame.this,
                                "Invalid username or password.");
                    }
                    statement.close();
                    connection.close();

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                            LoginFrame.this,
                            "Login failed!");
                    ex.printStackTrace();
                }
            }

        });
        forgotPasswordButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                ResetPasswordFrame resetPasswordFrame = new ResetPasswordFrame();

                resetPasswordFrame.setVisible(true);
            }
        });
        setLayout(null);
        setVisible(true);
    }
}
