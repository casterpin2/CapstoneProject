package project.view.gui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import project.view.R;
import project.view.util.CustomInterface;

public class OTPCodePage extends AppCompatActivity {

    private RelativeLayout main_layout,rela;
    private TextInputEditText etCode;
    private TextView tvConfirmCodeMess, tvResend;
    private Button btn;
    private boolean isCode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpcode_page);

        CustomInterface.setStatusBarColor(this);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorApplication)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Nhập mã xác nhận SMS");
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
                if(isCode){
                    Intent toLoginPage = new Intent(getBaseContext(),LoginPage.class);
                    startActivity(toLoginPage);
                } else {
                    tvConfirmCodeMess.setText("Mã xác nhận không đúng, vui lòng nhập lại!");
                }
            }
        });
        tvResend.setPaintFlags(tvResend.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OTPCodePage.this);

                View v = getLayoutInflater().inflate(R.layout.dialog_custom,null);
                TextView content1 = v.findViewById(R.id.tvContent1);
                TextView content2 = v.findViewById(R.id.tvContent2);
                Button btnOK = v.findViewById(R.id.btnOK);
                Button btnCancle = v.findViewById(R.id.btnCancle);
                content1.setText("Gửi lại mã");
                content2.setText("Bạn có muốn gửi lại mã xác nhận không?");
                builder.setView(v);
                final AlertDialog alertDialog = builder.create();
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tvConfirmCodeMess.setText("");
                        Toast.makeText(OTPCodePage.this,"Đã gửi lại",Toast.LENGTH_LONG).show();
                        alertDialog.hide();
                    }
                });

                btnCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.hide();
                    }
                });
                alertDialog.show();
            }
        });
    }

    private void findView(){
        main_layout = findViewById(R.id.main_layout);
        etCode = findViewById(R.id.etCode);
        tvConfirmCodeMess = findViewById(R.id.tvConfirmCodeMess);
        btn = findViewById(R.id.btn);
        tvResend = findViewById(R.id.tvReSend);
        rela = findViewById(R.id.rela);
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
