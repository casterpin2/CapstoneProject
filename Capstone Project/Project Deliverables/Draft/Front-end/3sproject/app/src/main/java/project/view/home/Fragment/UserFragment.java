package project.view.home.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import project.view.ChangePassword.ChangePasswordActivity;
import project.view.EditUserInformation.EditUserInformationPage;
import project.view.Login.LoginPage;
import project.view.R;
import project.view.UserInformation.TweakUI;
import project.view.UserInformation.UserInformationPage;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    private View view;
    private Toolbar toolbar;
    private Button btnLogout;
    private LinearLayout changePasswordLayout, userInfoLayout, cartLayout, orderLayout;
    private int userID;
    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_me,container,false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        findView();

        userID = getActivity().getIntent().getIntExtra("userID",-1);

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

        return view;
    }

    private void findView(){
        btnLogout = view.findViewById(R.id.btnLogout);
        changePasswordLayout = view.findViewById(R.id.changePasswordLayout);
        userInfoLayout = view.findViewById(R.id.userInfoLayout);
        cartLayout = view.findViewById(R.id.cartLayout);
        orderLayout = view.findViewById(R.id.orderLayout);
    }

}