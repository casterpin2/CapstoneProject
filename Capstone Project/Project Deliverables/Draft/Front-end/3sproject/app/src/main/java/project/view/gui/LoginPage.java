package project.view.gui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.provider.Settings.Secure;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;

import project.objects.User;
import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.fragment.home.HomeFragment;
import project.view.model.Store;
import project.view.model.Login;
import project.view.util.CustomInterface;
import project.view.util.MD5Library;
import retrofit2.Call;
import retrofit2.Response;

public class LoginPage extends AppCompatActivity {

    private ScrollView scroll;
    private Button loginBtn, loginFBBtn, loginGPBtn;
    private TextView toForgetPasswordPage, toRegisterPage, errorMessage, usernameValue, passwordValue ;
    private APIService mAPI;
    public static Login login = new Login();
    private CallbackManager callbackManager;
    private ProgressBar loadingBar;
    private RelativeLayout  main_layout;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        findView();
        mAPI = ApiUtils.getAPIService();
        callbackManager = CallbackManager.Factory.create();
        getSupportActionBar().setTitle(getResources().getString(R.string.login_page_login3SBtn));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CustomInterface.setStatusBarColor(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        loadingBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view,getBaseContext());
                return false;
            }
        });
        CustomInterface.setStatusBarColor(this);
        scroll.setVerticalScrollBarEnabled(false);
        scroll.setHorizontalScrollBarEnabled(false);


        toRegisterPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toRegisterPage = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(toRegisterPage);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameValue.getText().toString().trim();
                String password = MD5Library.md5(passwordValue.getText().toString().trim());
                Call<Login> call = mAPI.login(username,password);

                new LoginTo3S().execute(call);


            }
        });
        loginFBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginPage.this, Arrays.asList("public_profile"));
            }
        });

        toForgetPasswordPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toForgetPasswordPageLayout = new Intent(getBaseContext(), GetPasswordPage.class);
                startActivity(toForgetPasswordPageLayout);
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
                try {
                    Log.d("facebookId",profile.getId()+"");
                    Call<Login> call = mAPI.loginFB(user, profile.getId());
                    new LoginTo3S().execute(call);
                } catch(Exception e){

                }
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
                        return;
                    }
                }
                errorMessage.setText("Không có mạng ");
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
        errorMessage = (TextView) findViewById(R.id.errorMessage);
        usernameValue = (TextView) findViewById(R.id.usernameValue);
        passwordValue = (TextView) findViewById(R.id.passwordValue);
        String username = getIntent().getStringExtra("username");
        usernameValue.setText(username);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);
        toForgetPasswordPage = findViewById(R.id.toForgetPasswordPageBtn);
        main_layout = findViewById(R.id.main_layout);
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
//        if (requestCode == RC_SIGN_IN) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
//        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /////////////////////////////////////////////////////API////////////////////////////////////////////////////////////////
    private class LoginTo3S extends AsyncTask<Call, Void, Login> {
        @Override
        protected void onPreExecute() {
            loadingBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Login result) {
            if (result == null){
                errorMessage.setText("Có lỗi xảy ra ");
                loadingBar.setVisibility(View.INVISIBLE);
                return;
            }
            if (result.getUser().getId() == 0) {
                errorMessage.setText("Tên tài khoản hoặc mật khẩu không đúng, xin vui lòng đăng nhập lại");
            }else {
                //Toast.makeText(LoginPage.this, LoginPage.login.getUser().toString(), Toast.LENGTH_LONG).show();
                User user = result.getUser();
                Store store = result.getStore();
                savingPreferences(user,store);
                final Intent toHomePage = new Intent(LoginPage.this, HomePage.class);
                Bundle bundle = new Bundle();
                bundle.putString("user",new Gson().toJson(user));
                bundle.putString("store",new Gson().toJson(store));
                toHomePage.putExtras(bundle);
                //Glide.get(LoginPage.this).clearDiskCache();
                final String android_id = Secure.getString(LoginPage.this.getContentResolver(),
                        Secure.ANDROID_ID);
                myRef = database.getReference().child("authentication").child(String.valueOf(user.getId())).child("device_id");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.getValue(String.class).equalsIgnoreCase(android_id))
                                return;
                            myRef.setValue(android_id);
                            startActivity(toHomePage);
                            finishAffinity();
                            finish();
                        } else{
                            myRef.setValue(android_id);
                            startActivity(toHomePage);
                            finishAffinity();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
            loadingBar.setVisibility(View.INVISIBLE);
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
                //Glide.get(LoginPage.this).clearDiskCache();
            } catch (IOException e) {
            }

            return result;
        }
    }

    private void savingPreferences(User user , Store store){
            SharedPreferences pre = getSharedPreferences("authentication", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pre.edit();
            editor.putString("user", new Gson().toJson(user));
            editor.putString("store", new Gson().toJson(store));
            editor.commit();
    }
}
