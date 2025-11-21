package SkillForge;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Course
{
    String courseID;
    String courseTitle;
    String courseDescription;
    String courseStatus;
    String courseInstructor;
    ArrayList<Lesson> lessons;
    ArrayList<Student> students;

    public ArrayList<Student> getStudents() {
        return students;
    }

    public Course(String courseID, String courseTitle, String courseDescription, String courseStatus, String courseInstructor, ArrayList<Lesson> lessons, ArrayList<Student> students)
    {
        this.courseID = courseID;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.courseStatus = courseStatus;
        this.courseInstructor = courseInstructor;
        this.lessons = lessons;
        this.students = students;
    }

    //getters and setters
    //TODO remove useless getters and setters
    public String getCourseID() {
        return courseID;
    }



    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }



    public ArrayList<Lesson> getLessons() {
        return lessons;
    }



    public void addLesson(Lesson lesson)
    {
        lessons.add(lesson);

    }

    public JSONObject toJson() {  //from Object to JSONobject
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("courseID", this.courseID);
        jsonObject.put("title", this.courseTitle);
        jsonObject.put("description", this.courseDescription);
        jsonObject.put("status", this.courseStatus);
        jsonObject.put("instructor", this.courseInstructor);
        JSONArray lessonsArray = new JSONArray();

        for (Lesson l : lessons) {
            JSONObject lessonJson = new JSONObject();
            lessonJson.put("lessonId", l.getLessonID());
            lessonJson.put("title", l.getLessonTitle());
            lessonJson.put("content", l.getLessonContent());
            lessonJson.put("watchedStatus", l.getWatchedStatus());

            if (l.getQuiz() != null) {
                lessonJson.put("quiz", l.getQuiz().toJson());
            }

            lessonsArray.put(lessonJson);
        }

        jsonObject.put("lessons", lessonsArray);
        JSONArray studentsArray = new JSONArray();
        for (Student s : students) {
            studentsArray.put(s.getUserId());
        }
        jsonObject.put("students", studentsArray);

        return jsonObject;
    }
}
