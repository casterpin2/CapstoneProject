package project.view.gui;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.model.SmsResultEntities;
import project.view.util.CustomInterface;
import project.view.util.NetworkStateReceiver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPCodePage extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener{

    private RelativeLayout main_layout,rela;
    private TextInputEditText etCode;
    private TextView tvConfirmCodeMess, tvResend;
    private Button btn;
    private boolean isCode = true;
    private String username;
    private String code;
    private String phone;
    private APIService apiService;
    private boolean checkNetwork = true;
    private NetworkStateReceiver networkStateReceiver;
    private ProgressBar loadingBar;
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
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        username = getIntent().getStringExtra("user");
        phone =getIntent().getStringExtra("phone");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setVisibility(View.VISIBLE);
                btn.setEnabled(false);
                btn.setText("");
                apiService = ApiUtils.getAPIService();
                code = etCode.getText().toString();
                if(checkNetwork){
                    apiService.confirmOTP(code,phone).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if(response.isSuccessful()){
                                loadingBar.setVisibility(View.INVISIBLE);
                                btn.setEnabled(true);
                                btn.setText("Xác nhận");
                                Intent toChangePasswordPage = new Intent(getBaseContext(),ResetPasswordPage.class);
                                toChangePasswordPage.putExtra("username",username);
                                startActivity(toChangePasswordPage);
                            } else {

                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {

                        }
                    });
                }else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingBar.setVisibility(View.INVISIBLE);
                            btn.setEnabled(true);
                            btn.setText("Xác nhận");
                            tvConfirmCodeMess.setText("Có lỗi xảy ra. Vui lòng thử lại");
                        }
                    }, 5000);
                }

                if(isCode){

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
                        if(checkNetwork){
                            apiService.getCodeVerify(username).enqueue(new Callback<SmsResultEntities>() {
                                @Override
                                public void onResponse(Call<SmsResultEntities> call, Response<SmsResultEntities> response) {
                                    if(response.body()!=null){
                                        code = response.body().getCode();
                                        alertDialog.hide();
                                    }
                                }

                                @Override
                                public void onFailure(Call<SmsResultEntities> call, Throwable t) {

                                }
                            });
                            Toast.makeText(OTPCodePage.this,"Đã gửi lại",Toast.LENGTH_LONG).show();
                            alertDialog.hide();
                        }else{

                        }

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
        loadingBar = findViewById(R.id.loadingBar);
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


    @Override
    public void networkAvailable() {
        checkNetwork = true;
    }

    @Override
    public void networkUnavailable() {
        checkNetwork = false;
    }
}
