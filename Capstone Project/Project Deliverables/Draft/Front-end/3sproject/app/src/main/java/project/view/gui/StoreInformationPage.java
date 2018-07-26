package project.view.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import project.view.R;
import project.view.model.StoreInformation;

public class StoreInformationPage extends AppCompatActivity {

    private TextView storeName, ownerName, address, registerDate, phoneText;
    private ImageView storeImg, btnEdit;
    private ImageButton backBtn;
    private int storeID;
    private Button btnManagerProduct;
    private StoreInformation storeInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_information_page);

        storeInformation = new StoreInformation();

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
        Toast.makeText(this, "storeID: "+storeID, Toast.LENGTH_SHORT).show();

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
                startActivity(toProductManagement);
            }
        });


    }


}
