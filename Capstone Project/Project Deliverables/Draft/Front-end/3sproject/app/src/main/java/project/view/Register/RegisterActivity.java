package project.view.Register;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import project.view.Login.LoginPage;
import project.view.R;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText etUserName,etPassword,etConfirmPass,etEmail,etPhoneNumber;
    private TextView tvUserName, tvPassword, tvConfirmPassword, tvEmail, tvPhoneNumber, toLoginPageBtn;
    private Button btnRegister;
    private boolean isUserName,isPassword, confirm,isEmail,isPhone = true;
    private ScrollView scroll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        btnRegister = findViewById(R.id.btnRegister);
        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPass = findViewById(R.id.etConfirmPassword);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        scroll = findViewById(R.id.scroll);
        toLoginPageBtn = findViewById(R.id.toLoginPageBtn);

        tvUserName = findViewById(R.id.tvUserName);
        tvPassword = findViewById(R.id.tvPassword);
        tvConfirmPassword = findViewById(R.id.tvConfirmPassword);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);

        scroll.setVerticalScrollBarEnabled(false);
        scroll.setHorizontalScrollBarEnabled(false);

        etUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    checkUserName(etUserName.getText().toString());
                }
            }
        });

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    checkPassword(etPassword.getText().toString());
                }
            }
        });
        etConfirmPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                        tvConfirmPassword.setText("");
                }
            }
        });

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    checkEmail(etEmail.getText().toString());
                    Toast.makeText(getBaseContext(),isEmail+" a",Toast.LENGTH_SHORT);
                }
            }
        });
        etPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    tvPhoneNumber.setText("");
                }
            }
        });

        toLoginPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLoginPage = new Intent(RegisterActivity.this, LoginPage.class);
                startActivity(toLoginPage);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmPassword(etPassword.getText().toString(),etConfirmPass.getText().toString());
                if(etUserName.getText().toString().isEmpty()){
                    tvUserName.setText(R.string.error_empty);
                }
                if(etPassword.getText().toString().isEmpty()){
                    tvPassword.setText(R.string.error_empty);
                }
                if(etConfirmPass.getText().toString().isEmpty()){
                    tvConfirmPassword.setText(R.string.error_empty);
                }
                if(etEmail.getText().toString().isEmpty()){
                    tvEmail.setText(R.string.error_empty);
                }
                if(etPhoneNumber.getText().toString().isEmpty()){
                    tvPhoneNumber.setText(R.string.error_empty);
                }

                if(isUserName && isPassword && confirm && isEmail && isPhone){
                    tvUserName.setText("aa");



                }else {
                    tvUserName.setText("bb");
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkUserName(String input){
        Regex validate = new Regex();
        isUserName = validate.isUserName(input);
        if(!input.isEmpty()&& !isUserName) {
            tvUserName.setText(R.string.error_validate_username);
        } else
            tvUserName.setText("");
    }

    private void confirmPassword(String pass,String passConfirm){

        confirm = pass.equals(passConfirm);
        if(!confirm){
            tvConfirmPassword.setText(R.string.error_confirm_password);
        } else
            tvConfirmPassword.setText("");
    }

    private void checkPassword(String input){
        Regex validate = new Regex();
        isPassword = validate.isPassWord(input);
        if(!input.isEmpty()&& !isPassword) {
            tvPassword.setText(R.string.error_validate_password);
        } else
            tvPassword.setText("");
    }

    private void checkEmail(String input){
        Regex validate = new Regex();
        isEmail = validate.isEmail(input);
        if(!input.isEmpty()&& !isEmail) {
            tvEmail.setText(R.string.error_validate_email);
        } else
            tvEmail.setText("");
    }



}
