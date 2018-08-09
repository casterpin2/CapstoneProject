package project.view.gui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.IOException;

import project.firebase.Firebase;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.model.CartDetail;
import project.view.model.NearByStore;
import project.view.model.Store;
import project.view.model.StoreInformation;
import project.view.util.TweakUI;
import retrofit2.Call;
import retrofit2.Response;

public class StoreInformationPage extends AppCompatActivity {

    private TextView storeName, ownerName, address, registerDate, phoneText;
    private ImageView storeImg, btnEdit;
    private ImageButton backBtn;
    private int storeID;
    private Button btnManagerProduct;
    private StorageReference storageReference = Firebase.getFirebase();
    private LinearLayout callLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_information_page);
        TweakUI.makeTransparent(this);


        callLayout = findViewById(R.id.callLayout);
        storeName = (TextView) findViewById(R.id.storeName);
        ownerName = (TextView) findViewById(R.id.ownerName);
        address = (TextView) findViewById(R.id.address);
        registerDate = (TextView) findViewById(R.id.registerDate);
        phoneText = (TextView) findViewById(R.id.phoneText);
        storeImg = (ImageView) findViewById(R.id.storeImg);
        backBtn = findViewById(R.id.backBtn);
        btnManagerProduct = findViewById(R.id.btnManagerProduct);


        // set layout in case of store null or not
        // storeId is transported from login action
        storeID = getIntent().getIntExtra("storeID", -1);
        Call<Store> call = ApiUtils.getAPIService().getStoreById(storeID);
        new GetStoreById().execute(call);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        callLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:123456789"));
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
            }
        });

        btnManagerProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toProductManagement = new Intent(StoreInformationPage.this, ProductInStoreByUserDisplayPage.class);
                toProductManagement.putExtra("storeID", storeID);
                startActivity(toProductManagement);
            }
        });


    }
    private class GetStoreById extends AsyncTask<Call, Void, Store> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final Store store) {
            super.onPostExecute(store);
            if (store == null){
                Toast.makeText(StoreInformationPage.this, "Có lỗi xảy ra!!!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                storeName.setText(store.getName());
                ownerName.setText(store.getUser_name());
                address.setText(store.getAddress());
                registerDate.setText(store.getRegisterLog());
                phoneText.setText(store.getPhone());
                Glide.with(StoreInformationPage.this /* context */)
                        .using(new FirebaseImageLoader())
                        .load(storageReference.child(store.getImage_path()))
                        .skipMemoryCache(true)
                        .into(storeImg);
            }
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
            }
            return null;
        }
    }
}
