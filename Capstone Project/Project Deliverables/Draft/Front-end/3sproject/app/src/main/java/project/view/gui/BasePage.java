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
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.io.IOException;

import project.firebase.Firebase;
import project.firebase.FirebaseIDService;
import project.firebase.NotificationFirebaseService;
import project.objects.User;
import project.view.model.Notification;
import project.view.model.Store;

public class BasePage extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private User user;
    private Store store;
    private String deviceToken;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        restoringPreferences();
        if (user != null){
            myRef = database.getReference().child("authentication").child(String.valueOf(user.getId())).child("device_id");
            myRef.addValueEventListener(changeListener);
        }
        if (store != null){
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    deviceToken = instanceIdResult.getToken();
                    final DatabaseReference reference = database.getReference().child("notification").child(String.valueOf(store.getId()));
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (deviceToken != null){
                                reference.child("token").setValue(deviceToken);
                                reference.child("haveNotification").setValue("true");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });

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

    @Override
    protected void onStop() {
        if (user != null){
            //startService();
            //Log.d("notification","abc");
        }
        if (store != null){
            final DatabaseReference reference = database.getReference().child("notification").child(String.valueOf(store.getId()));
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (deviceToken != null){
                        reference.child("token").setValue(deviceToken);
                        reference.child("haveNotification").setValue("false");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        super.onStop();
    }

    private ValueEventListener changeListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            final String android_id = Secure.getString(BasePage.this.getContentResolver(),
                    Secure.ANDROID_ID);
            if (dataSnapshot.exists()){
                final boolean[] isIntent = {false};
                AlertDialog.Builder builder = new AlertDialog.Builder(BasePage.this);
                builder.setTitle("Cảnh báo đăng nhập");
                builder.setMessage("Tài khoản cửa bạn đã đăng nhập ở một phiên khác. Vui lòng kiểm tra lại!");

                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isIntent[0] = true;
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
                            if (isIntent[0] == false)
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
        Intent intent = new Intent(BasePage.this, HomePage.class);
        intent.putExtra("isLogin",true);
        startActivity(intent);
        finish();
    }
    public void startService(){
        Intent intent = new Intent(this,NotificationFirebaseService.class);
        startService(intent);
    }

    public void stopService(){
        Intent intent = new Intent(this,NotificationFirebaseService.class);
        stopService(intent);
    }
}
