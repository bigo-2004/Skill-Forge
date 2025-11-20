package SkillForge;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;

public class Quiz {
    private ArrayList<Question> questions;
    private String quizId;

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("quizId", quizId);
        JSONArray jsonQuestions = new JSONArray();
        for(int i = 0 ; i < this.questions.size();i++){
            jsonQuestions.put(this.questions.get(i).toJSON());
        }
        obj.put("questions", jsonQuestions);
        return obj;
    }
}
