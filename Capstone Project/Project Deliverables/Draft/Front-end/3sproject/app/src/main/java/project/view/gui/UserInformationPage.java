package project.view.gui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import project.objects.User;
import project.retrofit.APIService;
import project.view.R;
import project.view.fragment.home.UserFragment;
import project.view.model.Brand;
import project.view.util.TweakUI;
import retrofit2.Call;
import retrofit2.Response;

public class UserInformationPage extends BasePage {
    private TextView txtName, txtPhone, txtEmail, txtGender, txtDob, storeName;
    private ImageView btnBack;
    private Button btnEditInfo;
    private final int REQUEST_PROFILE_CODE = 123;
    private APIService apiService;
    private ProgressBar loadingBar;
    private String nameDisplay;
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
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PROFILE_CODE && resultCode == 200) {
            Bundle extras = data.getExtras();

            String name = extras.getString("name");
            String phone = extras.getString("phone");
            String email = extras.getString("email");
            String gender = extras.getString("gender");
            String dob = extras.getString("dob");
            txtName.setText(name);
            txtPhone.setText(phone);
            txtEmail.setText(email);
            txtDob.setText(dob);
            txtGender.setText(gender);
            nameDisplay = txtName.getText().toString();
         //   savingPreferences(name, phone, email, gender, dob);
            Toast.makeText(UserInformationPage.this, "Thay đổi thông tin thành công!", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == REQUEST_PROFILE_CODE && resultCode == 201) {
            Toast.makeText(UserInformationPage.this, "Thông tin không thay đổi", Toast.LENGTH_SHORT).show();
        }
    }

//    public void savingPreferences(String name, String phone, String email, String gender, String dob){
//        //tạo đối tượng getSharedPreferences
//        SharedPreferences pre=getSharedPreferences("main", MODE_PRIVATE);
//        //tạo đối tượng Editor để lưu thay đổi
//        SharedPreferences.Editor editor=pre.edit();
//        //lưu vào editor
//        editor.putString("name", name);
//        editor.putString("phone", phone);
//        editor.putString("email", email);
//        editor.putString("gender", gender);
//        editor.putString("dob", dob);
//        //chấp nhận lưu xuống file
//        editor.commit();
//    }

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
     public class UserDataClass extends AsyncTask<Call,User,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadingBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(User... values) {
            super.onProgressUpdate(values);
            User us = values[0];
            txtName.setText(us.getLast_name());
            txtPhone.setText(us.getPhone());
            txtEmail.setText(us.getEmail());
            txtGender.setText(us.getGender());
            txtDob.setText(us.getDateOfBirth());
            nameDisplay = txtName.getText().toString();
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
