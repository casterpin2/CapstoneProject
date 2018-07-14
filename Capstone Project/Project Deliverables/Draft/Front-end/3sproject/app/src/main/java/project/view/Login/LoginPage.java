package project.view.Login;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import project.view.R;
import project.view.Register.RegisterActivity;

public class LoginPage extends AppCompatActivity {

    private ScrollView scroll;
    private Button loginBtn, loginFBBtn, loginGPBtn;
    private TextView toRegisterPage, toForgetPasswordPage, errorMessage, usernameValue, passwordValue ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        getSupportActionBar().setTitle(getResources().getString(R.string.login_page_login3SBtn));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        findView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor((getResources().getColor(R.color.statusBarColor)));
        }

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

    private void findView(){
        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginFBBtn = (Button) findViewById(R.id.loginFBBtn);
        loginGPBtn = (Button) findViewById(R.id.loginGPBtn);
        scroll = (ScrollView) findViewById(R.id.scrollView);
        toRegisterPage = (TextView) findViewById(R.id.toRegisterPageBtn);
        toForgetPasswordPage = (TextView) findViewById(R.id.toForgetPasswordPageBtn);
        errorMessage = (TextView) findViewById(R.id.errorMessage);
        usernameValue = (TextView) findViewById(R.id.usernameValue);
        passwordValue = (TextView) findViewById(R.id.passwordValue);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
