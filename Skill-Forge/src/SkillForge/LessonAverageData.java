package SkillForge;

public class LessonAverageData {

    private final String lessonTitle;
    private final double averageRatio; // from 0.0 to 1.0

    public LessonAverageData(String lessonTitle, double averageRatio) {
        this.lessonTitle = lessonTitle;
        this.averageRatio = averageRatio;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public double getAverageRatio() {
        return averageRatio;
    }
}