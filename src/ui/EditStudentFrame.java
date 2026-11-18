package ui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public EditStudentFrame() {

        setTitle("Edit Student");

        setSize(500, 500);

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

        updateButton = new JButton("Update Student");
        updateButton.setBounds(160, 390, 150, 35);
        add(updateButton);

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

            String sql = "UPDATE students SET " +
                    "name = ?, " +
                    "roll_no = ?, " +
                    "course = ?, " +
                    "semester = ?, " +
                    "email = ?, " +
                    "phone = ? " +
                    "WHERE student_id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, name);
            statement.setString(2, rollNo);
            statement.setString(3, course);
            statement.setInt(4, semester);
            statement.setString(5, email);
            statement.setString(6, phone);
            statement.setInt(7, studentId);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {

                JOptionPane.showMessageDialog(
                        EditStudentFrame.this,
                        "Student updated successfully!");

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

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    EditStudentFrame.this,
                    "Could not update student.");

            ex.printStackTrace();
        }
    }
}