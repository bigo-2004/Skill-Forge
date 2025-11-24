package SkillForge;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class CourseAnalytics {
    private Course course;
    private UserJsonDatabase userDb;
    private List<Student> courseStudents;

    public CourseAnalytics(Course course, UserJsonDatabase userDb) {
        this.course = course;
        this.userDb = userDb;
        this.courseStudents = loadStudentsWithQuizData();
    }

    private List<Student> loadStudentsWithQuizData() {
        JSONArray rawUsersArray = userDb.loadAll();
        List<User> allUsers = new ArrayList<>();
        List<Student> students = new ArrayList<>();

        for (int i = 0; i < rawUsersArray.length(); i++) {
            JSONObject jsonObject = rawUsersArray.getJSONObject(i);
            try {
                String role = jsonObject.getString("role");
                if ("Student".equalsIgnoreCase(role)) {
                    allUsers.add(Student.fromJsonToStudent(jsonObject));
                } else if ("Instructor".equalsIgnoreCase(role)) {
                    allUsers.add(Instructor.fromJsonToInstructor(jsonObject));
                }
            } catch (Exception e) {
                System.err.println("Analytics Error: Failed to deserialize user object. " + e.getMessage());
            }
        }

        for (User user : allUsers) {
            if (user instanceof Student) {
                students.add((Student) user);
            }
        }

        return students;
    }

    public List<Student> getCourseStudents() {
        return courseStudents;
    }

    public double calculateLessonAverage(String lessonId) {
        int totalScore = 0;
        int studentCount = 0;

        String quizId = findQuizIdByLessonId(lessonId);
        if (quizId == null) return 0.0;

        for (Student student : this.courseStudents) {
            if (student.getQuizResults().containsKey(quizId)) {
                QuizAttemptData data = student.getQuizResults().get(quizId);
                if (data != null) {
                    totalScore += data.getBestScore();
                }
                studentCount++;
            }
        }

        if (studentCount == 0) return 0.0;

        // Returns the average score directly as an integer percentage (e.g., 75.0)
        return (double) totalScore / studentCount;
    }


    public double calculateCourseCompletionPercentage() {
        if (course.getLessons().isEmpty() || this.courseStudents.isEmpty()) return 0.0;

        List<String> quizIds = new ArrayList<>();
        for (Lesson lesson : course.getLessons()) {
            if (lesson.getQuiz() != null) {
                quizIds.add(lesson.getQuiz().getQuizId());
            }
        }

        if (quizIds.isEmpty()) return 0.0;

        int studentsPassedAllQuizzesCount = 0;

        for (Student student : this.courseStudents) {
            boolean passedAllQuizzes = true;
            for (String quizId : quizIds) {
                QuizAttemptData data = student.getQuizResults().get(quizId);
                if (data == null || data.getBestScore() < 50) {
                    passedAllQuizzes = false;
                    break;
                }
            }
            if (passedAllQuizzes) {
                studentsPassedAllQuizzesCount++;
            }
        }

        return (double) studentsPassedAllQuizzesCount / this.courseStudents.size();
    }
    private String findQuizIdByLessonId(String lessonId) {
        for (Lesson lesson : course.getLessons()) {
            if (lesson.getLessonID().equals(lessonId) && lesson.getQuiz() != null) {
                return lesson.getQuiz().getQuizId();
            }
        }
        return null;
    }
}