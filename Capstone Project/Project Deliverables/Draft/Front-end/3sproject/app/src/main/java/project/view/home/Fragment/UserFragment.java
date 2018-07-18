package project.view.home.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;
import project.firebase.Firebase;
import project.view.ChangePassword.ChangePasswordActivity;
import project.view.Login.LoginPage;
import project.view.R;
import project.view.UserInformation.UserInformationPage;

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
    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userID = LoginPage.login.getUser().getId();
        if(userID == 0) {
            restoringPreferences();
        }

        // Inflate the layout for this fragment
        if (userID == 0 && isNetworkAvailable() == true) {
            view = inflater.inflate(R.layout.no_loginned_user_fragment_home_page_layout, container, false);
            findViewInNotLoginnedLayout();
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
//            fullName = LoginPage.login.getUser().getFirst_name() + " " + LoginPage.login.getUser().getLast_name();

            tvUserName.setText(fullName);
            userAvatarPath = LoginPage.login.getUser().getImage_path();
            if (!userAvatarPath.isEmpty()){
                if (userAvatarPath.contains("graph")) {
                    Glide.with(getContext() /* context */)
                            .load(userAvatarPath)
                            .into(profile_image);
                } else {
                    Glide.with(getContext() /* context */)
                            .using(new FirebaseImageLoader())
                            .load(storageReference.child(userAvatarPath))
                            .into(profile_image);
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
                    Intent toChangePasswordScreen = new Intent(getContext(), ChangePasswordActivity.class);
                    toChangePasswordScreen.putExtra("userID",userID);
                    getContext().startActivity(toChangePasswordScreen);
                }
            });

            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
             /*   Intent toLoginScreen = new Intent(getContext(), LoginPage.class);
                getContext().startActivity(toLoginScreen);*/
                }
            });
            userInfoLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toUserInfoScreen = new Intent(getContext(), UserInformationPage.class);
                    toUserInfoScreen.putExtra("userID",userID);
                    getContext().startActivity(toUserInfoScreen);
                }
            });


            cartLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            orderLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else if (isNetworkAvailable() == false){
            view = inflater.inflate(R.layout.no_have_internet_user_fragment_home_page_layout, container, false);
        }

        return view;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //gọi hàm đọc trạng thái ở đây

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fullName = LoginPage.login.getUser().getFirst_name() + " " + LoginPage.login.getUser().getLast_name();
        savingPreferences(fullName);
    }



    @Override
    public void onPause() {
        super.onPause();

    }

    private void findViewInUserFragment(){
        btnLogout = view.findViewById(R.id.btnLogout);
        changePasswordLayout = view.findViewById(R.id.changePasswordLayout);
        userInfoLayout = view.findViewById(R.id.userInfoLayout);
        cartLayout = view.findViewById(R.id.cartLayout);
        orderLayout = view.findViewById(R.id.orderLayout);
        userInforLayout = view.findViewById(R.id.userInforLayout);
        tvUserName = view.findViewById(R.id.tvUserName);
        profile_image = view.findViewById(R.id.profile_image);

    } public void findViewInNotLoginnedLayout(){
        loginBtn = view .findViewById(R.id.loginBtn);
    }

    private void stopRefresh(){

        userInforLayout.setRefreshing(false);
    }

    public void setLayout(int userID, RelativeLayout loginnedLayout, RelativeLayout noLoginLayout, RelativeLayout noHaveInternetLayout){
        if(isNetworkAvailable() == true && userID == 0) {
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

    public void savingPreferences(String fullName){
        //tạo đối tượng getSharedPreferences
        SharedPreferences pre=this.getActivity().getSharedPreferences("userInformation", Context.MODE_PRIVATE);
        //tạo đối tượng Editor để lưu thay đổi
        SharedPreferences.Editor editor=pre.edit();
        fullName = LoginPage.login.getUser().getFirst_name() + " " + LoginPage.login.getUser().getLast_name();
        //lưu vào editor
        editor.putInt("userID", LoginPage.login.getUser().getId());
        editor.putString("userAvatarPath", LoginPage.login.getUser().getImage_path());
        editor.putString("fullName", fullName);
        //chấp nhận lưu xuống file
        editor.commit();
    }

    public void restoringPreferences(){
        SharedPreferences pre=this.getActivity().getSharedPreferences("userInformation", Context.MODE_PRIVATE);
        //lấy giá trị checked ra, nếu không thấy thì giá trị mặc định là false

        //lấy user, pwd, nếu không thấy giá trị mặc định là rỗng
        userID = pre.getInt("userID", 0);
        fullName = pre.getString("fullName", "");
        userAvatarPath = pre.getString("userAvatarPath", "");


        String userAvatarPath = pre.getString("userAvatarPath", "");
//        tvUserName.setText(fullName);
//        if (userAvatarPath.contains("graph")) {
//            Glide.with(getContext() /* context */)
//                    .load(userAvatarPath)
//                    .into(profile_image);
//        } else {
//            Glide.with(getContext() /* context */)
//                    .using(new FirebaseImageLoader())
//                    .load(storageReference.child(userAvatarPath))
//                    .into(profile_image);
//        }

        }

}