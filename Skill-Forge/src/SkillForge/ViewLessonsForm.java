package SkillForge;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewLessonsForm extends JFrame {

    private JPanel mainPanel;
    private JTable table1;
    private JButton markCompletedButton;
    private JButton loadQuizButton;
    private Course course;
    private User student;

    public ViewLessonsForm(Course course,User student){
        this.course = course;
        this.student=student;

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
        loadQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                launchQuiz();
            }
        });
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

        if (lessonToMark.isWatchedByStudent(student.getUserId())) {
            JOptionPane.showMessageDialog(this, lessonToMark.getLessonTitle() + " is already marked as completed.");
            return;
        }

        lessonToMark.markAsWatched(student.getUserId());

        try {
            CourseJsonDatabase db = new CourseJsonDatabase("courses.json");

            db.updateObject(course, course);

            loadLessons();
            JOptionPane.showMessageDialog(this, lessonToMark.getLessonTitle() + " marked as completed!");

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
            boolean watched = lesson.isWatchedByStudent(student.getUserId());
            String status = watched ? "Completed" : "Pending";

            String[] row = {
                    lesson.getLessonID(),
                    lesson.getLessonTitle(),
                    lesson.getLessonContent(),
                    status
            };
            model.addRow(row);
        }
    }


    private void launchQuiz() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a lesson to take the quiz.");
            return;
        }

        String lessonId = (String) table1.getValueAt(selectedRow, 0);

        Lesson selectedLesson = null;
        for (Lesson l : course.getLessons()) {
            if (l.getLessonID().equals(lessonId)) {
                selectedLesson = l;
                break;
            }
        }

        if (selectedLesson == null) {
            JOptionPane.showMessageDialog(this, "Lesson details not found.");
            return;
        }

        if (!selectedLesson.isWatchedByStudent(student.getUserId())) {
            JOptionPane.showMessageDialog(this, "You must complete the lesson content before attempting the quiz.");
            return;
        }

        if (selectedLesson.getQuiz() == null) {
            JOptionPane.showMessageDialog(this, "This lesson does not have an attached quiz.");
            return;
        }

        User freshStudent = student;
        try {
            UserJsonDatabase userDb = new UserJsonDatabase("users.json");
            freshStudent = (User) userDb.getObjectById(student.getUserId());
        } catch (Exception e) {
            System.out.println("Warning: Could not fetch fresh student data from database.");
        }

        new QuizForm(course, selectedLesson, freshStudent);
    }
}