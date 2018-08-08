package project.view.gui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.StringTokenizer;

import project.objects.User;
import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.model.ResultRegister;
import project.view.util.CustomInterface;
import project.view.util.MD5Library;
import project.view.util.Regex;
import retrofit2.Call;
import retrofit2.Response;

public class RegisterPage extends AppCompatActivity {
    private TextInputEditText etUserName,etName, etPassword, etConfirmPass, etEmail, etPhoneNumber;
    private TextView tvUserName,tvName, tvPassword, tvConfirmPassword, tvEmail, tvPhoneNumber, toLoginPageBtn;
    private Button btnRegister;
    private boolean isUserName,isName, isPassword, confirm, isEmail, isPhone = true;
    private APIService apiUserService;
    private User us;
    private Gson gson;
    private RelativeLayout main_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findView();
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view,getBaseContext());
                return false;
            }
        });
        CustomInterface.setStatusBarColor(this);
        getSupportActionBar().setTitle(R.string.title_register_screen);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        apiUserService = ApiUtils.getAPIService();
        us = new User();
        etUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    apiUserService = APIService.retrofit.create(APIService.class);
                    final Call<Integer> call = apiUserService.vadilator(etUserName.getText().toString(), etEmail.getText().toString(), etPhoneNumber.getText().toString(), "username");
                    new ValidatorUser().execute(call);
                    checkUserName(etUserName.getText().toString());
                }
            }
        });

        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    checkName(etName.getText().toString());
                }
            }
        });

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    us.setPassword(etPassword.getText().toString());
                    checkPassword(etPassword.getText().toString());
                }
            }
        });
        etConfirmPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    confirmPassword(etPassword.getText().toString(), etConfirmPass.getText().toString());
                }
            }
        });

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    apiUserService = APIService.retrofit.create(APIService.class);
                    final Call<Integer> call = apiUserService.vadilator(etUserName.getText().toString(), etEmail.getText().toString(), etPhoneNumber.getText().toString(), "email");
                    new ValidatorUser().execute(call);
                    checkEmail(etEmail.getText().toString());
                }
            }
        });
        etPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {

                    apiUserService = APIService.retrofit.create(APIService.class);
                    final Call<Integer> call = apiUserService.vadilator(etUserName.getText().toString(), etEmail.getText().toString(), etPhoneNumber.getText().toString(), "phone");
                    new ValidatorUser().execute(call);
                    if(etPhoneNumber.getText().toString().length()<10 || etPhoneNumber.getText().toString().length()>11){
                        tvPhoneNumber.setText(R.string.length_phone);
                        isPhone= true;
                    }else{
                        tvPhoneNumber.setText("");
                    }
                }
            }
        });

        toLoginPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLoginPage = new Intent(RegisterPage.this, LoginPage.class);
                startActivity(toLoginPage);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmPassword(etPassword.getText().toString(), etConfirmPass.getText().toString());
                if (etUserName.getText().toString().isEmpty()) {
                    tvUserName.setText(R.string.error_empty);
                }
                if(etName.getText().toString().isEmpty()){
                    tvName.setText(R.string.error_empty);
                }

                if (etPassword.getText().toString().isEmpty()) {
                    tvPassword.setText(R.string.error_empty);
                }
                if (etConfirmPass.getText().toString().isEmpty()) {
                    tvConfirmPassword.setText(R.string.error_empty);
                }
                if (etEmail.getText().toString().isEmpty()) {
                    tvEmail.setText(R.string.error_empty);
                }
                if (etPhoneNumber.getText().toString().isEmpty()) {
                    tvPhoneNumber.setText(R.string.error_empty);
                }



                if (isUserName /*&& !isName && !isPassword && confirm*/&& isEmail && isPhone ) {
                    us.setUsername(etUserName.getText().toString());
                    us.setPhone(etPhoneNumber.getText().toString().trim().replaceAll("\\s+",""));
                    StringTokenizer st = new StringTokenizer(etName.getText().toString().trim().replaceAll("\\s+"," ")," ");
                    if(st.hasMoreTokens())
                    us.setFirst_name(st.nextToken());
                    StringBuilder last_name = new StringBuilder();
                    while (st.hasMoreTokens()){
                        last_name.append(st.nextToken());
                    }
                    us.setLast_name(last_name.toString());
                    us.setEmail(etEmail.getText().toString().trim().replaceAll("\\s+",""));
                    us.setPassword(MD5Library.md5(etPassword.getText().toString().trim()));
                    gson = new Gson();
                    final Call<ResultRegister> call = apiUserService.registerUserNew(us);
                    RegisterUserAsyncTask1 asyncTask = new RegisterUserAsyncTask1();
                    asyncTask.execute(call);

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

    private void findView(){
        btnRegister = findViewById(R.id.btnRegister);
        etUserName = findViewById(R.id.etUserName);
        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPass = findViewById(R.id.etConfirmPassword);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        toLoginPageBtn = findViewById(R.id.toLoginPageBtn);
        tvUserName = findViewById(R.id.tvUserName);
        tvName = findViewById(R.id.tvName);
        tvPassword = findViewById(R.id.tvPassword);
        tvConfirmPassword = findViewById(R.id.tvConfirmPassword);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        main_layout = findViewById(R.id.main_layout);
    }

    private void checkUserName(final String input) {
        Regex validate = new Regex();
        isUserName = validate.isUserName(input);
        if (!input.isEmpty() && !isUserName) {
            tvUserName.setText(R.string.error_validate_username);
        } else
            tvUserName.setText("");
    }

    private void checkName(final String input) {
        Regex validate = new Regex();
        isName = validate.isName(input);
        if (!input.isEmpty() && !isName) {
            tvName.setText(R.string.error_validate_name);
        } else
            tvName.setText("");
    }

    private void confirmPassword(String pass, String passConfirm) {

        confirm = pass.equals(passConfirm);
        if (!confirm) {
            tvConfirmPassword.setText(R.string.error_confirm_password);
        } else
            tvConfirmPassword.setText("");
    }

    private void checkPassword(String input) {
        Regex validate = new Regex();
        isPassword = validate.isPassWord(input);
        if (!input.isEmpty() && !isPassword) {
            tvPassword.setText(R.string.error_validate_password);
        } else
            tvPassword.setText("");
    }

    private void checkEmail(String input) {
        Regex validate = new Regex();
        isEmail = validate.isEmail(input);
        if (!input.isEmpty() && !isEmail) {
            tvEmail.setText(R.string.error_validate_email);
        } else
            tvEmail.setText("");
    }


    private class ValidatorUser extends AsyncTask<Call, String, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String nameCheck = values[0];
            switch (nameCheck){
                case "1":
                    tvUserName.setText(R.string.duplicate_username);
                    isUserName = false;
                    break;
                case "2":
                    tvEmail.setText(R.string.duplicate_email);
                    isEmail = false;
                    break;
                case "3":
                    isPhone = false;
                    tvPhoneNumber.setText(R.string.duplicate_phone);
                    break;
            }
        }

        @Override
        protected Void doInBackground(Call... calls) {

            try {
                Call<Integer> call = calls[0];
                Response<Integer> re = call.execute();
                publishProgress(re.body()+"");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
    public class RegisterUserAsyncTask1 extends AsyncTask<Call, Void, String> {

        @Override
        protected String doInBackground(Call... calls) {
            try {
                Call<ResultRegister> call = calls[0];
                Response<ResultRegister> re = call.execute();
//            if (re.body() != null) {
                return re.body().getResult();
//            } else {
//                return null;
//            }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            //if (aVoid == null) return;
            Toast.makeText(RegisterPage.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterPage.this,LoginPage.class);
            intent.putExtra("username",aVoid);
            startActivity(intent);
        }
    }

}
