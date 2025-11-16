package SkillForge;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditLessonForm extends JFrame {

    private JPanel mainPanel;
    private JTextField textField1; // title
    private JTextField textField2; // content
    private JButton saveButton;
    private JTextField textField3; // id

    private Course course;
    private Lesson lesson;
    private CourseDetailsForm parentForm;

    public EditLessonForm(Course course, Lesson lesson, CourseDetailsForm parentForm) {
        this.course = course;
        this.lesson = lesson;
        this.parentForm = parentForm;

        setTitle("Edit Lesson: " + lesson.getLessonTitle());
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setVisible(true);

        textField3.setText(lesson.getLessonID());
        textField3.setEditable(false);
        textField1.setText(lesson.getLessonTitle());
        textField2.setText(lesson.getLessonContent());

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveLessonChanges();
            }
        });
    }

    private void saveLessonChanges() {
        String newTitle = textField1.getText().trim();
        String newContent = textField2.getText().trim();

        if (newTitle.isEmpty() || newContent.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title and Content cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        lesson.setLessonTitle(newTitle);
        lesson.setLessonContent(newContent);

        try {
            CourseJsonDatabase db = new CourseJsonDatabase("courses.json");
            db.updateObject(course, course);

            JOptionPane.showMessageDialog(this, "Lesson updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            if (parentForm != null) {
                parentForm.loadLessons();
            }

            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving changes: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}