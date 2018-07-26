package project.view.gui;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sackcentury.shinebuttonlib.ShineButton;

import project.view.R;

public class FeedbackPage extends AppCompatActivity {
    private ShineButton like, smile;
    private EditText content;
    private Button sendBtn, skipBtn;
    private boolean buyDone = false;
    private ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_page);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Đánh giá cửa hàng");

        findView();

        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);
        loadingBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorApplication), android.graphics.PorterDuff.Mode.MULTIPLY);

        buyDone = getIntent().getBooleanExtra("isBought", false);
        if(buyDone == false) {
            skipBtn.setVisibility(View.INVISIBLE);
            skipBtn.setEnabled(false);
        } else {
            skipBtn.setVisibility(View.VISIBLE);
            skipBtn.setEnabled(true);
            skipBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(FeedbackPage.this, "Skipped!!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        like.setAnimDuration(3000);
        smile.setAnimDuration(3000);
        if(like.isChecked()== true) {
            Toast.makeText(this, "Like checked!!!", Toast.LENGTH_SHORT).show();
        }
//        smile.setChecked(true, true);
//        like.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(View view, boolean checked) {
//                Toast.makeText(FeedbackPage.this, "Like Checked!!!", Toast.LENGTH_SHORT).show();
////                smile.setBtnFillColor(R.color.colorApplication);
////                smile.setBtnColor(R.color.darkGrey);
//            }
//        });
//        smile.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(View view, boolean checked) {
//                Toast.makeText(FeedbackPage.this, "SmileChecked!!!", Toast.LENGTH_SHORT).show();
////                like.setBtnFillColor(R.color.colorApplication);
////                like.setBtnColor(R.color.darkGrey);
//            }
//        });
        if(like.isChecked()){
            Toast.makeText(this, "Like", Toast.LENGTH_SHORT).show();

        } else if (smile.isChecked()) {
            Toast.makeText(this, "Smile", Toast.LENGTH_SHORT).show();
        }


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FeedbackPage.this, "Sent!!!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void findView(){
        like = findViewById(R.id.like);
        smile = findViewById(R.id.smile);
        content = findViewById(R.id.content);
        sendBtn = findViewById(R.id.sendBtn);
        skipBtn = findViewById(R.id.skipBtn);
    }
}
