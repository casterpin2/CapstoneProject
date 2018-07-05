package project.view.StoreInformation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import project.view.ProductInStore.ProductInStore;
import project.view.ProductInStore.ProductInStoreDisplayPage;
import project.view.R;

public class StoreInformationPage extends AppCompatActivity {

    private RelativeLayout notHaveStoreLayout, haveStoreLayout, productLayout, orderLayout, storeImgLayout;
    private TextView storeName, numberOfOrder, numberOfProduct, ownerName, address, registerDate, phoneText;
    private ImageView storeImg, btnEdit;
    private int storeID;
    private StoreInformation storeInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_information_page);

        storeInformation = new StoreInformation();

        notHaveStoreLayout = (RelativeLayout) findViewById(R.id.notHaveStoreLayout);
        haveStoreLayout = (RelativeLayout) findViewById(R.id.haveStoreLayout);
        productLayout = (RelativeLayout) findViewById(R.id.productLayout);
        orderLayout = (RelativeLayout) findViewById(R.id.orderLayout);
        storeImgLayout = (RelativeLayout) findViewById(R.id.storeImgLayout);

        storeName = (TextView) findViewById(R.id.storeName);
        numberOfOrder = (TextView) findViewById(R.id.numberOfOrder);
        numberOfProduct = (TextView) findViewById(R.id.numberOfProduct);
        ownerName = (TextView) findViewById(R.id.ownerName);
        address = (TextView) findViewById(R.id.address);
        registerDate = (TextView) findViewById(R.id.registerDate);
        phoneText = (TextView) findViewById(R.id.phoneText);
        storeImg = (ImageView) findViewById(R.id.storeImg);
        btnEdit = (ImageView) findViewById(R.id.btnEdit);


        // set layout in case of store null or not
        // storeId is transported from login action
        storeID = getIntent().getIntExtra("storeID", -1);
        setLayout(storeID,haveStoreLayout,notHaveStoreLayout);

        productLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toStoreProduct = new Intent(StoreInformationPage.this, ProductInStoreDisplayPage.class);
                toStoreProduct.putExtra("storeID", storeID);
                startActivity(toStoreProduct);
            }
        });

        orderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // click on to choose image from gallery
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void setLayout(int storeID, RelativeLayout haveStore, RelativeLayout notHaveStore){
        if (storeID == 0) {
            haveStore.setVisibility(View.INVISIBLE);
            notHaveStore.setVisibility(View.VISIBLE);
        } else if(storeID > 0) {
            haveStore.setVisibility(View.VISIBLE);
            notHaveStore.setVisibility(View.INVISIBLE);
        } else {

        }
    }

}
