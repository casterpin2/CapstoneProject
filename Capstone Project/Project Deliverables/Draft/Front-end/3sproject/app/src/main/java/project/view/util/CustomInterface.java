package project.view.util;

import android.app.Activity;
import android.os.Build;

import project.view.R;

public class CustomInterface {

    public static void setStatusBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor((activity.getResources().getColor(R.color.statusBarColor)));
        }
    }
}
