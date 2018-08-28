package project.view.fragment.home;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;
import project.firebase.Firebase;
import project.objects.User;
import project.view.gui.CartPage;
import project.view.gui.ChangePasswordPage;
import project.view.gui.ResetPasswordPage;
import project.view.gui.HomePage;
import project.view.gui.LoginPage;
import project.view.R;
import project.view.gui.UserManagementOrderPage;
import project.view.model.Store;
import project.view.gui.UserInformationPage;
import project.view.util.NetworkStateReceiver;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment implements NetworkStateReceiver.NetworkStateReceiverListener {

    private View view;
    private Toolbar toolbar;
    private Button btnLogout, loginBtn;
    private TextView tvUserName;
    private CircleImageView profile_image;
    private LinearLayout changePasswordLayout, userInfoLayout, cartLayout, orderLayout;
    private int userID = 0;
    private StorageReference storageReference = Firebase.getFirebase();
    String fullName = "";
    String userAvatarPath;
    private User user;
    private Store store;
    private NetworkStateReceiver networkStateReceiver;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private String currentPathImg;
    public UserFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String userJSON = getArguments().getString("userJSON");
        String storeJSON = getArguments().getString("storeJSON");

        //check network available
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        getContext().registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        if (userJSON.isEmpty()) {
            user = new User();
            store = new Store();
        } else {
            user = new Gson().fromJson(userJSON, User.class);
            store = new Gson().fromJson(storeJSON, Store.class);
        }
        userID = user.getId();
        if(store!=null){
            currentPathImg = store.getImage_path();
        }
        // Inflate the layout for this fragment
        if (userID == 0) {
            view = inflater.inflate(R.layout.no_loginned_user_fragment_home_page_layout, container, false);
            findViewInNotLoginnedLayout();
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new clearCache().execute();
                    Intent toLoginPage = new Intent(getContext(), LoginPage.class);
                    startActivity(toLoginPage);
                }
            });
        } else if (userID != 0) {
            view = inflater.inflate(R.layout.fragment_me, container, false);
            findViewInUserFragment();
            toolbar = view.findViewById(R.id.toolbar);
            toolbar.setTitle("");
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            fullName = user.getFirst_name() + " " + user.getLast_name();
            tvUserName.setText(fullName);
            userAvatarPath = user.getImage_path();
            if (!userAvatarPath.isEmpty()) {
                if (userAvatarPath.contains("graph") || userAvatarPath.contains("google")) {
                    Glide.with(getContext() /* context */)
                            .load(userAvatarPath)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(profile_image);
                } else {
                    Glide.with(getContext() /* context */)
                            .using(new FirebaseImageLoader())
                            .load(storageReference.child(userAvatarPath))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(profile_image);

                }
            }

            changePasswordLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toChangePasswordScreen = new Intent(getContext(), ChangePasswordPage.class);
                    toChangePasswordScreen.putExtra("username", user.getUsername());
                    toChangePasswordScreen.putExtra("displayName", tvUserName.getText().toString());
                    startActivityForResult(toChangePasswordScreen,113);
                }
            });

            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (store != null){
                        if (store.getId() != 0){
                            final DatabaseReference reference = database.getReference().child("notification").child(String.valueOf(store.getId()));
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        reference.child("token").setValue("");
                                        reference.child("haveNotification").setValue("true");
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    SharedPreferences preferences = getActivity().getSharedPreferences("authentication", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    //lưu vào editor
                    editor.putString("user", "");
                    editor.putString("store", "");
                    //chấp nhận lưu xuống file
                    editor.commit();
                    Intent intent = new Intent(getActivity(), HomePage.class);
                    startActivity(intent);
                    getActivity().finishAffinity();
                }
            });
            userInfoLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toUserInfoScreen = new Intent(getContext(), UserInformationPage.class);
                    toUserInfoScreen.putExtra("userID", userID);
                    toUserInfoScreen.putExtra("currentPath",currentPathImg);
                    ;
                    startActivityForResult(toUserInfoScreen, 111);

                }
            });


            cartLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toCartPage = new Intent(getContext(), CartPage.class);
                    toCartPage.putExtra("userID", userID);
                    getContext().startActivity(toCartPage);
                }
            });

            orderLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toUserOrderManagement = new Intent(getContext(), UserManagementOrderPage.class);
                    toUserOrderManagement.putExtra("userID", userID);
                    getContext().startActivity(toUserOrderManagement);
                }
            });
        }

        return view;
    }

    @Override
    public void networkAvailable() {
    }

    @Override
    public void networkUnavailable() {
        Toast.makeText(getContext(), "Vui lòng kiểm tra kết nối mạng", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    //    SharedPreferences pre = getSharedPreferences("authentication", Context.MODE_PRIVATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Glide.get(getContext()).clearMemory();
        networkStateReceiver.removeListener(this);
        getContext().unregisterReceiver(networkStateReceiver);
    }


    @Override
    public void onPause() {
        super.onPause();

    }

    private void findViewInUserFragment() {
        btnLogout = view.findViewById(R.id.btnLogout);
        changePasswordLayout = view.findViewById(R.id.changePasswordLayout);
        userInfoLayout = view.findViewById(R.id.userInfoLayout);
        cartLayout = view.findViewById(R.id.cartLayout);
        orderLayout = view.findViewById(R.id.orderLayout);
        tvUserName = view.findViewById(R.id.tvUserName);
        profile_image = view.findViewById(R.id.profile_image);

    }

    public void findViewInNotLoginnedLayout() {
        loginBtn = view.findViewById(R.id.loginBtn);
    }


    public class clearCache extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... Void) {

            Glide.get(getContext()).clearDiskCache();

            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 111 && resultCode == 222) {
            if (data.getStringExtra("nameDisplay") != null && !data.getStringExtra("nameDisplay").isEmpty()) {
                tvUserName.setText(data.getStringExtra("nameDisplay"));
               String pathImg = data.getStringExtra("path");
                if (!pathImg.isEmpty() && pathImg != null) {
                    if (pathImg.contains("graph") || pathImg.contains("google")) {
                        Glide.with(getContext() /* context */)
                                .load(pathImg)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(profile_image);
                    } else {
                        Glide.with(getContext() /* context */)
                                .using(new FirebaseImageLoader())
                                .load(storageReference.child(pathImg))
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(profile_image);

                    }
                }
            }
        }


    }
}