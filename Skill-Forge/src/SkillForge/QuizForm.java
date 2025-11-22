package SkillForge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.List;

public class QuizForm extends JFrame {
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
    private char[] studentAnswers;

    public QuizForm(Course course, Lesson lesson, User student) {
        this.course = course;
        this.lesson = lesson;
        this.student = student;
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

        optionsGroup = new ButtonGroup();
        optionsGroup.add(optionA);
        optionsGroup.add(optionB);
        optionsGroup.add(optionC);
        optionsGroup.add(optionD);

        progressLabel.setText("Question 1 of " + questions.size());
        submitButton.setEnabled(false);
        previousButton.setEnabled(false);

        setContentPane(mainPanel);


        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAnswer();
                if (currentQuestionIndex < questions.size() - 1) {
                    currentQuestionIndex++;
                    loadQuestion(currentQuestionIndex);
                    previousButton.setEnabled(true);
                } else {
                    nextButton.setEnabled(false);
                    submitButton.setEnabled(true);
                    JOptionPane.showMessageDialog(QuizForm.this, "You have reached the end of the quiz.\nClick Submit to see your results.");
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
        saveAnswer();
        Hashtable<String, String> studentAnswersMap = new Hashtable<>();
        int x = 0;

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            String studentChoice = String.valueOf(studentAnswers[i]);

            if (!studentChoice.trim().isEmpty()) {
                studentAnswersMap.put(q.getQuestionId(), studentChoice);
                if (q.getCorrectAnswer().equals(studentChoice)) {
                    x++;
                }
            }
        }

        int finalScorePercentage = 0;

        if (!questions.isEmpty()) {
            finalScorePercentage = QuizService.quizMarks(quiz, studentAnswersMap);
        }

        boolean passed = QuizService.isPassed(finalScorePercentage);
        try {
            UserJsonDatabase userDb = new UserJsonDatabase("users.json");
            Student studentOld = (Student) student;
            Student studentNew = Student.fromJsonToStudent(studentOld.toJson());
            Hashtable<String, Integer> merged = studentOld.getQuizResults();
            merged.put(lesson.getQuiz().getQuizId(), finalScorePercentage);
            studentNew.setQuizResults(merged);
            userDb.updateObject(studentOld, studentNew);
            studentOld.setQuizResults(studentNew.getQuizResults());
            showResults(x, questions.size(), finalScorePercentage, passed);
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error saving quiz results: " + ex.getMessage() + "\nData may not be saved.",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showResults(int correctCount, int totalQuestions, int scorePercentage, boolean passed) {
        String message = String.format(
                "Quiz Complete!\n\n" +
                        "Correct Answers: %d / %d\n" +
                        "Final Percentage: %d%%\n" +
                        "Status: %s",
                correctCount, totalQuestions, scorePercentage,
                passed ? "PASSED " : "FAILED "
        );

        JOptionPane.showMessageDialog(null, message, "Quiz Results", JOptionPane.INFORMATION_MESSAGE);
    }
}