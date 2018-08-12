package project.view.gui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.util.CustomInterface;
import project.view.util.MD5Library;
import project.view.util.Regex;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordPage extends BasePage{
    private  Button btnChangePass;
    private TextView tvOldPass, tvConfirmPass,tvNewPass;
    private TextInputEditText oldPass, newPass, confirmPass;
    private ProgressBar loadingBar;
    private RelativeLayout main_layout;
    private APIService apiService;
    private String username;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        findView();
        CustomInterface.setStatusBarColor(this);
        getSupportActionBar().setTitle(R.string.title_change_password);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view,getBaseContext());
                return false;
            }
        });
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);
        loadingBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);

        username = getIntent().getStringExtra("username");
        newPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    checkPassword(newPass.getText().toString());
                }
            }
        });

        btnChangePass = findViewById(R.id.btnChangePass);
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPassword(newPass.getText().toString(),confirmPass.getText().toString());
                if(newPass.getText().toString().isEmpty()){
                    tvNewPass.setText(R.string.error_empty);
                }
                if(confirmPass.getText().toString().isEmpty()){
                    tvConfirmPass.setText(R.string.error_empty);
                }
                apiService = ApiUtils.getAPIService();
                String password = MD5Library.md5(newPass.getText().toString());

                apiService.requestChangePassword(username, password).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(ChangePasswordPage.this, "Thay đổi thông tin thành công", Toast.LENGTH_LONG).show();
                            Intent toLoginPage = new Intent(getBaseContext(),LoginPage.class);
                            toLoginPage.putExtra("username",username);
                            startActivity(toLoginPage);
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void findView(){
        main_layout = findViewById(R.id.main_layout);
        tvOldPass = findViewById(R.id.tvOldPass);
        tvNewPass = findViewById(R.id.tvNewPassword);
        tvConfirmPass = findViewById(R.id.tvConfirmPass);

        oldPass = findViewById(R.id.etOldPassword);
        newPass = findViewById(R.id.etNewPassword);
        confirmPass = findViewById(R.id.etConfirmPass);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmPassword(String pass,String passConfirm){
       if(!pass.equals(passConfirm)){
           tvConfirmPass.setText(R.string.error_confirm_password);
       } else
       tvConfirmPass.setText("");
    }

    private void checkPassword(String input){
        Regex validate = new Regex();
        if(!input.isEmpty()&& !validate.isPassWord(input)) {
            tvNewPass.setText(R.string.error_validate_password);
        } else
            tvNewPass.setText("");
    }

}
