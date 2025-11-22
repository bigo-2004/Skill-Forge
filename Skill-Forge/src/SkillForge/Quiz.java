package SkillForge;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;

public class Quiz {
    private String quizId;
    private ArrayList<Question> questions;

    public Quiz(String quizId, ArrayList<Question> questions) {
        this.quizId = quizId;
        this.questions = questions;
    }

    public ArrayList<Question> returnQuestions() {
        return questions;
    }

    public String getQuizId() {
        return quizId;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("quizId", quizId);
        JSONArray jsonQuestions = new JSONArray();
        for (int i = 0; i < this.questions.size(); i++) {
            jsonQuestions.put(this.questions.get(i).toJSON());
        }
        obj.put("questions", jsonQuestions);
        return obj;
    }

    public int getQuestionsNumber() {
        return this.questions.size();
    }

    public static Quiz fromJson(JSONObject obj) {
        String quizId = obj.getString("quizId");
        ArrayList<Question> questions = new ArrayList<>();
        JSONArray jsonQuestions = obj.getJSONArray("questions");
        for (int i = 0; i < jsonQuestions.length(); i++) {
            JSONObject jsonQuestion = jsonQuestions.getJSONObject(i);
            questions.add(Question.fromJSON(jsonQuestion));
        }
        return new Quiz(quizId, questions);
    }
}
