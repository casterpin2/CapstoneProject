package project.view;

import android.app.ActionBar;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class LoginPage extends AppCompatActivity {

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_login_page);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0,187,0)));
        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        ScrollView scroll = (ScrollView) findViewById(R.id.scrollView);

        scroll.setVerticalScrollBarEnabled(false);
        scroll.setHorizontalScrollBarEnabled(false);



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(view);

                //errorText.setText("Tên đăng nhập hoặc mật khẩu không đúng. Vui lòng thử lại");
            }
        });
    }

    public void showDialog(View view) {

//        AlertDialog.Builder builderDialog = new AlertDialog.Builder(view.getContext());
//        builderDialog.setTitle("Thông báo");
//        builderDialog.setMessage("Tên đăng nhập hoặc mật khẩu không đúng. Vui lòng thử lại");
//        builderDialog.setView(R.layout.dialog);
//        AlertDialog alertDialog  = builderDialog.create();
//        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, 200);
//        alertDialog.show();



        dialog = new Dialog(LoginPage.this);
        dialog.setContentView(R.layout.dialog);
        final TextView dialogContent = (TextView) dialog.findViewById(R.id.content);
        final Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
        dialog.setTitle("Thông báo");
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = 500;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        dialogContent.setText("Tên đăng nhập hoặc mật khẩu không đúng. Vui lòng thử lại.");
        dialogContent.setTextColor(Color.rgb(255,0,0));
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
