package SkillForge;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.json.JSONArray;
import org.json.JSONObject;

public class InstructorForm extends JFrame {
    private JButton addCourseButton;
    private JButton logoutButton;
    private JPanel mainPanel;
    private JTable table1;
    private JButton reloadButton;
    private JButton deleteCourseButton;
    private JButton viewInsightsButton;

    private User instructor;

    public InstructorForm(User instructor){
        this.instructor=instructor;
        setTitle("Instructor Dashboard");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        loadInstructorCourses();

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginForm();
            }
        });

        addCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreateCourseForm(instructor);
            }
        });

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewCourseDetails();
                }
            }
        });

        if (deleteCourseButton != null) {
            deleteCourseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteCourse();
                }
            });
        }

        reloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadInstructorCourses();
            }
        });
        viewInsightsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                launchInsights();
            }
        });
    }

    private void viewCourseDetails() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a course.");
            return;
        }

        String courseId = (String) table1.getValueAt(selectedRow, 0);

        CourseJsonDatabase db = new CourseJsonDatabase("courses.json");
        Course course = (Course) db.getObjectById(courseId);

        if (course == null) {
            JOptionPane.showMessageDialog(null, "Course not found.");
            return;
        }
        new CourseDetailsForm(course);
    }


    public void loadInstructorCourses() {

        String[] columns = {"Course ID", "Title", "Description", "Status", "Enrolled Students"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table1.setModel(model);
        table1.setRowHeight(30);
        table1.getTableHeader().setReorderingAllowed(false);
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table1.setFillsViewportHeight(true);


        CourseJsonDatabase db = new CourseJsonDatabase("courses.json");
        JSONArray arr = db.loadAll();

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);

            if (!obj.getString("instructor").equals(instructor.getEmail())) {
                continue;
            }

            String id = obj.getString("courseID");
            String title = obj.getString("title");
            String desc = obj.getString("description");
            String status = obj.getString("status");

            JSONArray studentsArray = obj.getJSONArray("students");
            int enrolledCount = studentsArray.length();

            String[] row = {id, title, desc, status, String.valueOf(enrolledCount)};
            model.addRow(row);
        }
    }

    private void deleteCourse() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to delete.");
            return;
        }

        String courseId = (String) table1.getValueAt(selectedRow, 0);
        String courseTitle = (String) table1.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to permanently delete the course: " + courseTitle + " (" + courseId + ")?",
                "Confirm Course Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            CourseJsonDatabase db = new CourseJsonDatabase("courses.json");
            db.deleteObject(courseId);
            loadInstructorCourses();
            JOptionPane.showMessageDialog(this, "Course '" + courseTitle + "' deleted successfully.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting course: " + ex.getMessage());
        }
    }

    private void launchInsights() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to view insights.");
            return;
        }

        String courseId = (String) table1.getValueAt(selectedRow, 0);

        try {
            CourseJsonDatabase courseDb = new CourseJsonDatabase("courses.json");
            Course selectedCourse = (Course) courseDb.getObjectById(courseId);

            if (selectedCourse != null) {
                new InsightsForm(selectedCourse);
            } else {
                JOptionPane.showMessageDialog(this, "Selected course data not found.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading course data: " + ex.getMessage());
        }
    }
}