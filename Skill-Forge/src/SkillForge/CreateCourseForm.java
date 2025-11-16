package SkillForge;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CreateCourseForm extends JFrame{
    private JButton saveButton;
    private JTextField textField1;
    private JTextField textField2;
    private JPanel mainPanel;
    private JTextField textField3;
    private User instructor;

    public CreateCourseForm(User instructor){
        this.instructor=instructor;
        setTitle("Create course");
        setContentPane(mainPanel);
        setSize(500, 250);
        setLocationRelativeTo(null);
        setVisible(true);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createCourse();
            }
        });
    }

    private void createCourse() {

        String title = textField1.getText().trim();
        String description = textField2.getText().trim();

        if (title.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill all fields.");
            return;
        }

        String courseId = textField3.getText();

        ArrayList<Lesson> lessons = new ArrayList<>();
        ArrayList<Student> students = new ArrayList<>();


        Course newCourse = new Course(
                courseId,
                title,
                description,
                "Active",
                instructor.getUserId(),
                lessons,
                students
        );

        CourseJsonDatabase db = new CourseJsonDatabase("courses.json");
        db.saveObject(newCourse);
        JOptionPane.showMessageDialog(null, "Course created successfully!");
        dispose();
    }
}
