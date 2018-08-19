package project.view.gui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
    private ProgressBar loadingBar;
    private LinearLayout storeInforForm;
    private TextView nullMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_information_page);
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
    }

    private void findView(){
        backBtn = findViewById(R.id.backBtn);
        ownerName = findViewById(R.id.ownerName);
        address = findViewById(R.id.address);
        registerDate = findViewById(R.id.registerDate);
        phoneText = findViewById(R.id.phoneText);
        loadingBar = findViewById(R.id.loadingBar);
        storeInforForm = findViewById(R.id.storeInforForm);
        nullMessage = findViewById(R.id.nullMessage);
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
                ownerName.setText(store.getName());
                address.setText(store.getAddress());
                registerDate.setText(store.getRegisterLog());
                phoneText.setText(store.getPhone());
                haveData = true;
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
}
