package project.view.fragment.home;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import android.support.v4.content.ContextCompat;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.gui.EditStoreInformationPage;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import project.firebase.Firebase;
import project.objects.User;
import project.view.gui.FeedbackManagement;
import project.view.gui.LoginPage;
import project.view.gui.ProductInStoreDisplayPage;
import project.view.R;
import project.view.gui.RegisterStorePage;
import project.view.gui.StoreManagementOrderPage;
import project.view.model.Store;
import project.view.util.NetworkStateReceiver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends Fragment implements NetworkStateReceiver.NetworkStateReceiverListener {
    private TextView storeName, ownerName, address, registerDate, phoneText, tv_count_smile, tv_count_sad,tv_feedback_status;
    private ImageView storeImg, btnEdit;
    private int storeID = 1;
    private int hasStore = 0;
    private int userID = 1;
    private double latitude = 0.0;
    private double longtitude = 0.0;
    private View view;
    private Store store;
    private User user;
    private Button btnManagermentProduct, btnManagermentOrder, loginBtn, registerStoreBtn;
    private StorageReference storageReference = Firebase.getFirebase();
    private final int RESULT_CODE = 2222;
    private final int REQUEST_CODE = 1111;
    private NetworkStateReceiver networkStateReceiver;
    private ImageView imgCall;
    private static final int REQUEST_CALL = 1;
    private String phoneNumber;
    private static final int IMAGE_CODE = 100;
    private Uri imageURI;
    private LinearLayout changeStoreImageLayout;
    private String namePath;
    private Button btnSave,btnCacel;
    private APIService mApi;
    private  boolean checkNetWork = false;
    private ProgressBar loadingBarImage;
    private LinearLayout changeLayout;
    private String oldPathImg;
    private LinearLayout saveImageStoreLayout;
    private String testCommit;
    public StoreFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getPermission();
        String userJSON = getArguments().getString("userJSON");
        String storeJSON = getArguments().getString("storeJSON");
        if (userJSON.isEmpty()) {
            user = new User();
            store = new Store();
        } else {
            user = new Gson().fromJson(userJSON, User.class);
            store = new Gson().fromJson(storeJSON, Store.class);
        }
        storeID = store.getId();
        hasStore = user.getHasStore();
        userID = user.getId();
        oldPathImg=store.getImage_path();
        //check network available
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        getContext().registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        if (hasStore == 1 && userID != 0 && storeID != 0) {
            view = inflater.inflate(R.layout.fragment_store, container, false);
            findViewInStoreFragment();
            registerDate.setText(store.getRegisterLog());
            storeName.setText(store.getName());
            phoneText.setText(store.getPhone());
            address.setText(store.getAddress().replaceAll("null", "").replaceAll("\\s+", " "));
            ownerName.setText(user.getFirst_name() + " " + user.getLast_name());
            latitude = Double.parseDouble(store.getLatitude());
            longtitude = Double.parseDouble(store.getLongtitude());

            btnManagermentProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toStoreProduct = new Intent(getContext(), ProductInStoreDisplayPage.class);
                    toStoreProduct.putExtra("storeID", storeID);
                    startActivity(toStoreProduct);
                }
            });

            btnManagermentOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toStoreManagementOrder = new Intent(getContext(), StoreManagementOrderPage.class);
                    toStoreManagementOrder.putExtra("storeId", storeID);
                    getActivity().startActivity(toStoreManagementOrder);
                }
            });
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toEditStoreInformation = new Intent(getContext(), EditStoreInformationPage.class);
                    toEditStoreInformation.putExtra("storeID", storeID);
                    toEditStoreInformation.putExtra("storeName", storeName.getText().toString());
                    toEditStoreInformation.putExtra("ownerName", ownerName.getText().toString());
                    toEditStoreInformation.putExtra("address", address.getText().toString());
                    toEditStoreInformation.putExtra("phone", phoneText.getText().toString());
                    toEditStoreInformation.putExtra("registerDate", registerDate.getText().toString());
                    toEditStoreInformation.putExtra("longtitude", longtitude);
                    toEditStoreInformation.putExtra("latitude", latitude);
                    startActivityForResult(toEditStoreInformation, REQUEST_CODE);
                }
            });
            tv_feedback_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent toFeedbackManagement = new Intent(getContext(), FeedbackManagement.class);
                    toFeedbackManagement.putExtra("storeId",storeID);
                    getContext().startActivity(toFeedbackManagement);
                }
            });
            imgCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(checkNetWork){
                        loadingBarImage.setVisibility(View.VISIBLE);
                        mApi = ApiUtils.getAPIService();
                        if(!namePath.isEmpty() && namePath!=null){
                            mApi.updateImgStore(store,namePath).enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    if(response.isSuccessful()){
                                        Toast.makeText((Activity) getContext(), "Thay đổi ảnh thành công", Toast.LENGTH_SHORT).show();
                                        SharedPreferences pre = StoreFragment.this.getActivity().getSharedPreferences("authentication", Context.MODE_PRIVATE);
                                        if(store!=null){
                                            store.setImage_path(namePath);
                                            String storeJson = new Gson().toJson(store);
                                            String userJson = pre.getString("user", null);
                                            SharedPreferences.Editor editor = pre.edit();
                                            //lưu vào editor
                                            editor.putString("user", userJson);
                                            editor.putString("store", storeJson);
                                            editor.commit();
                                        }

                                        loadingBarImage.setVisibility(View.INVISIBLE);
                                        saveImageStoreLayout.setVisibility(View.INVISIBLE);
                                        storeName.setVisibility(View.VISIBLE);
                                    }
                                }
                                @Override
                                public void onFailure(Call<Boolean> call, Throwable t) {
                                    Toast.makeText((Activity) getContext(), "Đã có lỗi xảy ra, xin vui lòng xử lí lại", Toast.LENGTH_SHORT).show();
                                    loadingBarImage.setVisibility(View.INVISIBLE);
                                }
                            });
                        }

                    }
                }
            });

            btnCacel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    loadingBarImage.setVisibility(View.VISIBLE);


                    if (!oldPathImg.isEmpty() && oldPathImg != null) {
                        if (oldPathImg.contains("graph") || oldPathImg.contains("google")) {
                            Glide.with((Activity)getContext() /* context */)
                                    .load(oldPathImg)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(storeImg);
                            loadingBarImage.setVisibility(View.INVISIBLE);

                        } else {
                            Glide.with((Activity)getContext() /* context */)
                                    .using(new FirebaseImageLoader())
                                    .load(storageReference.child(oldPathImg))
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(storeImg);
                            loadingBarImage.setVisibility(View.INVISIBLE);
                            saveImageStoreLayout.setVisibility(View.INVISIBLE);

                            storeName.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
            changeStoreImageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(),"sadasd",Toast.LENGTH_LONG);
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

        } else if (hasStore == 0 && userID != 0) {
            view = inflater.inflate(R.layout.no_has_store_store_fragment_home_page_layout, container, false);
            findViewInNoHaveStoreLayout();
            registerStoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent toRegisterStorePage = new Intent(getContext(), RegisterStorePage.class);
                        toRegisterStorePage.putExtra("user_id", user.getId());
                        startActivity(toRegisterStorePage);

                }
            });
        } else if (userID == 0) {
            view = inflater.inflate(R.layout.no_loginned_store_fragment_home_page_layout, container, false);
            findViewInNoLoginnedLayout();
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Intent toLoginPage = new Intent(getContext(), LoginPage.class);
                        startActivity(toLoginPage);

                }
            });
        }
        return view;
    }
    public void getPermission(){
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
            } else {
                requestPermission();
            }
        } else {
        }
    }
    private boolean checkPermission() {
        boolean checkPermissionRead = ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return checkPermissionRead;
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{READ_EXTERNAL_STORAGE}, 1001);
    }


    @Override
    public void networkAvailable() {
        checkNetWork = true;
    }

    @Override
    public void networkUnavailable() {
        Toast.makeText(getContext(), "Vui lòng kiểm tra kết nối mạng", Toast.LENGTH_LONG).show();

    }


    private void findViewInStoreFragment() {
        changeStoreImageLayout = view.findViewById(R.id.changeStoreImageLayout);
        storeName = view.findViewById(R.id.storeName);
        ownerName = view.findViewById(R.id.ownerName);
        address = view.findViewById(R.id.address);
        registerDate = view.findViewById(R.id.registerDate);
        phoneText = view.findViewById(R.id.phoneText);
        storeImg = view.findViewById(R.id.storeImg);
        btnEdit = view.findViewById(R.id.imgEdit);
        imgCall = view.findViewById(R.id.image_call);
        btnManagermentOrder = view.findViewById(R.id.btnManagerOrder);
        btnManagermentProduct = view.findViewById(R.id.btnManagerProduct);
        tv_count_smile = (TextView) view.findViewById(R.id.tv_count_smile);
        tv_count_sad = (TextView) view.findViewById(R.id.tv_count_sad);
        Glide.with(getContext() /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(store.getImage_path()))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(storeImg);

        tv_feedback_status = view.findViewById(R.id.tv_feedback_status);
        btnSave = view.findViewById(R.id.btnSave);
        btnCacel = view.findViewById(R.id.btnCancel);
        loadingBarImage = view.findViewById(R.id.loadingBarImage);
        changeLayout = view.findViewById(R.id.changeStoreImageLayout);
        saveImageStoreLayout = view.findViewById(R.id.saveImageStoreLayout);

    }

    private void findViewInNoLoginnedLayout() {
        loginBtn = view.findViewById(R.id.loginBtn);
    }

    private void findViewInNoHaveStoreLayout() {
        registerStoreBtn = view.findViewById(R.id.registerStoreBtn);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasStore == 1 && userID != 0 && storeID != 0) {
            Call<List<Integer>> call = ApiUtils.getAPIService().countFeedback(store.getId());
            new CountFeedback().execute(call);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        getContext().unregisterReceiver(networkStateReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            String checkData = data.getStringExtra("data");
            if (!checkData.equals("NO")) {
                String userJSON = data.getStringExtra("userData");
                String storeJSON = data.getStringExtra("storeData");
                if (userJSON.isEmpty()) {
                    user = new User();
                    store = new Store();
                } else {
                    user = new Gson().fromJson(userJSON, User.class);
                    store = new Gson().fromJson(storeJSON, Store.class);
                }
                registerDate.setText(store.getRegisterLog());
                storeName.setText(store.getName());
                phoneText.setText(store.getPhone());
                address.setText(store.getAddress().replaceAll("null", "").replaceAll("\\s+", " "));
                ownerName.setText(user.getFirst_name() + " " + user.getLast_name());
                latitude = getActivity().getIntent().getDoubleExtra("latitude", 0.0);
                longtitude = getActivity().getIntent().getDoubleExtra("longtitude", 0.0);
            }
        }
        if (resultCode == RESULT_OK && requestCode == IMAGE_CODE) {
            storeName.setVisibility(View.INVISIBLE);
            imageURI = data.getData();

            try {
                uploadImg();
            } catch (Exception e) {

            }

        }
    }

    private void uploadImg() {
        loadingBarImage.setVisibility(View.VISIBLE);
        changeLayout.setEnabled(false);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Cảnh báo quá kích thước ảnh");
        builder.setMessage("Kích thích của ảnh đã vướt quá 2MB. Vui lòng thử lại ảnh khác!");
        final boolean[] isIntent = {false};
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isIntent[0] = true;
                namePath = store.getImage_path();
                return;
            }
        });
        if (imageURI != null) {
            final String nameImg = store.getId() + "--"+store.getName()+"--" + UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("Store/" + nameImg);

            ref.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get a URL to the uploaded content
                    if (taskSnapshot.getTask().isSuccessful()) {
                        InputStream imageStream = null;
                        try {
                            imageStream = getActivity().getApplicationContext().getContentResolver().openInputStream(imageURI);
                            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            selectedImage = getResizedBitmap(selectedImage, 1024);
                            storeImg.setImageBitmap(selectedImage);
                            namePath = "Store/" + nameImg;
                            btnSave.setEnabled(true);
                            loadingBarImage.setVisibility(View.INVISIBLE);
                            changeLayout.setEnabled(true);
                            saveImageStoreLayout.setVisibility(View.VISIBLE);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Toast.makeText(EditUserInformationPage.this, "Xin lỗi kích thước ảnh lớn hơn 2MB", Toast.LENGTH_SHORT).show();
                    if (!oldPathImg.isEmpty() && oldPathImg != null) {
                        if (oldPathImg.contains("graph") || oldPathImg.contains("google")) {
                            Glide.with((Activity)getContext() /* context */)
                                    .load(oldPathImg)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(storeImg);
                        } else {
                            Glide.with((Activity)getContext() /* context */)
                                    .using(new FirebaseImageLoader())
                                    .load(storageReference.child(oldPathImg))
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(storeImg);
                        }

                    }
                    builder.setCancelable(false);
                    loadingBarImage.setVisibility(View.INVISIBLE);
                    changeLayout.setEnabled(true);
                    builder.show();
                    storeName.setVisibility(View.VISIBLE);

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
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
    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }

    public class CountFeedback extends AsyncTask<Call,Void,List<Integer>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Integer> list) {
            super.onPostExecute(list);
            if (list != null) {
                tv_count_smile.setText(String.valueOf(list.get(0)));
                tv_count_sad.setText(String.valueOf(list.get(1)));
            }
            else {
                tv_count_smile.setText(String.valueOf(0));
                tv_count_sad.setText(String.valueOf(0));
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected List<Integer> doInBackground(Call... calls) {
            try {
                Call<List<Integer>> call = calls[0];
                Response<List<Integer>> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
