package project.view.Register;

import java.util.regex.Pattern;

public class Regex {

    private Pattern pattern;
    /*
     * password must compile 8-20 char with at least one digit, 1 upper case letter, 1 lower case letter and one special symbol
     * */
    //(?=.*[A-Z]) Upper Case
    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[!.#$@_+,?-]).{8,20})";
    private static final String USERNAME_PATTERN = "^[a-z0-9._-]{3,15}$";
    private static final String EMAIL_PATTERN = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    public boolean isPassWord(String password){
        pattern = Pattern.compile(PASSWORD_PATTERN);
        return pattern.matcher(password).matches();
    }
    public boolean isUserName(String username){
        pattern = Pattern.compile(USERNAME_PATTERN);
        return pattern.matcher(username).matches();
    }

    public boolean isEmail(String email){
        pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }

}
