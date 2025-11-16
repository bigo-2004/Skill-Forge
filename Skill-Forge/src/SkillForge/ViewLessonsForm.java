package SkillForge;

import javax.swing.*;

public class ViewLessonsForm extends JFrame {

    private JPanel mainPanel;
    private JTable table1;

    public ViewLessonsForm(){
        setTitle("Lessons Dashboard");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
