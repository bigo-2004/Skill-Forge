package SkillForge;

import org.json.JSONException;
import org.json.JSONObject;

public class Instructor extends User{
    public Instructor(String userId, String userName, String email, String password, String role) {
        super(userId, userName, email, password, role);
    }

    public static Instructor fromJsonToInstructor(JSONObject jsonObject) throws JSONException {
        String userId = jsonObject.getString("id");
        String userName = jsonObject.getString("username");
        String email = jsonObject.getString("email");
        String passwordHash = jsonObject.getString("passwordHash");
        String role = jsonObject.getString("role");

        return new Instructor(userId, userName, email, passwordHash, role);
    }
}
