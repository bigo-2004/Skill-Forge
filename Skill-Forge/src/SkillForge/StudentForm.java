package SkillForge;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
        setSize(600, 500);
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


        enrollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = CoursesTable.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a course to enroll.");
                    return;
                }

                String courseId = (String) CoursesTable.getValueAt(selectedRow, 0);
                if (courseId.equals("ID")) {
                    JOptionPane.showMessageDialog(null, "Please select a valid course.");
                    return;
                }

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

                    course.getStudents().add(new Student(student.getUserId(), student.getUserName(), student.getEmail()));
                    courseDB.updateObject(course, course);

                    JOptionPane.showMessageDialog(null, "Enrolled successfully!");
                    loadCoursesIntoTable();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error enrolling in course.");
                }
            }
        });
    }


        private void loadCoursesIntoTable() {

            String[] columns = {"Course ID", "Title", "Instructor", "Status"};

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
            String[] defaultRow = {"ID", "Title", "Instructor", "Status"};
            model.addRow(defaultRow);

            CourseJsonDatabase courseDB = new CourseJsonDatabase("courses.json");
            JSONArray arr = courseDB.loadAll();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);

                String id = obj.getString("courseID");
                String title = obj.getString("title");
                String instructor = obj.getString("instructor");
                String status = obj.getString("status");

                String[] row = {id, title, instructor, status};
                model.addRow(row);
            }
        }


    }


