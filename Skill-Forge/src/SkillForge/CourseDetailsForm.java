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
    private JButton deleteLessonButton;
    private JButton reloadButton;
    private JTextField titleField;
    private JTextField discriptionField;
    private JTextField idField;
    private JButton saveButton;
    private JButton addQuizButton;
    private JButton deleteQuizButton;

    private Course course;

    public CourseDetailsForm(Course course) {
        this.course = course;

        setTitle("Course Details: " + course.getCourseTitle());
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 700);
        setLocationRelativeTo(null);
        setVisible(true);


        loadCourseDetails();
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
                loadCourseDetails();
                loadLessons();
                loadStudents();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCourseDetails();
            }
        });
        addQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addQuizToLesson();
            }
        });
        deleteQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteQuizFromLesson();
            }
        });
    }

    private void addQuizToLesson() {
        int selectedRow = lessonsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a lesson to add a quiz to.");
            return;
        }
        String lessonId = (String) lessonsTable.getValueAt(selectedRow, 0);

        Lesson lessonToManageQuiz = null;
        for (Lesson lesson : course.getLessons()) {
            if (lesson.getLessonID().equals(lessonId)) {
                lessonToManageQuiz = lesson;
                break;
            }
        }
        if (lessonToManageQuiz == null) {
            JOptionPane.showMessageDialog(this, "Lesson not found.");
            return;
        }

        if (lessonToManageQuiz.getQuiz() != null) {
            JOptionPane.showMessageDialog(this,
                    "A quiz already exists for this lesson. Please use the 'Edit Lesson' button or double-click the lesson to modify the quiz questions.");
            return;
        }

        new CreateQuiz(course,lessonToManageQuiz);

    }

    private void deleteQuizFromLesson() {
        int selectedRow = lessonsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a lesson whose quiz you want to delete.");
            return;
        }
        String lessonId = (String) lessonsTable.getValueAt(selectedRow, 0);

        Lesson lessonToDeleteQuizFrom = null;
        for (Lesson lesson : course.getLessons()) {
            if (lesson.getLessonID().equals(lessonId)) {
                lessonToDeleteQuizFrom = lesson;
                break;
            }
        }

        if (lessonToDeleteQuizFrom == null || lessonToDeleteQuizFrom.getQuiz() == null) {
            JOptionPane.showMessageDialog(this, "The selected lesson has no quiz to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to permanently delete the quiz for lesson: " + lessonToDeleteQuizFrom.getLessonTitle() + "?",
                "Confirm Quiz Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String deletedQuizId = lessonToDeleteQuizFrom.getQuiz().getQuizId();

        try {
            lessonToDeleteQuizFrom.setQuiz(null);
            removeQuizResultsFromAllStudents(deletedQuizId);
            CourseJsonDatabase db = new CourseJsonDatabase("courses.json");
            db.updateObject(course, course);

            loadLessons();
            JOptionPane.showMessageDialog(this, "Quiz successfully deleted from lesson" + lessonToDeleteQuizFrom.getLessonTitle() );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting quiz: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void removeQuizResultsFromAllStudents(String quizId) {
        UserJsonDatabase userDb = new UserJsonDatabase("users.json");

        try {
            org.json.JSONArray jsonArray = userDb.loadAll();

            for (int i = 0; i < jsonArray.length(); i++) {
                org.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
                String userId = jsonObject.getString("id");

                Object userObj = userDb.getObjectById(userId);

                if (userObj instanceof Student) {
                    Student student = (Student) userObj;

                    if (student.getQuizResults().containsKey(quizId)) {
                        student.getQuizResults().remove(quizId);
                        userDb.updateObject(student, student);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error cleaning up quiz results for quiz ID " + quizId + ": " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Warning: Could not remove quiz results from all students in the database.");
        }
    }

    private void loadCourseDetails() {
        idField.setText(course.getCourseID());
        idField.setEditable(false);

        titleField.setText(course.getCourseTitle());
        discriptionField.setText(course.getCourseDescription());


    }

    private void saveCourseDetails() {
        String newTitle = titleField.getText().trim();
        String newDescription = discriptionField.getText().trim();



        if (newTitle.isEmpty() || newDescription.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title and Description cannot be empty.");
            return;
        }

        course.setCourseTitle(newTitle);
        course.setCourseDescription(newDescription);


        try {
            CourseJsonDatabase db = new CourseJsonDatabase("courses.json");
            db.updateObject(course, course);

            JOptionPane.showMessageDialog(this, "Course details saved successfully! ");

            setTitle("Course Details: " + course.getCourseTitle());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving course details: " + ex.getMessage());
        }
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
        String[] columns = {"Lesson ID", "Title", "Quiz Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        lessonsTable.setModel(model);
        lessonsTable.setRowHeight(30);
        lessonsTable.getTableHeader().setReorderingAllowed(false);
        lessonsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        lessonsTable.setFillsViewportHeight(true);


        for (Lesson lesson : course.getLessons()) {
            String quizStatus = lesson.getQuiz() != null ? "Quiz Exists" : "No Quiz";
            String[] row = {lesson.getLessonID(), lesson.getLessonTitle(), quizStatus};
            model.addRow(row);
        }

        lessonsTable.setModel(model);
    }

    public void loadStudents() {
        int totalQuizzesInCourse = 0;
        for (Lesson lesson : course.getLessons()) {
            if (lesson.getQuiz() != null) {
                totalQuizzesInCourse++;
            }
        }

        String[] columns = {"Student ID", "Username", "Email", "Quizzes Done"};
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
            Student currentStudentData = (Student) userDb.getObjectById(s.getUserId());

            if (currentStudentData == null) {
                continue;
            }


            String studentId = s.getUserId();
            String username = "N/A (Details Missing)";
            String email = "N/A (Details Missing)";
            String quizzesDoneText;

            username = currentStudentData.getUserName();
            email = currentStudentData.getEmail();


            int quizzesCompleted = 0;
            for (Lesson lesson : course.getLessons()) {
                if (lesson.getQuiz() != null) {
                    String quizId = lesson.getQuiz().getQuizId();
                    if (currentStudentData.getQuizResults().containsKey(quizId)) {
                        quizzesCompleted++;
                    }
                }
            }

            quizzesDoneText = quizzesCompleted + " / " + totalQuizzesInCourse;

            String[] row = {studentId, username, email, quizzesDoneText};
            model.addRow(row);
        }

        studentsTable.setModel(model);
    }
}