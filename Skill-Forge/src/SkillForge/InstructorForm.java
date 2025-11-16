package SkillForge;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginForm();
            }
        });
    }

}
