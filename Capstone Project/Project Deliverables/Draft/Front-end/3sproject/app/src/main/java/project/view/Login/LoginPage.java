package project.view.Login;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import project.view.MainActivity;
import project.view.R;
import project.view.Register.RegisterActivity;

public class LoginPage extends AppCompatActivity {

    private  View view;
    private Context context;
    private ScrollView scroll;
    private Button loginBtn, loginFBBtn, loginGPBtn;

    private TextView toRegisterPage, toForgetPasswordPage, errorMessage, usernameValue, passwordValue ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_login_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginFBBtn = (Button) findViewById(R.id.loginFBBtn);
        loginGPBtn = (Button) findViewById(R.id.loginGPBtn);
        scroll = (ScrollView) findViewById(R.id.scrollView);
        toRegisterPage = (TextView) findViewById(R.id.toRegisterPageBtn);
        toForgetPasswordPage = (TextView) findViewById(R.id.toForgetPasswordPageBtn);
        errorMessage = (TextView) findViewById(R.id.errorMessage);
        usernameValue = (TextView) findViewById(R.id.usernameValue);
        passwordValue = (TextView) findViewById(R.id.passwordValue);


        scroll.setVerticalScrollBarEnabled(false);
        scroll.setHorizontalScrollBarEnabled(false);
        toRegisterPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toRegisterPage = new Intent(LoginPage.this, RegisterActivity.class);
                startActivity(toRegisterPage);
            }
        });



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorMessage.setText("Tên đăng nhập hoặc mật khẩu không đúng. Vui lòng thử lại");
            }
        });
        usernameValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                errorMessage.setText("");
            }
        });
        passwordValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                errorMessage.setText("");
            }
        });


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
