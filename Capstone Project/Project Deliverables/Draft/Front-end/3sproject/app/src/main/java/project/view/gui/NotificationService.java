package project.view.gui;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import project.view.R;

import static project.view.gui.App.CHANNEL_ID;

public class NotificationService extends Service {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("notification").child("1");
    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);

        final Notification notification = new Notification.Builder(this,CHANNEL_ID)
                .setContentTitle("alo")
                .setContentText("blo")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_light_normal_background)
                .setContentIntent(pendingIntent)
                .build();
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                NotificationEntites notification1 = dataSnapshot.getValue(NotificationEntites.class);
//                if (notification1.getIsNotification().equals("abc")){
//                    startForeground(1,notification);
//                }
                Log.d("Test Service","dadas");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        myRef.addValueEventListener(listener);
        //startForeground(1,notification);
        //        //stopSelf();
        return START_NOT_STICKY;
    }
}
