package SkillForge;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class InsightsForm extends JFrame {

    private JPanel mainPanel;
    private JLabel completionLabel;
    private JProgressBar progressBar1;
    private JLabel averagesHeader;
    private JTable table1;
    private JButton viewChartsButton;
    private Course course;
    private UserJsonDatabase userDb;

    public InsightsForm(Course course) {
        this.course = course;
        this.userDb = new UserJsonDatabase("users.json");

        setTitle("Course Insights: " + course.getCourseTitle());
        setSize(750, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        progressBar1.setStringPainted(true);
        progressBar1.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressBar1.setMaximumSize(new Dimension(650, 25));


        table1.setRowHeight(30);
        table1.getTableHeader().setReorderingAllowed(false);
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table1.setFillsViewportHeight(true);

        setContentPane(mainPanel);

        loadAnalytics();
        setVisible(true);

        viewChartsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ChartFrame(course, userDb);
            }
        });
    }


    private void loadAnalytics() {
        String[] columnNames = {"Lesson Title", "Average Score"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table1.setModel(model);

        try {
            CourseAnalytics analytics = new CourseAnalytics(course, userDb);

            double completionRatio = analytics.calculateCourseCompletionPercentage();
            int completionPercentage = (int) (completionRatio * 100);
            progressBar1.setValue(completionPercentage);
            completionLabel.setText(String.format("Course Completion Rate (Passed All Quizzes): %.1f%%", completionRatio * 100));

            List<Lesson> lessons = course.getLessons();
            for (Lesson lesson : lessons) {
                if (lesson.getQuiz() != null) {
                    double averageRatio = analytics.calculateLessonAverage(lesson.getLessonID());
                    String averageScoreText = String.format("%.1f%%", averageRatio * 100);
                    model.addRow(new Object[]{lesson.getLessonTitle(), averageScoreText});
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading analytics data: " + e.getMessage());
        }
    }
}