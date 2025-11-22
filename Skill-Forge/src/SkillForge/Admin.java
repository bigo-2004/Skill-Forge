package SkillForge;

public class Admin extends User{
    public Admin(String userId, String userName, String email, String password, String role) {
        super(userId, userName, email, password, role);
    }

    public void approveCourse(Course course)
    {
        course.courseStatus = "approved";
    }
    public void rejectCourse(Course course)
    {
        course.courseStatus = "rejected";
    }
}
