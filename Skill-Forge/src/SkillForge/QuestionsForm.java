package SkillForge;

import org.json.JSONArray;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

public class QuestionsForm extends JFrame {

    private JPanel mainPanel;
    private JTextField questionTextField;
    private JTextField optionATextField;
    private JLabel questionIdLabel;
    private JTextField optionBTextField;
    private JTextField optionCTextField;
    private JTextField optionDTextField;
    private JRadioButton radioButtonA;
    private JRadioButton radioButtonD;
    private JRadioButton radioButtonB;
    private JRadioButton radioButtonC;
    private JButton nextButton;
    private JComboBox<Integer> questionNumberComboBox;

    private Lesson lesson;
    private String quizId;
    private int totalQuestions;
    private int currentQuestionIndex = 1;
    private List<Question> tempQuestions = new ArrayList<>();
    private ButtonGroup answerGroup;
    private Course course;


    public QuestionsForm(Lesson lesson, String quizId, int totalQuestions ,Course c) {
        this.lesson = lesson;
        this.quizId = quizId;
        this.totalQuestions = totalQuestions;
        this.course = c;

        setContentPane(mainPanel);
        setTitle("Create Quiz Questions: " + quizId);
        setContentPane(mainPanel);
        setSize(700, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        answerGroup = new ButtonGroup();
        answerGroup.add(radioButtonA);
        answerGroup.add(radioButtonB);
        answerGroup.add(radioButtonC);
        answerGroup.add(radioButtonD);

        for (int i = 1; i <= totalQuestions; i++) {
            questionNumberComboBox.addItem(i);
        }

        setupListeners();
        loadQuestionInput();

        setVisible(true);
    }

    private void setupListeners() {
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveQuestion();
            }
        });

        questionNumberComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (questionNumberComboBox.getSelectedItem() != null) {
                    currentQuestionIndex = (int) questionNumberComboBox.getSelectedItem();
                    loadQuestionInput();
                }
            }
        });
    }

    private void clearInputFields() {
        questionTextField.setText("");
        optionATextField.setText("");
        optionBTextField.setText("");
        optionCTextField.setText("");
        optionDTextField.setText("");
        answerGroup.clearSelection();
    }

    private void loadQuestionInput() {
        clearInputFields();

        questionIdLabel.setText("Question " + currentQuestionIndex + " of " + totalQuestions +
                " | ID: " + generateQuestionId(currentQuestionIndex));

        int listIndex = currentQuestionIndex - 1;
        if (listIndex >= 0 && listIndex < tempQuestions.size()) {
            Question existingQuestion = tempQuestions.get(listIndex);
            questionTextField.setText(existingQuestion.getQuestionText());

            Hashtable<String, String> answers = existingQuestion.getAnswers();
            optionATextField.setText(answers.get("A"));
            optionBTextField.setText(answers.get("B"));
            optionCTextField.setText(answers.get("C"));
            optionDTextField.setText(answers.get("D"));

            String correctKey = existingQuestion.getCorrectAnswer();
            if ("A".equals(correctKey)) radioButtonA.setSelected(true);
            else if ("B".equals(correctKey)) radioButtonB.setSelected(true);
            else if ("C".equals(correctKey)) radioButtonC.setSelected(true);
            else if ("D".equals(correctKey)) radioButtonD.setSelected(true);

            nextButton.setText("Update Question");

        } else {
            nextButton.setText("Save Question");
        }

        if (tempQuestions.size() == totalQuestions) {
            nextButton.setText("Update & Finish Quiz");
        }
    }

    private String generateQuestionId(int questionNumber) {
        return this.quizId + "-Q" + questionNumber;
    }

    private String getSelectedAnswerKey() {
        if (radioButtonA.isSelected()) return "A";
        if (radioButtonB.isSelected()) return "B";
        if (radioButtonC.isSelected()) return "C";
        if (radioButtonD.isSelected()) return "D";
        return null;
    }

    private void saveQuestion() {
        String correctAnswerKey = getSelectedAnswerKey();
        if (questionTextField.getText().trim().isEmpty() ||
                optionATextField.getText().trim().isEmpty() ||
                optionBTextField.getText().trim().isEmpty() ||
                optionCTextField.getText().trim().isEmpty() ||
                optionDTextField.getText().trim().isEmpty() ||
                correctAnswerKey == null) {

            JOptionPane.showMessageDialog(this, "Please complete all fields and select a correct answer.");
            return;
        }

        String[] answersArray = new String[4];
        answersArray[0] = optionATextField.getText().trim();
        answersArray[1] = optionBTextField.getText().trim();
        answersArray[2] = optionCTextField.getText().trim();
        answersArray[3] = optionDTextField.getText().trim();

        try {
            Question newQuestion = new Question(
                    generateQuestionId(currentQuestionIndex),
                    questionTextField.getText().trim(),
                    answersArray,
                    correctAnswerKey
            );

            int listIndex = currentQuestionIndex - 1;

            if (listIndex < tempQuestions.size()) {
                tempQuestions.set(listIndex, newQuestion);
                JOptionPane.showMessageDialog(this, "Question " + currentQuestionIndex + " updated successfully.");
            } else {
                tempQuestions.add(newQuestion);
                JOptionPane.showMessageDialog(this, "Question " + currentQuestionIndex + " saved successfully.");
            }

            if (tempQuestions.size() == totalQuestions) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "All questions have been entered. Do you want to finalize the quiz now?",
                        "Quiz Complete", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    finishQuizAndSaveToLesson();
                } else {
                    loadQuestionInput();
                }
            } else if (listIndex == tempQuestions.size() - 1 && currentQuestionIndex < totalQuestions) {
                currentQuestionIndex++;
                questionNumberComboBox.setSelectedIndex(currentQuestionIndex - 1);
            } else {
                loadQuestionInput();
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Internal Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void finishQuizAndSaveToLesson() {
        if (tempQuestions.size() != totalQuestions) {
            JOptionPane.showMessageDialog(this,
                    "You must save all " + totalQuestions + " questions before finishing.",
                    "Incomplete Quiz", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Quiz newQuiz = new Quiz(this.quizId, (ArrayList<Question>) tempQuestions);
            lesson.addQuiz(newQuiz);

            CourseJsonDatabase db = new CourseJsonDatabase("courses.json");
            db.updateObject(course,course);



            JOptionPane.showMessageDialog(this,
                    "Quiz '" + newQuiz.getQuizId() + "' successfully created and attached to lesson!");

            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "An error occurred while finalizing the quiz: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}