package SkillForge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserJsonDatabase extends JsonDatabase {


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


    public void deleteUser(User user) {

        JSONArray oldJsonArray = readJsonArrayFromFile();
        JSONArray newJsonArray = new JSONArray();
        for(int i = 0; i < oldJsonArray.length(); i++){
            JSONObject jsonObject = oldJsonArray.getJSONObject(i);
            if(!jsonObject.get("id").equals(user.getUserId())){
                newJsonArray.put(jsonObject);
            }
        }
        writeJsonArrayToFile(newJsonArray);

    }

    @Override
    public Object getObjectById(String id) {

        JSONArray jsonArray = readJsonArrayFromFile();
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if(jsonObject.getString("id").equals(id) ) {
                return new User(jsonObject.getString("id"), jsonObject.getString("username"), jsonObject.getString("email"), jsonObject.getString("passwordHash"), jsonObject.getString("role"));
            }
        }
        return null;
    }

    @Override
    public void updateObject(Object oldObj, Object newObj) {
        User userOld = (User) oldObj;
        User userNew = (User) newObj;
        JSONArray jsonArray = readJsonArrayFromFile();
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if(jsonObject.getString("id").equals(userOld.getUserId())){
                jsonArray.put(i, userNew.toJson());
                writeJsonArrayToFile(jsonArray);
                return;
            }
        }
    }


}
