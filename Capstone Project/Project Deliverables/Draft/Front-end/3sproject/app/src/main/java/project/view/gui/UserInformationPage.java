package project.view.gui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import project.firebase.Firebase;
import project.objects.User;
import project.retrofit.APIService;
import project.view.R;
import project.view.fragment.home.UserFragment;
import project.view.model.Brand;
import project.view.util.TweakUI;
import retrofit2.Call;
import retrofit2.Response;

public class UserInformationPage extends BasePage {
    private TextView txtName, txtPhone, txtEmail, txtGender, txtDob, storeName,nullMessage;
    private ImageView btnBack;
    private Button btnEditInfo;
    private final int REQUEST_PROFILE_CODE = 123;
    private APIService apiService;
    private ProgressBar loadingBar;
    private String nameDisplay;
    private CircleImageView profileImage;
    private User us;
    private RelativeLayout userInformation;
    String userAvatarPath;
    private StorageReference storageReference = Firebase.getFirebase();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information_page);
        TweakUI.makeTransparent(this);
        mapping();
        loadingBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);

        apiService = APIService.retrofit.create(APIService.class);
       // Toast.makeText(this, this.getIntent().getIntExtra("userID",0)+"", Toast.LENGTH_LONG).show();

        Call<User> call = apiService.getInformation(getIntent().getIntExtra("userID",0));
        new UserDataClass().execute(call);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent backToUserFragment = new Intent(UserInformationPage.this, UserFragment.class);
               if(nameDisplay!=null){
                   backToUserFragment.putExtra("nameDisplay",nameDisplay+"");
               }
               if(userAvatarPath!=null && !userAvatarPath.isEmpty()){
                   backToUserFragment.putExtra("path",userAvatarPath);
               }

               setResult(222,backToUserFragment);
               finish();
            }
        });
        btnEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                extras.putString("name", txtName.getText().toString());
                extras.putString("phone", txtPhone.getText().toString());
                extras.putString("email", txtEmail.getText().toString());
                extras.putString("dob", txtDob.getText().toString());
                extras.putString("gender", txtGender.getText().toString());
                extras.putInt("idUser",getIntent().getIntExtra("userID",0));
                extras.putString("path",userAvatarPath);
                Intent toEditUserInformationPage = new Intent(UserInformationPage.this, EditUserInformationPage.class);
                toEditUserInformationPage.putExtras(extras);
                startActivityForResult(toEditUserInformationPage, REQUEST_PROFILE_CODE);

            }
        });
        //gọi hàm lưu trạng thái ở đây
//        savingPreferences(name, phone, email, gender, dob);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
//
//        String name = txtName.getText().toString();
//        String phone = txtPhone.getText().toString();
//        String email = txtEmail.getText().toString();
//        String gender = txtGender.getText().toString();
//        String dob = txtDob.getText().toString();
//
//        //gọi hàm lưu trạng thái ở đây
//        savingPreferences(name, phone, email, gender, dob);
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //gọi hàm đọc trạng thái ở đây
      //  restoringPreferences();
    }


    private void mapping() {
        btnEditInfo = findViewById(R.id.btnEditInfo);
        btnBack = findViewById(R.id.backBtn);
        txtName = findViewById(R.id.txt_name);
        txtPhone = findViewById(R.id.txt_phone);
        txtEmail = findViewById(R.id.txt_email);
        txtGender = findViewById(R.id.txt_gender);
        txtDob = findViewById(R.id.txt_dob);
        storeName = findViewById(R.id.storeName);
        loadingBar = findViewById(R.id.loadingBar);
        userInformation = findViewById(R.id.userInformation);
        nullMessage = findViewById(R.id.nullMessage);
        profileImage = findViewById(R.id.profile_image);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PROFILE_CODE && resultCode == 200) {
            Call<User> call = apiService.getInformation(getIntent().getIntExtra("userID",0));
            new UserDataClass().execute(call);
            Toast.makeText(UserInformationPage.this, "Thay đổi thông tin thành công!", Toast.LENGTH_LONG).show();
        }
        else if (requestCode == REQUEST_PROFILE_CODE && resultCode == 201) {
            Toast.makeText(UserInformationPage.this, "Thông tin không thay đổi", Toast.LENGTH_LONG).show();
        }
        if (requestCode == REQUEST_PROFILE_CODE && resultCode == 202) {
            Toast.makeText(UserInformationPage.this, "Thay đổi thông tin thành công", Toast.LENGTH_LONG).show();
            userAvatarPath = data.getStringExtra("path");
            if (!userAvatarPath.isEmpty() && userAvatarPath!=null) {
                if (userAvatarPath.contains("graph") || userAvatarPath.contains("google")) {
                    Glide.with(UserInformationPage.this /* context */)
                            .load(userAvatarPath)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(profileImage);
                } else {
                    Glide.with(UserInformationPage.this /* context */)
                            .using(new FirebaseImageLoader())
                            .load(storageReference.child(userAvatarPath))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(profileImage);

                }
            }
        }
    }



    public void restoringPreferences(){
        SharedPreferences pre=getSharedPreferences
                ("main",MODE_PRIVATE);
        //lấy giá trị checked ra, nếu không thấy thì giá trị mặc định là false

        //lấy user, pwd, nếu không thấy giá trị mặc định là rỗng
        String name = pre.getString("name", "");
        String phone = pre.getString("phone", "");
        String email = pre.getString("email", "");
        String gender = pre.getString("gender", "");
        String dob = pre.getString("dob", "");

        txtName.setText(name);
        txtPhone.setText(phone);
        txtEmail.setText(email);
        txtGender.setText(gender);
        txtDob.setText(dob);

    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
     public class UserDataClass extends AsyncTask<Call,User,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingBar.setVisibility(View.VISIBLE);
            userInformation.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            boolean check = false;
            if (us.getLast_name() != null) {
                check = true;
            }
            if(check == true){
                loadingBar.setVisibility(View.INVISIBLE);
                userInformation.setVisibility(View.VISIBLE);
            }
            final boolean finalCheck = check;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (finalCheck == false) {
                        Toast.makeText(UserInformationPage.this, "Có lỗi xảy ra. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                        nullMessage.setText("Có lỗi xảy ra, vui lòng tải lại trang!");
                        loadingBar.setVisibility(View.INVISIBLE);
                        userInformation.setVisibility(View.INVISIBLE);
                    }
                }
            },10000);
        }

        @Override
        protected void onProgressUpdate(User... values) {
            super.onProgressUpdate(values);
            if(values[0]!=null){
                us = values[0];
                txtName.setText(us.getLast_name());
                txtPhone.setText(us.getPhone());
                txtEmail.setText(us.getEmail());
                txtGender.setText(us.getGender());
                txtDob.setText(us.getDateOfBirth());
                nameDisplay = txtName.getText().toString();
                userAvatarPath = us.getImage_path();
                if (!userAvatarPath.isEmpty()) {
                    if (userAvatarPath.contains("graph") || userAvatarPath.contains("google")) {
                        Glide.with(UserInformationPage.this /* context */)
                                .load(userAvatarPath)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(profileImage);
                    } else {
                        Glide.with(UserInformationPage.this /* context */)
                                .using(new FirebaseImageLoader())
                                .load(storageReference.child(userAvatarPath))
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(profileImage);

                    }
                }
            }

        }

        @Override
        protected Void doInBackground(Call... calls) {

            try {
                Call<User> call = calls[0];
                Response<User> response = call.execute();
                if(response.body() != null){
                    publishProgress(response.body());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
