// SkillForge/Student.java
package SkillForge;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Student extends User {
    private Hashtable<String, QuizAttemptData> quizResults = new Hashtable<>();
    private JSONArray certificates = new JSONArray();

    public Student(String userId, String userName, String email, String password, String role) {
        super(userId, userName, email, password, role);
    }

    public Student(String userId, String userName, String email) {
        super(userId, userName, email);
    }

    public Hashtable<String, QuizAttemptData> getQuizResults() {
        return quizResults;
    }

    public void setQuizResults(Hashtable<String, QuizAttemptData> quizResults) {
        this.quizResults = quizResults;

    }

    public JSONArray getCertificates() {
        return certificates;
    }

    public void setCertificates(JSONArray certificates) {
        this.certificates = certificates;
    }

    public void recordQuizAttempt(String quizId, int score) {
        QuizAttemptData data = quizResults.get(quizId);
        if (data == null) {
            data = new QuizAttemptData(0, 0);
        }

        if (data.getAttempts() < 2) {
            data.recordAttempt(score);
            quizResults.put(quizId, data);
        }
    }

    public boolean canAttemptQuiz(String quizId) {
        QuizAttemptData data = quizResults.get(quizId);
        if (data == null) return true;
        if (data.getBestScore() >= 50) return false;
        return data.getAttempts() < 2;
    }

    public boolean hasPassedLesson(String quizId) {
        QuizAttemptData data = quizResults.get(quizId);
        return data != null && data.getBestScore() >= 50;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", getUserId());
        jsonObject.put("username", getUserName());
        jsonObject.put("email", getEmail());
        jsonObject.put("passwordHash", getPassword());
        jsonObject.put("role", getRole());

        JSONObject jsonQuiz = new JSONObject();
        for (String key : quizResults.keySet()) {
            QuizAttemptData data = quizResults.get(key);
            if (data != null) {
                jsonQuiz.put(key, data.toJson());
            }
        }
        jsonObject.put("certificates", certificates);
        jsonObject.put("quizResults", jsonQuiz);
        return jsonObject;
    }


    public static Student fromJsonToStudent(JSONObject jsonObject) {
        Student s = new Student(
                jsonObject.getString("id"),
                jsonObject.getString("username"),
                jsonObject.getString("email"),
                jsonObject.optString("passwordHash", ""),   // safety
                jsonObject.optString("role", "Student")
        );

        if (jsonObject.has("quizResults")) {
            JSONObject quizResultsObject = jsonObject.getJSONObject("quizResults");
            Hashtable<String, QuizAttemptData> quizResults = new Hashtable<>();
            List<String> keys = new ArrayList<>(quizResultsObject.keySet());
            for (String key : keys) {
                Object entry = quizResultsObject.get(key);
                if (entry instanceof JSONObject) {
                    JSONObject attemptObj = quizResultsObject.getJSONObject(key);
                    QuizAttemptData data = QuizAttemptData.fromJson(attemptObj);
                    quizResults.put(key, data);
                } else {
                    try {
                        int value = quizResultsObject.getInt(key);
                        QuizAttemptData data = new QuizAttemptData(1, value);
                        quizResults.put(key, data);
                    } catch (Exception ex) {
                    }
                }
            }
            if (jsonObject.has("certificates")) {
                s.setCertificates(jsonObject.getJSONArray("certificates"));
            }
            s.setQuizResults(quizResults);
        }
        return s;
    }
}
