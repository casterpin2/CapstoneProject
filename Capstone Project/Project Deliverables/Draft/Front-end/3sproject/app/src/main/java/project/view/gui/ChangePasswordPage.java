package project.view.gui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.google.gson.Gson;

import java.util.Map;

import project.objects.User;
import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.fragment.home.UserFragment;
import project.view.util.CustomInterface;
import project.view.util.MD5Library;
import project.view.util.Regex;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordPage extends BasePage {
    private Button btnChangePass;
    private TextView tvOldPass, tvConfirmPass, tvNewPass;
    private TextInputEditText oldPass, newPass, confirmPass;
    private TextInputLayout etPasswordLayout;
    private ProgressBar loadingBar;
    private RelativeLayout main_layout;
    private APIService apiService;
    private String username;
    private Regex regex;
    private boolean isNewPass = false, isOldPass = false, isConfirm = false;
    private boolean checkPassword = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        findView();
        regex = new Regex();
        customView();
        username = getIntent().getStringExtra("username");

        oldPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    isOldPass = regex.checkPass(tvOldPass,oldPass);
                    SharedPreferences pre = getSharedPreferences("authentication", Context.MODE_PRIVATE);
                    String userJSON = pre.getString("user", "");

                    if(userJSON!=null && !userJSON.isEmpty()){
                        User usCheckPassword = new Gson().fromJson(userJSON, User.class);
                        apiService = ApiUtils.getAPIService();
                        Call<User> call = apiService.getInformation(usCheckPassword.getId());
                        new ValidatorPassword().execute(call);
                    }
                    if(!checkPassword){
                        tvOldPass.setText(R.string.error_old_pass);
                    } else {
                        tvOldPass.setText("");
                    }
                }
            }
        });
        newPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                   isNewPass = regex.checkPass(tvNewPass,newPass);
                   if (isNewPass) {
                       if(newPass.getText().toString().equals(oldPass.getText().toString())){
                           isNewPass = false;
                           tvNewPass.setText("Mật khẩu mới không được giống với mật khẩu cũ");
                       }else {
                       tvNewPass.setText("");
                       }
                   }
                }
            }
        });

        confirmPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!newPass.getText().toString().equals(confirmPass.getText().toString())) {
                        tvConfirmPass.setText(R.string.confirm_password);
                        isConfirm = false;
                    } else {
                        tvConfirmPass.setText("");
                        isConfirm = true;
                    }
                }
            }
        });
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOldPass = regex.checkPass(tvOldPass,oldPass);
                isNewPass = regex.checkPass(tvNewPass,newPass);
                if (!newPass.getText().toString().equals(confirmPass.getText().toString())) {
                    tvConfirmPass.setText(R.string.confirm_password);
                    isConfirm = false;
                } else {
                    tvConfirmPass.setText("");
                    isConfirm = true;
                }
                   if (isConfirm && isNewPass && isOldPass) {
                        tvConfirmPass.setText("");
                        if (getIntent().getStringExtra("username") != null && !getIntent().getStringExtra("username").isEmpty()) {
                            username = getIntent().getStringExtra("username");
                        }
                        if(username!=null && !username.isEmpty()){
                            apiService = ApiUtils.getAPIService();
                            String password = MD5Library.md5(newPass.getText().toString());
                            loadingBar.setVisibility(View.VISIBLE);
                            btnChangePass.setEnabled(false);
                            btnChangePass.setText("");
                            apiService.requestChangePassword(username,password).enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    if(response.isSuccessful()){
                                        loadingBar.setVisibility(View.INVISIBLE);
                                        btnChangePass.setEnabled(true);
                                        btnChangePass.setText("Lưu thay đổi");
                                        Toast.makeText(ChangePasswordPage.this, "Thay đổi mật khẩu thành công.", Toast.LENGTH_LONG).show();
                                        Intent backToUserFragment = new Intent(ChangePasswordPage.this, UserFragment.class);
                                        tvOldPass.setText("");
                                        oldPass.setText("");
                                        confirmPass.setText("");
//                                        backToUserFragment.putExtra(getIntent().getStringExtra("displayName"),"");
                                        setResult(223,backToUserFragment);
                                        finish();
                                    }

                                }

                                @Override
                                public void onFailure(Call<Boolean> call, Throwable t) {
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                                Toast.makeText(ChangePasswordPage.this, "Có lỗi xảy ra. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                                            loadingBar.setVisibility(View.INVISIBLE);
                                            btnChangePass.setEnabled(true);
                                            btnChangePass.setText("Lưu thay đổi");
                                        }
                                    },10000);
                                }
                            });
                        }
                }
            }
        });
    }

    private void customView(){
        CustomInterface.setStatusBarColor(this);
        getSupportActionBar().setTitle(R.string.title_change_password);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view, getBaseContext());
                return false;
            }
        });
    }

    private void findView() {
        main_layout = findViewById(R.id.main_layout);
        tvOldPass = findViewById(R.id.tvOldPass);
        tvNewPass = findViewById(R.id.tvNewPassword);
        tvConfirmPass = findViewById(R.id.tvConfirmPass);
        btnChangePass = findViewById(R.id.btnChangePass);
        loadingBar = findViewById(R.id.loadingBar);
        oldPass = findViewById(R.id.etOldPassword);
        newPass = findViewById(R.id.etNewPassword);
        confirmPass = findViewById(R.id.etConfirmPass);
        etPasswordLayout = findViewById(R.id.etPasswordLayout);
        etPasswordLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public class ValidatorPassword extends AsyncTask<Call,Void,User>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            String md5Pass = MD5Library.md5(oldPass.getText().toString());
            if(!md5Pass.equals(user.getPassword())){
                checkPassword = false;
            }else {
                checkPassword = true;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected User doInBackground(Call... calls) {
            try{
                Call<User> call = calls[0];
                Response<User> response = call.execute();
                if(response.body()!=null){
                    return response.body();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}