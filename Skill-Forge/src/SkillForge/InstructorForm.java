package SkillForge;

import javax.swing.*;

public class InstructorForm extends JFrame {
    private JButton createNewCourseButton;
    private JButton listOfCoursesButton;
    private JButton manageCourseButton;
    private JButton logoutButton;
    private JPanel mainPanel;

    public InstructorForm(){
        setTitle("Instructor Dashboard");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setVisible(true);

    }

}
