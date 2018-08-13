package project.view.gui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.provider.Settings.Secure;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import project.objects.User;
import project.view.model.Store;

public class BasePage extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private User user;
    private Store store;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        restoringPreferences();
        if (user != null){
            myRef = database.getReference().child("authentication").child(String.valueOf(user.getId())).child("device_id");
            myRef.addValueEventListener(changeListener);
        }
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        if (user!=null) {
            myRef.removeEventListener(changeListener);
        }
        super.onDestroy();
    }

    private ValueEventListener changeListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            final String android_id = Secure.getString(BasePage.this.getContentResolver(),
                    Secure.ANDROID_ID);
            if (dataSnapshot.exists()){
                AlertDialog.Builder builder = new AlertDialog.Builder(BasePage.this);
                builder.setTitle("Cảnh báo đăng nhập");
                builder.setMessage("Tài khoản cửa bạn đã đăng nhập ở một phiên khác. Vui lòng kiểm tra lại!");

                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                        return;
                    }
                });

                if (!dataSnapshot.getValue(String.class).equalsIgnoreCase(android_id)) {
                    builder.setCancelable(false);
                    builder.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            logout();
                        }
                    },10000);
                }

            } else {
                return;
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void restoringPreferences(){
        SharedPreferences pre = getSharedPreferences("authentication", Context.MODE_PRIVATE);
        String userJSON = pre.getString("user", "");
        user = new Gson().fromJson(userJSON,User.class);
        String storeJSON = pre.getString("store", "");
        store = new Gson().fromJson(storeJSON,Store.class);
    }

    private void logout(){
        SharedPreferences preferences = getSharedPreferences("authentication", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        //lưu vào editor
        editor.putString("user", "");
        editor.putString("store", "");
        //chấp nhận lưu xuống file
        editor.commit();
        Intent intent = new Intent(BasePage.this, LoginPage.class);
        startActivity(intent);
        finishAffinity();
    }
}
