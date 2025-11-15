package SkillForge;

public class SkillForge {
    public static void main(String[] args) {

        System.out.println("SkillForge");
        new LoginForm();


        UserJsonDatabase db = new UserJsonDatabase("users.json");
        Instructor s1 = (Instructor) SigningOperations.login("bigo@gmail.com","Sh-123");
        System.out.println(s1);




    }
}
