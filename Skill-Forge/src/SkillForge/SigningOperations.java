package SkillForge;

import org.json.JSONArray;
import org.json.JSONObject;

public class SigningOperations {
    private static final UserJsonDatabase db = new UserJsonDatabase("users.json");

    public static User login(String email, String password) {
        JSONArray jsonArray = db.readJsonArrayFromFile();
        String hashedPassword = PasswordHasher.hash(password);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            String storedPasswordHash = object.optString("passwordHash", "");

            if (object.getString("email").equals(email) && hashedPassword.equals(storedPasswordHash)) {
                return new User(object.getString("id"), object.getString("username"), object.getString("email"), object.getString("passwordHash"),object.getString("role"));



            }
        }
        return null;

    }

  public static  boolean  signup(String id,String username,String email, String password,String role) {
        JSONArray jsonArray = db.readJsonArrayFromFile();
        String hashedPassword = PasswordHasher.hash(password);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            if (object.getString("email").equals(email)) {
                return false;
            }
        }
            User u = new User(id,username,email,hashedPassword,role);
            JSONObject o = u.toJson();
            jsonArray.put(o);
            db.writeJsonArrayToFile(jsonArray);
            return  true;
        }


}
