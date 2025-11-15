package SkillForge;

import org.json.JSONArray;
import org.json.JSONException;

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
    }

    @Override
    public void deleteObject(Object obj) {
    }

    @Override
    public Object getObjectById(Object obj) {
        return null;
    }


}
