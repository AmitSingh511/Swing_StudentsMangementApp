package ui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.io.File;
import java.io.FileInputStream;
import javax.swing.JFileChooser;
import database.DatabaseConnection;
import java.sql.Types;
import javax.swing.JButton;
import javax.swing.JOptionPane;
// ActionEvent = information about a button click.
import java.awt.event.ActionEvent;
// ActionListener = listens for the button click.
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddStudentFrame extends JFrame {
    private JTextField nameField;
    private JTextField rollNoField;
    private JTextField courseField;
    private JTextField semesterField;
    private JTextField emailField;
    private JTextField phoneField;
    private JButton addButton;
    private JButton choosePhotoButton;
    private JLabel photoNameLabel;
    private File selectedPhotoFile = null; // Stores the actual file object

    public AddStudentFrame() {

        setTitle("Student Management System - Add Student");

        setSize(400, 490);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(null);
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 40, 100, 30);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(160, 40, 200, 30);
        add(nameField);

        JLabel rollNoLabel = new JLabel("Roll No:");
        rollNoLabel.setBounds(50, 90, 100, 30);
        add(rollNoLabel);

        rollNoField = new JTextField();
        rollNoField.setBounds(160, 90, 200, 30);
        add(rollNoField);

        JLabel courseLabel = new JLabel("Course:");
        courseLabel.setBounds(50, 140, 100, 30);
        add(courseLabel);

        courseField = new JTextField();
        courseField.setBounds(160, 140, 200, 30);
        add(courseField);

        JLabel semesterLabel = new JLabel("Semester:");
        semesterLabel.setBounds(50, 190, 100, 30);
        add(semesterLabel);

        semesterField = new JTextField();
        semesterField.setBounds(160, 190, 200, 30);
        add(semesterField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 240, 100, 30);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(160, 240, 200, 30);
        add(emailField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(50, 290, 100, 30);
        add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(160, 290, 200, 30);
        add(phoneField);

        choosePhotoButton = new JButton("Choose Photo");
        choosePhotoButton.setBounds(50, 340, 120, 30);
        add(choosePhotoButton);

        photoNameLabel = new JLabel("No file selected");
        photoNameLabel.setBounds(180, 340, 180, 30);
        add(photoNameLabel);

        addButton = new JButton("Add Student");
        addButton.setBounds(150, 400, 140, 35);
        add(addButton);
        choosePhotoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int choice = fileChooser.showOpenDialog(AddStudentFrame.this);
                if (choice == JFileChooser.APPROVE_OPTION) {
                    selectedPhotoFile = fileChooser.getSelectedFile();
                    photoNameLabel.setText(selectedPhotoFile.getName());
                }
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String name = nameField.getText();
                String rollNo = rollNoField.getText();
                String course = courseField.getText();
                String semesterText = semesterField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();

                int semester = Integer.parseInt(semesterText);
                try {
                    Connection connection = DatabaseConnection.getConnection();

                    String sql = "INSERT INTO students(name, roll_no, course, semester, email, phone,photo) VALUES (?, ?, ?, ?, ?, ?,?)";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, name);
                    statement.setString(2, rollNo);
                    statement.setString(3, course);
                    statement.setInt(4, semester);
                    statement.setString(5, email);
                    statement.setString(6, phone);
                    if (selectedPhotoFile != null) {
                        FileInputStream fis = new FileInputStream(selectedPhotoFile);
                        statement.setBinaryStream(7, fis, (int) selectedPhotoFile.length());
                    } else {
                        statement.setNull(7, Types.BLOB);
                    }
                    statement.executeUpdate();
                    JOptionPane.showMessageDialog(
                            AddStudentFrame.this,
                            "Student added successfully!");

                    nameField.setText("");
                    rollNoField.setText("");
                    courseField.setText("");
                    semesterField.setText("");
                    emailField.setText("");
                    phoneField.setText("");
                    selectedPhotoFile = null;
                    photoNameLabel.setText("No file selected");
                    statement.close();
                    connection.close();
                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                            AddStudentFrame.this,
                            "Could not add student.");

                    ex.printStackTrace();
                }
            }
        });
    }
}