package SkillForge;

import org.json.JSONObject;

public class Student extends User {
    private String userId;
    private String username;
    private String email;
    private String passwordHash;
    private final  static String role = "student" ;

    public Student(String userId, String username, String email, String passwordHash) {
       super(userId, username, email, passwordHash);
    }
    @Override
    public  Student fromJson(JSONObject obj) {
        return new Student(obj.getString("id"), obj.getString("username"), obj.getString("email"), obj.getString("password"));
    }
    @Override
    public  JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", getUserId());
        jsonObject.put("username", getUserName());
        jsonObject.put("email", getEmail());
        jsonObject.put("password", getPassword());
        jsonObject.put("role", role);
        return jsonObject;
    }

}
