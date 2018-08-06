package project.view.gui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import project.view.R;
import project.view.util.CustomInterface;

public class GetPasswordPage extends AppCompatActivity {
    private RelativeLayout main_layout;
    private TextView tvSendCodeMess;
    private TextInputEditText etPhoneNumber;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_password_page);
        CustomInterface.setStatusBarColor(this);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Quên mật khẩu?");
        findView();
        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view,getBaseContext());
                return false;
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(etPhoneNumber.getText())){
                    tvSendCodeMess.setText(R.string.error_empty);
                }else if(etPhoneNumber.getText().length()<10
                        ||etPhoneNumber.getText().length()>=12){
                    tvSendCodeMess.setText(R.string.error_validate_phone);
                }
                else{
                    tvSendCodeMess.setText("");
                    Intent toOTPCodePage = new Intent(getBaseContext(),OTPCodePage.class);
                    startActivity(toOTPCodePage);
                }

            }
        });

    }

    private void findView(){
        main_layout = findViewById(R.id.main_layout);
        tvSendCodeMess = findViewById(R.id.tvSendMess);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btn = findViewById(R.id.btn);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
