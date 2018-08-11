package project.view.gui;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import project.view.R;
import project.view.util.CustomInterface;
import project.view.util.TweakUI;

public class UserFeedbackPage extends BasePage {
    private LinearLayout main_layout;
    private boolean isSmile = false;
    protected boolean isSad = false;
    private ImageView smile_checked, sad_checked, back_btn;
    private TextView tv_feedback_status, error_mess;
    private Button btn_send_feedback;
    private EditText content_feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback_page);
     //   TweakUI.makeTransparent(this);
        CustomInterface.setStatusBarColor(this);
        CustomInterface.setSoftInputMode(this);
        findView();

        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view,getBaseContext());
                return false;
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_send_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(content_feedback.getText().length()==0){
                    error_mess.setText("Bạn chưa có nhận xét nào!");
                } else {
                    error_mess.setText("");
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserFeedbackPage.this);
                    View v = getLayoutInflater().inflate(R.layout.dialog_custom,null);
                    TextView content1 = v.findViewById(R.id.tvContent1);
                    TextView content2 = v.findViewById(R.id.tvContent2);
                    Button btnOK = v.findViewById(R.id.btnOK);
                    Button btnCancle = v.findViewById(R.id.btnCancle);
                    content1.setText("Đánh giá cửa hàng");
                    content2.setText("Bạn có thật sự muốn gửi đánh giá này cho chúng tôi không?");
                    builder.setView(v);
                    final AlertDialog alertDialog = builder.create();
                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog();
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
            }
        });

        setIconSelected();
    }
    private void findView(){
        main_layout = findViewById(R.id.main_layout);
        smile_checked = findViewById(R.id.smile_unchecked);
        sad_checked = findViewById(R.id.sad_unchecked);
        tv_feedback_status = findViewById(R.id.tv_feedback_status);
        back_btn = findViewById(R.id.backBtn);
        btn_send_feedback = findViewById(R.id.btn_send_feedback);
        content_feedback = findViewById(R.id.content);
        error_mess = findViewById(R.id.errorMessage);
    }

    private void dialog(){
        error_mess.setText("");
        AlertDialog.Builder builder = new AlertDialog.Builder(UserFeedbackPage.this);
        View v = getLayoutInflater().inflate(R.layout.dialog_ok,null);
        TextView content1 = v.findViewById(R.id.tvContent1);
        TextView content2 = v.findViewById(R.id.tvContent2);
        Button btnOK = v.findViewById(R.id.btnOK);
        content1.setText("Lời cám ơn");
        content2.setText("Cám ơn bạn đã gửi đánh giá cho chúng tôi!");
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toOTPCodePage = new Intent(getBaseContext(),OTPCodePage.class);
                startActivity(toOTPCodePage);
                alertDialog.hide();
            }
        });
        alertDialog.show();
    }


    private void setIconSelected(){

        smile_checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isSmile){
                    smile_checked.setImageResource(R.drawable.smile_checked);
                    sad_checked.setImageResource(R.drawable.sad_unchecked);
                    isSmile=true;
                    isSad=false;
                    tv_feedback_status.setText("Hài lòng!");

                }else {
                    smile_checked.setImageResource(R.drawable.smile_unchecked);
                    sad_checked.setImageResource(R.drawable.sad_checked);
                    isSmile=false;
                    isSad = true;
                    tv_feedback_status.setText("Không hài lòng!");
                }
            }
        });
        sad_checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isSad){
                    sad_checked.setImageResource(R.drawable.sad_checked);
                    smile_checked.setImageResource(R.drawable.smile_unchecked);
                    isSad=true;
                    isSmile=false;
                    tv_feedback_status.setText("Không hài lòng!");
                }else {
                    sad_checked.setImageResource(R.drawable.sad_unchecked);
                    smile_checked.setImageResource(R.drawable.smile_checked);
                    isSad=false;
                    isSmile=true;
                    tv_feedback_status.setText("Hài lòng!");
                }
            }
        });
    }
}
