package SkillForge;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ViewLessonsForm extends JFrame {

    private JPanel mainPanel;
    private JTable table1;
    private JButton markCompletedButton;
    private Course course;

    public ViewLessonsForm(Course course){
        this.course = course;

        setTitle("Lessons for: " + course.getCourseTitle());
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        loadLessons();

        if (markCompletedButton != null) {
            markCompletedButton.addActionListener(e -> markLessonAsCompleted());
        }
    }

    private void markLessonAsCompleted() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a lesson to mark as completed.");
            return;
        }

        String lessonId = (String) table1.getValueAt(selectedRow, 0);


        Lesson lessonToMark = null;
        for (Lesson lesson : course.getLessons()) {
            if (lesson.getLessonID().equals(lessonId)) {
                lessonToMark = lesson;
                break;
            }
        }

        if (lessonToMark == null) {
            JOptionPane.showMessageDialog(this, "Lesson not found.");
            return;
        }

        if (lessonToMark.isWatched()) {
            JOptionPane.showMessageDialog(this, lessonToMark.getLessonTitle() + " is already marked as completed.");
            return;
        }

        lessonToMark.setWatched(true);

        try {
            CourseJsonDatabase db = new CourseJsonDatabase("courses.json");
            db.updateObject(course, course);

            loadLessons();
            JOptionPane.showMessageDialog(this, lessonToMark.getLessonTitle() + " marked as completed! ");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving completion status: " + ex.getMessage());
        }
    }


    private void loadLessons() {
        String[] columns = {"Lesson ID", "Title", "Content", "Status"};
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
            String status = lesson.isWatched() ? "Completed" : "Pending";

            String[] row = {
                    lesson.getLessonID(),
                    lesson.getLessonTitle(),
                    lesson.getLessonContent(),
                    status
            };
            model.addRow(row);
        }
    }
}