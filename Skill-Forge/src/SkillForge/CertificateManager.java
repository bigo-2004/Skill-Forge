package SkillForge;

import org.json.JSONArray;
import org.json.JSONObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CertificateManager {
    public Course getCourse() {
        return course;
    }

    private Course course;
    private UserJsonDatabase userDb;

    public CertificateManager(Course course, UserJsonDatabase userDb) {
        this.course = course;
        this.userDb = userDb;
    }

    public void generateCertificatesForCompletedStudents() throws Exception {
        List<Student> students = getAllStudents();


        for (Student student : students) {
            if (hasCompletedCourse(student)) {

                generateCertificate(student);
            }
        }
    }

    public JSONArray getCertificates(Student student) {

        Student loaded = (Student) userDb.getObjectById(student.getUserId());
        if (loaded == null) return new JSONArray();

        JSONObject json = loaded.toJson();

        JSONArray certs = json.optJSONArray("certificates");
        if (certs == null) return new JSONArray();

        return certs;
    }


    private boolean hasCompletedCourse(Student student) {
        if(course == null) return false;
        for (Lesson lesson : course.getLessons()) {
            if (lesson.getQuiz() != null) {
                QuizAttemptData data = student.getQuizResults().get(lesson.getLessonID());
                if (data == null || data.getBestScore() < 50) {
                    return false;
                }
            }
        }
        return true;
    }

    private void generateCertificate(Student student) {



        Student loaded = (Student) userDb.getObjectById(student.getUserId());
        if (loaded == null) return;


        JSONObject studentJson = loaded.toJson();


        JSONArray certs = studentJson.optJSONArray("certificates");
        if (certs == null) certs = new JSONArray();


        for (int i = 0; i < certs.length(); i++) {
            JSONObject c = certs.getJSONObject(i);
            if (c.getString("courseId").equals(course.getCourseID())) {
                return;
            }
        }


        JSONObject cert = new JSONObject();
        cert.put("certificateId", UUID.randomUUID().toString());
        cert.put("studentId", loaded.getUserId());
        cert.put("courseId", course.getCourseID());
        cert.put("issueDate", LocalDate.now().toString());


        certs.put(cert);
        studentJson.put("certificates", certs);


        Student updated = Student.fromJsonToStudent(studentJson);
        userDb.updateObject(loaded, updated);
    }

    private List<Student> getAllStudents() {
        JSONArray array = userDb.loadAll();
        List<Student> students = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String role = obj.optString("role", "");
            if ("student".equalsIgnoreCase(role)) {

                students.add(Student.fromJsonToStudent(obj));
            }
        }
        return students;
    }
}
