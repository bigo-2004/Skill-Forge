package SkillForge;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddLessonForm extends JFrame {
    private JButton saveButton;
    private JTextField textField1;
    private JTextField textField2;
    private JPanel mainPanel;
    private JTextField textField3;
    private Course course;

    public AddLessonForm(Course course){
        this.course = course;
        setTitle("Add Lesson to " + course.getCourseTitle());
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setVisible(true);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createLesson();
            }
        });
    }

    private void createLesson() {
        String title = textField1.getText().trim();
        String content = textField2.getText().trim();

        if (title.isEmpty() || content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in both the Title and Content.");
            return;
        }

        Lesson newLesson = new Lesson(
                textField3.getText(),
                title,
                content,
                course.getCourseID()
        );

        course.addLesson(newLesson);

        CourseJsonDatabase db = new CourseJsonDatabase("courses.json");
        db.updateObject(course, course);

        JOptionPane.showMessageDialog(this, "Lesson created and saved successfully!");
        dispose();
    }
}