package project.view.Register;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.Category.Category;
import project.view.Login.LoginPage;
import project.view.R;
import project.view.UserInformation.UserInformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText etUserName, etPassword, etConfirmPass, etEmail, etPhoneNumber;
    private TextView tvUserName, tvPassword, tvConfirmPassword, tvEmail, tvPhoneNumber, toLoginPageBtn;
    private Button btnRegister;
    private boolean isUserName, isPassword, confirm, isEmail, isPhone = true;
    private boolean checkUsernameTest;
    private ScrollView scroll;
    private APIService apiUserService;
    private UserInformation us;
    private String usernameTemp;
    private Gson gson;
    private String jsonString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        apiUserService = ApiUtils.getAPIService();
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
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
        us = new UserInformation();
        //   etUserName.setText("datduc");

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
                    Toast.makeText(getBaseContext(), isEmail + " a", Toast.LENGTH_SHORT);
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
                Intent toLoginPage = new Intent(RegisterActivity.this, LoginPage.class);
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



                if (isUserName && isPassword && confirm && isEmail && isPhone ) {
                    us.setUserName(etUserName.getText().toString());
                    us.setPhone(etPhoneNumber.getText().toString());
                    us.setEmail(etEmail.getText().toString());
                    us.setPassword(etPassword.getText().toString());
                    gson = new Gson();
                    List<UserInformation> list = new ArrayList<>();
                    list.add(us);
                    String test = gson.toJson(list);
                    //Toast.makeText(RegisterActivity.this, test+"", Toast.LENGTH_LONG).show();
                    apiUserService.registerUserNew(test).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
//                            String inta = response.body();
//                            Toast.makeText(RegisterActivity.this, inta+"", Toast.LENGTH_SHORT).show();
                            if (response.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "OK", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {

                        }
                    });


                } else {
//                    apiUserService.getCategory().enqueue(new Callback<List<Category>>() {
//                        @Override
//                        public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
//                            int i = response.body().size();
//                            Toast.makeText(RegisterActivity.this, i+"", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onFailure(Call<List<Category>> call, Throwable t) {
//
//                        }
//                    });
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

    private void checkUserName(final String input) {
        Regex validate = new Regex();
        isUserName = validate.isUserName(input);
        if (!input.isEmpty() && !isUserName) {
            tvUserName.setText(R.string.error_validate_username);
        } else
            tvUserName.setText("");
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
                    isUserName = true;
                    break;
                case "2":
                    tvEmail.setText(R.string.duplicate_email);
                    isEmail = true;
                    break;
                case "3":
                    isPhone = true;
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
}
