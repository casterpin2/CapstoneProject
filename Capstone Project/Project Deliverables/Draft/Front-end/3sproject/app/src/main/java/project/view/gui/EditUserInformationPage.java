package project.view.gui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import project.objects.User;
import project.retrofit.APIService;
import project.view.R;
import project.view.util.CustomInterface;
import project.view.util.Regex;
import project.view.util.TweakUI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserInformationPage extends BasePage {
    private static final String TAG = "EditUserInformationPage";

    private EditText userNameText, phoneText, emailText, dobText;
    private TextView usernameError, phoneError, emailError, dobError;
    private Button saveBtn, cancelBtn;
    private ImageButton backBtn;
    private Spinner genderSpinner;
    private CircleImageView profile_image;
    Calendar myCalendar ;
    private Bundle extras;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private RelativeLayout main_layout;
    private  boolean checkUpdate;
    String[] genderName = {"Chưa xác định", "Nam", "Nữ"};
    private String storeUser;
    private APIService mApi;
    private SharedPreferences pre;
    private LinearLayout changeImageLayout;
    private static final int IMAGE_CODE = 100;
    private Uri imageURI;
    User us;
    boolean checkCall = false;
    private Regex regex;
    private boolean  isUserName= false, isPhone = false ,isEmail = false;
    private  DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_information_page);

        mApi = APIService.retrofit.create(APIService.class);
        mapping();
        customView();
        regex = new Regex();
        getIncomingIntent();
        setLastSelector();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genderName);
        genderSpinner.setAdapter(adapter);

        Intent intent = getIntent();
        extras = intent.getExtras();

        String gender = extras.getString("gender");
        if(gender.equals("")){
            genderSpinner.setSelection(0);
        } else if (gender.equals("Nam")){
            genderSpinner.setSelection(1);
        } else if(gender.equals("Nữ")) {
            genderSpinner.setSelection(2);
        }

        userNameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    isUserName = regex.checkDisplayName(usernameError,userNameText);
                }
            }
        });

        phoneText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    isPhone = regex.checkPhone(phoneError,phoneText);
                }
            }
        });

        emailText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    isEmail = regex.checkEmail(emailError,emailText);
                }
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditUserInformationPage.this, "Hello?", Toast.LENGTH_SHORT).show();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                String name = userNameText.getText().toString();
                String phone = phoneText.getText().toString();
                String email = emailText.getText().toString();
                String dob = dobText.getText().toString();


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
                        && extras.getString("gender").equals(gender)) {

                    Intent toUserInformationPage = new Intent(EditUserInformationPage.this, UserInformationPage.class);
                    setResult(201, toUserInformationPage);
                    finish();


                } else {

                    isUserName = regex.checkDisplayName(usernameError,userNameText);
                    isPhone = regex.checkPhone(phoneError,phoneText);
                    isEmail = regex.checkEmail(emailError,emailText);

                    // code service here
                    if(isEmail && isPhone &&isUserName) {
                        us = new User();
                        pre = getSharedPreferences("authentication", Context.MODE_PRIVATE);
                        String existUserJson = pre.getString("user", null);
                        User userTemp = new Gson().fromJson(existUserJson, User.class);

                        us.setDateOfBirth(dob);
                        us.setDisplayName(name);
                        us.setFirst_name("");
                        us.setLast_name(name);
                        us.setImage_path(userTemp.getImage_path());
                        us.setUsername(userTemp.getUsername());
                        us.setHasStore(userTemp.getHasStore());
                        us.setEmail(email);
                        us.setPhone(phone);
                        us.setGender(gender);
                        us.setId(extras.getInt("idUser", 0));
                        final Call<User> call = mApi.updateInfotmation(us);
                        new UserUpdate().execute(call);
                        saveProfile(v);
                    }
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(extras.getString("name").equals(userNameText.getText().toString())
                        && extras.getString("phone").equals(phoneText.getText().toString())
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

        date = new DatePickerDialog.OnDateSetListener() {

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
                DatePickerDialog dpDialog  = new DatePickerDialog(EditUserInformationPage.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                dpDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
                dpDialog.show();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(extras.getString("name").equals(userNameText.getText().toString())
                        && extras.getString("phone").equals(phoneText.getText().toString())
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

        changeImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalery();
            }
        });
    }

    private void customView(){
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view,getBaseContext());
                return false;
            }
        });
        CustomInterface.setStatusBarColor(this);
        CustomInterface.setSoftInputMode(this);
    }

    private void openGalery(){
        Intent toGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(toGallery,IMAGE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_CODE){
            imageURI = data.getData();
            profile_image.setImageURI(imageURI);
        }
    }

    private void mapping() {
        main_layout = findViewById(R.id.main_layout);
        userNameText = findViewById(R.id.userNameText);
        phoneText = findViewById(R.id.phoneText);
        emailText = findViewById(R.id.emailText);
        dobText =  findViewById(R.id.dobText);
        profile_image= findViewById(R.id.profile_image);

        usernameError = findViewById(R.id.usernameError);
        phoneError = findViewById(R.id.phoneError);
        emailError = findViewById(R.id.emailError);
        dobError = findViewById(R.id.dobError);

        saveBtn = findViewById(R.id.saveBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        backBtn = findViewById(R.id.backBtn);
        genderSpinner = findViewById(R.id.genderSpinner);
        changeImageLayout = findViewById(R.id.changeImage);

    }

    private void getIncomingIntent() {
        Intent intent = getIntent();
        extras = intent.getExtras();

        if (extras != null) {
            userNameText.setText(extras.getString("name"));
            dobText.setText(extras.getString("dob"));
            emailText.setText(extras.getString("email"));
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
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dobText.setText(sdf.format(myCalendar.getTime()));
    }

    private void setLastSelector(){
        userNameText.setSelection(userNameText.getText().length());
        dobText.setSelection(dobText.getText().length());
        emailText.setSelection(emailText.getText().length());
        phoneText.setSelection(phoneText.getText().length());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(userNameText.getText().toString().equals(extras.getString("name"))
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
        extras.putString("gender", gender);
        Intent intent = new Intent(EditUserInformationPage.this, UserInformationPage.class);
        intent.putExtras(extras);
        setResult(200,intent);
        finish();

    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == 1) {
//            if (resultCode == Activity.RESULT_OK) {
////                cityID = data.getIntExtra("cityID", cityID);
////                townID = data.getIntExtra("townID", townID);
////                communeID = data.getIntExtra("communeID", communeID);
//
////                city.setText(String.valueOf(cityID));
////                town.setText(String.valueOf(townID));
////                commune.setText(String.valueOf(communeID));
//
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                //Write your code if there's no result
//            }
//
//            if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
//                if (resultCode == RESULT_OK) {
//                    Place place = PlaceAutocomplete.getPlace(this, data);
//
//
//                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
//                    Status status = PlaceAutocomplete.getStatus(this, data);
//                    // TODO: Handle the error.
//                    Toast.makeText(EditUserInformationPage.this, "An error occurred: " + status, Toast.LENGTH_SHORT).show();
//
//                } else if (resultCode == RESULT_CANCELED) {
//                    // The user canceled the operation.
//                }
//            }
//        }
//    }


    private class UserUpdate extends AsyncTask<Call,User,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(checkCall){

                String userJson = new Gson().toJson(us);
                storeUser = pre.getString("store",null);
                SharedPreferences.Editor editor = pre.edit();
                //lưu vào editor
                editor.putString("user", userJson);
                editor.putString("store", storeUser);
                editor.commit();
                Toast.makeText(EditUserInformationPage.this, R.string.succesfully, Toast.LENGTH_LONG).show();
                checkUpdate = true;
            }

        }

        @Override
        protected void onProgressUpdate(User... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected Void doInBackground(Call... calls) {
            try{
                Call<User> call = calls[0];
                Response<User> reponse = call.execute();
                if(reponse.body()!=null){
                    checkCall = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}
