package project.view.gui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
    private TextView etUserName, etName, etPassword, etConfirmPass, etEmail, etPhoneNumber;
    private TextView tvUserName, tvName, tvPassword, tvConfirmPassword, tvEmail, tvPhoneNumber, toLoginPageBtn;
    private Button btnRegister;
    private boolean isUserName = false, isName = false, isPassword = false, confirm = false, isEmail = false, isPhone = false;
    private APIService apiUserService;
    private User us;
    private Gson gson;
    private RelativeLayout main_layout;
    private Regex regex;
    private ProgressBar loadingBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findView();
        customView();

        us = new User();
        regex = new Regex();
        apiUserService = APIService.retrofit.create(APIService.class);

        etUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    isUserName = regex.checkUserName(tvUserName, etUserName);
                    if (isUserName) {
                        apiUserService = ApiUtils.getAPIService();
                        final Call<Integer> call = apiUserService.vadilator(etUserName.getText().toString(), etEmail.getText().toString(), etPhoneNumber.getText().toString(), "username");
                        new ValidatorUser().execute(call);
                    }
                }

            }
        });

        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    isName = regex.checkDisplayName(tvName, etName);
                }
            }
        });

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    isPassword = regex.checkPass(tvPassword, etPassword);
                }
            }
        });
//        etConfirmPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    if(etConfirmPass.getText().toString().equals(etPassword.getText().toString())){
//                        confirm = true;
//                        tvConfirmPassword.setText("");
//                    }
//                    else {
//                        tvConfirmPassword.setText(R.string.confirm_password);
//                    }
//                }
//            }
//        });

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    isEmail = regex.checkEmail(tvEmail, etEmail);
                    if (isEmail) {
                        apiUserService = ApiUtils.getAPIService();
                        final Call<Integer> call = apiUserService.vadilator(etUserName.getText().toString(), etEmail.getText().toString(), etPhoneNumber.getText().toString(), "email");
                        new ValidatorUser().execute(call);
                    }
                }
            }
        });

        etPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    isPhone = regex.checkPhone(tvPhoneNumber, etPhoneNumber);
                    if (isPhone) {
                        apiUserService = ApiUtils.getAPIService();
                        final Call<Integer> call = apiUserService.vadilator(etUserName.getText().toString(), etEmail.getText().toString(), etPhoneNumber.getText().toString(), "phone");
                        new ValidatorUser().execute(call);
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

                isUserName = regex.checkUserName(tvUserName, etUserName);
                isName = regex.checkDisplayName(tvName, etName);
                isPassword = regex.checkPass(tvPassword, etPassword);
                if (etConfirmPass.getText().toString().equals(etPassword.getText().toString())) {
                    confirm = true;
                } else {
                    tvConfirmPassword.setText(R.string.confirm_password);
                }
                isEmail = regex.checkEmail(tvEmail, etEmail);
                isPhone = regex.checkPhone(tvPhoneNumber, etPhoneNumber);

                if (isUserName && isName && isPassword && confirm && isEmail && isPhone) {
                    us.setUsername(etUserName.getText().toString());
                    us.setPhone(etPhoneNumber.getText().toString().trim().replaceAll("\\s+", ""));
                    StringTokenizer st = new StringTokenizer(etName.getText().toString().trim().replaceAll("\\s+", " "), " ");
                    if (st.hasMoreTokens())
                        us.setFirst_name(st.nextToken());
                    StringBuilder last_name = new StringBuilder();
                    while (st.hasMoreTokens()) {
                        last_name.append(st.nextToken()+" ");
                    }
                    us.setLast_name(last_name.toString().trim());
                    us.setEmail(etEmail.getText().toString().trim().replaceAll("\\s+", ""));
                    us.setPassword(MD5Library.md5(etPassword.getText().toString().trim()));
                    gson = new Gson();
                    final Call<ResultRegister> call = apiUserService.registerUserNew(us);
                    RegisterUserAsyncTask1 asyncTask = new RegisterUserAsyncTask1();
                    if (call != null) {
                        asyncTask.execute(call);
                    } else {
                        Toast.makeText(getBaseContext(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void customView() {
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view, getBaseContext());
                return false;
            }
        });
        CustomInterface.setStatusBarColor(this);
        getSupportActionBar().setTitle(R.string.title_register_screen);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void findView() {
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
        loadingBar = findViewById(R.id.loadingBar);
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
            if (nameCheck != null) {
                switch (nameCheck) {
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
            } else {
                Toast.makeText(getBaseContext(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(Call... calls) {

            try {
                Call<Integer> call = calls[0];
                if (call != null) {
                    Response<Integer> re = call.execute();
                    publishProgress(re.body() + "");
                } else {
                    Toast.makeText(getBaseContext(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class RegisterUserAsyncTask1 extends AsyncTask<Call, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingBar.setVisibility(View.VISIBLE);
            btnRegister.setText("");
            btnRegister.setEnabled(false);
            toLoginPageBtn.setEnabled(false);
        }

        @Override
        protected String doInBackground(Call... calls) {
            try {
                Call<ResultRegister> call = calls[0];
                if (call != null) {
                    Response<ResultRegister> re = call.execute();
//            if (re.body() != null) {
                    return re.body().getResult();
                } else {
                    Toast.makeText(getBaseContext(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
                }

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
            if (aVoid == null) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterPage.this, "Có lỗi xảy ra. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                        loadingBar.setVisibility(View.INVISIBLE);
                        btnRegister.setText("Đăng kí");
                        btnRegister.setEnabled(true);
                        toLoginPageBtn.setEnabled(true);
                        return;
                    }
                }, 3000);
            } else {
                loadingBar.setVisibility(View.INVISIBLE);
                btnRegister.setText("Đăng kí");
                btnRegister.setEnabled(true);
                toLoginPageBtn.setEnabled(true);
                Toast.makeText(RegisterPage.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                intent.putExtra("username", aVoid);
                startActivity(intent);
                finish();
            }

        }
    }

}
