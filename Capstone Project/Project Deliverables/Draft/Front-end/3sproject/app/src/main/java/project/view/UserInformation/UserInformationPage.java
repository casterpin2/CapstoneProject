package project.view.UserInformation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import project.view.EditUserInformation.EditUserInformationPage;
import project.view.R;

public class UserInformationPage extends AppCompatActivity {
    private UserInformation userInfor;
    private TextView txtName, txtAddress, txtPhone, txtEmail, txtGender, txtDob;
    private ImageView btnEditImg;


    private final int REQUEST_PROFILE_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information_page);

        mapping();

        btnEditImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                extras.putString("name", txtName.getText().toString());
                extras.putString("phone", txtPhone.getText().toString());
                extras.putString("address", txtAddress.getText().toString());
                extras.putString("email", txtEmail.getText().toString());
                extras.putString("dob", txtDob.getText().toString());
                extras.putString("gender", txtGender.getText().toString());

                Intent intent = new Intent(UserInformationPage.this, EditUserInformationPage.class);
                intent.putExtras(extras);
                startActivityForResult(intent, REQUEST_PROFILE_CODE);
            }
        });
    }

    private void mapping() {
        txtName = findViewById(R.id.txt_name);
        txtPhone = findViewById(R.id.txt_phone);
        txtAddress = findViewById(R.id.txt_address);
        txtEmail = findViewById(R.id.txt_email);
        txtGender = findViewById(R.id.txt_gender);
        txtDob = findViewById(R.id.txt_dob);
        btnEditImg = findViewById(R.id.btnEdit);
    }

//    @SuppressLint("ResourceType")
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.layout.activity_editprofile, menu);
//        return true;
//    }

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
