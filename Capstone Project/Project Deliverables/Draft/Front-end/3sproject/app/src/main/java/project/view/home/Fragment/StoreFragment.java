package project.view.home.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import project.view.Login.LoginPage;
import project.view.OrderManagerment.OrderManagement;
import project.view.ProductInStore.ProductInStoreDisplayPage;
import project.view.R;
import project.view.RegisterStore.RegisterStorePage;
import project.view.StoreInformation.StoreInformation;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends Fragment {
    private Context context;
    private RelativeLayout notHaveStoreLayout, haveStoreLayout, productLayout, orderLayout, storeImgLayout, noHaveInternet, noLoginLayout;
    private TextView storeName, numberOfOrder, numberOfProduct, ownerName, address, registerDate, phoneText;
    private ImageView storeImg, btnEdit;
    private int storeID = 1;
    private int hasStore = 1;
    private int userID = 1;
    private StoreInformation storeInformation;
    private View view;
    private Button btnManagermentProduct,btnManagermentOrder, loginBtn, registerStoreBtn;
    private SwipeRefreshLayout mainLayout;

    public StoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_store,container,false);
        storeInformation = new StoreInformation();

        //get value here
//        storeID = getActivity().getIntent().getIntExtra("storeID", 0);
//        hasStore = getActivity().getIntent().getIntExtra("hasStore", 0);
//        userID = getActivity().getIntent().getIntExtra("userID", 0);

        findView();
        setLayout(hasStore,userID,haveStoreLayout, notHaveStoreLayout, noHaveInternet,noLoginLayout);

        mainLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setLayout(hasStore,userID,haveStoreLayout, notHaveStoreLayout, noHaveInternet,noLoginLayout);
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

        // click on to choose image from gallery
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"dsfsdfs",Toast.LENGTH_SHORT).show();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLoginPage = new Intent(getContext(), LoginPage.class);
                startActivity(toLoginPage);
            }
        });

        registerStoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toRegisterStorePage = new Intent(getContext(), RegisterStorePage.class);
                startActivity(toRegisterStorePage);
            }
        });

        int storeID = getActivity().getIntent().getIntExtra("storeID", -1);
        String nameStoree = storeName.getText().toString();
        String ownerNameStoree = ownerName.getText().toString();
        String addressStoree = address.getText().toString();
        String registerDateStoree = registerDate.getText().toString();
        String phoneTextStoree = phoneText.getText().toString();

        savingPreferences(storeID, nameStoree, ownerNameStoree, addressStoree, registerDateStoree, phoneTextStoree);
        return view;
    }

    private void findView(){

        notHaveStoreLayout = view.findViewById(R.id.notHaveStoreLayout);
        haveStoreLayout = view.findViewById(R.id.haveStoreLayout);
        orderLayout = view.findViewById(R.id.orderLayout);
        storeImgLayout = view.findViewById(R.id.storeImgLayout);

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
        noHaveInternet = view.findViewById(R.id.noHaveInternet);
        noLoginLayout = view.findViewById(R.id.noLoginLayout);
        loginBtn = view.findViewById(R.id.loginBtn);
        registerStoreBtn = view.findViewById(R.id.registerStoreBtn);

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //gọi hàm đọc trạng thái ở đây
        restoringPreferences();
    }

    @Override
    public void onPause() {
        super.onPause();

        int storeID = getActivity().getIntent().getIntExtra("storeID", -1);
        String nameStoree = storeName.getText().toString();

        String ownerNameStoree = ownerName.getText().toString();
        String addressStoree = address.getText().toString();
        String registerDateStoree = registerDate.getText().toString();
        String phoneTextStoree = phoneText.getText().toString();

        savingPreferences(storeID, nameStoree, ownerNameStoree, addressStoree, registerDateStoree, phoneTextStoree);
    }

    public void setLayout(int hasStore, int userID, RelativeLayout haveStore, RelativeLayout notHaveStore, RelativeLayout noHaveInternet, RelativeLayout noLoginLayout){
//        if (hasStore == 0) {
//            haveStore.setVisibility(View.INVISIBLE);
//            notHaveStore.setVisibility(View.VISIBLE);
//        } else if(hasStore == 1) {
//            haveStore.setVisibility(View.VISIBLE);
//            notHaveStore.setVisibility(View.INVISIBLE);
//        }
        if (isNetworkAvailable() == true && hasStore == 1) {
            haveStore.setVisibility(View.VISIBLE);
            notHaveStore.setVisibility(View.INVISIBLE);
            noHaveInternet.setVisibility(View.INVISIBLE);
            noLoginLayout.setVisibility(View.INVISIBLE);
        } else if (isNetworkAvailable() == true && hasStore == 0 && userID == 0) {
            haveStore.setVisibility(View.INVISIBLE);
            notHaveStore.setVisibility(View.INVISIBLE);
            noHaveInternet.setVisibility(View.INVISIBLE);
            noLoginLayout.setVisibility(View.VISIBLE);
        } else if(isNetworkAvailable() == true && hasStore == 0 && userID == 1) {
            haveStore.setVisibility(View.INVISIBLE);
            notHaveStore.setVisibility(View.VISIBLE);
            noHaveInternet.setVisibility(View.INVISIBLE);
            noLoginLayout.setVisibility(View.INVISIBLE);
        } else if (isNetworkAvailable() == false) {
            haveStore.setVisibility(View.INVISIBLE);
            notHaveStore.setVisibility(View.INVISIBLE);
            noHaveInternet.setVisibility(View.VISIBLE);
            noLoginLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void savingPreferences(int storeID, String name, String ownerName, String address, String registerDate, String phoneText){
        //tạo đối tượng getSharedPreferences
        SharedPreferences pre=this.getActivity().getSharedPreferences("storeInformation", Context.MODE_PRIVATE);
        //tạo đối tượng Editor để lưu thay đổi
        SharedPreferences.Editor editor=pre.edit();
        //lưu vào editor
        editor.putString("name", name);
        editor.putString("ownerName", ownerName);
        editor.putString("address", address);
        editor.putString("registerDate", registerDate);
        editor.putString("phoneText", phoneText);
        editor.putInt("storeID", storeID);

        //chấp nhận lưu xuống file
        editor.commit();
    }

    public void restoringPreferences(){
        SharedPreferences pre=this.getActivity().getSharedPreferences("storeInformation", Context.MODE_PRIVATE);
        //lấy giá trị checked ra, nếu không thấy thì giá trị mặc định là false

        //lấy user, pwd, nếu không thấy giá trị mặc định là rỗng
        int storeID = pre.getInt("storeID", -1);
        String nameStoree = pre.getString("name", "");
        String ownerNameStoree = pre.getString("ownerName", "");
        String addressStoree = pre.getString("address", "");
        String registerDateStoree = pre.getString("registerDate", "");
        String phoneTextStoree = pre.getString("phoneText", "");


        storeName.setText(nameStoree);
        ownerName.setText(ownerNameStoree);
        address.setText(addressStoree);
        registerDate.setText(registerDateStoree);
        phoneText.setText(phoneTextStoree);

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
