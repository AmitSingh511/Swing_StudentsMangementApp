package ui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import database.DatabaseConnection;

public class RegisterFrame extends JFrame {
    private JTextField nameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton loginButton;

    public RegisterFrame() {
        setTitle("Student Management System - Register");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 40, 100, 30);
        add(nameLabel);
        nameField = new JTextField();
        nameField.setBounds(150, 40, 180, 30);
        add(nameField);
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 90, 100, 30);
        add(usernameLabel);
        usernameField = new JTextField();
        usernameField.setBounds(150, 90, 180, 30);
        add(usernameField);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 140, 100, 30);
        add(passwordLabel);
        passwordField = new JPasswordField();
        passwordField.setBounds(150, 140, 180, 30);
        add(passwordField);
        registerButton = new JButton("Register");
        registerButton.setBounds(150, 190, 100, 30);
        add(registerButton);
        loginButton = new JButton("Back to Login");
        loginButton.setBounds(130, 240, 140, 30);
        add(loginButton);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                setVisible(false);
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                System.out.println(name);
                System.out.println(username);
                System.out.println(password);
                if (name.isEmpty() ||
                        username.isEmpty() ||
                        password.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            RegisterFrame.this,
                            "Please enter all the required information.");

                    return;
                }
                try {
                    Connection connection = DatabaseConnection.getConnection();
                    String sql = "INSERT INTO users(name,username,password)values(?,?,?)";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, name);
                    statement.setString(2, username);
                    statement.setString(3, password);
                    statement.executeUpdate();
                    System.out.println("User Registered Successfully");
                    JOptionPane.showMessageDialog(RegisterFrame.this, "User Registered Successfully!");
                    statement.close();
                    connection.close();

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "Registration Failed");
                    ex.printStackTrace();
                }

            }
        });
        setVisible(true);
    }
}
