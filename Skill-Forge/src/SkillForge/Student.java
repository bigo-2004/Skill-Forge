package SkillForge;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;

public class Student extends User {
    //                key -> will be lessonId value will be scored
    private Hashtable<String, Integer> quizResults = new Hashtable<>();

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
        jsonObject.put("quizResults", jsonQuiz);
        return jsonObject;
    }

//    public static Student fromJsonToStudent(JSONObject jsonObject) {
//        Student s = new Student(jsonObject.getString("id"),
//                jsonObject.getString("username"),
//                jsonObject.getString("email")
//                , jsonObject.getString("passwordHash"),
//                jsonObject.getString("role"));
//
//        if (jsonObject.has("quizResults")) {
//            JSONObject quizResultsObject = jsonObject.getJSONObject("quizResults");
//            Hashtable<String, Integer> quizResults = new Hashtable<>();
//
//            for (String key : quizResultsObject.keySet()) {
//                quizResults.put(key, quizResultsObject.getInt(key));
//            }
//
//            s.setQuizResults(quizResults);
//        }
//        return s;
//    }
}
