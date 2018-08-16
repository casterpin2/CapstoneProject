package project.view.gui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    TextView ownerName, address, registerDate, phoneText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_information_page);
        findView();
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
    }

    private void findView(){
        backBtn = findViewById(R.id.backBtn);
        ownerName = findViewById(R.id.ownerName);
        address = findViewById(R.id.address);
        registerDate = findViewById(R.id.registerDate);
        phoneText = findViewById(R.id.phoneText);
    }

    public class StoreInformation extends AsyncTask<Call,Void,Store> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Store store1) {
            super.onPostExecute(store1);
            if (store1 != null){
                store = store1;
                ownerName.setText(store.getName());
                address.setText(store.getAddress());
                registerDate.setText(store.getRegisterLog());
                phoneText.setText(store.getPhone());
            } else {
                Toast.makeText(getBaseContext(),"Có lỗi xảy ra",Toast.LENGTH_LONG).show();
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
                e.printStackTrace();
            }
            return null;
        }
    }
}
