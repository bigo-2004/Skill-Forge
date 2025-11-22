package SkillForge;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

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

            if(jsonObject.getString("courseID").equals(courseOld.getCourseID())){
                jsonArray.put(i, courseNew.toJson());
                writeJsonArrayToFile(jsonArray);
                return;
            }
        }
    }

    @Override
    public Object getObjectById(String id) {
        JSONArray jsonArray = readJsonArrayFromFile();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            if (jsonObject.getString("courseID").equals(id)) {

                JSONArray lessonsArray = jsonObject.getJSONArray("lessons");
                ArrayList<Lesson> lessons = new ArrayList<>();

                for (int j = 0; j < lessonsArray.length(); j++) {
                    JSONObject lessonJson = lessonsArray.getJSONObject(j);

                    Lesson lesson = new Lesson(
                            lessonJson.getString("lessonId"),
                            lessonJson.getString("title"),
                            lessonJson.getString("content"),
                            id
                    );

                    if (lessonJson.has("watchedStatus")) {

                            JSONObject statusJson = lessonJson.getJSONObject("watchedStatus");
                            Hashtable<String, Boolean> watchedStatusMap = new Hashtable<>();

                            Iterator<String> keys = statusJson.keys();
                            while (keys.hasNext()) {
                                String studentId = keys.next();
                                watchedStatusMap.put(studentId, statusJson.getBoolean(studentId));
                            }
                            lesson.setWatchedStatus(watchedStatusMap);

                    }

                    lessons.add(lesson);
                }

                JSONArray studentsArray = jsonObject.getJSONArray("students");
                ArrayList<Student> students = new ArrayList<>();

                for (int k = 0; k < studentsArray.length(); k++) {
                    String studentId = studentsArray.getString(k);
                    students.add(new Student(studentId, "", ""));
                }

                return new Course(
                        jsonObject.getString("courseID"),
                        jsonObject.getString("title"),
                        jsonObject.getString("description"),
                        jsonObject.getString("status"),
                        jsonObject.getString("instructor"),
                        lessons,
                        students
                );
            }
        }
        return null;
    }

    public void deleteObject(String id) {
        JSONArray jsonArray = readJsonArrayFromFile();

        for (int i = jsonArray.length() - 1; i >= 0; i--) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            if (jsonObject.getString("courseID").equals(id)) {
                jsonArray.remove(i);
                writeJsonArrayToFile(jsonArray);
                return;
            }
        }
    }
}