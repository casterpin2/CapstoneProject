package project.view.UserInformation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import project.view.EditUserInformation.EditUserInformationPage;
import project.view.R;

public class UserInformationPage extends AppCompatActivity {
    private TextView txtName, txtAddress, txtPhone, txtEmail, txtGender, txtDob, storeName;

    private final int REQUEST_PROFILE_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information_page);



        TweakUI.makeTransparent(this);
        mapping();

        String name = txtName.getText().toString();
        String address = txtAddress.getText().toString();
        String phone = txtPhone.getText().toString();
        String email = txtEmail.getText().toString();
        String gender = txtGender.getText().toString();
        String dob = txtDob.getText().toString();

        //gọi hàm lưu trạng thái ở đây
        savingPreferences(name, address, phone, email, gender, dob);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        String name = txtName.getText().toString();
        String address = txtAddress.getText().toString();
        String phone = txtPhone.getText().toString();
        String email = txtEmail.getText().toString();
        String gender = txtGender.getText().toString();
        String dob = txtDob.getText().toString();

        //gọi hàm lưu trạng thái ở đây
        savingPreferences(name, address, phone, email, gender, dob);
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //gọi hàm đọc trạng thái ở đây
        restoringPreferences();
    }

    public void editProfile(View view) {
        Bundle extras = new Bundle();
        extras.putString("name", txtName.getText().toString());
        extras.putString("phone", txtPhone.getText().toString());
        extras.putString("address", txtAddress.getText().toString());
        extras.putString("email", txtEmail.getText().toString());
        extras.putString("dob", txtDob.getText().toString());
        extras.putString("gender", txtGender.getText().toString());

        Intent toEditUserInformationPage = new Intent(UserInformationPage.this, EditUserInformationPage.class);
        toEditUserInformationPage.putExtras(extras);
        startActivityForResult(toEditUserInformationPage, REQUEST_PROFILE_CODE);
    }


    private void mapping() {
        txtName = findViewById(R.id.txt_name);
        txtPhone = findViewById(R.id.txt_phone);
        txtAddress = findViewById(R.id.txt_address);
        txtEmail = findViewById(R.id.txt_email);
        txtGender = findViewById(R.id.txt_gender);
        txtDob = findViewById(R.id.txt_dob);
        storeName = findViewById(R.id.storeName);
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.layout.activity_edit_user_information_page, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PROFILE_CODE && resultCode == 200) {
            Bundle extras = data.getExtras();

            String name = extras.getString("name");
            String address = extras.getString("address");
            String phone = extras.getString("phone");
            String email = extras.getString("email");
            String gender = extras.getString("gender");
            String dob = extras.getString("dob");

            txtName.setText(name);
            txtAddress.setText(address);
            txtPhone.setText(phone);
            txtEmail.setText(email);
            txtDob.setText(dob);
            txtGender.setText(gender);

            savingPreferences(name, address, phone, email, gender, dob);
        }
        else if (requestCode == REQUEST_PROFILE_CODE && resultCode == 201) {
            Toast.makeText(UserInformationPage.this, "Thông tin không thay đổi", Toast.LENGTH_SHORT).show();
        }
    }

    public void savingPreferences(String name, String address, String phone, String email, String gender, String dob){
        //tạo đối tượng getSharedPreferences
        SharedPreferences pre=getSharedPreferences("main", MODE_PRIVATE);
        //tạo đối tượng Editor để lưu thay đổi
        SharedPreferences.Editor editor=pre.edit();
        //lưu vào editor
        editor.putString("name", name);
        editor.putString("address", address);
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
        String address = pre.getString("address", "");
        String phone = pre.getString("phone", "");
        String email = pre.getString("email", "");
        String gender = pre.getString("gender", "");
        String dob = pre.getString("dob", "");

//        Toast.makeText(this, "name" + name, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "address" + address, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "phone" + phone, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "gender" + gender, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "dob" + dob, Toast.LENGTH_SHORT).show();

        txtName.setText(name);
        txtAddress.setText(address);
        txtPhone.setText(phone);
        txtEmail.setText(email);
        txtGender.setText(gender);
        txtDob.setText(dob);

    }

}
