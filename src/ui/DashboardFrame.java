package ui;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardFrame extends JFrame {

    private JButton addStudentButton;
    private JButton viewStudentsButton;
    private JButton logoutButton;
    private JButton editStudentButton;

    public DashboardFrame(String userName) {

        setTitle("Student Management System - Dashboard");

        setSize(500, 380);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(null);

        JLabel titleLabel = new JLabel("Student Management System");

        titleLabel.setBounds(150, 40, 250, 30);

        add(titleLabel);

        JLabel welcomeLabel = new JLabel("Welcome! " + userName);

        welcomeLabel.setBounds(180, 70, 200, 30);

        add(welcomeLabel);

        addStudentButton = new JButton("Add Student");

        addStudentButton.setBounds(150, 100, 180, 40);

        add(addStudentButton);

        viewStudentsButton = new JButton("View Students");

        viewStudentsButton.setBounds(150, 160, 180, 40);

        add(viewStudentsButton);
        editStudentButton = new JButton("Edit Student");
        editStudentButton.setBounds(150, 220, 180, 40);
        add(editStudentButton);
        logoutButton = new JButton("Logout");

        logoutButton.setBounds(150, 280, 180, 40);

        add(logoutButton);

        addStudentButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                AddStudentFrame addStudentFrame = new AddStudentFrame();

                addStudentFrame.setVisible(true);
            }
        });

        viewStudentsButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                StudentFrame studentFrame = new StudentFrame();

                studentFrame.setVisible(true);
            }
        });
        editStudentButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                EditStudentFrame editStudentFrame = new EditStudentFrame();

                editStudentFrame.setVisible(true);
            }
        });
        logoutButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                LoginFrame loginFrame = new LoginFrame();

                loginFrame.setVisible(true);

                setVisible(false);
            }
        });

        setVisible(true);
    }
}