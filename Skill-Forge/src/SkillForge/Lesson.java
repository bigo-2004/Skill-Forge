package SkillForge;

public class Lesson
{
    String lessonID;
    String lessonTitle;
    String lessonContent;
    String courseID;


    public void markAsWatched(User student)
    {
        //TODO
    }

    public Lesson(String lessonID, String lessonTitle, String lessonContent, String courseID) {
        this.lessonID = lessonID;
        this.lessonTitle = lessonTitle;
        this.lessonContent = lessonContent;
        this.courseID = courseID;
    }
}
