package SkillForge;

public class SkillForge {
    public static void main(String[] args) {

       // System.out.println("SkillForge");
       // new LoginForm();


        Student s2 = new Student("2","ragab","bigo@gmail.com","Sh-123");
        Instructor s3 = new Instructor("3","ragab","bigo@gmail.com","Sh-123");
        UserJsonDatabase db = new UserJsonDatabase("users.json");
         db.saveObject(s2);

         db.saveObject(s3);




    }
}
