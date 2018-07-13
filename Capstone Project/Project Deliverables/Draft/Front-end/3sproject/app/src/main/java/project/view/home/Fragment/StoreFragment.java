package project.view.home.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import project.view.OrderManagerment.OrderManagement;
import project.view.ProductInStore.ProductInStoreDisplayPage;
import project.view.R;
import project.view.StoreInformation.StoreInformation;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends Fragment {
    private Context context;
    private RelativeLayout notHaveStoreLayout, haveStoreLayout, productLayout, orderLayout, storeImgLayout;
    private TextView storeName, numberOfOrder, numberOfProduct, ownerName, address, registerDate, phoneText;
    private ImageView storeImg, btnEdit;
    private int storeID = 1;
    private StoreInformation storeInformation;
    private View view;
    private Button btnManagermentProduct,btnManagermentOrder;

    public StoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_store,container,false);
        storeInformation = new StoreInformation();
        findView();
        setLayout(storeID,haveStoreLayout, notHaveStoreLayout);
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
        productLayout = view.findViewById(R.id.productLayout);
        orderLayout = view.findViewById(R.id.orderLayout);
        storeImgLayout = view.findViewById(R.id.storeImgLayout);

        storeName = view.findViewById(R.id.storeName);
        numberOfOrder = view.findViewById(R.id.numberOfOrder);
        numberOfProduct = view.findViewById(R.id.numberOfProduct);
        ownerName = view.findViewById(R.id.ownerName);
        address = view.findViewById(R.id.address);
        registerDate = view.findViewById(R.id.registerDate);
        phoneText = view.findViewById(R.id.phoneText);
        storeImg = view.findViewById(R.id.storeImg);
        btnEdit = view.findViewById(R.id.imgEdit);
        btnManagermentOrder = view.findViewById(R.id.btnManagerOrder);
        btnManagermentProduct = view.findViewById(R.id.btnManagerProduct);

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

    public void setLayout(int storeID, RelativeLayout haveStore, RelativeLayout notHaveStore){
        if (storeID == 0) {
            haveStore.setVisibility(View.INVISIBLE);
            notHaveStore.setVisibility(View.VISIBLE);
        } else if(storeID > 0) {
            haveStore.setVisibility(View.VISIBLE);
            notHaveStore.setVisibility(View.INVISIBLE);
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

}
