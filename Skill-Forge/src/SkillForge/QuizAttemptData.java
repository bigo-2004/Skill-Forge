package SkillForge;

import org.json.JSONObject;

public class QuizAttemptData {
    private int attempts;
    private int bestScore;

    public QuizAttemptData(int attempts, int bestScore) {
        this.attempts = attempts;
        this.bestScore = bestScore;
    }

    public int getAttempts() {
        return attempts;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void recordAttempt(int score) {
        attempts++;
        if (score > bestScore) {
            bestScore = score;
        }
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("attempts", attempts);
        obj.put("bestScore", bestScore);
        return obj;
    }

    public static QuizAttemptData fromJson(JSONObject obj) {
        int attempts = obj.has("attempts") ? obj.getInt("attempts") : 0;
        int bestScore = obj.has("bestScore") ? obj.getInt("bestScore") : 0;
        return new QuizAttemptData(attempts, bestScore);
    }
}