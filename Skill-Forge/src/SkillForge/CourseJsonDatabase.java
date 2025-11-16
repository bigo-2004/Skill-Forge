package SkillForge;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CourseJsonDatabase extends JsonDatabase
{
    public CourseJsonDatabase(String fileName) {
        super(fileName);
    }

    @Override
    public JSONArray loadAll() {
        return readJsonArrayFromFile();
    }

    @Override
    public void saveObject(Object obj) {
        Course course = (Course) obj;
        JSONArray jsonArray = readJsonArrayFromFile();
        JSONObject jsonObject = course.toJson();
        jsonArray.put(jsonObject);
        writeJsonArrayToFile(jsonArray);
    }

    @Override
    public void updateObject(Object objOld, Object newObj) {
        Course courseOld = (Course) objOld;
        Course courseNew = (Course) newObj;
        JSONArray jsonArray = readJsonArrayFromFile();
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if(jsonObject.getString("course ID").equals(courseOld.getCourseID())){
                jsonArray.put(i, courseNew.toJson());
                writeJsonArrayToFile(jsonArray);
                return;
            }
        }
    }

    @Override
    public Object getObjectById(String id) {
        JSONArray jsonArray = readJsonArrayFromFile();
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            if(jsonObject.getString("course ID").equals(id) ) {
                return new Course(jsonObject.getString("course ID"), jsonObject.getString("title"), jsonObject.getString("description"), jsonObject.getString("course status"), jsonObject.getString("instructor"), (ArrayList<Lesson>) jsonObject.get("lessons"));
            }
        }
        return null;
    }
}
