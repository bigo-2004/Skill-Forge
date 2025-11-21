package SkillForge;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList; // Needed if intermediate list were used, but we are avoiding it

public class InsightsForm extends JFrame {

    private JPanel mainPanel;
    private JLabel completionLabel;
    private JProgressBar progressBar1;
    private JLabel averagesHeader;
    private JTable table1;

    private Course course;
    private UserJsonDatabase userDb;

    public InsightsForm(Course course) {
        this.course = course;
        this.userDb = new UserJsonDatabase("users.json");

        setTitle("Course Insights: " + course.getCourseTitle());
        setSize(750, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel courseTitleHeader = new JLabel("Performance Dashboard: " + course.getCourseTitle(), SwingConstants.CENTER);
        courseTitleHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(courseTitleHeader);
        mainPanel.add(Box.createVerticalStrut(20));

        progressBar1.setStringPainted(true);
        progressBar1.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressBar1.setMaximumSize(new Dimension(650, 25));

        mainPanel.add(completionLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(progressBar1);
        mainPanel.add(Box.createVerticalStrut(30));

        table1.setRowHeight(30);
        table1.getTableHeader().setReorderingAllowed(false);
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table1.setFillsViewportHeight(true);
        setContentPane(mainPanel);

        setVisible(true);
    }



}