package project.view.gui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import project.view.R;

public class Main3Activity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        listView = (ListView) findViewById(R.id.abc);
        DatabaseReference myRef = database.getReference();
        DatabaseReference myRef1 = myRef.child("ordersUser").child("1");


        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dttSnapshot2 : dataSnapshot.getChildren()) {
                    Log.d("Test Firebase" , dttSnapshot2 .getKey().toString()+":"+dttSnapshot2 .getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
