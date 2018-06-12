package project.view;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class ChangePasswordActivity extends AppCompatActivity {

    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
    }

    private void showDialog(){
        dialog = new Dialog(ChangePasswordActivity.this);
        dialog.setContentView(R.layout.dialog);
        final TextView dialogContent = (TextView) dialog.findViewById(R.id.content);
        final Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
        dialog.setTitle(R.string.title_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = 500;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialogContent.setText(R.string.content_dialog);
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
