package project.view.gui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import project.view.R;

public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);
         project.view.util.CustomInterface.setStatusBarColor(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent toHomeActivity = new Intent(WelcomePage.this,HomePage.class);
                startActivity(toHomeActivity);
                finish();
            }
        },1500);
    }

}
