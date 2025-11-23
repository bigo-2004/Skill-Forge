package SkillForge;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class Student extends User {
    //                key -> will be lessonId value will be scored
    private Hashtable<String, Integer> quizResults = new Hashtable<>();
    private JSONArray certificates = new JSONArray();

    public Student(String userId, String userName, String email, String password, String role) {
        super(userId, userName, email, password, role);
    }

    public Student(String userId, String userName, String email) {
        super(userId, userName, email);
    }

    public Hashtable<String, Integer> getQuizResults() {
        return quizResults;
    }

    public void setQuizResults(Hashtable<String, Integer> quizResults) {
        this.quizResults = quizResults;

    }

    public JSONArray getCertificates() {
        return certificates;
    }

    public void setCertificates(JSONArray certificates) {
        this.certificates = certificates;
    }

    @Override
    public JSONObject toJson() {  //from Object to JSONobject
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", getUserId());
        jsonObject.put("username", getUserName());
        jsonObject.put("email", getEmail());
        jsonObject.put("passwordHash", getPassword());
        jsonObject.put("role", getRole());
        JSONObject jsonQuiz = new JSONObject();
        for (String key : quizResults.keySet()) {
            jsonQuiz.put(key, quizResults.get(key));
        }
        jsonObject.put("certificates", certificates);
        jsonObject.put("quizResults", jsonQuiz);
        return jsonObject;
    }

    public static Student fromJsonToStudent(JSONObject jsonObject) {
        Student s = new Student(jsonObject.getString("id"),
                jsonObject.getString("username"),
                jsonObject.getString("email")
                ,jsonObject.getString("passwordHash"),
                jsonObject.getString("role"));

        if (jsonObject.has("quizResults")) {
            JSONObject quizResultsObject = jsonObject.getJSONObject("quizResults");
            Hashtable<String, Integer> quizResults = new Hashtable<>();
            ArrayList<String> keys = new ArrayList<>(quizResultsObject.keySet());
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                int value = quizResultsObject.getInt(key);
                quizResults.put(key, value);
            }
            if (jsonObject.has("certificates")) {
                s.setCertificates(jsonObject.getJSONArray("certificates"));
            }
            s.setQuizResults(quizResults);
        }
        return s;
    }
}
