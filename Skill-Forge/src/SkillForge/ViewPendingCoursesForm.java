package SkillForge;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.JSONArray;
import org.json.JSONObject;

public class ViewPendingCoursesForm extends JFrame {

    private JTable PendingCourses;
    private JPanel panel1;
    private JButton btnApprove;
    private JButton btnReject;

    public ViewPendingCoursesForm() {

        setContentPane(panel1);
        setTitle("Pending Courses");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columns = {"Course ID", "Title", "Instructor", "Description", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        PendingCourses.setModel(model);
        PendingCourses.setRowHeight(30);
        PendingCourses.getTableHeader().setReorderingAllowed(false);

        btnApprove.addActionListener(e -> approveCourse());
        btnReject.addActionListener(e -> rejectCourse());

        loadPendingCourses();
        setVisible(true);
    }

    private void loadPendingCourses() {
        DefaultTableModel model = (DefaultTableModel) PendingCourses.getModel();
        model.setRowCount(0);

        CourseJsonDatabase db = new CourseJsonDatabase("courses.json");
        JSONArray arr = db.loadAll();

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);

            if (obj.getString("status").equalsIgnoreCase("PENDING")) {

                Course c = new Course(
                        obj.getString("courseID"),
                        obj.getString("title"),
                        obj.getString("description"),
                        obj.getString("status"),
                        obj.getString("instructor"),
                        new java.util.ArrayList<>(),
                        new java.util.ArrayList<>()
                );

                Object[] row = {
                        c.getCourseID(),
                        c.getCourseTitle(),
                        c.getCourseInstructor(),
                        c.getCourseDescription(),
                        c.getCourseStatus()
                };

                model.addRow(row);
            }
        }
    }

    private void approveCourse() {
        int selectedRow = PendingCourses.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to approve.");
            return;
        }

        String courseId = PendingCourses.getValueAt(selectedRow, 0).toString();
        CourseJsonDatabase db = new CourseJsonDatabase("courses.json");
        Course course = (Course) db.getObjectById(courseId);

        if (course != null) {
            course.setCourseStatus("APPROVED");
            db.updateObject(course, course);
            JOptionPane.showMessageDialog(this, "Course approved successfully!");
            loadPendingCourses();
        }
    }

    private void rejectCourse() {
        int selectedRow = PendingCourses.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to reject.");
            return;
        }

        String courseId = PendingCourses.getValueAt(selectedRow, 0).toString();
        CourseJsonDatabase db = new CourseJsonDatabase("courses.json");
        Course course = (Course) db.getObjectById(courseId);

        if (course != null) {
            course.setCourseStatus("REJECTED");
            db.updateObject(course, course);
            JOptionPane.showMessageDialog(this, "Course rejected successfully!");
            loadPendingCourses();
        }
    }
}
