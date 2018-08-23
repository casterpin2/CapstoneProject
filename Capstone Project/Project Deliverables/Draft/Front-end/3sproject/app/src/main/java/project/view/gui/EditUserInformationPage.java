package project.view.gui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

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

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

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
    private boolean checkUpdate = false;
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
    private boolean checkAva = false;
    private String namePath;
    private boolean isCheckSave = false;
    private StorageReference storageReference;
    private String currentImg;
    private ProgressBar loadingBarImage, loadingBarSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_information_page);
        storageReference = FirebaseStorage.getInstance().getReference();
        mApi = APIService.retrofit.create(APIService.class);
        mapping();
        customView();
        regex = new Regex();
        getIncomingIntent();
        setLastSelector();
        loadingBarImage.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);
        loadingBarSave.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);
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
                isUserName = regex.checkDisplayName(usernameError,userNameText);
                isPhone = regex.checkPhone(phoneError,phoneText);
                isEmail = regex.checkEmail(emailError,emailText);
                boolean checkAllValidate = isEmail && isPhone &&isUserName;
                boolean checkChange = extras.getString("name").equals(name) && extras.getString("phone").equals(phone)
                        && extras.getString("email").equals(email) && extras.getString("dob").equals(dob)
                        && extras.getString("gender").equals(gender);
                //thong tin ko thay doi anh khong thay doi
                if (checkChange && !checkAva ) {
                    Intent toUserInformationPage = new Intent(EditUserInformationPage.this, UserInformationPage.class);
                    setResult(201, toUserInformationPage);
                    finish();
                }

                if (!checkAva && !checkChange && checkAllValidate ) {
                    // code service here
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


                }
                if (checkAva && checkChange) {
                    pre = getSharedPreferences("authentication", Context.MODE_PRIVATE);
                    String existUserJson = pre.getString("user", null);
                    us = new Gson().fromJson(existUserJson, User.class);
                    //uploadImg(us.getId());
                    us.setImage_path(namePath);
                    final Call<Boolean> call = mApi.updateImgUser(us);
                    new UserUpdateImg().execute(call);
                    Intent toUserInformationPage = new Intent(EditUserInformationPage.this, UserInformationPage.class);
                    toUserInformationPage.putExtra("path", us.getImage_path());
                    setResult(202, toUserInformationPage);
                    finish();
                }
                if (checkAva && !checkChange && checkAllValidate) {
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
                   // uploadImg(us.getId());
                    us.setImage_path(namePath);
                    final Call<User> call = mApi.updateInfotmation(us);
                    new UserUpdate().execute(call);
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
                    Intent intent = new Intent(EditUserInformationPage.this, UserInformationPage.class);
                    intent.putExtra("userID", extras.getInt("idUser", 0));
                    setResult(201,intent);
                    finish();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(EditUserInformationPage.this);
                    builder.setTitle(R.string.back_alertdialog_title);
                    builder.setMessage(R.string.back_alertdialog_content);

                    builder.setPositiveButton(R.string.alertdialog_acceptButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(EditUserInformationPage.this, UserInformationPage.class);
                            intent.putExtra("userID", extras.getInt("idUser", 0));
                            setResult(201,intent);
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
                    Intent intent = new Intent(EditUserInformationPage.this, UserInformationPage.class);
                    intent.putExtra("userID", extras.getInt("idUser", 0));
                    setResult(201,intent);
                    finish();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(EditUserInformationPage.this);
                    builder.setTitle(R.string.back_alertdialog_title);
                    builder.setMessage(R.string.back_alertdialog_content);

                    builder.setPositiveButton(R.string.alertdialog_acceptButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(EditUserInformationPage.this, UserInformationPage.class);
                            intent.putExtra("userID", extras.getInt("idUser", 0));
                            setResult(201,intent);
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

            }
        });

        changeImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
                    if (checkPermission()) {
                        Intent toGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        startActivityForResult(toGallery, IMAGE_CODE);
                    } else {
                        requestPermission();
                    }
                } else {
                    Intent toGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(toGallery, IMAGE_CODE);
                }
            }
        });
    }
    private boolean checkPermission() {
        boolean checkPermissionRead = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return checkPermissionRead;
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, 1001);
    }
    @Override
    protected void onResume() {
        super.onResume();
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
            } else {
                requestPermission();
            }
        } else {
        }
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
        if (resultCode == RESULT_OK && requestCode == IMAGE_CODE) {
            imageURI = data.getData();
            checkAva = true;
            try {
                saveBtn.setEnabled(false);
                uploadImg();
            } catch (Exception e) {

            }

        }
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    private void uploadImg() {
        loadingBarImage.setVisibility(View.VISIBLE);
        changeImageLayout.setEnabled(false);
        saveBtn.setEnabled(false);
        final AlertDialog.Builder builder = new AlertDialog.Builder(EditUserInformationPage.this);
        builder.setTitle("Cảnh báo quá kích thước ảnh");
        builder.setMessage("Kích thích của ảnh đã vướt quá 2MB. Vui lòng thử lại ảnh khác!");
        final boolean[] isIntent = {false};
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isIntent[0] = true;
                namePath = extras.getString("path");
                return;
            }
        });
        if (imageURI != null) {
            final String nameImg = extras.getInt("idUser", 0) + "--" + UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("User/image/" + nameImg);

            ref.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get a URL to the uploaded content
                    if (taskSnapshot.getTask().isSuccessful()) {
                        InputStream imageStream = null;
                        try {
                            imageStream = getContentResolver().openInputStream(imageURI);
                            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            selectedImage = getResizedBitmap(selectedImage, 1024);
                            profile_image.setImageBitmap(selectedImage);
                            namePath = "User/image/" + nameImg;
                            saveBtn.setEnabled(true);
                            loadingBarImage.setVisibility(View.INVISIBLE);
                            changeImageLayout.setEnabled(true);
                            saveBtn.setEnabled(true);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                   // Toast.makeText(EditUserInformationPage.this, "Xin lỗi kích thước ảnh lớn hơn 2MB", Toast.LENGTH_SHORT).show();
                    if (!extras.getString("path").isEmpty() && extras.getString("path") != null) {
                        if (extras.getString("path").contains("graph") || extras.getString("path").contains("google")) {
                            Glide.with(EditUserInformationPage.this /* context */)
                                    .load(extras.getString("path"))
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(profile_image);
                        } else {
                            Glide.with(EditUserInformationPage.this /* context */)
                                    .using(new FirebaseImageLoader())
                                    .load(storageReference.child(extras.getString("path")))
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(profile_image);
                        }

                    }
                    builder.setCancelable(false);
                    loadingBarImage.setVisibility(View.INVISIBLE);
                    changeImageLayout.setEnabled(true);
                    saveBtn.setEnabled(true);
                    builder.show();


                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
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
        loadingBarImage = findViewById(R.id.loadingBarImage);
        loadingBarSave = findViewById(R.id.loadingBarSave);
    }

    private void getIncomingIntent() {
        Intent intent = getIntent();
        extras = intent.getExtras();

        if (extras != null) {
            userNameText.setText(extras.getString("name"));
            dobText.setText(extras.getString("dob"));
            emailText.setText(extras.getString("email"));
            phoneText.setText(extras.getString("phone"));
            if (!extras.getString("path").isEmpty() && extras.getString("path") != null) {
                if (extras.getString("path").contains("graph") || extras.getString("path").contains("google")) {
                    Glide.with(EditUserInformationPage.this /* context */)
                            .load(extras.getString("path"))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(profile_image);
                } else {
                    Glide.with(EditUserInformationPage.this /* context */)
                            .using(new FirebaseImageLoader())
                            .load(storageReference.child(extras.getString("path")))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(profile_image);

                }
            }
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


    private class UserUpdate extends AsyncTask<Call,User,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingBarSave.setVisibility(View.VISIBLE);
            saveBtn.setText("");
            saveBtn.setEnabled(false);
            changeImageLayout.setEnabled(false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (checkCall) {

                String userJson = new Gson().toJson(us);
                storeUser = pre.getString("store", null);
                SharedPreferences.Editor editor = pre.edit();
                //lưu vào editor
                editor.putString("user", userJson);
                editor.putString("store", storeUser);
                editor.commit();

                loadingBarSave.setVisibility(View.INVISIBLE);
                saveBtn.setText("Lưu thay đổi");
                saveBtn.setEnabled(true);
                changeImageLayout.setEnabled(true);

                Intent intent = new Intent(EditUserInformationPage.this, UserInformationPage.class);
                intent.putExtra("userID", us.getId());
                setResult(200, intent);
                finish();

            } else {
//                Toast.makeText(EditUserInformationPage.this, "Thông tin không thay đổi", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            Toast.makeText(EditUserInformationPage.this, "Có lỗi xảy ra. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                            loadingBarSave.setVisibility(View.INVISIBLE);
                            saveBtn.setText("Lưu thay đổi");
                            saveBtn.setEnabled(true);
                            changeImageLayout.setEnabled(true);
                    }
                },10000);
                return;
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
    private class UserUpdateImg extends AsyncTask<Call, User, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (checkCall) {

                String userJson = new Gson().toJson(us);
                storeUser = pre.getString("store", null);
                SharedPreferences.Editor editor = pre.edit();
                //lưu vào editor
                editor.putString("user", userJson);
                editor.putString("store", storeUser);
                editor.commit();

                Intent intent = new Intent(EditUserInformationPage.this, UserInformationPage.class);
                intent.putExtra("userID", us.getId());
                setResult(200, intent);
                finish();

            } else {
                Toast.makeText(EditUserInformationPage.this, "Thông tin không thay đổi", Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        protected void onProgressUpdate(User... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected Void doInBackground(Call... calls) {
            try {
                Call<Boolean> call = calls[0];
                Response<Boolean> reponse = call.execute();
                if (reponse.isSuccessful()) {
                    checkCall = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}
