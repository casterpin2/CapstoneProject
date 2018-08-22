package project.view.fragment.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import android.support.v4.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import project.retrofit.ApiUtils;
import project.view.gui.EditStoreInformationPage;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import project.firebase.Firebase;
import project.objects.User;
import project.view.gui.FeedbackManagement;
import project.view.gui.LoginPage;
import project.view.gui.MainActivity;
import project.view.gui.ProductInStoreDisplayPage;
import project.view.R;
import project.view.gui.RegisterStorePage;
import project.view.gui.StoreManagementOrderPage;
import project.view.model.Store;
import project.view.util.NetworkStateReceiver;
import retrofit2.Call;
import retrofit2.Response;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends Fragment implements NetworkStateReceiver.NetworkStateReceiverListener {
    private TextView storeName, ownerName, address, registerDate, phoneText, tv_count_smile, tv_count_sad,tv_feedback_status;
    private ImageView storeImg, btnEdit;
    private int storeID = 1;
    private int hasStore = 0;
    private int userID = 1;
    private double latitude = 0.0;
    private double longtitude = 0.0;
    private View view;
    private Store store;
    private User user;
    private Button btnManagermentProduct, btnManagermentOrder, loginBtn, registerStoreBtn;
    private StorageReference storageReference = Firebase.getFirebase();
    private final int RESULT_CODE = 2222;
    private final int REQUEST_CODE = 1111;
    private NetworkStateReceiver networkStateReceiver;
    private ImageView imgCall;
    private static final int REQUEST_CALL = 1;
    private String phoneNumber;
    private static final int IMAGE_CODE = 100;

    private LinearLayout changeStoreImageLayout;
    public StoreFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String userJSON = getArguments().getString("userJSON");
        String storeJSON = getArguments().getString("storeJSON");
        if (userJSON.isEmpty()) {
            user = new User();
            store = new Store();
        } else {
            user = new Gson().fromJson(userJSON, User.class);
            store = new Gson().fromJson(storeJSON, Store.class);
        }
        storeID = store.getId();
        hasStore = user.getHasStore();
        userID = user.getId();

        //check network available
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        getContext().registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        if (hasStore == 1 && userID != 0 && storeID != 0) {
            view = inflater.inflate(R.layout.fragment_store, container, false);
            findViewInStoreFragment();
            registerDate.setText(store.getRegisterLog());
            storeName.setText(store.getName());
            phoneText.setText(store.getPhone());
            address.setText(store.getAddress().replaceAll("null", "").replaceAll("\\s+", " "));
            ownerName.setText(user.getFirst_name() + " " + user.getLast_name());
            latitude = Double.parseDouble(store.getLatitude());
            longtitude = Double.parseDouble(store.getLongtitude());

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
                    Intent toStoreManagementOrder = new Intent(getContext(), StoreManagementOrderPage.class);
                    toStoreManagementOrder.putExtra("storeId", storeID);
                    getActivity().startActivity(toStoreManagementOrder);
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
                    startActivityForResult(toEditStoreInformation, REQUEST_CODE);
                }
            });
            tv_feedback_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent toFeedbackManagement = new Intent(getContext(), FeedbackManagement.class);
                    toFeedbackManagement.putExtra("storeId",storeID);
                    getContext().startActivity(toFeedbackManagement);
                }
            });
            imgCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            changeStoreImageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(),"sadasd",Toast.LENGTH_LONG);
                    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                    if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
                        if (checkPermission()) {
                            Intent toGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                            startActivityForResult(toGallery, IMAGE_CODE);
                        } else {
                            requestPermission();
                        }
                    } else {
                        Intent toGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        startActivityForResult(toGallery, IMAGE_CODE);
                    }
                }
            });

        } else if (hasStore == 0 && userID != 0) {
            view = inflater.inflate(R.layout.no_has_store_store_fragment_home_page_layout, container, false);
            findViewInNoHaveStoreLayout();
            registerStoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent toRegisterStorePage = new Intent(getContext(), RegisterStorePage.class);
                        toRegisterStorePage.putExtra("user_id", user.getId());
                        startActivity(toRegisterStorePage);

                }
            });
        } else if (userID == 0) {
            view = inflater.inflate(R.layout.no_loginned_store_fragment_home_page_layout, container, false);
            findViewInNoLoginnedLayout();
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Intent toLoginPage = new Intent(getContext(), LoginPage.class);
                        startActivity(toLoginPage);

                }
            });
        }
        return view;
    }
    private boolean checkPermission() {
        boolean checkPermissionRead = ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return checkPermissionRead;
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{READ_EXTERNAL_STORAGE}, 1001);
    }


    @Override
    public void networkAvailable() {
    }

    @Override
    public void networkUnavailable() {
        Toast.makeText(getContext(), "Vui lòng kiểm tra kết nối mạng", Toast.LENGTH_LONG).show();
    }


    private void findViewInStoreFragment() {
        changeStoreImageLayout = view.findViewById(R.id.changeStoreImageLayout);
        storeName = view.findViewById(R.id.storeName);
        ownerName = view.findViewById(R.id.ownerName);
        address = view.findViewById(R.id.address);
        registerDate = view.findViewById(R.id.registerDate);
        phoneText = view.findViewById(R.id.phoneText);
        storeImg = view.findViewById(R.id.storeImg);
        btnEdit = view.findViewById(R.id.imgEdit);
        imgCall = view.findViewById(R.id.image_call);
        btnManagermentOrder = view.findViewById(R.id.btnManagerOrder);
        btnManagermentProduct = view.findViewById(R.id.btnManagerProduct);
        tv_count_smile = (TextView) view.findViewById(R.id.tv_count_smile);
        tv_count_sad = (TextView) view.findViewById(R.id.tv_count_sad);
        Glide.with(getContext() /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child(store.getImage_path()))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(storeImg);

        tv_feedback_status = view.findViewById(R.id.tv_feedback_status);
    }

    private void findViewInNoLoginnedLayout() {
        loginBtn = view.findViewById(R.id.loginBtn);
    }

    private void findViewInNoHaveStoreLayout() {
        registerStoreBtn = view.findViewById(R.id.registerStoreBtn);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasStore == 1 && userID != 0 && storeID != 0) {
            Call<List<Integer>> call = ApiUtils.getAPIService().countFeedback(store.getId());
            new CountFeedback().execute(call);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        getContext().unregisterReceiver(networkStateReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            String checkData = data.getStringExtra("data");
            if (!checkData.equals("NO")) {
                String userJSON = data.getStringExtra("userData");
                String storeJSON = data.getStringExtra("storeData");
                if (userJSON.isEmpty()) {
                    user = new User();
                    store = new Store();
                } else {
                    user = new Gson().fromJson(userJSON, User.class);
                    store = new Gson().fromJson(storeJSON, Store.class);
                }
                registerDate.setText(store.getRegisterLog());
                storeName.setText(store.getName());
                phoneText.setText(store.getPhone());
                address.setText(store.getAddress().replaceAll("null", "").replaceAll("\\s+", " "));
                ownerName.setText(user.getFirst_name() + " " + user.getLast_name());
                latitude = getActivity().getIntent().getDoubleExtra("latitude", 0.0);
                longtitude = getActivity().getIntent().getDoubleExtra("longtitude", 0.0);
            }
        }
    }


    public class CountFeedback extends AsyncTask<Call,Void,List<Integer>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Integer> list) {
            super.onPostExecute(list);
            if (list != null) {
                tv_count_smile.setText(String.valueOf(list.get(0)));
                tv_count_sad.setText(String.valueOf(list.get(1)));
            }
            else {
                tv_count_smile.setText(String.valueOf(0));
                tv_count_sad.setText(String.valueOf(0));
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected List<Integer> doInBackground(Call... calls) {
            try {
                Call<List<Integer>> call = calls[0];
                Response<List<Integer>> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
