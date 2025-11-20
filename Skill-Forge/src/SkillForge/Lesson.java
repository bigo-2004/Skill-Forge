package SkillForge;

public class Lesson
{
    private String lessonID;
    private String lessonTitle;
    private String lessonContent;
    private Quiz quiz;
    private String courseID;
    private boolean watched = false;

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

    public Quiz getQuiz() {
        return quiz;
    }

    public void addQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}
