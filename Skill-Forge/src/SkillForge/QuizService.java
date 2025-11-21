package SkillForge;

import java.util.ArrayList;
import java.util.Hashtable;

public class QuizService {
    public int gradeSize(Quiz quiz, Hashtable<String, String> studentAnswers) {
     int correctAnswers = 0;
     int questionNumbers = quiz.getQuestionsNumber();
     ArrayList<Question> questions = quiz.getQuestions();
     for( int i = 0; i < questionNumbers; i++ ) {
         String studentChoice = studentAnswers.get(questions.get(i).getQuestionId());
         if( studentChoice.equals(questions.get(i).getCorrectAnswer()) ) {
             correctAnswers++;
         }
     }
     return  (correctAnswers*100) / questionNumbers;

    }

    public boolean isPassed(int score){
        return  score >= 50;
    }
}
