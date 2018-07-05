package project.view.RegisterStore;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import project.view.R;

public class CityChoosenPage extends AppCompatActivity {

    private EditText testData;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_choosen_page);

        testData = findViewById(R.id.testData);
        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("cityID",Integer.parseInt(testData.getText().toString()));
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }
}
