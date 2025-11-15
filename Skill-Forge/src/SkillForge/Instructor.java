package SkillForge;

import org.json.JSONObject;

public class Instructor extends User {
    private String userId;
    private String username;
    private String email;
    private String passwordHash;
    private final  static String role = "instructor";

    public Instructor(String userId, String username, String email, String passwordHash) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }
    @Override
    public Instructor fromJson(JSONObject obj){
        return new Instructor(userId, username, email, passwordHash);

    }

    @Override
    public JSONObject toJson(){
        JSONObject obj = new JSONObject();
        obj.put("id", userId);
        obj.put("username", username);
        obj.put("email", email);
        obj.put("passwordHash", passwordHash);
        obj.put("role", role);
        return obj;
    }
}
