package project.view.home.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import project.view.EditUserInformation.EditUserInformationPage;
import project.view.R;
import project.view.UserInformation.TweakUI;
import project.view.UserInformation.UserInformationPage;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {
    private TextView txtName, txtAddress, txtPhone, txtEmail, txtGender, txtDob, txtstoreName, txtOder;
    ImageView btnEdit;
    RelativeLayout storeLayout;
    private View view;
    private final int REQUEST_PROFILE_CODE = 123;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_me,container,false);
        TweakUI.makeTransparent(getActivity());
        mapping();
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile(v);
            }
        });

        storeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        String name = txtName.getText().toString();
        String address = txtAddress.getText().toString();
        String phone = txtPhone.getText().toString();
        String email = txtEmail.getText().toString();
        String gender = txtGender.getText().toString();
        String dob = txtDob.getText().toString();
        int numberOfOrder = Integer.parseInt(txtOder.getText().toString());
        String storeName = txtstoreName.getText().toString();

        //gọi hàm lưu trạng thái ở đây
        savingPreferences(name, address, phone, email, gender, dob, numberOfOrder, storeName);

        return view;
    }


    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        String name = txtName.getText().toString();
        String address = txtAddress.getText().toString();
        String phone = txtPhone.getText().toString();
        String email = txtEmail.getText().toString();
        String gender = txtGender.getText().toString();
        String dob = txtDob.getText().toString();
        int numberOfOrder = Integer.parseInt(txtOder.getText().toString());
        String storeName = txtstoreName.getText().toString();

        //gọi hàm lưu trạng thái ở đây
        savingPreferences(name, address, phone, email, gender, dob, numberOfOrder, storeName);
    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //gọi hàm đọc trạng thái ở đây
        restoringPreferences();
    }

    public void editProfile(View view) {
        Bundle extras = new Bundle();
        extras.putString("name", txtName.getText().toString());
        extras.putString("phone", txtPhone.getText().toString());
        extras.putString("address", txtAddress.getText().toString());
        extras.putString("email", txtEmail.getText().toString());
        extras.putString("dob", txtDob.getText().toString());
        extras.putString("gender", txtGender.getText().toString());

        Intent toEditUserInformationPage = new Intent(getContext(), EditUserInformationPage.class);
        toEditUserInformationPage.putExtras(extras);
        startActivityForResult(toEditUserInformationPage, REQUEST_PROFILE_CODE);
    }


    private void mapping() {
        btnEdit = view.findViewById(R.id.btnEdit);
        txtName = view.findViewById(R.id.txt_name);
        txtPhone = view.findViewById(R.id.txt_phone);
        txtAddress = view.findViewById(R.id.txt_address);
        txtEmail = view.findViewById(R.id.txt_email);
        txtGender = view.findViewById(R.id.txt_gender);
        txtDob = view.findViewById(R.id.txt_dob);
        txtstoreName = view.findViewById(R.id.storeName);
        storeLayout = view.findViewById(R.id.storeLayout);
        txtOder = view.findViewById(R.id.txtOder);
            }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PROFILE_CODE && resultCode == 200) {
            Bundle extras = data.getExtras();

            String name = extras.getString("name");
            String address = extras.getString("address");
            String phone = extras.getString("phone");
            String email = extras.getString("email");
            String gender = extras.getString("gender");
            String dob = extras.getString("dob");
            int numberOfOrder = Integer.parseInt(txtOder.getText().toString());
            String storeName = txtstoreName.getText().toString();


            txtName.setText(name);
            txtAddress.setText(address);
            txtPhone.setText(phone);
            txtEmail.setText(email);
            txtDob.setText(dob);
            txtGender.setText(gender);

            savingPreferences(name, address, phone, email, gender, dob, numberOfOrder, storeName);
        }
        else if (requestCode == REQUEST_PROFILE_CODE && resultCode == 201) {
            Toast.makeText(getContext(), "Thông tin không thay đổi", Toast.LENGTH_SHORT).show();
        }
    }

    public void savingPreferences(String name, String address, String phone, String email, String gender, String dob, int numberOfOrder, String storeName){
        //tạo đối tượng getSharedPreferences
        SharedPreferences pre = this.getActivity().getSharedPreferences("main", Context.MODE_PRIVATE);
        //tạo đối tượng Editor để lưu thay đổi
        SharedPreferences.Editor editor=pre.edit();
        //lưu vào editor
        editor.putString("name", name);
        editor.putString("address", address);
        editor.putString("phone", phone);
        editor.putString("email", email);
        editor.putString("gender", gender);
        editor.putString("dob", dob);
        editor.putInt("numberOfOrder", numberOfOrder);
        editor.putString("storeName",storeName );


        //chấp nhận lưu xuống file
        editor.commit();
    }

    public void restoringPreferences(){
        SharedPreferences pre = this.getActivity().getSharedPreferences("main", Context.MODE_PRIVATE);
        //lấy giá trị checked ra, nếu không thấy thì giá trị mặc định là false

        //lấy user, pwd, nếu không thấy giá trị mặc định là rỗng
        String name = pre.getString("name", "");
        String address = pre.getString("address", "");
        String phone = pre.getString("phone", "");
        String email = pre.getString("email", "");
        String gender = pre.getString("gender", "");
        String dob = pre.getString("dob", "");

//        Toast.makeText(this, "name" + name, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "address" + address, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "phone" + phone, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "gender" + gender, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "dob" + dob, Toast.LENGTH_SHORT).show();

        txtName.setText(name);
        txtAddress.setText(address);
        txtPhone.setText(phone);
        txtEmail.setText(email);
        txtGender.setText(gender);
        txtDob.setText(dob);

    }

}