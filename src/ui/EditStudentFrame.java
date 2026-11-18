package ui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser; // <--- NEW: File picker for changing photo
import javax.swing.ImageIcon; // <--- NEW: Render byte[] or File as image
import javax.swing.BorderFactory; // <--- NEW: Border for photo box
import javax.swing.SwingConstants; // <--- NEW: Center placeholder text
import java.awt.Color; // <--- NEW: Border color
import java.awt.Image; // <--- NEW: Smooth image scaling
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File; // <--- NEW: Holds newly selected photo file
import java.io.FileInputStream; // <--- NEW: Streams file bytes into MySQL BLOB
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;

public class EditStudentFrame extends JFrame {

    private JTextField idField;
    private JTextField nameField;
    private JTextField rollNoField;
    private JTextField courseField;
    private JTextField semesterField;
    private JTextField emailField;
    private JTextField phoneField;

    private JButton loadButton;
    private JButton updateButton;
    private JLabel photoLabel;
    // <--- NEW: UI & File fields for viewing/changing the student's photo
    private JButton changePhotoButton;
    private File selectedPhotoFile = null;

    public EditStudentFrame() {

        setTitle("Edit Student");

        setSize(700, 500);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(null);

        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setBounds(50, 30, 100, 30);
        add(idLabel);

        idField = new JTextField();
        idField.setBounds(160, 30, 200, 30);
        add(idField);

        loadButton = new JButton("Load Student");
        loadButton.setBounds(370, 30, 110, 30);
        add(loadButton);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 80, 100, 30);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(160, 80, 200, 30);
        add(nameField);

        JLabel rollNoLabel = new JLabel("Roll No:");
        rollNoLabel.setBounds(50, 130, 100, 30);
        add(rollNoLabel);

        rollNoField = new JTextField();
        rollNoField.setBounds(160, 130, 200, 30);
        add(rollNoField);

        JLabel courseLabel = new JLabel("Course:");
        courseLabel.setBounds(50, 180, 100, 30);
        add(courseLabel);

        courseField = new JTextField();
        courseField.setBounds(160, 180, 200, 30);
        add(courseField);

        JLabel semesterLabel = new JLabel("Semester:");
        semesterLabel.setBounds(50, 230, 100, 30);
        add(semesterLabel);

        semesterField = new JTextField();
        semesterField.setBounds(160, 230, 200, 30);
        add(semesterField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 280, 100, 30);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(160, 280, 200, 30);
        add(emailField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(50, 330, 100, 30);
        add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(160, 330, 200, 30);
        add(phoneField);
        // <--- NEW: Photo Preview Box and "Change Photo" Button on the right side
        photoLabel = new JLabel("No Photo", SwingConstants.CENTER);
        photoLabel.setBounds(495, 80, 160, 190);
        photoLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(photoLabel);

        changePhotoButton = new JButton("Change Photo");
        changePhotoButton.setBounds(510, 285, 130, 30);
        add(changePhotoButton);
        updateButton = new JButton("Update Student");
        updateButton.setBounds(160, 390, 150, 35);
        add(updateButton);
        // <--- NEW: Listener to pick a new photo and preview it immediately
        changePhotoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int choice = fileChooser.showOpenDialog(EditStudentFrame.this);
                if (choice == JFileChooser.APPROVE_OPTION) {
                    selectedPhotoFile = fileChooser.getSelectedFile();
                    displayPhoto(new ImageIcon(selectedPhotoFile.getAbsolutePath()));
                }
            }
        });

        loadButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                loadStudent();
            }
        });

        updateButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                updateStudent();
            }
        });

        setVisible(true);
    }

    private void loadStudent() {

        String idText = idField.getText();

        if (idText.trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    EditStudentFrame.this,
                    "Please enter Student ID.");

            return;
        }

        try {

            int studentId = Integer.parseInt(idText);

            Connection connection = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM students WHERE student_id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, studentId);

            ResultSet result = statement.executeQuery();

            if (result.next()) {

                nameField.setText(
                        result.getString("name"));

                rollNoField.setText(
                        result.getString("roll_no"));

                courseField.setText(
                        result.getString("course"));

                semesterField.setText(
                        String.valueOf(
                                result.getInt("semester")));

                emailField.setText(
                        result.getString("email"));

                phoneField.setText(
                        result.getString("phone"));
                // <--- NEW: Reset pending file choice and show existing BLOB photo from DB
                selectedPhotoFile = null;
                byte[] photoBytes = result.getBytes("photo");
                if (photoBytes != null && photoBytes.length > 0) {
                    displayPhoto(new ImageIcon(photoBytes));
                } else {
                    photoLabel.setIcon(null);
                    photoLabel.setText("No Photo");
                }

            } else {

                JOptionPane.showMessageDialog(
                        EditStudentFrame.this,
                        "Student not found.");
            }

            result.close();

            statement.close();

            connection.close();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    EditStudentFrame.this,
                    "Student ID must be a number.");

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    EditStudentFrame.this,
                    "Could not load student.");

            ex.printStackTrace();
        }
    }

    private void updateStudent() {

        String idText = idField.getText();
        String name = nameField.getText();
        String rollNo = rollNoField.getText();
        String course = courseField.getText();
        String semesterText = semesterField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        if (idText.trim().isEmpty() ||
                name.trim().isEmpty() ||
                rollNo.trim().isEmpty() ||
                course.trim().isEmpty() ||
                semesterText.trim().isEmpty() ||
                email.trim().isEmpty() ||
                phone.trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    EditStudentFrame.this,
                    "Please enter all information.");

            return;
        }

        try {

            int studentId = Integer.parseInt(idText);
            int semester = Integer.parseInt(semesterText);

            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement;

            // 1. Check if the user selected a new photo to upload
            if (selectedPhotoFile != null) {
                String sql = "UPDATE students SET " +
                        "name = ?, roll_no = ?, course = ?, semester = ?, email = ?, phone = ?, photo = ? " +
                        "WHERE student_id = ?";

                statement = connection.prepareStatement(sql);
                statement.setString(1, name);
                statement.setString(2, rollNo);
                statement.setString(3, course);
                statement.setInt(4, semester);
                statement.setString(5, email);
                statement.setString(6, phone);

                FileInputStream fis = new FileInputStream(selectedPhotoFile);
                statement.setBinaryStream(7, fis, (int) selectedPhotoFile.length());
                statement.setInt(8, studentId);
            } else {
                // 2. If no new photo was chosen, keep their existing photo untouched in MySQL
                String sql = "UPDATE students SET " +
                        "name = ?, roll_no = ?, course = ?, semester = ?, email = ?, phone = ? " +
                        "WHERE student_id = ?";

                statement = connection.prepareStatement(sql);
                statement.setString(1, name);
                statement.setString(2, rollNo);
                statement.setString(3, course);
                statement.setInt(4, semester);
                statement.setString(5, email);
                statement.setString(6, phone);
                statement.setInt(7, studentId);
            }

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(
                        EditStudentFrame.this,
                        "Student updated successfully!");
                selectedPhotoFile = null; // Clear pending file after saving
            } else {
                JOptionPane.showMessageDialog(
                        EditStudentFrame.this,
                        "Student not found.");
            }

            statement.close();
            connection.close();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    EditStudentFrame.this,
                    "Student ID and Semester must be numbers.");

        } catch (Exception ex) { // Catches both SQLException and FileNotFoundException

            JOptionPane.showMessageDialog(
                    EditStudentFrame.this,
                    "Could not update student.");

            ex.printStackTrace();
        }
    }

    // Helper method to resize any ImageIcon smoothly to fit the 160x190 photoLabel
    private void displayPhoto(ImageIcon originalIcon) {
        Image resizedImage = originalIcon.getImage().getScaledInstance(
                photoLabel.getWidth(),
                photoLabel.getHeight(),
                Image.SCALE_SMOOTH);
        photoLabel.setText("");
        photoLabel.setIcon(new ImageIcon(resizedImage));
    }
}