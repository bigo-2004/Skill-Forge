package SkillForge;

import org.json.JSONArray;
import org.json.JSONObject;

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
        CourseJsonDatabase db = new CourseJsonDatabase("courses.json");
        String title = textField1.getText().trim();
        String content = textField2.getText().trim();

        if (title.isEmpty() || content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in both the Title and Content.");
            return;
        }
        JSONArray  jsonArray = db.readJsonArrayFromFile();

        for (int i = 0; i < jsonArray.length(); i++) {



            JSONObject courseObject = (JSONObject) jsonArray.get(i);
            if (!courseObject.has("lessons")) continue;
            JSONArray lessonsArray = courseObject.getJSONArray("lessons");
            for(int j = 0; j < lessonsArray.length(); j++){
                JSONObject lessonObject = (JSONObject) lessonsArray.get(j);
                String lessonID = lessonObject.getString("lessonId");
                if(lessonID.equals(textField3.getText())){
                    JOptionPane.showMessageDialog(this, "Lesson with this ID already exists.");
                    return;
                }
            }

        }

        Lesson newLesson = new Lesson(
                textField3.getText(),
                title,
                content,
                course.getCourseID()
        );

        course.addLesson(newLesson);


        db.updateObject(course, course);

        JOptionPane.showMessageDialog(this, "Lesson created and saved successfully!");
        dispose();
    }
}