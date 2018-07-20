package project.view.home.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import project.view.EditStoreInformation.EditStoreInformationPage;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;

import project.firebase.Firebase;
import project.objects.User;
import project.view.Login.LoginPage;
import project.view.OrderManagerment.OrderManagement;
import project.view.ProductInStore.ProductInStoreDisplayPage;
import project.view.R;
import project.view.RegisterStore.RegisterStorePage;
import project.view.RegisterStore.Store;
import project.view.StoreInformation.StoreInformation;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends Fragment {
    private Context context;
    private TextView storeName, ownerName, address, registerDate, phoneText;
    private ImageView storeImg, btnEdit;
    private int storeID = 1;
    private int hasStore = 0;
    private int userID = 1;
    private double latitude = 0.0;
    private double longtitude = 0.0;
    private StoreInformation storeInformation;
    private View view;
    private Store store;
    private User user;
    private Button btnManagermentProduct,btnManagermentOrder, loginBtn, registerStoreBtn;
    private StorageReference storageReference = Firebase.getFirebase();
    private SwipeRefreshLayout mainLayout;

    public StoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String userJSON = getArguments().getString("userJSON");
        String storeJSON = getArguments().getString("storeJSON");
        if (userJSON .isEmpty()){
            user = new User();
            store = new Store();
        } else {
            user = new Gson().fromJson(userJSON,User.class);
            store = new Gson().fromJson(storeJSON,Store.class);
        }
        storeID = store.getId();
        hasStore = user.getHasStore();
        userID = user.getId();

        if (isNetworkAvailable() == true && hasStore == 1 && userID != 0 && storeID != 0) {
            view = inflater.inflate(R.layout.fragment_store,container,false);
            findViewInStoreFragment();
//        setLayout(hasStore,userID,haveStoreLayout, notHaveStoreLayout, noHaveInternet,noLoginLayout);
//            SimpleDateFormat timeFormat= new SimpleDateFormat("dd/MM/yyyy");
//            String s=timeFormat.format(store.getRegisterLog());
//            registerDate.setText(s);
            registerDate.setText(store.getRegisterLog());
            storeName.setText(store.getName());
            phoneText.setText(store.getPhone());
            address.setText(store.getAddress().replaceAll("null","").replaceAll("\\s+"," "));
            ownerName.setText(user.getFirst_name() + " " + user.getLast_name());
            latitude = getActivity().getIntent().getDoubleExtra("latitude", 0.0);
            longtitude = getActivity().getIntent().getDoubleExtra("longtitude", 0.0);

            mainLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
//                setLayout(hasStore,userID,haveStoreLayout, notHaveStoreLayout, noHaveInternet,noLoginLayout);
                    stopRefresh();
                }
            });
            mainLayout.setColorSchemeResources(R.color.colorPrimary);

            btnManagermentProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toStoreProduct = new Intent(getContext(), ProductInStoreDisplayPage.class);
                    toStoreProduct.putExtra("storeID", storeID);
                    startActivity(toStoreProduct);
                }
            });

            btnManagermentOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toOrderManagement = new Intent(getContext(), OrderManagement.class);
                    toOrderManagement.putExtra("storeID", storeID);
                    getActivity().startActivity(toOrderManagement);
                }
            });
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toEditStoreInformation = new Intent(getContext(), EditStoreInformationPage.class);
                    toEditStoreInformation.putExtra("storeID", storeID);
                    toEditStoreInformation.putExtra("storeName", storeName.getText().toString());
                    toEditStoreInformation.putExtra("ownerName", ownerName.getText().toString());
                    toEditStoreInformation.putExtra("address", address.getText().toString());
                    toEditStoreInformation.putExtra("phone", phoneText.getText().toString());
                    toEditStoreInformation.putExtra("registerDate", registerDate.getText().toString());
                    toEditStoreInformation.putExtra("longtitude", longtitude);
                    toEditStoreInformation.putExtra("latitude", latitude);
                    getActivity().startActivity(toEditStoreInformation);
                }
            });
        } else if (isNetworkAvailable() == true && hasStore == 0 && userID != 0){
            view = inflater.inflate(R.layout.no_has_store_store_fragment_home_page_layout,container,false);
            findViewInNoHaveStoreLayout();
            registerStoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toRegisterStorePage = new Intent(getContext(), RegisterStorePage.class);
                    toRegisterStorePage.putExtra("user_id",user.getId());
                    startActivity(toRegisterStorePage);
                }
            });
        } else if (isNetworkAvailable() == true && userID == 0) {
            view = inflater.inflate(R.layout.no_loginned_store_fragment_home_page_layout,container,false);
            findViewInNoLoginnedLayout();
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toLoginPage = new Intent(getContext(), LoginPage.class);
                    startActivity(toLoginPage);
                }
            });
        }
        else if (isNetworkAvailable() == false){
            view = inflater.inflate(R.layout.no_have_internet_store_fragment_home_page_layout,container,false);
        }
        return view;
    }

    private void findViewInStoreFragment(){
        storeName = view.findViewById(R.id.storeName);
        ownerName = view.findViewById(R.id.ownerName);
        address = view.findViewById(R.id.address);
        registerDate = view.findViewById(R.id.registerDate);
        phoneText = view.findViewById(R.id.phoneText);
        storeImg = view.findViewById(R.id.storeImg);
        btnEdit = view.findViewById(R.id.imgEdit);
        btnManagermentOrder = view.findViewById(R.id.btnManagerOrder);
        btnManagermentProduct = view.findViewById(R.id.btnManagerProduct);

        mainLayout = view.findViewById(R.id.storeFragmentLayout);
        Glide.with(getContext() /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(store.getImage_path()))
                //.asBitmap()
                //.toBytes(Bitmap.CompressFormat.PNG, 100)
                //.format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                //.asBitmap()

                .skipMemoryCache(true)
                .into(storeImg);
    }

    private void findViewInNoLoginnedLayout(){
        loginBtn = view.findViewById(R.id.loginBtn);
    }

    private void findViewInNoHaveStoreLayout(){
        registerStoreBtn = view.findViewById(R.id.registerStoreBtn);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void stopRefresh(){

        mainLayout.setRefreshing(false);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
