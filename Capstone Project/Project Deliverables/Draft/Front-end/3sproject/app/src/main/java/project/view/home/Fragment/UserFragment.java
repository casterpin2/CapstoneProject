package project.view.home.Fragment;


import android.content.Context;
import android.content.Intent;
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

import project.view.ChangePassword.ChangePasswordActivity;
import project.view.R;
import project.view.UserInformation.UserInformationPage;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    private View view;
    private Toolbar toolbar;
    private Button btnLogout;
    private LinearLayout changePasswordLayout, userInfoLayout, cartLayout, orderLayout;
    private int userID = 1;
    private SwipeRefreshLayout userInforLayout;
    private RelativeLayout loginnedLayout, noLoginLayout, noHaveInternetLayout;
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

//        userID = getActivity().getIntent().getIntExtra("userID",0);

        setLayout(userID,loginnedLayout, noLoginLayout, noHaveInternetLayout);

        userInforLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setLayout(userID,loginnedLayout, noLoginLayout, noHaveInternetLayout);
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

        return view;
    }

    private void findView(){
        btnLogout = view.findViewById(R.id.btnLogout);
        changePasswordLayout = view.findViewById(R.id.changePasswordLayout);
        userInfoLayout = view.findViewById(R.id.userInfoLayout);
        cartLayout = view.findViewById(R.id.cartLayout);
        orderLayout = view.findViewById(R.id.orderLayout);
        userInforLayout = view.findViewById(R.id.userInforLayout);
        loginnedLayout = view.findViewById(R.id.loginnedLayout);
        noLoginLayout = view.findViewById(R.id.noLoginLayout);
        noHaveInternetLayout = view.findViewById(R.id.noHaveInternetLayout);
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

}