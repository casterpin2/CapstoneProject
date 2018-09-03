package project.view.util;

import android.support.design.widget.TextInputEditText;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

import project.view.R;

public class Regex {

    private Pattern pattern;

    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[!.#$@_+,?-]).{8,20})";
    private static final String USERNAME_PATTERN = "^[a-z0-9._-]{6,30}$";
    private static final String STORENAME_PATTERN = "^[a-zA-ZáàạãảăắằặẵẳâấầậẫẩéèẹẽẻêếềệễểíìịĩỉóòọõỏôốồộỗổơớờợỡởúùụũủưứừựữửýỳỵỹỷÁÀẠÃẢĂẮẰẶẴẲÂẤẦẬẪẨÉÈẸẼẺÊẾỀỆỄỂÍÌỊĨỈÓÒỌÕỎÔỐỒỘỖỔƠỚỜỢỠỞÚÙỤŨỦỨỪỰỮỬÝỲỴỸỶđĐ\\s._-]{6,30}$";
    private static final String NAME_PATTERN = "^[a-zA-ZáàạãảăắằặẵẳâấầậẫẩéèẹẽẻêếềệễểíìịĩỉóòọõỏôốồộỗổơớờợỡởúùụũủưứừựữửýỳỵỹỷÁÀẠÃẢĂẮẰẶẴẲÂẤẦẬẪẨÉÈẸẼẺÊẾỀỆỄỂÍÌỊĨỈÓÒỌÕỎÔỐỒỘỖỔƠỚỜỢỠỞÚÙỤŨỦỨỪỰỮỬÝỲỴỸỶđĐ\\s._-]{6,30}$";
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

    public boolean checkPass(TextView tvPasswordError, TextView etPassword){
        if(isPassWord(etPassword.getText().toString())){
            tvPasswordError.setText("");
            return true;
        } else
            tvPasswordError.setText(R.string.error_validate_password);
        return false;
    }


    public boolean checkUserName(TextView tvUserNameError, TextView etUserName){
        if(isUserName(etUserName.getText().toString())){
            tvUserNameError.setText("");
            return true;
        } else
            tvUserNameError.setText(R.string.error_validate_username);
        return false;
    }
    public boolean checkDisplayName(TextView tvDisplayNameError, TextView etDisplayName){
        if(isName(etDisplayName.getText().toString())){
            tvDisplayNameError.setText("");
            return true;
        } else
            tvDisplayNameError.setText(R.string.error_validate_username);
        return false;
    }

    public boolean checkEmail(TextView tvEmailError, TextView etEmail){
        if(isEmail(etEmail.getText().toString())){
            tvEmailError.setText("");
            return true;
        } else
            tvEmailError.setText(R.string.error_validate_email);
        return false;
    }

    public boolean checkPhone(TextView tvPhone, TextView etPhone){
        if(etPhone.getText().toString().length()<10||etPhone.getText().toString().length()>11){
            tvPhone.setText(R.string.error_validate_phone);
            return false;
        } else
            tvPhone.setText("");
        return true;
    }
}
