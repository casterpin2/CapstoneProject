package project.view.EditUserInformation;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import project.view.R;
import project.view.UserInformation.UserInformationPage;

public class EditUserInformationPage extends AppCompatActivity {

    private EditText userNameText, addressText, phoneText, emailText, dobText;
    private Button saveBtn, cancelBtn;
    private ImageButton backBtn;
    private Spinner genderSpinner;
    Calendar myCalendar ;
    Bundle extras;

    String[] genderName = {"Chưa xác định", "Nam", "Nữ"};
    int spinnerImage[] = {R.color.transparent, R.drawable.male, R.drawable.female};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_information_page);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mapping();
        getIncomingIntent();
        setLastSelector();


        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),spinnerImage,genderName);
        genderSpinner.setAdapter(customAdapter);

        Intent intent = getIntent();
        final Bundle extras = intent.getExtras();

        String gender = extras.getString("gender");
        if(gender.equals("")){
            genderSpinner.setSelection(0);
        } else if (gender.equals("Nam")){
            genderSpinner.setSelection(1);
        } else if(gender.equals("Nữ")) {
            genderSpinner.setSelection(2);
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();

                String name = userNameText.getText().toString();
                String phone = phoneText.getText().toString();
                String email = emailText.getText().toString();
                String dob = dobText.getText().toString();
                String address = addressText.getText().toString();

                String gender = "";
                if (genderSpinner.getSelectedItemPosition() == 0) {
                    gender = "";
                } else if (genderSpinner.getSelectedItemPosition() == 1) {
                    gender = "Nam";
                } else if (genderSpinner.getSelectedItemPosition() == 2){
                    gender = "Nữ";
                }


                if (extras.getString("name").equals(name) && extras.getString("phone").equals(phone)
                        && extras.getString("email").equals(email) && extras.getString("dob").equals(dob)
                        && extras.getString("address").equals(address) && extras.getString("gender").equals(gender)) {

                    Intent toUserInformationPage = new Intent(EditUserInformationPage.this, UserInformationPage.class);
                    startActivity(toUserInformationPage);

                } else {
                    // code service here
                    Toast.makeText(EditUserInformationPage.this, "Done!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(extras.getString("name").equals(userNameText.getText().toString())
                        && extras.getString("phone").equals(phoneText.getText().toString())
                        && extras.getString("address").equals(addressText.getText().toString())
                        && extras.getString("email").equals(emailText.getText().toString())
                        && extras.getString("dob").equals(dobText.getText().toString())) {
                    onBackPressed();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(EditUserInformationPage.this);
                    builder.setTitle(R.string.back_alertdialog_title);
                    builder.setMessage(R.string.back_alertdialog_content);

                    builder.setPositiveButton(R.string.alertdialog_acceptButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onBackPressed();
                        }
                    });

                    builder.setNegativeButton(R.string.alertdialog_cancelButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    builder.show();
                }
            }
        });

        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(year,monthOfYear,dayOfMonth);
                updateLabel();
            }

        };

        dobText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditUserInformationPage.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(extras.getString("name").equals(userNameText.getText().toString())
                        && extras.getString("phone").equals(phoneText.getText().toString())
                        && extras.getString("address").equals(addressText.getText().toString())
                        && extras.getString("email").equals(emailText.getText().toString())
                        && extras.getString("dob").equals(dobText.getText().toString())) {
                    onBackPressed();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(EditUserInformationPage.this);
                    builder.setTitle(R.string.back_alertdialog_title);
                    builder.setMessage(R.string.back_alertdialog_content);

                    builder.setPositiveButton(R.string.alertdialog_acceptButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onBackPressed();
                        }
                    });

                    builder.setNegativeButton(R.string.alertdialog_cancelButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    builder.show();
                }

            }
        });
    }

    private void mapping() {
        userNameText = findViewById(R.id.userNameText);
        addressText = findViewById(R.id.addressText);
        phoneText = findViewById(R.id.phoneText);
        emailText = findViewById(R.id.emailText);
        dobText =  findViewById(R.id.dobText);

        saveBtn = findViewById(R.id.saveBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        backBtn = findViewById(R.id.backBtn);
        genderSpinner = findViewById(R.id.genderSpinner);

    }

    private void getIncomingIntent() {
        Intent intent = getIntent();
        extras = intent.getExtras();

        if (extras != null) {
            userNameText.setText(extras.getString("name"));
            dobText.setText(extras.getString("dob"));
            emailText.setText(extras.getString("email"));
            addressText.setText(extras.getString("address"));
            phoneText.setText(extras.getString("phone"));

//            String gender = extras.getString("gender");
//            if (gender != null) {
//                if (gender.equals("Nam")) {
//                    rdMale.setChecked(true);
//                } else {
//                    rdFemale.setChecked(true);
//                }
//            }
        }
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dobText.setText(sdf.format(myCalendar.getTime()));
    }

    private void setLastSelector(){
        userNameText.setSelection(userNameText.getText().length());
        dobText.setSelection(dobText.getText().length());
        emailText.setSelection(emailText.getText().length());
        addressText.setSelection(addressText.getText().length());
        phoneText.setSelection(phoneText.getText().length());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(userNameText.getText().toString().equals(extras.getString("name")) && addressText.getText().toString().equals(extras.getString("address"))
                        && phoneText.getText().toString().equals(extras.getString("phone")) && emailText.getText().toString().equals(extras.getString("email"))){
                    finish();
                    return true;
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditUserInformationPage.this);
                    builder.setTitle(R.string.back_alertdialog_title);
                    builder.setMessage(R.string.back_alertdialog_content);

                    builder.setPositiveButton(R.string.alertdialog_acceptButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

                    builder.setNegativeButton(R.string.alertdialog_cancelButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    builder.show();
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
