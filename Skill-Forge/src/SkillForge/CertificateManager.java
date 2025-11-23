package SkillForge;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CertificateManager {

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
        JSONObject json = (JSONObject) userDb.getObjectById(student.getUserId());
        if (json == null) return new JSONArray();

        JSONArray certs = json.optJSONArray("certificates");
        return (certs != null) ? certs : new JSONArray();
    }


    private boolean hasCompletedCourse(Student student) {
        for (Lesson lesson : course.getLessons()) {

            if (lesson.getQuiz() != null) {
                Integer score = student.getQuizResults().get(lesson.getLessonID());
                if (score == null || score < 50) {
                    return false;
                }
            }
        }
        return true;
    }

    private void generateCertificate(Student student) {

        JSONObject studentJson = (JSONObject) userDb.getObjectById(student.getUserId());
        if (studentJson == null) return;

        JSONObject certificate = new JSONObject();
        certificate.put("certificateId", UUID.randomUUID().toString());
        certificate.put("studentId", student.getUserId());
        certificate.put("courseId", course.getCourseID());
        certificate.put("issueDate", LocalDate.now().toString());

        // Append certificate to "certificates" array
        JSONArray certificates = studentJson.optJSONArray("certificates");
        if (certificates == null) certificates = new JSONArray();

        certificates.put(certificate);
        studentJson.put("certificates", certificates);

        userDb.updateObject(
                User.fromJson(studentJson),
                User.fromJson(studentJson).toJson()
        );
    }

    private List<Student> getAllStudents() {
        JSONArray array = userDb.loadAll();
        List<Student> students = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);

            if (obj.getString("role").equals("student")) {
                students.add((Student) User.fromJson(obj));
            }
        }
        return students;
    }
}
