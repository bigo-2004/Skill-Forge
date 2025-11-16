package SkillForge;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.JSONArray;
import org.json.JSONObject;

public class InstructorForm extends JFrame {
    private JButton addCourseButton;
    private JButton viewCourseButton;
    private JButton logoutButton;
    private JPanel mainPanel;
    private JTable table1;

    private User instructor;

    public InstructorForm(User instructor){
        this.instructor=instructor;
        setTitle("Instructor Dashboard");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
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
                loadInstructorCourses();
            }
        });
        viewCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void loadInstructorCourses() {

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
        String[] defaultRow = {"ID", "Title", "Description", "Status", "Enrolled"};
        model.addRow(defaultRow);

        CourseJsonDatabase db = new CourseJsonDatabase("courses.json");
        JSONArray arr = db.loadAll();

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);

            if (!obj.getString("instructor").equals(instructor.getUserId())) {
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




}
