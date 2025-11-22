package SkillForge;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

public class Question {
    private String questionId;
    private String questionText;
    private Hashtable<String, String> answers;
    private String correctAnswer;

    public Question(String questionId, String questionText, String[] answers, String correctAnswer) throws IllegalArgumentException {
        if (answers.length != 4) {
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

    public String getQuestionText() {
        return questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getQuestionId() {return questionId; }

    public Hashtable<String, String> getAnswers() {
        return answers;
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("questionId", questionId);
        jsonObject.put("questionText", questionText);
        jsonObject.put("A", answers.get("A"));
        jsonObject.put("B", answers.get("B"));
        jsonObject.put("C", answers.get("C"));
        jsonObject.put("D", answers.get("D"));
        jsonObject.put("correctAnswer", correctAnswer);
        return jsonObject;

    }

    public static Question fromJSON(JSONObject obj) throws JSONException {
        String questionId = obj.getString("questionId");
        String questionText = obj.getString("questionText");
        String q1 = obj.getString("A");
        String q2 = obj.getString("B");
        String q3 = obj.getString("C");
        String q4 = obj.getString("D");
        String[] answers = {q1,q2,q3,q4};
        String correctAnswer = obj.getString("correctAnswer");
        return new Question(questionId, questionText, answers, correctAnswer);

    }
}

