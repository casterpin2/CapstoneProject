package project.view.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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

public class ResetPasswordPage extends BasePage{
    private  Button btnChangePass;
    private TextView  tvConfirmPass,tvNewPass;
    private TextInputEditText  newPass, confirmPass;
    private ProgressBar loadingBar;
    private RelativeLayout main_layout;
    private APIService apiService;
    private String username;
    private Regex regex;
    private boolean isNewPass = false ,isConfirm = false;
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

        regex= new Regex();

        loadingBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);

        username = getIntent().getStringExtra("username");

        newPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    isNewPass = regex.isPassWord(newPass.getText().toString());
                    if(!isNewPass){
                        tvNewPass.setText(R.string.error_validate_password);
                    }else {
                        tvNewPass.setText("");
                    }
                }
            }
        });

        confirmPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    isConfirm = regex.isPassWord(confirmPass.getText().toString());
                    if(!isConfirm){
                        tvConfirmPass.setText(R.string.error_validate_password);
                    }else {
                        tvConfirmPass.setText("");
                    }
                }
            }
        });

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isNewPass){
                    tvNewPass.setText(R.string.error_validate_password);
                }
                if(!isConfirm){
                    tvConfirmPass.setText(R.string.error_validate_password);
                }
                if (isConfirm && isNewPass) {
                    if(!newPass.getText().toString().equals(confirmPass.getText().toString())){
                        tvConfirmPass.setText(R.string.error_confirm_password);
                    }else {
                        tvConfirmPass.setText("");
                        apiService = ApiUtils.getAPIService();
                        String password = MD5Library.md5(newPass.getText().toString());

                        apiService.requestChangePassword(username, password).enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if(response.isSuccessful()){
                                    Toast.makeText(ResetPasswordPage.this, "Thay đổi thông tin thành công", Toast.LENGTH_LONG).show();
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
                }
            }
        });
    }

    private void findView(){
        main_layout = findViewById(R.id.main_layout);
        tvNewPass = findViewById(R.id.tvNewPassword);
        tvConfirmPass = findViewById(R.id.tvConfirmPass);
        newPass = findViewById(R.id.etNewPassword);
        confirmPass = findViewById(R.id.etConfirmPass);
        btnChangePass = findViewById(R.id.btnChangePass);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



}
