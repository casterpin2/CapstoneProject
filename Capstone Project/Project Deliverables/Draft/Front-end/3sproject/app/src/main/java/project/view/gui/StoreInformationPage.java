package project.view.gui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import project.firebase.Firebase;
import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.adapter.ProductInStoreByUserCustomCardViewAdapter;
import project.view.model.Product;
import project.view.model.Store;
import project.view.util.CustomInterface;
import project.view.util.Formater;
import project.view.util.GridSpacingItemDecoration;
import project.view.util.ProductFilter;
import project.view.util.ProductInStoreCompareableDecrease;
import project.view.util.ProductInStoreCompareableIncrease;
import project.view.util.TweakUI;
import retrofit2.Call;
import retrofit2.Response;

public class StoreInformationPage extends AppCompatActivity {
    private Store store;
    private APIService mAPI;
    private int storeID;
    ImageView backBtn;
    TextView ownerName, address, registerDate, phoneText,tvSmile,tvSad , tvStoreName , viewFeedback;
    private ProgressBar loadingBar;
    private LinearLayout storeInforForm;
    private TextView nullMessage,tvCall;
    private static final int REQUEST_CALL = 1;
    private String phoneNumber;
    private ImageView storeImg;
    private StorageReference storageReference = Firebase.getFirebase();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_information_page);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        findView();
        loadingBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);
        mAPI = ApiUtils.getAPIService();
        CustomInterface.setStatusBarColor(this);
        storeID = getIntent().getIntExtra("storeID", -1);
        Call<Store> call = mAPI.getStoreById(storeID);
        new StoreInformation().execute(call);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        viewFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toFeedbackManagement = new Intent(StoreInformationPage.this, FeedbackManagement.class);
                toFeedbackManagement.putExtra("storeId",storeID);
                startActivity(toFeedbackManagement);

            }
        });
        tvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = phoneText.getText().toString();
                makePhoneCall(phoneNumber);
            }
        });
    }

    private void findView(){
        tvCall = findViewById(R.id.tvCall);
        backBtn = findViewById(R.id.backBtn);
        ownerName = findViewById(R.id.ownerName);
        address = findViewById(R.id.address);
        registerDate = findViewById(R.id.registerDate);
        phoneText = findViewById(R.id.phoneText);
        loadingBar = findViewById(R.id.loadingBar);
        storeInforForm = findViewById(R.id.storeInforForm);
        nullMessage = findViewById(R.id.nullMessage);
        tvSmile = findViewById(R.id.tv_count_smile);
        tvSad = findViewById(R.id.tv_count_sad);
        tvStoreName = findViewById(R.id.storeName);
        viewFeedback = findViewById(R.id.tv_feedback_status);
        storeImg = findViewById(R.id.storeImg);
;    }

    public class StoreInformation extends AsyncTask<Call,Void,Store> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingBar.setVisibility(View.VISIBLE);
            storeInforForm.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(Store store1) {
            super.onPostExecute(store1);
            boolean haveData = false;
            if (store1 != null){
                store = store1;
                tvStoreName.setText(store.getName());
                ownerName.setText(store.getUser_name());
                address.setText(store.getAddress());
                registerDate.setText(store.getRegisterLog());
                phoneText.setText(store.getPhone());
                tvSmile.setText(String.valueOf(store.getSmile()));
                tvSad.setText(String.valueOf(store.getSad()));
                haveData = true;
                if (!store.getImage_path().isEmpty()) {
                    Glide.with(getBaseContext())
                            .using(new FirebaseImageLoader())
                            .load(storageReference.child(store.getImage_path()))
                            .into(storeImg);
                }
            } else {
//                Toast.makeText(getBaseContext(),"Có lỗi xảy ra",Toast.LENGTH_LONG).show();
                haveData = false;
            }
            if(haveData){
                loadingBar.setVisibility(View.INVISIBLE);
                storeInforForm.setVisibility(View.VISIBLE);
            }
            final boolean finalHaveData = haveData;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (!finalHaveData) {
                        Toast.makeText(getBaseContext(), "Có lỗi xảy ra. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        nullMessage.setText("Có lỗi xảy ra, vui lòng tải lại trang!");
                        loadingBar.setVisibility(View.INVISIBLE);
                    }
                }
            },10000);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Store doInBackground(Call... calls) {
            try {
                Call<Store> call = calls[0];
                Response<Store> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private void makePhoneCall(String number) {

        if (number.trim().length() > 0) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(StoreInformationPage.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(phoneNumber);
            } else {
                Toast.makeText(StoreInformationPage.this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
