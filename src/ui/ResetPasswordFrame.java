package ui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import database.DatabaseConnection;

public class ResetPasswordFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField newPasswordField;
    private JButton resetButton;

    public ResetPasswordFrame() {

        setTitle("Reset Password");

        setSize(400, 300);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");

        usernameLabel.setBounds(50, 50, 100, 30);

        add(usernameLabel);

        usernameField = new JTextField();

        usernameField.setBounds(150, 50, 180, 30);

        add(usernameField);

        JLabel passwordLabel = new JLabel("New Password:");

        passwordLabel.setBounds(50, 100, 100, 30);

        add(passwordLabel);

        newPasswordField = new JPasswordField();

        newPasswordField.setBounds(150, 100, 180, 30);

        add(newPasswordField);

        resetButton = new JButton("Reset Password");

        resetButton.setBounds(130, 160, 140, 35);

        add(resetButton);

        resetButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String username = usernameField.getText();

                String newPassword = new String(
                        newPasswordField.getPassword());

                if (username.trim().isEmpty() ||
                        newPassword.trim().isEmpty()) {

                    JOptionPane.showMessageDialog(
                            ResetPasswordFrame.this,
                            "Please enter all information.");

                    return;
                }

                try {

                    Connection connection = DatabaseConnection.getConnection();

                    String sql = "UPDATE users " +
                            "SET password = ? " +
                            "WHERE username = ?";

                    PreparedStatement statement = connection.prepareStatement(sql);

                    statement.setString(1, newPassword);

                    statement.setString(2, username);

                    int rowsUpdated = statement.executeUpdate();

                    if (rowsUpdated > 0) {

                        JOptionPane.showMessageDialog(
                                ResetPasswordFrame.this,
                                "Password reset successfully!");

                        dispose();

                    } else {

                        JOptionPane.showMessageDialog(
                                ResetPasswordFrame.this,
                                "Username not found.");
                    }

                    statement.close();

                    connection.close();

                } catch (SQLException ex) {

                    JOptionPane.showMessageDialog(
                            ResetPasswordFrame.this,
                            "Could not reset password.");

                    ex.printStackTrace();
                }
            }
        });

        setVisible(true);
    }
}