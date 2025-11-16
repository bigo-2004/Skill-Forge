package SkillForge;

import org.json.JSONArray;
import org.json.JSONObject;

public class SigningOperations {
    private static final UserJsonDatabase db = new UserJsonDatabase("users.json");

    public static User login(String email, String password , String expectedRole) {
        JSONArray jsonArray = db.readJsonArrayFromFile();
        String hashedPassword = PasswordHasher.hash(password);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            String storedPasswordHash = object.optString("passwordHash", "");

            if (object.getString("email").equals(email) && hashedPassword.equals(storedPasswordHash) && expectedRole.equals(object.getString("role"))  ) {
                return new Student(object.getString("id"), object.getString("username"), object.getString("email"), object.getString("passwordHash"), object.getString("role"));
            } else if (object.getString("email").equals(email) && hashedPassword.equals(storedPasswordHash) && expectedRole.equals(object.getString("role"))) {
                return new Instructor(object.getString("id"), object.getString("username"), object.getString("email"), object.getString("passwordHash"), object.getString("role"));
            }
        }
        return null;

    }

    public static boolean signup(String id, String username, String email, String password, String role) {
        JSONArray jsonArray = db.readJsonArrayFromFile();
        String hashedPassword = PasswordHasher.hash(password);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            if (object.getString("email").equals(email)) {
                return false;
            }
        }
        if(role.equals("Student")) {
            Student s = new Student(id, username, email, hashedPassword, role);
            JSONObject o = s.toJson();
            jsonArray.put(o);
            db.writeJsonArrayToFile(jsonArray);
            return true;
        }
        else if(role.equals("Instructor")) {
            Instructor i = new Instructor(id, username, email, hashedPassword, role);
            JSONObject o = i.toJson();
            jsonArray.put(o);
            db.writeJsonArrayToFile(jsonArray);
            return true;

        }

        return false;
    }


}
