package SkillForge;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.json.JSONArray;
import org.json.JSONObject;

public class StudentForm extends JFrame{
    private JTable CoursesTable;
    private JButton enrollButton;
    private JButton logoutButton;
    private JPanel mainPanel;

    private User student;

    public StudentForm(User student) {
        this.student = student;

        setTitle("Student Dashboard");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        loadCoursesIntoTable();

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginForm();
            }
        });

        CoursesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewCourseDetails();
                }
            }
        });

        enrollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = CoursesTable.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a course to enroll.");
                    return;
                }

                String courseId = (String) CoursesTable.getValueAt(selectedRow, 0);

                try {
                    CourseJsonDatabase courseDB = new CourseJsonDatabase("courses.json");
                    Course course = (Course) courseDB.getObjectById(courseId);

                    if (course == null) {
                        JOptionPane.showMessageDialog(null, "Course not found.");
                        return;
                    }

                    boolean alreadyEnrolled = false;
                    for (Student s : course.getStudents()) {
                        if (s.getUserId().equals(student.getUserId())) {
                            alreadyEnrolled = true;
                            break;
                        }
                    }

                    if (alreadyEnrolled) {
                        JOptionPane.showMessageDialog(null, "You are already enrolled in this course.");
                        return;
                    }

                    if (!"APPROVED".equalsIgnoreCase(course.getCourseStatus())) {
                        JOptionPane.showMessageDialog(null, "Cannot enroll: This course is currently inactive.");
                        return;
                    }

                    course.getStudents().add(new Student(student.getUserId(), student.getUserName(), student.getEmail()));
                    courseDB.updateObject(course, course);

                    JOptionPane.showMessageDialog(null, "Enrolled successfully!");
                    loadCoursesIntoTable();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error enrolling in course: " + ex.getMessage());
                }
            }
        });
    }

    private void viewCourseDetails() {
        int selectedRow = CoursesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a course to view details.");
            return;
        }

        String courseId = (String) CoursesTable.getValueAt(selectedRow, 0);

        CourseJsonDatabase courseDB = new CourseJsonDatabase("courses.json");
        Course course = (Course) courseDB.getObjectById(courseId);

        if (course == null) {
            JOptionPane.showMessageDialog(null, "Course not found.");
            return;
        }

        boolean isEnrolled = false;
        for (Student s : course.getStudents()) {
            if (s.getUserId().equals(student.getUserId())) {
                isEnrolled = true;
                break;
            }
        }

        if (!isEnrolled) {
            JOptionPane.showMessageDialog(null, "You must be enrolled in this course to view its lessons.");
            return;
        }

        new ViewLessonsForm(course,student);
    }

    private void loadCoursesIntoTable() {
        String[] columns = {"Course ID", "Title", "Description", "Instructor", "Status"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        CoursesTable.setModel(model);
        CoursesTable.setRowHeight(30);
        CoursesTable.getTableHeader().setReorderingAllowed(false);
        CoursesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        CoursesTable.setFillsViewportHeight(true);


        CourseJsonDatabase courseDB = new CourseJsonDatabase("courses.json");
        JSONArray arr = courseDB.loadAll();

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);

            String id = obj.getString("courseID");
            String title = obj.getString("title");
            String description = obj.getString("description");
            String instructor = obj.getString("instructor");
            String status = obj.getString("status");

            if (!"APPROVED".equalsIgnoreCase(status)) {
                continue;
            }

            String[] row = {id, title, description, instructor, status};
            model.addRow(row);
        }
    }
}