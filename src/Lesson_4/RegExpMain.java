package Lesson_4;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpMain {
    static String expression = "^(?=[\\d\\w]*\\d)(?=[\\d\\w]*[a-z])(?=[\\d\\w]*[A-Z])[\\d\\w]{8,20}$";
    static Pattern p = Pattern.compile(expression);
    public static void main(String[] args) {
        System.out.println(checkPassword("12345678"));
        System.out.println(checkPassword("aS2"));
        System.out.println(checkPassword("GhjdthRf123"));
    }

    static boolean checkPassword(String _passWord){
        Matcher m = p.matcher(_passWord);
        return m.matches();
    }
}
