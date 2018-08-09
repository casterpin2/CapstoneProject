package project.view.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class StoreInformationPage extends AppCompatActivity {

    private TextView storeName, ownerName, address, registerDate, phoneText;
    private ImageView storeImg, btnEdit;
    private ImageButton backBtn;
    private int storeID;
    private Button btnManagerProduct;
    private StorageReference storageReference = Firebase.getFirebase();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_information_page);
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

        btnManagerProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toProductManagement = new Intent(StoreInformationPage.this, ProductInStoreByUserDisplayPage.class);
                toProductManagement.putExtra("storeID", storeID);
                Store s = new Store(store.getId(),store.getName(),store.getPhone(),store.getImage_path());
                toProductManagement.putExtra("nearByStore", new Gson().toJson(s));
                startActivity(toProductManagement);
            }
        });


    }


}
