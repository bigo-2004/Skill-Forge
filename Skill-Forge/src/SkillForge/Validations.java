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
}
