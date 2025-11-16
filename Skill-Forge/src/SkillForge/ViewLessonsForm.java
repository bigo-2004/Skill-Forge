package SkillForge;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ViewLessonsForm extends JFrame {

    private JPanel mainPanel;
    private JTable table1;
    private Course course;

    public ViewLessonsForm(Course course){
        this.course = course;

        setTitle("Lessons for: " + course.getCourseTitle());
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setVisible(true);

        loadLessons();
    }

    private void loadLessons() {
        String[] columns = {"Lesson ID", "Title", "Content"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table1.setModel(model);
        table1.setRowHeight(30);
        table1.getTableHeader().setReorderingAllowed(false);
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table1.setFillsViewportHeight(true);

        for (Lesson lesson : course.getLessons()) {
            String[] row = {lesson.getLessonID(), lesson.getLessonTitle(), lesson.getLessonContent()};
            model.addRow(row);
        }
    }
}