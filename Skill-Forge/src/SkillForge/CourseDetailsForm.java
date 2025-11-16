package SkillForge;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class CourseDetailsForm extends JFrame {

    private JPanel mainPanel;
    private JTable lessonsTable;
    private JTable studentsTable;
    private JButton addLessonButton;

    private Course course;

    public CourseDetailsForm(Course course) {
        this.course = course;

        setTitle("Course Details: " + course.getCourseTitle());
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        loadLessons();
        loadStudents();

        addLessonButton.addActionListener(e -> {
            new AddLessonForm(course);
        });
    }

    private void loadLessons() {
        String[] columns = {"Lesson ID", "Title", "Content"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        lessonsTable.setModel(model);
        lessonsTable.setRowHeight(30);
        lessonsTable.getTableHeader().setReorderingAllowed(false);
        lessonsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        lessonsTable.setFillsViewportHeight(true);

        for (Lesson lesson : course.getLessons()) {
            String[] row = {lesson.getLessonID(), lesson.getLessonTitle(), lesson.getLessonContent()};
            model.addRow(row);
        }

        lessonsTable.setModel(model);
    }

    private void loadStudents() {
        String[] columns = {"Student ID", "Username", "Email"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };

        studentsTable.setModel(model);
        studentsTable.setRowHeight(30);
        studentsTable.getTableHeader().setReorderingAllowed(false);
        studentsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        studentsTable.setFillsViewportHeight(true);

        UserJsonDatabase userDb = new UserJsonDatabase("users.json");

        ArrayList<Student> students = course.getStudents();
        for (Student s : students) {
            User studentUser = (User) userDb.getObjectById(s.getUserId());

            String studentId = s.getUserId();
            String username = "N/A (Details Missing)";
            String email = "N/A (Details Missing)";

            if (studentUser != null) {
                username = studentUser.getUserName();
                email = studentUser.getEmail();
            } else {
                username = s.getUserName();
                email = s.getEmail();
            }

            String[] row = {studentId, username, email};
            model.addRow(row);
        }

        studentsTable.setModel(model);
    }
}