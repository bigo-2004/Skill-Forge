package SkillForge;

import javax.swing.*;

public class StudentForm extends JFrame{
    private JTable CoursesTable;
    private JButton enrollButton;
    private JButton logoutButton;
    private JPanel mainPanel;

    public StudentForm(){
        setTitle("Student Dashboard");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setVisible(true);

    }

}
