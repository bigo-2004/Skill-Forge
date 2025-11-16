package SkillForge;

public class Lesson
{
    String lessonID;
    String lessonTitle;
    String lessonContent;
    String courseID;
    boolean watched = false;

    public void markAsWatched()
    {
        watched = true;
    }

    public String getLessonID() {
        return lessonID;
    }

    public void setLessonID(String lessonID) {
        this.lessonID = lessonID;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
    }

    public String getLessonContent() {
        return lessonContent;
    }

    public void setLessonContent(String lessonContent) {
        this.lessonContent = lessonContent;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public Lesson(String lessonID, String lessonTitle, String lessonContent, String courseID) {
        this.lessonID = lessonID;
        this.lessonTitle = lessonTitle;
        this.lessonContent = lessonContent;
        this.courseID = courseID;
    }
}
