package project.view.gui;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import project.view.R;
import project.view.util.CustomInterface;

public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor((this.getResources().getColor(R.color.tab_unchecked)));
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent toHomeActivity = new Intent(WelcomePage.this,HomePage.class);
                startActivity(toHomeActivity);
                finish();
            }
        },2000);
    }

}
