package project.view.RegisterStore;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import project.view.EditProductInStore.EditProductInStorePage;
import project.view.MainActivity;
import project.view.R;

public class RegisterStorePage extends AppCompatActivity {

    final static int REQUEST_LOCATION = 1;
    private LocationManager locationManager;
    private RelativeLayout cityLayout, townLayout, communeLayout;
    private CheckBox checkbox;
    private Button registerBtn;
    TextView city, town, commune, storeName, phone, tvStoreNameError, etDetailAddress;
    ImageView rightArrowCity, rightArrowTown, rightArrowCommune;

    private int userID;
    private int cityID = 0;
    private int townID = 0;
    private int communeID = 0;

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public void setTownID(int townID) {
        this.townID = townID;
    }

    public void setCommuneID(int communeID) {
        this.communeID = communeID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_store_page);
        getSupportActionBar().setTitle("Đăng kí cửa hàng");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkbox = findViewById(R.id.checkBox);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkbox.isChecked()){
                    getLocation();
                } else {
                    setEnableAndVisibleLayout();
                }
            }
        });

        cityLayout = (RelativeLayout) findViewById(R.id.cityLayout);
        townLayout = (RelativeLayout) findViewById(R.id.townLayout);
        communeLayout = (RelativeLayout) findViewById(R.id.communeLayout);

        city = (TextView) findViewById(R.id.city);
        town = (TextView) findViewById(R.id.town);
        commune = (TextView) findViewById(R.id.commune);
        storeName = (TextView) findViewById(R.id.etStoreName);
        phone = (TextView) findViewById(R.id.etPhone);
        tvStoreNameError = (TextView) findViewById(R.id.tvStoreNameError);
        etDetailAddress =(TextView) findViewById(R.id.etDetailAddress);

        rightArrowCity = (ImageView) findViewById(R.id.rightArrowCity);
        rightArrowTown = (ImageView) findViewById(R.id.rightArrowTown);
        rightArrowCommune = (ImageView) findViewById(R.id.rightArrowCommune);

        registerBtn = (Button) findViewById(R.id.registerBtn);

        city.setText(String.valueOf(cityID));
        town.setText(String.valueOf(townID));
        commune.setText(String.valueOf(communeID));

        userID = getIntent().getIntExtra("userID", -1);


        cityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCitySelectorPage = new Intent(RegisterStorePage.this, CityChoosenPage.class);
                startActivityForResult(toCitySelectorPage, 1);
            }

        });

        townLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivityInTown(cityID);
            }
        });

        communeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivityInCommune(cityID, townID);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterStorePage.this, "cityID : "+cityID, Toast.LENGTH_SHORT).show();
                Toast.makeText(RegisterStorePage.this, "townID : "+townID, Toast.LENGTH_SHORT).show();
                Toast.makeText(RegisterStorePage.this, "communeID : "+communeID, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                cityID = data.getIntExtra("cityID", cityID);
                townID = data.getIntExtra("townID", townID);
                communeID = data.getIntExtra("communeID", communeID);

                city.setText(String.valueOf(cityID));
                town.setText(String.valueOf(townID));
                commune.setText(String.valueOf(communeID));

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    public void getActivityInTown(int cityID){
        if(cityID == 0) {
            // to
            Intent i = new Intent(RegisterStorePage.this, CityChoosenPage.class);
            startActivityForResult(i, 1);
        } else {
            Intent i = new Intent(RegisterStorePage.this, TownChoosenPage.class);
            startActivityForResult(i,1);
        }
    }


    public void getActivityInCommune(int cityID, int townID){
        if(cityID == 0) {
            // to
            Intent i = new Intent(RegisterStorePage.this, CityChoosenPage.class);
            startActivityForResult(i, 1);
        } else if (cityID != 0 && townID == 0) {
            Intent i = new Intent(RegisterStorePage.this, TownChoosenPage.class);
            startActivityForResult(i,1);
        } else if (cityID != 0 && townID != 0) {
            Intent i = new Intent(RegisterStorePage.this, CommuneChoosenPage.class);
            startActivityForResult(i,1);
        }
    }

    public void setEnableAndVisibleLayout(){
        cityLayout.setEnabled(true);
        townLayout.setEnabled(true);
        communeLayout.setEnabled(true);
        rightArrowCity.setVisibility(View.VISIBLE);
        rightArrowTown.setVisibility(View.VISIBLE);
        rightArrowCommune.setVisibility(View.VISIBLE);
    }
    public void setDisableAndInvisibleLayout(){
        cityLayout.setEnabled(false);
        townLayout.setEnabled(false);
        communeLayout.setEnabled(false);
        rightArrowCity.setVisibility(View.INVISIBLE);
        rightArrowTown.setVisibility(View.INVISIBLE);
        rightArrowCommune.setVisibility(View.INVISIBLE);
    }

    public void getLocation(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null){
                double latitude = location.getLatitude();
                double longtitude = location.getLongitude();
            }
            setDisableAndInvisibleLayout();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(storeName.getText().toString().length() == 0 && phone.getText().toString().length() == 0 && etDetailAddress.getText().toString().length() == 0
                        && cityID == 0 && townID == 0 && communeID == 0) {
                    finish();
                    return true;
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterStorePage.this);
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


}
