package SkillForge;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

public class CourseDetailsForm extends JFrame {

    private JPanel mainPanel;
    private JTable lessonsTable;
    private JTable studentsTable;
    private JButton addLessonButton;
    private JButton editLessonButton;
    private JButton removeStudentButton;
    private JButton deleteLessonButton; // The correct name from your form
    private JButton reloadButton;

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

        if (editLessonButton != null) {
            editLessonButton.addActionListener(e -> editLesson());
        }

        if (deleteLessonButton != null) {
            deleteLessonButton.addActionListener(e -> deleteLesson());
        }

        if (removeStudentButton != null) {
            removeStudentButton.addActionListener(e -> removeStudent());
        }

        lessonsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editLesson();
                }
            }
        });
        reloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadLessons();
            }
        });
    }


    private void editLesson() {
        int selectedRow = lessonsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a lesson to edit.");
            return;
        }

        String lessonId = (String) lessonsTable.getValueAt(selectedRow, 0);

        Lesson lessonToEdit = null;
        for (Lesson lesson : course.getLessons()) {
            if (lesson.getLessonID().equals(lessonId)) {
                lessonToEdit = lesson;
                break;
            }
        }

        if (lessonToEdit == null) {
            JOptionPane.showMessageDialog(this, "Lesson not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        new EditLessonForm(course, lessonToEdit, this);
    }

    private void deleteLesson() {
        int selectedRow = lessonsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a lesson to delete.");
            return;
        }

        String lessonIdToRemove = (String) lessonsTable.getValueAt(selectedRow, 0);
        String lessonTitle = (String) lessonsTable.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to permanently delete the lesson: " + lessonTitle + "?",
                "Confirm Lesson Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        boolean removed = false;
        Iterator<Lesson> iterator = course.getLessons().iterator();
        while (iterator.hasNext()) {
            Lesson lesson = iterator.next();
            if (lesson.getLessonID().equals(lessonIdToRemove)) {
                iterator.remove();
                removed = true;
                break;
            }
        }

        if (removed) {
            try {
                CourseJsonDatabase db = new CourseJsonDatabase("courses.json");
                db.updateObject(course, course);
                loadLessons();
                JOptionPane.showMessageDialog(this, "Lesson '" + lessonTitle + "' deleted successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving course changes: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Lesson not found in course list.");
        }
    }

    private void removeStudent() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to remove.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String studentIdToRemove = (String) studentsTable.getValueAt(selectedRow, 0);
        String studentName = (String) studentsTable.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove student " + studentName + " (" + studentIdToRemove + ") from this course?",
                "Confirm Removal", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        boolean removed = false;
        Iterator<Student> iterator = course.getStudents().iterator();
        while (iterator.hasNext()) {
            Student s = iterator.next();
            if (s.getUserId().equals(studentIdToRemove)) {
                iterator.remove();
                removed = true;
                break;
            }
        }

        if (removed) {
            try {
                CourseJsonDatabase db = new CourseJsonDatabase("courses.json");
                db.updateObject(course, course);
                loadStudents();
                JOptionPane.showMessageDialog(this, "Student " + studentIdToRemove + " removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving course changes: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Student not found in course list.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void loadLessons() {
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

    public void loadStudents() {
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