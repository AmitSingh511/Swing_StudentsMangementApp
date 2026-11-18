package ui;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;

public class StudentFrame extends JFrame {

    private JTable studentTable;

    public StudentFrame() {

        setTitle("Student Management System - Students");

        setSize(900, 400);

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
}