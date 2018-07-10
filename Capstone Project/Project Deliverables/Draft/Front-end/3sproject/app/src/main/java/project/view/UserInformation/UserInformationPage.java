package project.view.UserInformation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

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
        startActivity(toEditUserInformationPage);
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
            txtName.setText(extras.getString("name"));
            txtAddress.setText(extras.getString("address"));
            txtPhone.setText(extras.getString("phone"));
            txtEmail.setText(extras.getString("email"));
            txtDob.setText(extras.getString("dob"));
            txtGender.setText(extras.getString("gender"));
        }
    }

}
