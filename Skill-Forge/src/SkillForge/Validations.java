package SkillForge;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validations {
    public static boolean isValidEmail(String email) {

        email = email.trim();

        String regex = "^[a-zA-Z0-9][._]?[a-zA-Z0-9]+([-._][a-zA-Z0-9]+(_?[a-zA-Z0-9]+)*)*@[a-zA-Z0-9]+([-.][a-zA-Z0-9]+)*\\.[a-zA-Z]{2,4}$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    public static boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Z]).{8,}$";
        if(password.matches(regex)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidName(String name) {
        name = name.trim();
        if (name.isEmpty()) return false;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!Character.isLetter(c) && c != ' '  && c != '\'') {
                return false;
            }
        }
        return true;
    }
}
