package project.view.Register;

import java.util.regex.Pattern;

public class Regex {

    private Pattern pattern;

    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[!.#$@_+,?-]).{8,20})";
    private static final String USERNAME_PATTERN = "^[a-z0-9._-]{6,30}$";
    private static final String STORENAME_PATTERN = "^[a-zA-Z0-9._-]{6,64}$";
    private static final String NAME_PATTERN = "^[a-zA-Z._-]{6,30}$";
    private static final String EMAIL_PATTERN = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    public boolean isPassWord(String password){
        pattern = Pattern.compile(PASSWORD_PATTERN);
        return pattern.matcher(password).matches();
    }
    public boolean isUserName(String username){
        pattern = Pattern.compile(USERNAME_PATTERN);
        return pattern.matcher(username).matches();
    }

    public boolean isName(String name){
        pattern = Pattern.compile(NAME_PATTERN);
        return pattern.matcher(name).matches();
    }

    public boolean isStoreName(String storeName){
        pattern = Pattern.compile(STORENAME_PATTERN);
        return pattern.matcher(storeName).matches();
    }

    public boolean isEmail(String email){
        pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }

}
