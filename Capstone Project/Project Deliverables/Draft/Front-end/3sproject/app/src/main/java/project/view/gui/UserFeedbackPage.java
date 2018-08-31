package project.view.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import project.retrofit.ApiUtils;
import project.view.R;
import project.view.model.Feedback;
import project.view.model.ResultNotification;
import project.view.util.CustomInterface;
import project.view.util.TweakUI;
import retrofit2.Call;
import retrofit2.Response;

public class UserFeedbackPage extends BasePage {
    private LinearLayout main_layout;
    private boolean isSmile = false;
    protected boolean isSad = false;
    private ImageView smile_checked, sad_checked, back_btn;
    private TextView tv_feedback_status, error_mess, tvStoreName;
    private Button btn_send_feedback;
    private EditText content_feedback;
    private ProgressBar loadingBar;
    private int userId,storeId;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private String orderId;
    private String storeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback_page);
     //   TweakUI.makeTransparent(this);
        CustomInterface.setStatusBarColor(this);
        CustomInterface.setSoftInputMode(this);
        findView();
        storeName = getIntent().getStringExtra("storeName");

        main_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CustomInterface.hideKeyboard(view,getBaseContext());
                return false;
            }
        });
        tvStoreName.setText(storeName);

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
                            if (userId == -1 || storeId == -1 || orderId.isEmpty()){
                                //Toast.makeText(UserFeedbackPage.this, "Có lỗi xảy ra!!!", Toast.LENGTH_SHORT).show();
                            } else {
                                String content = content_feedback.getText().toString();
                                int isSatisfied = 1;
                                if (isSad) isSatisfied = 0;
                                Feedback feedback = new Feedback();
                                feedback.setContent(content);
                                feedback.setUser_id(userId);
                                feedback.setStore_id(storeId);
                                feedback.setIsSatisfied(isSatisfied);
                                Calendar c = Calendar.getInstance();
                                Date date = c.getTime();
                                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
                                String dateString = dateFormat.format(date);
                                feedback.setRegisterLog(dateString);
                                Call<Boolean> call = ApiUtils.getAPIService().getFeedback(feedback);
                                new SendFeedback().execute(call);
                            }
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
        loadingBar = findViewById(R.id.loadingBar);
        tvStoreName = findViewById(R.id.tvStoreName);
        orderId = getIntent().getStringExtra("orderId");
        userId = getIntent().getIntExtra("userId",-1);
        storeId = getIntent().getIntExtra("storeId",-1);
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
                alertDialog.hide();
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK,intent);
                finish();
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

    public class SendFeedback extends AsyncTask<Call,Void,Boolean> {


        @Override
        protected void onPreExecute() {
            loadingBar.setVisibility(View.VISIBLE);
            btn_send_feedback.setEnabled(false);
            btn_send_feedback.setText("");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result != null) {
                DatabaseReference databaseReference = database.getReference().child("ordersUser").child(String.valueOf(userId)).child(orderId).child("isFeedback");
                databaseReference.setValue("true");
                dialog();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            Toast.makeText(UserFeedbackPage.this, "Có lỗi xảy ra. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                            loadingBar.setVisibility(View.INVISIBLE);
                            btn_send_feedback.setEnabled(true);
                            btn_send_feedback.setText("Gửi phản hồi");
                        }

                },10000);
                return;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(Call... calls) {
            try {
                Call<Boolean> call = calls[0];
                Response<Boolean> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
