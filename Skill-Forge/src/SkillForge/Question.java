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
}

