package SkillForge;

import javax.swing.*;

public class CreateCourseForm extends JFrame{
    private JButton saveButton;
    private JTextField textField1;
    private JTextField textField2;
    private JPanel mainPanel;

    public CreateCourseForm(){
        setTitle("Instructor Dashboard");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setVisible(true);

    }
}
