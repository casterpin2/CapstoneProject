package project.view.gui;

import android.content.DialogInterface;
import android.content.Intent;
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

import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.model.SmsResultEntities;
import project.view.util.CustomInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetPasswordPage extends AppCompatActivity {
    private RelativeLayout main_layout;
    private TextView tvSendCodeMess;
    private TextInputEditText etUsername;
    private Button btn;
    private APIService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_password_page);
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
                if ("".equals(etUsername.getText())){
                    tvSendCodeMess.setText(R.string.error_empty);
                }else if(etUsername.getText().length()<10
                        ||etUsername.getText().length()>=12){
                    tvSendCodeMess.setText(R.string.error_validate_username);
                }
                else{
                    tvSendCodeMess.setText("");
                    AlertDialog.Builder builder = new AlertDialog.Builder(GetPasswordPage.this);
                    View v = getLayoutInflater().inflate(R.layout.dialog_custom,null);
                    TextView content1 = v.findViewById(R.id.tvContent1);
                    TextView content2 = v.findViewById(R.id.tvContent2);
                    Button btnOK = v.findViewById(R.id.btnOK);
                    Button btnCancle = v.findViewById(R.id.btnCancle);
                    content1.setText("Đây có phải tài khoản mà bạn đã đăng kí tài khoản không?");
                    content2.setText(etUsername.getText());
                    builder.setView(v);
                    final AlertDialog alertDialog = builder.create();
                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tvSendCodeMess.setText("");
                            apiService = ApiUtils.getAPIService();

                            alertDialog.hide();
                            apiService.getCodeVerify(etUsername.getText().toString()).enqueue(new Callback<SmsResultEntities>() {
                                @Override
                                public void onResponse(Call<SmsResultEntities> call, Response<SmsResultEntities> response) {
                                    if(response.body()!=null){
                                        Intent toOTPCodePage = new Intent(getBaseContext(),OTPCodePage.class);
                                        toOTPCodePage.putExtra("phone",response.body().getPhoneUser());
                                        toOTPCodePage.putExtra("user",response.body().getUsername());
                                        startActivity(toOTPCodePage);
                                        alertDialog.hide();
                                    }else {
                                        Toast.makeText(GetPasswordPage.this, "Sai thông tin tài khoản", Toast.LENGTH_LONG).show();

                                    }

                                }

                                @Override
                                public void onFailure(Call<SmsResultEntities> call, Throwable t) {

                                }
                            });

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

    }

    private void findView(){
        main_layout = findViewById(R.id.main_layout);
        tvSendCodeMess = findViewById(R.id.tvSendMess);
        etUsername = findViewById(R.id.etUsername);
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
