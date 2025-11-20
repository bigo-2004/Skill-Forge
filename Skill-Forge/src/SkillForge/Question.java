package SkillForge;

import java.util.Hashtable;

public class Question {
    private String questionId;
    private String questionText;
    private Hashtable<String, String> answers;
    private char correctAnswer;

    public Question( String questionId , String questionText , String[] answers , char correctAnswer ) throws IllegalArgumentException {
        if( answers.length != 4) {
            throw new IllegalArgumentException();
        }
        this.questionText = questionText;
        this.questionId = questionId;
        this.correctAnswer = correctAnswer;
        this.answers = new Hashtable<>();
        this.answers.put("A", answers[0]);
        this.answers.put("B", answers[1]);
        this.answers.put("C", answers[2]);
        this.answers.put("D", answers[3]);
        }
    }

