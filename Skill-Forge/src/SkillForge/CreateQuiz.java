package SkillForge;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class CreateQuiz extends JFrame {


    private Course course;
    private Lesson lesson;
    private int totalQuestions;
    private List<Question> tempQuestions = new ArrayList<>();

    private JPanel mainPanel;
    private JTextField quizIdTextField;
    private JTextField numberOfQuestionTextField;
    private JButton nextButton;


    public CreateQuiz(Course course,Lesson lesson) {
        this.course = course;
        this.lesson = lesson;
        setContentPane(mainPanel);
        setTitle("Create Quiz for: " + lesson.getLessonTitle());
        setSize(500, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        quizIdTextField.setText(course.getCourseID()+"-"+lesson.getLessonID() + "-QZ");

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startQuestionsForm();            }
        });
        setVisible(true);
    }
    private void startQuestionsForm() {
        try {
            String quizId = quizIdTextField.getText().trim();

            totalQuestions = Integer.parseInt(numberOfQuestionTextField.getText().trim());

            if (totalQuestions <= 0) {
                JOptionPane.showMessageDialog(this, "Total Questions must be greater than zero.");
                return;
            }

            new QuestionsForm(lesson, quizId, totalQuestions);
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for Total Questions.");
        }
    }

}