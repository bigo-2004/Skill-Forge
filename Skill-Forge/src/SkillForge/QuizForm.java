package SkillForge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class QuizForm extends JFrame{
    private JPanel mainPanel;
    private JLabel questionLabel;
    private JRadioButton optionA;
    private JRadioButton optionB;
    private JRadioButton optionC;
    private JRadioButton optionD;
    private JButton nextButton;
    private JButton submitButton;
    private JLabel progressLabel;
    private JButton previousButton;
    private ButtonGroup optionsGroup;

    private Course course;
    private Lesson lesson;
    private User student;
    private Quiz quiz;

    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    private char[] studentAnswers;

    public QuizForm(Course course, Lesson lesson, User student){
        this.course =course;
        this.lesson=lesson;
        this.student=student;
        this.quiz = lesson.getQuiz();

        if (quiz == null || quiz.getQuestions() == null || quiz.getQuestions().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No quiz found for this lesson.");
            return;
        }

        this.questions = quiz.getQuestions();
        this.studentAnswers = new char[questions.size()];

        setTitle("Quiz: " + lesson.getLessonTitle());
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel.setLayout(new BorderLayout(10, 10));

        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.add(questionLabel, BorderLayout.NORTH);
        mainPanel.add(questionPanel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(4, 1, 5, 5));

        optionsGroup = new ButtonGroup();
        optionsGroup.add(optionA);
        optionsPanel.add(optionA);
        optionsGroup.add(optionB);
        optionsPanel.add(optionB);
        optionsGroup.add(optionC);
        optionsPanel.add(optionC);
        optionsGroup.add(optionD);
        optionsPanel.add(optionD);

        mainPanel.add(optionsPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        progressLabel.setText("Question 1 of " + questions.size());
        submitButton.setEnabled(false);
        previousButton.setEnabled(false);

        controlPanel.add(progressLabel);
        controlPanel.add(nextButton);
        controlPanel.add(submitButton);
        controlPanel.add(previousButton);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAnswer();
                if (currentQuestionIndex < questions.size() - 1) {
                    currentQuestionIndex++;
                    loadQuestion(currentQuestionIndex);
                } else {
                    saveAnswer();
                    nextButton.setEnabled(false);
                    submitButton.setEnabled(true);
                    JOptionPane.showMessageDialog(QuizForm.this,
                            "You have reached the end of the quiz. Click Submit to see your results.",
                            "Ready to Submit", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitQuiz();
            }
        });

        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAnswer();
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex--;
                    loadQuestion(currentQuestionIndex);
                    nextButton.setEnabled(true);
                    submitButton.setEnabled(false);
                }
                if (currentQuestionIndex == 0) {
                    previousButton.setEnabled(false);
                }
            }
        });

        loadQuestion(currentQuestionIndex);
        setVisible(true);

    }

    private void saveAnswer() {
        char selectedOption = ' ';
        if (optionA.isSelected())
            selectedOption = 'A';
        else if (optionB.isSelected())
            selectedOption = 'B';
        else if (optionC.isSelected())
            selectedOption = 'C';
        else if (optionD.isSelected())
            selectedOption = 'D';

        studentAnswers[currentQuestionIndex] = selectedOption;
    }

    private void loadQuestion(int index) {
        Question q = questions.get(index);
        questionLabel.setText("Q" + (index + 1) + ": " + q.getQuestionText());

        optionA.setText("A) " + q.getAnswers().get("A"));
        optionB.setText("B) " + q.getAnswers().get("B"));
        optionC.setText("C) " + q.getAnswers().get("C"));
        optionD.setText("D) " + q.getAnswers().get("D"));

        optionsGroup.clearSelection();

        char savedAnswer = studentAnswers[index];
        if (savedAnswer == 'A')
            optionA.setSelected(true);
        else if (savedAnswer == 'B')
            optionB.setSelected(true);
        else if (savedAnswer == 'C')
            optionC.setSelected(true);
        else if (savedAnswer == 'D')
            optionD.setSelected(true);

        progressLabel.setText("Question " + (index + 1) + " of " + questions.size());
        if (index == questions.size() - 1) {
            nextButton.setText("Finish");
        } else {
            nextButton.setText("Next");
        }
    }

    private void submitQuiz() {
        int maxScore = questions.size();
        correctAnswers = 0;

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            String correctAns = q.getCorrectAnswer();
            char studentAns = studentAnswers[i];
            if (studentAns != ' ' && String.valueOf(studentAns).equals(correctAns)) {
                correctAnswers++;
            }
        }
        double pass = 0.5;
        boolean passed = (double) correctAnswers / maxScore >= 0.5;




        showResults(maxScore, passed);
        this.dispose();
    }

    private void showResults(int maxScore, boolean passed) {
        String message = String.format(
                "Quiz Complete!\n\nYour Score: %d / %d (%.0f%%)\nStatus: %s",
                correctAnswers, maxScore,
                ((double)correctAnswers / maxScore) * 100,
                passed ? "PASSED" : "FAILED"
        );

        JOptionPane.showMessageDialog(null, message, "Quiz Results", JOptionPane.INFORMATION_MESSAGE);
    }
}


