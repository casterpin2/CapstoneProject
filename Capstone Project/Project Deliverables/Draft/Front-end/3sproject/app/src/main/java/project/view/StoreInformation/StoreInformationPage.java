package project.view.StoreInformation;

import android.content.Intent;
import android.content.SharedPreferences;
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

        int storeID = getIntent().getIntExtra("storeID", -1);
        String nameStoree = storeName.getText().toString();
        int numberOfOrderStoree = Integer.parseInt(numberOfOrder.getText().toString());
        int numberOfProductStoree = Integer.parseInt(numberOfProduct.getText().toString());
        String ownerNameStoree = ownerName.getText().toString();
        String addressStoree = address.getText().toString();
        String registerDateStoree = registerDate.getText().toString();
        String phoneTextStoree = phoneText.getText().toString();

        savingPreferences(storeID, nameStoree, numberOfOrderStoree, numberOfProductStoree, ownerNameStoree, addressStoree, registerDateStoree, phoneTextStoree);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //gọi hàm đọc trạng thái ở đây
        restoringPreferences();
    }

    @Override
    protected void onPause() {
        super.onPause();

        int storeID = getIntent().getIntExtra("storeID", -1);
        String nameStoree = storeName.getText().toString();
        int numberOfOrderStoree = Integer.parseInt(numberOfOrder.getText().toString());
        int numberOfProductStoree = Integer.parseInt(numberOfProduct.getText().toString());
        String ownerNameStoree = ownerName.getText().toString();
        String addressStoree = address.getText().toString();
        String registerDateStoree = registerDate.getText().toString();
        String phoneTextStoree = phoneText.getText().toString();

        savingPreferences(storeID, nameStoree, numberOfOrderStoree, numberOfProductStoree, ownerNameStoree, addressStoree, registerDateStoree, phoneTextStoree);
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

    public void savingPreferences(int storeID, String name, int numberOfOrder, int numberOfProduct, String ownerName, String address, String registerDate, String phoneText){
        //tạo đối tượng getSharedPreferences
        SharedPreferences pre=getSharedPreferences("storeInformation", MODE_PRIVATE);
        //tạo đối tượng Editor để lưu thay đổi
        SharedPreferences.Editor editor=pre.edit();
        //lưu vào editor
        editor.putString("name", name);
        editor.putInt("numberOfOrder", numberOfOrder);
        editor.putInt("numberOfProduct", numberOfProduct);
        editor.putString("ownerName", ownerName);
        editor.putString("address", address);
        editor.putString("registerDate", registerDate);
        editor.putString("phoneText", phoneText);
        editor.putInt("storeID", storeID);

        //chấp nhận lưu xuống file
        editor.commit();
    }

    public void restoringPreferences(){
        SharedPreferences pre=getSharedPreferences
                ("storeInformation",MODE_PRIVATE);
        //lấy giá trị checked ra, nếu không thấy thì giá trị mặc định là false

        //lấy user, pwd, nếu không thấy giá trị mặc định là rỗng
        int storeID = pre.getInt("storeID", -1);
        String nameStoree = pre.getString("name", "");
        int numberOfOrderStoree = pre.getInt("numberOfOrder", 0);
        int numberOfProductStoree = pre.getInt("numberOfProduct", 0);
        String ownerNameStoree = pre.getString("ownerName", "");
        String addressStoree = pre.getString("address", "");
        String registerDateStoree = pre.getString("registerDate", "");
        String phoneTextStoree = pre.getString("phoneText", "");


        storeName.setText(nameStoree);
        numberOfOrder.setText(String.valueOf(numberOfOrderStoree));
        numberOfProduct.setText(String.valueOf(numberOfProductStoree));
        ownerName.setText(ownerNameStoree);
        address.setText(addressStoree);
        registerDate.setText(registerDateStoree);
        phoneText.setText(phoneTextStoree);

    }

}
