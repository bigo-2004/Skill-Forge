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
    public Object getObjectById(Object obj) {
        return null;
    }


}
