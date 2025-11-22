package SkillForge;

import java.util.Hashtable;

public class Lesson
{
    private String lessonID;
    private String lessonTitle;
    private String lessonContent;
    private Quiz quiz;
    private String courseID;

    private Hashtable<String, Boolean> watchedStatus = new Hashtable<>();

    public void markAsWatched(String studentId) {
        watchedStatus.put(studentId, true);
    }

    public boolean isWatchedByStudent(String studentId) {
        return watchedStatus.getOrDefault(studentId, false);
    }

    public Hashtable<String, Boolean> getWatchedStatus() {
        return watchedStatus;
    }

    public void setWatchedStatus(Hashtable<String, Boolean> watchedStatus) {
        this.watchedStatus = watchedStatus;
    }

    public String getLessonID() {
        return lessonID;
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
