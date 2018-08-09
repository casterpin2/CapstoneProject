package project.view.gui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import project.firebase.Firebase;
import project.view.R;
import project.view.model.NearByStore;
import project.view.model.Store;
import project.view.model.StoreInformation;
import project.view.util.TweakUI;

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
        String storeJson = getIntent().getStringExtra("nearByStore");
        final NearByStore store = new Gson().fromJson(storeJson, NearByStore.class);
        storeName.setText(store.getName());
        ownerName.setText(store.getUser_name());
        address.setText(store.getAddress());
        registerDate.setText(store.getRegisterLog());
        phoneText.setText(store.getPhone());
        Glide.with(this /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(store.getImage_path()))
                .skipMemoryCache(true)
                .into(storeImg);

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


}
