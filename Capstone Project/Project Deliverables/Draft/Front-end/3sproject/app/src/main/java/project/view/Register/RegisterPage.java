package project.view.Register;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import project.view.Login.LoginPage;
import project.view.MainActivity;
import project.view.R;

public class RegisterPage extends AppCompatActivity {

    private Context context;
    private View view;
    private ScrollView scroll;
    private TextView usernameValue, passwordValue, rePasswordValue, emailValue, phoneValue, toRulePage, toLoginPage;
    private TextView usernameErrorMessage, passwordErrorMessage, confirmPasswordErrorMessage, emailErrorMessage, phoneErrorMessage;
    private CheckBox acceptRuleCheckBox;
    private Button registerBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.register_page_actionbar_custom);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        scroll = (ScrollView) findViewById(R.id.scrollView);
        usernameValue = (TextView) findViewById(R.id.passwordValue);
        passwordValue = (TextView) findViewById(R.id.usernameValue);
        rePasswordValue = (TextView) findViewById(R.id.rePasswordValue);
        emailValue = (TextView) findViewById(R.id.emailValue);
        phoneValue = (TextView) findViewById(R.id.phoneValue);
        toRulePage = (TextView) findViewById(R.id.toRulePage);
        toLoginPage = (TextView) findViewById(R.id.toLoginPage);
        usernameErrorMessage = (TextView) findViewById(R.id.usernameErrorMessage);
        passwordErrorMessage = (TextView) findViewById(R.id.passwordErrorMessage);
        acceptRuleCheckBox = (CheckBox) findViewById(R.id.checkBox);
        registerBtn = (Button) findViewById(R.id.registerBtn);


        scroll.setVerticalScrollBarEnabled(false);
        scroll.setHorizontalScrollBarEnabled(false);

        String username = (String) usernameValue.getText().toString();
        final String password = (String) passwordValue.getText().toString();
        final String confirmPassword = (String) rePasswordValue.getText().toString();
        String email = (String) emailValue.getText().toString();
        String phone = (String) phoneValue.getText().toString();
        boolean isAcceptRule = (Boolean) acceptRuleCheckBox.isChecked();


        toLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLoginPage = new Intent(RegisterPage.this, LoginPage.class);
                startActivity(toLoginPage);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    public Context getContext() {
        return context;
    }
}
