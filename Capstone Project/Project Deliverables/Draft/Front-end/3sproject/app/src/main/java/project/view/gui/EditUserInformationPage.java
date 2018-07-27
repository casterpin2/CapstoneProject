package project.view.gui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import project.view.R;
import project.view.util.CustomInterface;
import project.view.util.TweakUI;

public class EditUserInformationPage extends AppCompatActivity {
    private static final String TAG = "EditUserInformationPage";

    private EditText userNameText, addressText, phoneText, emailText, dobText;
    private Button saveBtn, cancelBtn;
    private ImageButton backBtn;
    private Spinner genderSpinner;
    Calendar myCalendar ;
    Bundle extras;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private RelativeLayout main_layout;

    String[] genderName = {"Chưa xác định", "Nam", "Nữ"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_information_page);
        main_layout = findViewById(R.id.main_layout);
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view,getBaseContext());
                return false;
            }
        });

        TweakUI.makeTransparent(this);
        mapping();
        getIncomingIntent();
        setLastSelector();

        Log.d(TAG, "onCreate: ");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genderName);
        genderSpinner.setAdapter(adapter);


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
                    setResult(201, toUserInformationPage);
                    finish();


                } else {
                    // code service here

                    saveProfile(v);
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

        addressText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(EditUserInformationPage.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
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

    public void saveProfile(View view) {
        Bundle extras = new Bundle();

        String gender = "";
        if (genderSpinner.getSelectedItemPosition() == 0) {
            gender = "";
        } else if (genderSpinner.getSelectedItemPosition() == 1) {
            gender = "Nam";
        } else if (genderSpinner.getSelectedItemPosition() == 2){
            gender = "Nữ";
        }

        extras.putString("name", userNameText.getText().toString());
        extras.putString("phone", phoneText.getText().toString());
        extras.putString("email", emailText.getText().toString());
        extras.putString("dob", dobText.getText().toString());
        extras.putString("address", addressText.getText().toString());
        extras.putString("gender", gender);



        Intent intent = new Intent(EditUserInformationPage.this, UserInformationPage.class);
        intent.putExtras(extras);
        setResult(200, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
//                cityID = data.getIntExtra("cityID", cityID);
//                townID = data.getIntExtra("townID", townID);
//                communeID = data.getIntExtra("communeID", communeID);

//                city.setText(String.valueOf(cityID));
//                town.setText(String.valueOf(townID));
//                commune.setText(String.valueOf(communeID));

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }

            if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    String placeAddress = place.getAddress().toString();
                    addressText.setText(placeAddress);
                    Toast.makeText(EditUserInformationPage.this, "Place : " +placeAddress, Toast.LENGTH_SHORT).show();

                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    // TODO: Handle the error.
                    Toast.makeText(EditUserInformationPage.this, "An error occurred: " + status, Toast.LENGTH_SHORT).show();

                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
            }
        }
    }



}
