package SkillForge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserJsonDatabase extends JsonDatabase {
    private final String fileName = "user.json";

    public UserJsonDatabase(String fileName) {
        super(fileName);
    }

    @Override
    public JSONArray loadAll() {
        return readJsonArrayFromFile();
    }

    @Override
    public void saveObject(Object obj) {
        User user = (User) obj;
        JSONArray jsonArray = readJsonArrayFromFile();
        JSONObject jsonObject = user.toJson();
        jsonArray.put(jsonObject);
        writeJsonArrayToFile(jsonArray);
    }

    @Override
    public void deleteObject(Object obj) {
    }

    @Override
    public Object getObjectById(String id) {

        JSONArray jsonArray = readJsonArrayFromFile();
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if(jsonObject.getString("id").equals(id) && jsonObject.getString("role").equals("student")) {
                return new Student(jsonObject.getString("id"), jsonObject.getString("username"), jsonObject.getString("email"), jsonObject.getString("password"));
            }
            else if(jsonObject.getString("id").equals(id) && jsonObject.getString("role").equals("instructor")) {
                return new Instructor(jsonObject.getString("id"), jsonObject.getString("username"), jsonObject.getString("email"), jsonObject.getString("password"));
            }
        }
        return null;
    }


}
