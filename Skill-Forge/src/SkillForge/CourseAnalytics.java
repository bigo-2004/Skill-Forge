package SkillForge;

import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;
import org.json.JSONArray;
import org.json.JSONObject;

public class CourseAnalytics {

    private Course course;
    private UserJsonDatabase userDb;

    public CourseAnalytics(Course course, UserJsonDatabase userDb) {
        this.course = course;
        this.userDb = userDb;
    }

    private List<Student> getAllStudents() {
        JSONArray jsonArray = userDb.loadAll();
        List<Student> students = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            if (jsonObject.getString("role").equals("student")) {
                students.add((Student) User.fromJson(jsonObject));
            }
        }
        return students;
    }


    private int getScore(Student student, String lessonId) {
        Hashtable<String, Integer> quizResults = student.getQuizResults();
        Integer score = quizResults.get(lessonId);
        return (score != null) ? score : 0;
    }

    public double calculateLessonAverage(String lessonId) throws Exception {
        List<Student> students = getAllStudents();
        double totalScoreSum = 0;
        int studentCountWithScores = 0;

        for (Student student : students) {
            int score = getScore(student, lessonId);

            if (score > 0) {
                totalScoreSum += score;
                studentCountWithScores++;
            }
        }

        if (studentCountWithScores == 0) {
            return 0.0;
        }

        double averagePercentage = totalScoreSum / studentCountWithScores;
        return averagePercentage / 100.0;
    }

    public double calculateCourseCompletionPercentage() throws Exception {
        List<Student> students = getAllStudents();
        if (students.isEmpty()) {
            return 0.0;
        }

        List<String> requiredLessonIds = new ArrayList<>();
        for (Lesson lesson : course.getLessons()) {
            if (lesson.getQuiz() != null) {
                requiredLessonIds.add(lesson.getLessonID());
            }
        }

        int studentsCompletedAll = 0;

        for (Student student : students) {
            boolean passedAllRequired = true;

            for (String lessonId : requiredLessonIds) {
                int score = getScore(student, lessonId);

                if (score < 50) {
                    passedAllRequired = false;
                    break;
                }
            }
            if (passedAllRequired) {
                studentsCompletedAll++;
            }
        }

        return (double) studentsCompletedAll / students.size();
    }
}