package project.view.fragment.home;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    private View view;
    private Toolbar toolbar;
    private Button btnLogout, loginBtn;
    private TextView tvUserName;
    private CircleImageView profile_image;
    private LinearLayout changePasswordLayout, userInfoLayout, cartLayout, orderLayout;
    private int userID = 0;
    private SwipeRefreshLayout userInforLayout;
    private StorageReference storageReference = Firebase.getFirebase();
    String fullName = "";
    String userAvatarPath = "";
    private User user;
    private Store store;

    public UserFragment() {
        // Required empty public constructor
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
        userID = user.getId();
        // Inflate the layout for this fragment
        if (userID == 0 && isNetworkAvailable() == true) {
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
        } else if (isNetworkAvailable() == true && userID != 0) {
            view = inflater.inflate(R.layout.fragment_me, container, false);
            findViewInUserFragment();
            toolbar = view.findViewById(R.id.toolbar);
            toolbar.setTitle("");
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            fullName = user.getFirst_name() + " " + user.getLast_name();
            tvUserName.setText(fullName);
            userAvatarPath = user.getImage_path();
            if (!userAvatarPath.isEmpty()) {
                if (userAvatarPath.contains("graph")) {
                    Glide.with(getContext() /* context */)
                            .load(userAvatarPath)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(profile_image);
                } else {
                    Glide.with(getContext() /* context */)
                            .using(new FirebaseImageLoader())
                            .load(storageReference.child(userAvatarPath))
                            //.asBitmap()
                            //.toBytes(Bitmap.CompressFormat.PNG, 100)
                            //.format(DecodeFormat.PREFER_ARGB_8888)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(profile_image);
//                    storageReference.child(userAvatarPath).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                        @Override
//                        public void onSuccess(byte[] bytes) {
//                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                            profile_image.setImageBitmap(bitmap);
//                            // Use the bytes to display the image
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            // Handle any errors
//                        }
//                    });
                }
            }

            userInforLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    stopRefresh();
                }
            });
            userInforLayout.setColorSchemeResources(R.color.colorPrimary);

            changePasswordLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toChangePasswordScreen = new Intent(getContext(), ChangePasswordPage.class);
                    toChangePasswordScreen.putExtra("username", user.getUsername());

                    startActivityForResult(toChangePasswordScreen,113);
                }
            });

            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
             /*   Intent toLoginScreen = new Intent(getContext(), LoginPage.class);
                getContext().startActivity(toLoginScreen);*/
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
        } else if (isNetworkAvailable() == false) {
            view = inflater.inflate(R.layout.no_have_internet_user_fragment_home_page_layout, container, false);
        }

        return view;
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
        userInforLayout = view.findViewById(R.id.userInforLayout);
        tvUserName = view.findViewById(R.id.tvUserName);
        profile_image = view.findViewById(R.id.profile_image);

    }

    public void findViewInNotLoginnedLayout() {
        loginBtn = view.findViewById(R.id.loginBtn);
    }

    private void stopRefresh() {

        userInforLayout.setRefreshing(false);
    }

    public void setLayout(int userID, RelativeLayout loginnedLayout, RelativeLayout noLoginLayout, RelativeLayout noHaveInternetLayout) {
        if (isNetworkAvailable() == true && userID == 0) {
            noLoginLayout.setVisibility(View.VISIBLE);
            loginnedLayout.setVisibility(View.INVISIBLE);
            noHaveInternetLayout.setVisibility(View.INVISIBLE);
        } else if (isNetworkAvailable() == true && userID != 0) {
            noLoginLayout.setVisibility(View.INVISIBLE);
            loginnedLayout.setVisibility(View.VISIBLE);
            noHaveInternetLayout.setVisibility(View.INVISIBLE);
        } else if (isNetworkAvailable() == false) {
            noLoginLayout.setVisibility(View.INVISIBLE);
            loginnedLayout.setVisibility(View.INVISIBLE);
            noHaveInternetLayout.setVisibility(View.VISIBLE);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
        String a = data.getStringExtra("nameDisplay");
        if (requestCode == 111 && resultCode == 222) {
            if (data.getStringExtra("nameDisplay") != null && !data.getStringExtra("nameDisplay").isEmpty()) {
                tvUserName.setText(data.getStringExtra("nameDisplay"));
            }
        }
    }
}