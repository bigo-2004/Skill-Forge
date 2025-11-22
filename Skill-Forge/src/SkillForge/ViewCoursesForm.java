package SkillForge;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewCoursesForm extends JFrame {
    private JTable AllCourses;
    private JPanel panel1;

    public ViewCoursesForm() {

        setContentPane(panel1);
        setTitle("All Courses");
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

        AllCourses.setModel(model);
        AllCourses.setRowHeight(30);
        AllCourses.getTableHeader().setReorderingAllowed(false);

        loadAllCourses();
        setVisible(true);
    }

    private void loadAllCourses() {
        DefaultTableModel model = (DefaultTableModel) AllCourses.getModel();
        model.setRowCount(0);

        CourseJsonDatabase db = new CourseJsonDatabase("courses.json");
        JSONArray arr = db.loadAll();

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);

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
