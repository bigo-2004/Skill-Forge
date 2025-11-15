package SkillForge;

import org.json.JSONObject;

public class Instructor extends User {
    private String userId;
    private String username;
    private String email;
    private String passwordHash;
    private final  static String role = "instructor";

    public Instructor(String userId, String username, String email, String passwordHash) {
      super(userId, username, email, passwordHash);
    }
    @Override
    public Instructor fromJson(JSONObject obj){
        return new Instructor(userId, username, email, passwordHash);

    }

    @Override
    public JSONObject toJson(){
        JSONObject obj = new JSONObject();
        obj.put("id", getUserId());
        obj.put("username", getUserName());
        obj.put("email", getEmail());
        obj.put("passwordHash", getPassword());
        obj.put("role", role);
        return obj;
    }

}
