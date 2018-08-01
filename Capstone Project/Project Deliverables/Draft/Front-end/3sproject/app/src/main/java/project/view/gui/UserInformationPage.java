package project.view.gui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import project.view.R;
import project.view.util.TweakUI;

public class UserInformationPage extends AppCompatActivity {
    private TextView txtName, txtPhone, txtEmail, txtGender, txtDob, storeName;
    private ImageView btnBack;
    private Button btnEditInfo;
    private final int REQUEST_PROFILE_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information_page);
        TweakUI.makeTransparent(this);
        mapping();

        String name = txtName.getText().toString();
        String phone = txtPhone.getText().toString();
        String email = txtEmail.getText().toString();
        String gender = txtGender.getText().toString();
        String dob = txtDob.getText().toString();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

                Intent toEditUserInformationPage = new Intent(UserInformationPage.this, EditUserInformationPage.class);
                toEditUserInformationPage.putExtras(extras);
                startActivityForResult(toEditUserInformationPage, REQUEST_PROFILE_CODE);
            }
        });
        //gọi hàm lưu trạng thái ở đây
        savingPreferences(name, phone, email, gender, dob);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        String name = txtName.getText().toString();
        String phone = txtPhone.getText().toString();
        String email = txtEmail.getText().toString();
        String gender = txtGender.getText().toString();
        String dob = txtDob.getText().toString();

        //gọi hàm lưu trạng thái ở đây
        savingPreferences(name, phone, email, gender, dob);
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //gọi hàm đọc trạng thái ở đây
        restoringPreferences();
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

            savingPreferences(name, phone, email, gender, dob);
        }
        else if (requestCode == REQUEST_PROFILE_CODE && resultCode == 201) {
            Toast.makeText(UserInformationPage.this, "Thông tin không thay đổi", Toast.LENGTH_SHORT).show();
        }
    }

    public void savingPreferences(String name, String phone, String email, String gender, String dob){
        //tạo đối tượng getSharedPreferences
        SharedPreferences pre=getSharedPreferences("main", MODE_PRIVATE);
        //tạo đối tượng Editor để lưu thay đổi
        SharedPreferences.Editor editor=pre.edit();
        //lưu vào editor
        editor.putString("name", name);
        editor.putString("phone", phone);
        editor.putString("email", email);
        editor.putString("gender", gender);
        editor.putString("dob", dob);
        //chấp nhận lưu xuống file
        editor.commit();
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

}
