package SkillForge;

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

    public Course(String courseID, String courseTitle, String courseDescription, String courseStatus, String courseInstructor, ArrayList<Lesson> lessons)
    {
        this.courseID = courseID;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.courseStatus = courseStatus;
        this.courseInstructor = courseInstructor;
        this.lessons = lessons;
    }

    //getters and setters
    //TODO remove useless getters and setters
    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
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

    public String getCourseInstructor() {
        return courseInstructor;
    }

    public void setCourseInstructor(String courseInstructor) {
        this.courseInstructor = courseInstructor;
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(ArrayList<Lesson> lessons) {
        this.lessons = lessons;
    }

    public String getCourseDetails()
    {
        return String.format("""
                Course ID: %s
                Course title: %s
                Course description: %s
                Course status: %s
                Course instructor: %s 
                """, courseID, courseTitle, courseDescription, courseStatus, courseInstructor);
        //TODO courseInstructor.getID() getter
    }

    public void updateCourse(String title, String description)
    {
        courseTitle = title;
        courseDescription = description;
        //TODO save to file
    }

    public void addLesson(Lesson lesson)
    {
        lessons.add(lesson);
        //TODO save to file
    }

    public JSONObject toJson() {  //from Object to JSONobject
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("course ID", this.courseID);
        jsonObject.put("title", this.courseTitle);
        jsonObject.put("description", this.courseDescription);
        jsonObject.put("course status", this.courseStatus);
        jsonObject.put("instructor", this.courseInstructor); //TODO instructor name or ID
        jsonObject.put("lessons",lessons);

        return jsonObject;
    }
}
