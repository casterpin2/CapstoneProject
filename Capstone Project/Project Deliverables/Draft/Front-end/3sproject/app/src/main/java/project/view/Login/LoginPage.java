package project.view.Login;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import project.objects.User;
import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.Register.RegisterActivity;
import project.view.home.HomeActivity;
import project.view.util.CustomInterface;
import retrofit2.Call;
import retrofit2.Response;

public class LoginPage extends AppCompatActivity {

    private ScrollView scroll;
    private Button loginBtn, loginFBBtn, loginGPBtn;
    private TextView toRegisterPage, toForgetPasswordPage, errorMessage, usernameValue, passwordValue ;
    private APIService mAPI;
    public static Login login = new Login();
    private CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        mAPI = ApiUtils.getAPIService();
        callbackManager = CallbackManager.Factory.create();
        getSupportActionBar().setTitle(getResources().getString(R.string.login_page_login3SBtn));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        findView();

        CustomInterface.setStatusBarColor(this);

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
                String username = usernameValue.getText().toString().trim();
                String password = MD5Library.md5(passwordValue.getText().toString().trim());
                Call<Login> call = mAPI.login(username,password);
                new CallAPI().execute(call);

            }
        });
        loginFBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginPage.this, Arrays.asList("public_profile"));

            }
        });

        //Facebook Login/////////////////////////////////////////////////////////////
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                final User user = new User();
                final Profile profile = Profile.getCurrentProfile();
                if (profile.getProfilePictureUri(100,100).getPath() != null)
                    user.setImage_path("http://graph.facebook.com" + profile.getProfilePictureUri(100,100).getPath());
                else {user.setImage_path("");}
                user.setFirst_name(profile.getFirstName());
                user.setLast_name(profile.getLastName());
                Call<Login> call = mAPI.loginFB(user,profile.getId());
                new CallAPI().execute(call);
            }

            @Override
            public void onCancel() {

                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                if (exception instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                        LoginManager.getInstance().logInWithReadPermissions(LoginPage.this, Arrays.asList("public_profile"));
                    }
                }
            }
        });
        //Facebook Login///////////////////////////////////////////////////////////////////
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /////////////////////////////////////////////////////API////////////////////////////////////////////////////////////////
    private class CallAPI extends AsyncTask<Call, Void, Login> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Login result) {
            LoginPage.login = result;
            if (login.getUser() == null) {
                errorMessage.setText("Sai ");
            }else {
                //Toast.makeText(LoginPage.this, LoginPage.login.getUser().toString(), Toast.LENGTH_LONG).show();
                Intent toHomePage = new Intent(LoginPage.this, HomeActivity.class);
                startActivity(toHomePage);
            }
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected Login doInBackground(Call... calls) {
            Login result = null;
            try {
                Call<Login> call = calls[0];
                Response<Login> response = call.execute();
                result = response.body();
            } catch (IOException e) {
            }
            return result;
        }
    }
}
