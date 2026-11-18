package ui;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel; // <--- NEW: To display the student's photo
import javax.swing.ImageIcon; // <--- NEW: To convert byte[] into an image
import javax.swing.BorderFactory; // <--- NEW: To draw a border around the photo box
import javax.swing.SwingConstants; // <--- NEW: To center placeholder text
import javax.swing.event.ListSelectionEvent; // <--- NEW: Row click event
import javax.swing.event.ListSelectionListener; // <--- NEW: Row click listener
import java.awt.Color; // <--- NEW: Border color
import java.awt.Image; // <--- NEW: For resizing the image smoothly
import java.util.ArrayList; // <--- NEW: Dynamic list to hold BLOB byte arrays
import java.util.List; // <--- NEW: List interface
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;

public class StudentFrame extends JFrame {

    private JTable studentTable;
    private JLabel photoLabel; // <--- NEW: Picture frame on the right
    private List<byte[]> studentPhotos = new ArrayList<>(); // <--- NEW: Stores BLOB bytes per row

    public StudentFrame() {

        setTitle("Student Management System - Students");

        setSize(1120, 400);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(null);

        String[] columns = {
                "ID",
                "Name",
                "Roll No",
                "Course",
                "Semester",
                "Email",
                "Phone"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0);

        studentTable = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(studentTable);

        scrollPane.setBounds(20, 20, 850, 300);

        add(scrollPane);
        photoLabel = new JLabel("Click a student row", SwingConstants.CENTER);
        photoLabel.setBounds(890, 20, 190, 220);
        photoLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(photoLabel);
        // <--- NEW: Listen for when the user clicks a row in studentTable
        studentTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Prevents firing twice (once when pressing mouse down, once when releasing)
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = studentTable.getSelectedRow();
                    if (selectedRow != -1) {
                        byte[] photoBytes = studentPhotos.get(selectedRow);
                        displayStudentPhoto(photoBytes);
                    }
                }
            }
        });

        loadStudents();

        setVisible(true);
    }

    private void loadStudents() {

        String sql = "SELECT * FROM students";

        try {

            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet result = statement.executeQuery();

            DefaultTableModel model = (DefaultTableModel) studentTable.getModel();

            while (result.next()) {

                int studentId = result.getInt("student_id");

                String name = result.getString("name");

                String rollNo = result.getString("roll_no");

                String course = result.getString("course");

                int semester = result.getInt("semester");

                String email = result.getString("email");

                String phone = result.getString("phone");
                byte[] photoBytes = result.getBytes("photo"); // <--- NEW: Read BLOB column as byte[]

                studentPhotos.add(photoBytes); // <--- NEW: Keep list index aligned with table row

                model.addRow(new Object[] {
                        studentId,
                        name,
                        rollNo,
                        course,
                        semester,
                        email,
                        phone
                });
            }

            result.close();

            statement.close();

            connection.close();

        } catch (SQLException ex) {

            ex.printStackTrace();
        }
    }

    private void displayStudentPhoto(byte[] photoBytes) {
        if (photoBytes == null || photoBytes.length == 0) {
            photoLabel.setIcon(null);
            photoLabel.setText("No Photo Available");
            return;
        }

        ImageIcon originalIcon = new ImageIcon(photoBytes);
        Image resizedImage = originalIcon.getImage().getScaledInstance(
                photoLabel.getWidth(),
                photoLabel.getHeight(),
                Image.SCALE_SMOOTH);

        photoLabel.setText("");
        photoLabel.setIcon(new ImageIcon(resizedImage));
    }
}