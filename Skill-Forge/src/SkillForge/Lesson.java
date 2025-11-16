package SkillForge;

public class Lesson
{
    String lessonID;
    String lessonTitle;
    String lessonContent;
    String courseID;
    boolean watched = false;

    public void markAsWatched(Student student)
    {
        watched = true;
    }

    public Lesson(String lessonID, String lessonTitle, String lessonContent, String courseID) {
        this.lessonID = lessonID;
        this.lessonTitle = lessonTitle;
        this.lessonContent = lessonContent;
        this.courseID = courseID;
    }
}
